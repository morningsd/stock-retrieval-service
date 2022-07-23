package edu.demian.stockmarket.service;

import edu.demian.stockmarket.entity.StockInformationLog;
import edu.demian.stockmarket.repository.StockInformationLogRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class StockInformationLogService {

  private final StockInformationLogRepository stockInformationLogRepository;

  public StockInformationLogService(
      StockInformationLogRepository stockInformationLogRepository) {
    this.stockInformationLogRepository = stockInformationLogRepository;
  }

  public void save(StockInformationLog csil) {
    stockInformationLogRepository.save(csil);
  }

  public List<StockInformationLog> findAll() {
    return stockInformationLogRepository.findAll();
  }


}
