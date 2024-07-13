package hr.rba.test.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import hr.rba.test.dto.ErrorResponseDTO;
import hr.rba.test.dto.OibAndStatusDTO;
import hr.rba.test.dto.CardRequestDTO;
import hr.rba.test.dto.ResponseDTO;
import hr.rba.test.service.CardService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@NoArgsConstructor
@Slf4j
@RequestMapping("/card-request")
public class CardController {

    @Autowired
    CardService cardService;

    @Operation(summary = "Create a new card data", description = "Create a new card data with the provided details")
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<String> createCard(@RequestBody CardRequestDTO cardRequestDTO) {
        LOGGER.info("Save new card data:" + cardRequestDTO);
        try {
            String result = new ObjectMapper().writeValueAsString(cardService.insert(cardRequestDTO));
            return new ResponseEntity<>(new ObjectMapper().writeValueAsString(new ResponseDTO(result)), HttpStatus.CREATED);
        } catch (Exception e) {
            LOGGER.error("Save new card data failed", e);
            return getErrorResponseEntity("400", "0", e.getMessage());
        }
    }

    @Operation(summary = "Find all card data", description = "Find all card data")
    @RequestMapping(path = "/find-all", method = RequestMethod.GET)
    public ResponseEntity<String> listCards() {
        LOGGER.info("List cards");
        try {
            String result = new ObjectMapper().writeValueAsString(cardService.findAll());
            return new ResponseEntity<>(new ObjectMapper().writeValueAsString(new ResponseDTO(result)), HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.error("List cards failed", e);
            return getErrorResponseEntity("404", "0", "Card data not found");
        }
    }

    @Operation(summary = "Change status of card", description = "Change status of card given by oib of person")
    @RequestMapping(path = "/status", method = RequestMethod.PUT)
    public ResponseEntity<String> status(@RequestBody OibAndStatusDTO oibAndStatusDTO) {
        LOGGER.info("Update card data status with oib: " + oibAndStatusDTO.getOib());
        try {
            cardService.updateStatusByOib(oibAndStatusDTO);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.error("Update status card data with oib " + oibAndStatusDTO.getOib() + " failed", e);
            return getErrorResponseEntity("400", "0", "Oib not found");
        }
    }

    @Operation(summary = "Delete a card", description = "Delete a card given by the oib")
    @DeleteMapping("/{oib}")
    public ResponseEntity<String> delete(@PathVariable String oib) {
        LOGGER.info("Delete card data with oib: " + oib);
        try {
            cardService.deleteByOib(oib);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.error("Delete card data with oib " + oib + " failed", e);
            return getErrorResponseEntity("404", "0", "Oib not found");
        }
    }

    @Operation(summary = "Search card data", description = "Search card data by oib")
    @GetMapping("/{oib}")
    public ResponseEntity<String> search(@PathVariable String oib) {
        LOGGER.info("Search card data for oib: " + oib);

        try {
            String result = new ObjectMapper().writeValueAsString(cardService.findByOib(oib));
            return new ResponseEntity<>(new ObjectMapper().writeValueAsString(new ResponseDTO(result)), HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.error("Find card data for oib " + oib + " failed", e);
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
