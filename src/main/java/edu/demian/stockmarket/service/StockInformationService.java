package edu.demian.stockmarket.service;

import edu.demian.stockmarket.entity.StockInformation;
import edu.demian.stockmarket.exception.ResourceNotFoundException;
import edu.demian.stockmarket.repository.StockInformationRepository;
import java.util.List;
import javax.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class StockInformationService {

  private final StockInformationRepository stockInformationRepository;

  public StockInformationService(StockInformationRepository stockInformationRepository) {
    this.stockInformationRepository = stockInformationRepository;
  }

  public List<StockInformation> findAll() {
    return stockInformationRepository.findAll();
  }

  public void save(StockInformation stockInformation) {
    stockInformationRepository.save(stockInformation);
  }

  public <S extends StockInformation> List<S> saveAll(Iterable<S> entities) {
      return stockInformationRepository.saveAll(entities);
  }

  public StockInformation findBySymbol(String symbol) {
    return stockInformationRepository.findCompanyStockInformationBySymbol(symbol);
  }

  public List<StockInformation> find5WithTheBiggestLatestPrice() {
    return stockInformationRepository.findTop5ByOrderByLatestPriceDesc();
  }

  public List<StockInformation> find5WithTheBiggestChangePercent() {
    return stockInformationRepository.findTop5ByOrderByChangePercentDesc();
  }

  @Transactional
  public StockInformation replace(StockInformation newStockInformation, Long id) {
    return stockInformationRepository
        .findById(id)
        .map(
            csi -> {
              csi.setLatestPrice(newStockInformation.getLatestPrice());
              csi.setChangePercent(newStockInformation.getChangePercent());
              csi.setAvgTotalVolume(newStockInformation.getAvgTotalVolume());
              csi.setLatestVolume(newStockInformation.getLatestVolume());
              csi.setPreviousVolume(newStockInformation.getPreviousVolume());
              csi.setLatestUpdate(newStockInformation.getLatestUpdate());
              return csi;
            })
        .orElseThrow(() -> new ResourceNotFoundException("No company stock information with id: " + id + " was found"));
  }

}
