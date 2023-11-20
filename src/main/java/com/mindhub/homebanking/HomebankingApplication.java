package com.mindhub.homebanking;

import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static com.mindhub.homebanking.models.CardColor.*;

@SpringBootApplication
public class HomebankingApplication {

	public static void main(String[] args) {
		SpringApplication.run(HomebankingApplication.class, args);
	}
/*
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Bean
	public CommandLineRunner initData(ClientRepository clientRepository, AccountRepository accountRepository, TransactionRepository transactionRepository, LoanRepository loanRepository, ClientLoanRepository clientLoanRepository, CardRepository cardRepository){
		return args -> {
			//CLIENT-1
			Client melba = new Client("Melba", "Morel", "melba@gmail.com", passwordEncoder.encode("Melba"),false);
			clientRepository.save(melba);

			//ACCOUNT-1 CLIENT-1
			Account melbaA1 = new Account("VIN001", LocalDate.now(), 5000);
			melba.addAccount(melbaA1);
			accountRepository.save(melbaA1);

			//TRANSACTIONS CLIENT-1 ACCOUNT-1
			Transaction melbaA1T = new Transaction(TransactionType.CREDIT, 2500, "Buy a iPhone", LocalDateTime.now());
			melbaA1.addTransaction(melbaA1T);
			transactionRepository.save(melbaA1T);

			Transaction melbaA1T2 = new Transaction(TransactionType.DEBIT, -500, "Buy a case", LocalDateTime.now());
			melbaA1.addTransaction(melbaA1T2);
			transactionRepository.save(melbaA1T2);

			//ACCOUNT-2 CLIENT-1
			Account melbaA2 = new Account("VIN002", LocalDate.now().plusDays(1), 7500);
			melba.addAccount(melbaA2);
			accountRepository.save(melbaA2);

			//TRANSACTION CLIENT-1 ACCOUNT-2
			Transaction melbaA2T = new Transaction(TransactionType.DEBIT, -350, "Tax payment", LocalDateTime.now());
			melbaA2.addTransaction(melbaA2T);
			transactionRepository.save(melbaA2T);

			//CLIENT-2
			Client israel = new Client("Israel", "Carrion", "israelcarrion.g@gmail.com", passwordEncoder.encode("Israel"),true);
			clientRepository.save(israel);

			//ACCOUNT-1 CLIENT-2
			Account israelA1 = new Account("VIN003", LocalDate.now(), 8500);
			israel.addAccount(israelA1);
			accountRepository.save(israelA1);

			//TRANSACTION CLIENT-2 ACCOUNT-1
			Transaction israelA1T = new Transaction(TransactionType.CREDIT, 5000, "Buy Mac", LocalDateTime.now());
			israelA1.addTransaction(israelA1T);
			transactionRepository.save(israelA1T);

			//LOANS
			Loan mortgage = new Loan("Mortgage", 500000.00, List.of(12, 24, 36, 48, 60));
			loanRepository.save(mortgage);

			Loan personal = new Loan("Personal", 100000.00, List.of(6, 12, 24));
			loanRepository.save(personal);

			Loan automotive = new Loan("Automotive", 300000.00, List.of(6, 12, 24, 36));
			loanRepository.save(automotive);

			//LOANS CLIENT-1
			ClientLoan melbaL1 = new ClientLoan(400000.00, 60);
			melba.addClientLoan(melbaL1);
			mortgage.addClientLoan(melbaL1);
			clientLoanRepository.save(melbaL1);

			ClientLoan melbaL2 = new ClientLoan(50000.00, 12);
			melba.addClientLoan(melbaL2);
			personal.addClientLoan(melbaL2);
			clientLoanRepository.save(melbaL2);

			//LOANS CLIENT-2
			ClientLoan israelL1 = new ClientLoan(100000.00, 24);
			israel.addClientLoan(israelL1);
			personal.addClientLoan(israelL1);
			clientLoanRepository.save(israelL1);

			ClientLoan israelL2 = new ClientLoan(200000.00, 36);
			israel.addClientLoan(israelL2);
			automotive.addClientLoan(israelL2);
			clientLoanRepository.save(israelL2);

			//CLIENT-1 CARD-1
			Card melbaC1 = new Card(melba.nameCard(), CardType.DEBIT, GOLD, "4055-1600-1739-7609", "185", LocalDate.now(), LocalDate.now().plusYears(5));
			melba.addCard(melbaC1);
			cardRepository.save(melbaC1);

			//CLIENT-1 CARD-2
			Card melbaC2 = new Card(melba.nameCard(), CardType.CREDIT, TITANIUM, "4055-1600-1739-3300", "963", LocalDate.now(), LocalDate.now().plusYears(5));
			melba.addCard(melbaC2);
			cardRepository.save(melbaC2);

			//CLIENT-2 CARD-1
			Card israelC1 = new Card(israel.nameCard(), CardType.CREDIT, SILVER, "4111-9729-7856-5783", "663", LocalDate.now(), LocalDate.now().plusYears(5));
			israel.addCard(israelC1);
			cardRepository.save(israelC1);

		};
	}
*/

}