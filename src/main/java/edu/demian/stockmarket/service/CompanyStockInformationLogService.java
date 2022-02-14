package edu.demian.stockmarket.service;

import edu.demian.stockmarket.dto.CompanyStockInformationLog;
import edu.demian.stockmarket.repository.CompanyStockInformationLogRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class CompanyStockInformationLogService {

  private final CompanyStockInformationLogRepository companyStockInformationLogRepository;

  public CompanyStockInformationLogService(
      CompanyStockInformationLogRepository companyStockInformationLogRepository) {
    this.companyStockInformationLogRepository = companyStockInformationLogRepository;
  }

  public void save(CompanyStockInformationLog csil) {
    companyStockInformationLogRepository.save(csil);
  }

  public List<CompanyStockInformationLog> findAll() {
    return companyStockInformationLogRepository.findAll();
  }


}
