package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.CardColor;
import com.mindhub.homebanking.models.CardType;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.CardRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/api")
public class CardController {
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private CardRepository cardRepository;

    public int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }

     public String generateNumberCard() {
        StringBuilder cardNumber; //3973-4475-2239-2248
        do {
            cardNumber = new StringBuilder();
            for (int i = 0; i < 16; i++) {
                cardNumber.append(getRandomNumber(0, 9));
                if ((i + 1) % 4 == 0 && i != 15) cardNumber.append("-");
            }
        } while (cardRepository.existsByNumber(cardNumber.toString()));
        return cardNumber.toString();
    }

    public String generateCvvCard() {
        StringBuilder cvvNumber;
        do {
            cvvNumber = new StringBuilder();
            for (byte i = 0; i <= 2; i++) {
                cvvNumber.append(getRandomNumber(0, 9));
            }
        } while (cardRepository.existsByCvv(cvvNumber.toString()));
        return cvvNumber.toString();
    }


    @PostMapping("/clients/current/cards")
    public ResponseEntity<Object> newCard(Authentication authentication, @RequestParam String cardType, @RequestParam String cardColor){

        if (cardType.isEmpty() || cardType.isBlank()) {
            return new ResponseEntity<>("Missing data", HttpStatus.FORBIDDEN);
        }

        if(cardColor.isEmpty() || cardColor.isBlank()){
            return new ResponseEntity<>("Missing data", HttpStatus.FORBIDDEN);
        }

        Client client = clientRepository.findByEmail(authentication.getName());

        int numberOfCardType = (int) client.getCards().stream().filter(card -> card.getType().equals(CardType.valueOf(cardType))).count();

        if (numberOfCardType == 3) {
            return new ResponseEntity<>("You cannot have more than three cards of the same type.", HttpStatus.FORBIDDEN);
        }

        Card card = new Card(client.nameCard(), CardType.valueOf(cardType), CardColor.valueOf(cardColor), generateNumberCard(), generateCvvCard(), LocalDate.now().plusYears(5), LocalDate.now());
        client.addCard(card);
        cardRepository.save(card);
        clientRepository.save(client);

        return new ResponseEntity<>("Card created successfully", HttpStatus.CREATED);
    }
}
