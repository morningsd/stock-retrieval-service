package edu.demian.stockmarket;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.demian.stockmarket.dto.Company;
import edu.demian.stockmarket.dto.CompanyStockInformation;
import edu.demian.stockmarket.service.StockRetrievalService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.ResponseEntity;

@SpringBootApplication
public class StockMarketApplication implements CommandLineRunner {

  private final ObjectMapper mapper;
  private final StockRetrievalService retrievalService;

  public StockMarketApplication(ObjectMapper mapper,
      StockRetrievalService retrievalService) {
    this.mapper = mapper;
    this.retrievalService = retrievalService;
  }

  public static void main(String[] args) {
    SpringApplication.run(StockMarketApplication.class, args);
  }

  @Override
  public void run(String... args) throws Exception {
    ResponseEntity<String> response = retrievalService.getCompanies();
    Company[] companyList = mapper.readValue(response.getBody(), Company[].class);
    for (Company c : companyList) {
      System.out.println(c);
    }

    Company c = companyList[0];
    response = retrievalService.getCompanyStockInformation(c.getSymbol());
    CompanyStockInformation stockInformation = mapper.readValue(response.getBody(), CompanyStockInformation.class);
    System.out.println(stockInformation);
  }
}
