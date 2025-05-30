package dev.mar.agregadorinvestimentos.controller;

import dev.mar.agregadorinvestimentos.controller.dto.AccountStockResponseDto;
import dev.mar.agregadorinvestimentos.controller.dto.AssociateAccountStockDto;
import dev.mar.agregadorinvestimentos.controller.dto.CreateAccountDto;
import dev.mar.agregadorinvestimentos.service.AccountService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/accounts")
public class AccountController {

    private AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping("/{accountsId}/stocks")
    public ResponseEntity<Void> associateStock(@PathVariable("accountsId") String accountsId,
                                               @RequestBody AssociateAccountStockDto assAccountStockDto) {
        accountService.associateStock(accountsId, assAccountStockDto);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{accountsId}/stocks")
    public ResponseEntity<List<AccountStockResponseDto>> listStock(@PathVariable("accountsId") String accountsId) {
        var stocks = accountService.listStock(accountsId);
        return ResponseEntity.ok(stocks);
    }

}
