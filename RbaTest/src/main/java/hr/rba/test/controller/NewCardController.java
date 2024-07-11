package hr.rba.test.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import hr.rba.test.dto.ErrorResponseDTO;
import hr.rba.test.dto.OibAndStatusDTO;
import hr.rba.test.dto.NewCardRequestDTO;
import hr.rba.test.dto.ResponseDTO;
import hr.rba.test.service.NewCardService;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@NoArgsConstructor
@Slf4j
@RequestMapping("/card_request")
public class NewCardController {

    @Autowired
    NewCardService personService;

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<String> createPerson(@RequestBody NewCardRequestDTO newCardRequestDTO) {
        LOGGER.info("Save person:" + newCardRequestDTO);
        try {
            String result = new ObjectMapper().writeValueAsString(personService.insert(newCardRequestDTO));
            return new ResponseEntity<>(new ObjectMapper().writeValueAsString(new ResponseDTO(result)), HttpStatus.CREATED);
        } catch (Exception e) {
            LOGGER.error("Save person failed", e);
            return getErrorResponseEntity("400", "0", "Save person failed");
        }
    }

    @RequestMapping(path = "/find_all", method = RequestMethod.GET)
    public ResponseEntity<String> listPersons() {
        LOGGER.info("List persons");
        try {
            String result = new ObjectMapper().writeValueAsString(personService.findAll());
            return new ResponseEntity<>(new ObjectMapper().writeValueAsString(new ResponseDTO(result)), HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.error("List persons failed", e);
            return getErrorResponseEntity("404", "0", "Persons not found");
        }
    }

    @RequestMapping(path = "/status", method = RequestMethod.PUT)
    public ResponseEntity<String> status(@RequestBody OibAndStatusDTO oibAndStatusDTO) {
        LOGGER.info("Update person status with oib: " + oibAndStatusDTO.getOib());
        try {
            personService.updateStatusByOib(oibAndStatusDTO);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.error("Update status person with oib " + oibAndStatusDTO.getOib() + " failed", e);
            return getErrorResponseEntity("400", "0", "Oib not found");
        }
    }

    @DeleteMapping("/{oib}")
    public ResponseEntity<String> delete(@PathVariable String oib) {
        LOGGER.info("Delete person with oib: " + oib);
        try {
            personService.deleteByOib(oib);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.error("Delete person with oib " + oib + " failed", e);
            return getErrorResponseEntity("404", "0", "Oib not found");
        }
    }

    @GetMapping("/{oib}")
    public ResponseEntity<String> search(@PathVariable String oib) {
        LOGGER.info("Search person with oib: " + oib);

        try {
            String result = new ObjectMapper().writeValueAsString(personService.findByOib(oib));
            return new ResponseEntity<>(new ObjectMapper().writeValueAsString(new ResponseDTO(result)), HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.error("Find person with oib " + oib + " failed", e);
            return getErrorResponseEntity("404", "0", "Oib not found");
        }
    }

    private ResponseEntity<String> getErrorResponseEntity(String code, String id, String description) {
        try {
            return new ResponseEntity<>(new ObjectMapper().writeValueAsString(
                    new ErrorResponseDTO(code, id, description)), HttpStatus.BAD_REQUEST);
        } catch (Exception ex)
        {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
