package edu.demian.stockmarket.service;

import edu.demian.stockmarket.entity.StockInformation;
import edu.demian.stockmarket.repository.StockInformationRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StockInformationService {

  private final StockInformationRepository stockInformationRepository;

  public StockInformationService(StockInformationRepository stockInformationRepository) {
    this.stockInformationRepository = stockInformationRepository;
  }

  public <S extends StockInformation> void saveAll(Iterable<S> entities) {
       stockInformationRepository.saveAll(entities);
  }

  public List<StockInformation> find5WithTheBiggestLatestPrice() {
    return stockInformationRepository.findTop5ByOrderByLatestPriceDesc();
  }

  public List<StockInformation> find5WithTheBiggestChangePercent() {
    return stockInformationRepository.findTop5ByOrderByChangePercentDesc();
  }

}
