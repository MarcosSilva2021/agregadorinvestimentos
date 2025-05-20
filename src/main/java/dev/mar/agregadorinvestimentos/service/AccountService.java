package dev.mar.agregadorinvestimentos.service;

import dev.mar.agregadorinvestimentos.repository.AccountRepository;
import dev.mar.agregadorinvestimentos.repository.StockRepository;
import org.springframework.stereotype.Service;

@Service
public class AccountService {

    private AccountRepository accountRepository;
    private StockRepository stockRepository;

    public AccountService(AccountRepository accountRepository, StockRepository stockRepository) {
        this.accountRepository = accountRepository;
        this.stockRepository = stockRepository;
    }

}
