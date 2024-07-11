package hr.rba.test.service;

import hr.rba.test.dto.NewCardRequestDTO;
import hr.rba.test.dto.OibAndStatusDTO;
import hr.rba.test.entity.NewCard;
import hr.rba.test.repository.NewCardRepository;
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
public class NewCardService implements INewCardService {
    @Autowired
    private NewCardRepository newCardRepository;

    @Autowired
    private Validator validator;


    DozerBeanMapper dozerBeanMapper = new DozerBeanMapper();

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW,
            rollbackFor = Exception.class)
    public NewCardRequestDTO insert(NewCardRequestDTO NewCardRequestDTO) {
        return savePerson(validationOfPerson(NewCardRequestDTO), 0L);
    }

    public NewCardRequestDTO validationOfPerson(@Valid NewCardRequestDTO newCardRequestDTO) {
        DataBinder binder = new DataBinder(newCardRequestDTO);
        binder.setValidator(validator);
        binder.validate();
        Errors errors = binder.getBindingResult();

        if (errors.hasErrors()) {
            StringBuilder sb = new StringBuilder();
            errors.getAllErrors().forEach(error -> sb.append(error.getDefaultMessage()).append("\n"));
            throw new IllegalArgumentException("Person is not valid: \n" + sb.toString());
        }

        return newCardRequestDTO;
    }

    private NewCardRequestDTO savePerson(NewCardRequestDTO newCardRequestDTO, Long id) {
        NewCard newCard = new NewCard();
        dozerBeanMapper.map(newCardRequestDTO, newCard);
        newCard = newCardRepository.save(newCard);
        NewCardRequestDTO result = new NewCardRequestDTO();
        dozerBeanMapper.map(newCard, result);
        return result;
    }

    @Override
    public List<NewCardRequestDTO> findAll() {
        List<NewCard> newCards = newCardRepository.findAll();
        return getPersonDTOS(newCards);
    }

    @Override
    public NewCardRequestDTO findByOib(String oib) {
        NewCard newCard = newCardRepository.findByOib(oib);
        NewCardRequestDTO result = new NewCardRequestDTO();
        dozerBeanMapper.map(newCard, result);
        return result;
    }

    private List<NewCardRequestDTO> getPersonDTOS(List<NewCard> newCards) {
        List<NewCardRequestDTO> newCardRequestDTOS = new LinkedList<>();
        for (var person : newCards) {
            NewCardRequestDTO newCardRequestDTO = new NewCardRequestDTO();
            dozerBeanMapper.map(person, newCardRequestDTO);
            newCardRequestDTOS.add(newCardRequestDTO);
        }

        return newCardRequestDTOS;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteByOib(String oib)
    {
        newCardRepository.delete(newCardRepository.findByOib(oib));
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW,
            rollbackFor = Exception.class)
    public void updateStatusByOib(OibAndStatusDTO oibAndStatusDTO) {
        NewCard newCard = newCardRepository.findByOib(oibAndStatusDTO.getOib());

        newCard.setStatus(oibAndStatusDTO.getStatus());

        newCardRepository.save(newCard);
    }
}
