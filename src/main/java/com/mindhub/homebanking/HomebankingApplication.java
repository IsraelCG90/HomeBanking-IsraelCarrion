package com.mindhub.homebanking;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.repositories.TransactionRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static com.mindhub.homebanking.models.TransactionType.*;

@SpringBootApplication
public class HomebankingApplication {

	public static void main(String[] args) {
		SpringApplication.run(HomebankingApplication.class, args);
	}
	@Bean
	public CommandLineRunner initData(ClientRepository clientRepository, AccountRepository accountRepository, TransactionRepository transactionRepository){
		return args -> {
			//CLIENT 1
			Client melba = new Client("Melba", "Morel", "melbam@gmail.com");
			clientRepository.save(melba);

			Account melbaA1 = new Account("VIN001", LocalDate.now(), 5000);
			melba.addAccount(melbaA1);
			accountRepository.save(melbaA1);

			Transaction melbaA1T = new Transaction(CREDIT, 2500, "Buy a iPhone", LocalDateTime.now());
			melbaA1.addTransaction(melbaA1T);
			transactionRepository.save(melbaA1T);

			Transaction melbaA1T2 = new Transaction(DEBIT, -500, "Buy a case", LocalDateTime.now());
			melbaA1.addTransaction(melbaA1T2);
			transactionRepository.save(melbaA1T2);

			Account melbaA2 = new Account("VIN002", LocalDate.now().plusDays(1), 7500);
			melba.addAccount(melbaA2);
			accountRepository.save(melbaA2);

			Transaction melbaA2T = new Transaction(DEBIT, -350, "Tax payment", LocalDateTime.now());
			melbaA2.addTransaction(melbaA2T);
			transactionRepository.save(melbaA2T);

			//CLIENT 2
			Client israel = new Client("Israel", "Carrion", "israelcarrion.g@gmail.com");
			clientRepository.save(israel);

			Account israelA1 = new Account("VIN001", LocalDate.now(), 8500);
			israel.addAccount(israelA1);
			accountRepository.save(israelA1);

			Transaction israelA1T = new Transaction(CREDIT, 5000, "Buy Mac", LocalDateTime.now());
			israelA1.addTransaction(israelA1T);
			transactionRepository.save(israelA1T);

		};
	}
}
