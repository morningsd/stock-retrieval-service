package edu.demian.stockmarket;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.demian.stockmarket.dto.Company;
import edu.demian.stockmarket.dto.CompanyStockInformation;
import edu.demian.stockmarket.service.CompanyStockInformationService;
import edu.demian.stockmarket.service.StockRetrievalService;
import java.util.List;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class StockMarketApplication implements CommandLineRunner {

  private final ObjectMapper mapper;
  private final StockRetrievalService retrievalService;
  private final CompanyStockInformationService stockInformationService;

  public StockMarketApplication(ObjectMapper mapper,
      StockRetrievalService retrievalService,
      CompanyStockInformationService stockInformationService) {
    this.mapper = mapper;
    this.retrievalService = retrievalService;
    this.stockInformationService = stockInformationService;
  }

  public static void main(String[] args) {
    SpringApplication.run(StockMarketApplication.class, args);
  }

  @Override
  public void run(String... args) {
    List<Company> response = retrievalService.getCompanies().getBody();
    for (Company c : response) {
      System.out.println(c);
    }

    System.out.println("Company numberP = " + response.size());

    Company c = response.get(0);
    CompanyStockInformation response2 = retrievalService.getCompanyStockInformation(c.getSymbol()).getBody();
    System.out.println(response2);

    stockInformationService.save(response2);

    List<CompanyStockInformation> all = stockInformationService.findAll();

    for (CompanyStockInformation i : all) {
      System.out.println(i);
    }

  }
}
