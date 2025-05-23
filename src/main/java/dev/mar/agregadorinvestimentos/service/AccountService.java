package dev.mar.agregadorinvestimentos.service;

import dev.mar.agregadorinvestimentos.controller.dto.AccountStockResponseDto;
import dev.mar.agregadorinvestimentos.controller.dto.AssociateAccountStockDto;
import dev.mar.agregadorinvestimentos.entity.AccountStock;
import dev.mar.agregadorinvestimentos.entity.AccountStockId;
import dev.mar.agregadorinvestimentos.repository.AccountRepository;
import dev.mar.agregadorinvestimentos.repository.AccountStockRepository;
import dev.mar.agregadorinvestimentos.repository.StockRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@Service
public class AccountService {

    private AccountRepository accountRepository;
    private StockRepository stockRepository;
    private AccountStockRepository accountStockRepository;

    public AccountService(AccountRepository accountRepository, StockRepository stockRepository, AccountStockRepository accountStockRepository) {
        this.accountRepository = accountRepository;
        this.stockRepository = stockRepository;
        this.accountStockRepository = accountStockRepository;
    }

    public void associateStock(String accountsId, AssociateAccountStockDto assAccountStockDto) {

        var account = accountRepository.findById(UUID.fromString(accountsId))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        var stock = stockRepository.findById(assAccountStockDto.stockId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        // converter DTO -> Entity
        var id = new AccountStockId(account.getAccountId(), stock.getStockId());
        var entity = new AccountStock(
            id,
            account,
            stock,
            assAccountStockDto.quantity()
        );

        accountStockRepository.save(entity);
    }

    public List<AccountStockResponseDto> listStock(String accountsId) {

        var account = accountRepository.findById(UUID.fromString(accountsId))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        return account.getAccountStocks()
                .stream()
                .map(as ->
                        new AccountStockResponseDto(as.getStock().getStockId(), as.getQuantity(), 0.0))
                .toList();
    }
}