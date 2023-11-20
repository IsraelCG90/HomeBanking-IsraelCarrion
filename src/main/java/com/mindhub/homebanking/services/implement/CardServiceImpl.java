package com.mindhub.homebanking.services.implement;

import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.CardColor;
import com.mindhub.homebanking.models.CardType;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.CardRepository;
import com.mindhub.homebanking.services.CardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CardServiceImpl implements CardService {
    @Autowired
    private CardRepository cardRepository;

    @Override
    public boolean existsCardByNumber(String number) {
        return cardRepository.existsByNumber(number);
    }

    @Override
    public void saveCard(Card card) {
        cardRepository.save(card);
    }

    @Override
    public void deleteCard(Long id) {
        Card card = cardRepository.findById(id).orElse(null);
        card.setDelete(true);
        cardRepository.save(card);
    }

    @Override
    public boolean existsCardById(Long id) {
        return cardRepository.existsById(id);
    }

    @Override
    public boolean existsByClientAndTypeAndColorAndIsDeleted(Client client, CardType type, CardColor color, Boolean isDelete) {
        return cardRepository.existsByClientAndTypeAndColorAndIsDelete(client, type, color, isDelete);
    }

}
