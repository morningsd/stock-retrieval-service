package edu.demian.stockmarket.service;

import edu.demian.stockmarket.dto.CompanyStockInformation;
import edu.demian.stockmarket.exception.ResourceNotFoundException;
import edu.demian.stockmarket.repository.CompanyStockInformationRepository;
import java.util.List;
import javax.transaction.Transactional;
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

  public CompanyStockInformation findBySymbol(String symbol) {
    return companyStockInformationRepository.findCompanyStockInformationBySymbol(symbol);
  }

  public List<CompanyStockInformation> find5WithTheBiggestLatestVolume() {
    return companyStockInformationRepository.findTop5ByOrderByLatestVolumeDesc();
  }

  public List<CompanyStockInformation> find5WithTheBiggestChangePercent() {
    return companyStockInformationRepository.findTop5ByOrderByChangePercentDesc();
  }

  @Transactional
  public CompanyStockInformation replace(CompanyStockInformation newCompanyStockInformation, Long id) {
    return companyStockInformationRepository
        .findById(id)
        .map(
            csi -> {
              csi.setLatestPrice(newCompanyStockInformation.getLatestPrice());
              csi.setChangePercent(newCompanyStockInformation.getChangePercent());
              csi.setAvgTotalVolume(newCompanyStockInformation.getAvgTotalVolume());
              csi.setLatestVolume(newCompanyStockInformation.getLatestVolume());
              csi.setPreviousVolume(newCompanyStockInformation.getPreviousVolume());
              csi.setLatestUpdate(newCompanyStockInformation.getLatestUpdate());
              return csi;
            })
        .orElseThrow(() -> new ResourceNotFoundException("No company stock information with id: " + id + " was found"));
  }

}
