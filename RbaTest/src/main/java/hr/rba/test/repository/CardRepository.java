package hr.rba.test.repository;

import hr.rba.test.entity.Card;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CardRepository extends CrudRepository<Card, Long> {
    List<Card> findAll();

    Card findByOib(String oib);
}
