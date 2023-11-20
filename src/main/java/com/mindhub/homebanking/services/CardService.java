package com.mindhub.homebanking.services;

import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.CardColor;
import com.mindhub.homebanking.models.CardType;
import com.mindhub.homebanking.models.Client;

public interface CardService {
    boolean existsCardByNumber(String number);

    void saveCard(Card card);

    void deleteCard(Long id);

    boolean existsCardById(Long id);

    boolean existsByClientAndTypeAndColorAndIsDeleted (Client client, CardType type, CardColor color, Boolean isDelete);
}
