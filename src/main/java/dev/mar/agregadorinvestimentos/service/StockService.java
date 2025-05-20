package dev.mar.agregadorinvestimentos.service;

import dev.mar.agregadorinvestimentos.controller.dto.CreateStockDto;
import dev.mar.agregadorinvestimentos.entity.Stock;
import dev.mar.agregadorinvestimentos.repository.StockRepository;
import org.springframework.stereotype.Service;

@Service
public class StockService {

    private StockRepository stockRepository;

    public StockService(StockRepository stockRepository) {
        this.stockRepository = stockRepository;
    }

    public void createStock(CreateStockDto createStockDto) {

        // Converter DTO -> ENTITY
        var stock = new Stock(
                createStockDto.stockId(),
                createStockDto.description()
        );

        stockRepository.save(stock);

    }
}
