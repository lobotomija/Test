package hr.rba.test.service;

import hr.rba.test.dto.NewCardRequestDTO;
import hr.rba.test.dto.OibAndStatusDTO;

import java.util.List;

public interface INewCardService {
    NewCardRequestDTO insert(NewCardRequestDTO NewCardRequestDTO);
    List<NewCardRequestDTO> findAll();

    NewCardRequestDTO findByOib(String oib);

    void deleteByOib(String oib);

    void updateStatusByOib(OibAndStatusDTO oibAndStatusDTO);
}
