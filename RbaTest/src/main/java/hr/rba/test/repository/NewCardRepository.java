package hr.rba.test.repository;

import hr.rba.test.entity.NewCard;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface NewCardRepository extends CrudRepository<NewCard, Long> {
    List<NewCard> findAll();

    NewCard findByOib(String oib);
}
