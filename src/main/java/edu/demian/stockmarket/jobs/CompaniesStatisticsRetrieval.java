package edu.demian.stockmarket.jobs;

import edu.demian.stockmarket.dto.CompanyStockInformation;
import edu.demian.stockmarket.service.CompanyStockInformationService;
import java.util.List;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class CompaniesStatisticsRetrieval {

  private final CompanyStockInformationService companyStockInformationService;

  public CompaniesStatisticsRetrieval(
      CompanyStockInformationService companyStockInformationService) {
    this.companyStockInformationService = companyStockInformationService;
  }

  @Scheduled(cron = "*/5 * * * * *")
  public void retrieveStatistics() {
    System.out.println("\n\nTop 5 highest value stocks:");
    List<CompanyStockInformation> companyStockInformationList = companyStockInformationService.find5WithTheBiggestLatestVolume();
    for (CompanyStockInformation csi : companyStockInformationList) {
      System.out.println("Symbol: " + csi.getSymbol() + ", name: " + csi.getCompanyName() + ", latest volume: " + csi.getLatestVolume());
    }

    System.out.println("Top 5 companies with the biggest change percent:");
    companyStockInformationList = companyStockInformationService.find5WithTheBiggestChangePercent();
    for (CompanyStockInformation csi : companyStockInformationList) {
      System.out.println("Symbol: " + csi.getSymbol() + ", name: " + csi.getCompanyName() + ", change percent: " + csi.getChangePercent());
    }
  }

}
