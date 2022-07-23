package edu.demian.stockmarket.jobs;

import edu.demian.stockmarket.entity.StockInformation;
import edu.demian.stockmarket.service.StockInformationService;
import java.util.List;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class CompaniesStatisticsRetrieval {

  private final StockInformationService stockInformationService;

  public CompaniesStatisticsRetrieval(
      StockInformationService stockInformationService) {
    this.stockInformationService = stockInformationService;
  }

  @Scheduled(cron = "*/5 * * * * *")
  public void retrieveStatistics() {
    System.out.println("\n\n############################################################################################");
    System.out.println("Top 5 highest value stocks:");
    List<StockInformation> stockInformationList = stockInformationService.find5WithTheBiggestLatestPrice();
    for (StockInformation csi : stockInformationList) {
      System.out.println(csi);
    }

    System.out.println("\nTop 5 companies with the biggest change percent:");
    stockInformationList = stockInformationService.find5WithTheBiggestChangePercent();
    for (StockInformation csi : stockInformationList) {
      System.out.println(csi);
    }
    System.out.println("############################################################################################");
  }

}
