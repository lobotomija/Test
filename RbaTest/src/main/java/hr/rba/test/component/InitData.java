package hr.rba.test.component;

import hr.rba.test.entity.Card;
import hr.rba.test.repository.CardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class InitData {

    @Autowired
    private CardRepository cardRepository;

    public void initData() {
        Card card = Card.builder()
                .firstName("Pero")
                .lastName("Peric")
                .oib("12345678901")
                .status("U izradi")
                .build();

        cardRepository.save(card);
    }
}
