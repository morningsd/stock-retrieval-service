package edu.demian.stockmarket;

import edu.demian.stockmarket.dto.CompanyStockInformation;
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

  public StockMarketApplication(
      CompanyStockInformationService companyStockInformationService) {
    this.companyStockInformationService = companyStockInformationService;
  }

  public static void main(String[] args) {
    SpringApplication.run(StockMarketApplication.class, args);
  }

  @Override
  public void run(String... args) throws ExecutionException, InterruptedException {
    while (true) {
      Thread.sleep(10000);

      List<CompanyStockInformation> all = companyStockInformationService.findAll();
      System.out.println("Retrieved " + all.size() + " instances");
    }
  }






}
