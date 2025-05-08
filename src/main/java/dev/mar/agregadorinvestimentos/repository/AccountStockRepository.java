package dev.mar.agregadorinvestimentos.repository;

import dev.mar.agregadorinvestimentos.entity.AccountStock;
import dev.mar.agregadorinvestimentos.entity.AccountStockId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AccountStockRepository extends JpaRepository<AccountStock, AccountStockId> {
}
