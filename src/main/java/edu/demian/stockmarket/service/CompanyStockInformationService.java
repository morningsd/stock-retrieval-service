package edu.demian.stockmarket.service;

import edu.demian.stockmarket.dto.CompanyStockInformation;
import edu.demian.stockmarket.repository.CompanyStockInformationRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class CompanyStockInformationService {

  private final CompanyStockInformationRepository companyStockInformationRepository;

  public CompanyStockInformationService(CompanyStockInformationRepository companyStockInformationRepository) {
    this.companyStockInformationRepository = companyStockInformationRepository;
  }

  public List<CompanyStockInformation> findAll() {
    return companyStockInformationRepository.findAll();
  }

  public void save(CompanyStockInformation companyStockInformation) {
    companyStockInformationRepository.save(companyStockInformation);
  }

  public void saveAll(Iterable<CompanyStockInformation> companyStockInformationList) {
    companyStockInformationRepository.saveAll(companyStockInformationList);
  }
}
