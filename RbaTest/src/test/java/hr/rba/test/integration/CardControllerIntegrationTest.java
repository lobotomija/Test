package hr.rba.test.integration;

import hr.rba.test.RbaTest;
import hr.rba.test.dto.CardRequestDTO;
import hr.rba.test.dto.ResponseDTO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertTrue;

@SpringBootTest(classes = RbaTest.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class)
public class CardControllerIntegrationTest {
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void testCardCRUD() {
        CardRequestDTO cardRequestDTO = CardRequestDTO.builder()
                .firstName("first")
                .lastName("last")
                .oib("12345678900")
                .status("u izradi")
                .build();

        ResponseEntity<ResponseDTO> responseEntity = restTemplate
                .postForEntity("http://localhost:" + port + "/card-request", cardRequestDTO, ResponseDTO.class);
        assertTrue(responseEntity.getStatusCode().toString().equals("201 CREATED"));

        responseEntity = restTemplate
                .getForEntity("http://localhost:" + port + "/card-request/" + cardRequestDTO.getOib(), ResponseDTO.class);
        assertTrue(responseEntity.getStatusCode().toString().equals("200 OK"));

        restTemplate.delete("http://localhost:" + port + "/card-request/"+cardRequestDTO.getOib());
    }
}
