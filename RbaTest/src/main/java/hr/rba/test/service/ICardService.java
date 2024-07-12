package hr.rba.test.service;

import hr.rba.test.dto.CardRequestDTO;
import hr.rba.test.dto.OibAndStatusDTO;

import java.util.List;

public interface ICardService {
    CardRequestDTO insert(CardRequestDTO CardRequestDTO);
    List<CardRequestDTO> findAll();

    CardRequestDTO findByOib(String oib);

    void deleteByOib(String oib);

    void updateStatusByOib(OibAndStatusDTO oibAndStatusDTO);
}
