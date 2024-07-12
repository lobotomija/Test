package hr.rba.test.service;

import hr.rba.test.dto.CardRequestDTO;
import hr.rba.test.dto.OibAndStatusDTO;
import hr.rba.test.entity.Card;
import hr.rba.test.repository.CardRepository;
import jakarta.validation.Valid;
import org.dozer.DozerBeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.DataBinder;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.LinkedList;
import java.util.List;

@Service
public class CardService implements ICardService {
    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private Validator validator;


    DozerBeanMapper dozerBeanMapper = new DozerBeanMapper();

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW,
            rollbackFor = Exception.class)
    public CardRequestDTO insert(CardRequestDTO CardRequestDTO) {
        return saveCard(validationOfCard(CardRequestDTO), 0L);
    }

    public CardRequestDTO validationOfCard(@Valid CardRequestDTO cardRequestDTO) {
        DataBinder binder = new DataBinder(cardRequestDTO);
        binder.setValidator(validator);
        binder.validate();
        Errors errors = binder.getBindingResult();

        if (errors.hasErrors()) {
            StringBuilder sb = new StringBuilder();
            errors.getAllErrors().forEach(error -> sb.append(error.getDefaultMessage()).append("\n"));
            throw new IllegalArgumentException("Card is not valid: \n" + sb.toString());
        }

        return cardRequestDTO;
    }

    private CardRequestDTO saveCard(CardRequestDTO cardRequestDTO, Long id) {
        Card card = new Card();
        dozerBeanMapper.map(cardRequestDTO, card);
        card = cardRepository.save(card);
        CardRequestDTO result = new CardRequestDTO();
        dozerBeanMapper.map(card, result);
        return result;
    }

    @Override
    public List<CardRequestDTO> findAll() {
        List<Card> cards = cardRepository.findAll();
        return getCardDTOS(cards);
    }

    @Override
    public CardRequestDTO findByOib(String oib) {
        Card card = cardRepository.findByOib(oib);
        CardRequestDTO result = new CardRequestDTO();
        dozerBeanMapper.map(card, result);
        return result;
    }

    private List<CardRequestDTO> getCardDTOS(List<Card> cards) {
        List<CardRequestDTO> cardRequestDTOS = new LinkedList<>();
        for (var card : cards) {
            CardRequestDTO cardRequestDTO = new CardRequestDTO();
            dozerBeanMapper.map(card, cardRequestDTO);
            cardRequestDTOS.add(cardRequestDTO);
        }

        return cardRequestDTOS;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteByOib(String oib)
    {
        cardRepository.delete(cardRepository.findByOib(oib));
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW,
            rollbackFor = Exception.class)
    public void updateStatusByOib(OibAndStatusDTO oibAndStatusDTO) {
        Card card = cardRepository.findByOib(oibAndStatusDTO.getOib());

        card.setStatus(oibAndStatusDTO.getStatus());

        cardRepository.save(card);
    }
}
