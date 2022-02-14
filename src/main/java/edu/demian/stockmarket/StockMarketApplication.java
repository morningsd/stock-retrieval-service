package edu.demian.stockmarket;

import edu.demian.stockmarket.dto.CompanyStockInformation;
import edu.demian.stockmarket.dto.CompanyStockInformationLog;
import edu.demian.stockmarket.service.CompanyStockInformationLogService;
import edu.demian.stockmarket.service.CompanyStockInformationService;
import java.util.List;
import java.util.concurrent.ExecutionException;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class StockMarketApplication implements CommandLineRunner {

  private final CompanyStockInformationService companyStockInformationService;
  private final CompanyStockInformationLogService companyStockInformationLogService;

  public StockMarketApplication(
      CompanyStockInformationService companyStockInformationService,
      CompanyStockInformationLogService companyStockInformationLogService) {
    this.companyStockInformationService = companyStockInformationService;
    this.companyStockInformationLogService = companyStockInformationLogService;
  }

  public static void main(String[] args) {
    SpringApplication.run(StockMarketApplication.class, args);
  }

  @Override
  public void run(String... args) throws ExecutionException, InterruptedException {
    System.out.println("Start");
    while (true) {
      Thread.sleep(10000);

      List<CompanyStockInformation> all = companyStockInformationService.findAll();
      System.out.println("CSI size: " + all.size());

      List<CompanyStockInformationLog> allLog = companyStockInformationLogService.findAll();
      System.out.println("CSIL size: " + allLog.size());
    }
  }






}
