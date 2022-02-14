package edu.demian.stockmarket.jobs;

import edu.demian.stockmarket.dto.Company;
import edu.demian.stockmarket.http.HttpUtils;
import edu.demian.stockmarket.http.StockRetrievalService;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class AvailableCompaniesInformationRetrieval {

  private final StockRetrievalService stockRetrievalService;

  public static Company[] companies = new Company[0];

  public AvailableCompaniesInformationRetrieval(StockRetrievalService stockRetrievalService) {
    this.stockRetrievalService = stockRetrievalService;
  }

  @Scheduled(fixedDelay = 1000 * 60 * 5)
  public void retrieveCompanyStockInformation() throws ExecutionException, InterruptedException {
    System.out.println("In retrieve companies task");
    CompletableFuture<Company[]> futureCompanies = getCompaniesAsync();
    companies = futureCompanies.get();
  }

  public CompletableFuture<Company[]> getCompaniesAsync() {
    return CompletableFuture.supplyAsync(
        () -> {
          ResponseEntity<Company[]> companies = stockRetrievalService.getCompanies();
          return HttpUtils.checkStatusCodeAndReturnResponseBody(companies, HttpStatus.OK);
        });
  }
}
