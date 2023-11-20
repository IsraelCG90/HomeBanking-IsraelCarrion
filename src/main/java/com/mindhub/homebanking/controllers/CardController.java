package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.CardColor;
import com.mindhub.homebanking.models.CardType;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.services.implement.CardServiceImpl;
import com.mindhub.homebanking.services.implement.ClientServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

import static com.mindhub.homebanking.utils.CardUtils.generateCvvCard;
import static com.mindhub.homebanking.utils.CardUtils.generateNumberCard;

@RestController
@RequestMapping("/api")
public class CardController {
    @Autowired
    private ClientServiceImpl clientService;
    @Autowired
    private CardServiceImpl cardService;

    @PostMapping("/clients/current/cards")
    public ResponseEntity<String> newCard(Authentication authentication, @RequestParam String cardType, @RequestParam String cardColor){

        if (cardType.isBlank() || !cardType.equals("DEBIT") && !cardType.equals("CREDIT")) {
            return new ResponseEntity<>("Missing Card Type", HttpStatus.FORBIDDEN);
        }

        if(cardColor.isBlank() || !cardColor.equals("GOLD") && !cardColor.equals("TITANIUM") && !cardColor.equals("SILVER")){
            return new ResponseEntity<>("Missing Card Color", HttpStatus.FORBIDDEN);
        }

        Client client = clientService.findClientByEmail(authentication.getName());

        int numberOfCardType = (int) client.getCards().stream().filter(card -> card.getType().equals(CardType.valueOf(cardType))).count();

        if (cardService.existsByClientAndTypeAndColorAndIsDeleted(client, CardType.valueOf(cardType), CardColor.valueOf(cardColor), false)) {
            return new ResponseEntity<>("You cannot apply for a card of the same type.", HttpStatus.FORBIDDEN);
        }

        String cardNumber;
        do {
            cardNumber = generateNumberCard();
        } while (cardService.existsCardByNumber(cardNumber));

        Card card = new Card(client.nameCard(), CardType.valueOf(cardType), CardColor.valueOf(cardColor), cardNumber, generateCvvCard(), LocalDate.now().plusYears(5), LocalDate.now());
        client.addCard(card);
        cardService.saveCard(card);
        clientService.saveClient(client);

        return new ResponseEntity<>("Card created successfully", HttpStatus.CREATED);
    }

    @PostMapping("/clients/current/cards/delete")
    public ResponseEntity<String> deleteCard(@RequestParam Long id){
        if (!cardService.existsCardById(id)){
            return new ResponseEntity<>("The card you are trying to delete does not exist.", HttpStatus.FORBIDDEN);
        }
        cardService.deleteCard(id);
        return new ResponseEntity<>("The card was successfully removed", HttpStatus.OK);
    }
}
