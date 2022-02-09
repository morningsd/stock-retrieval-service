package edu.demian.stockmarket;

import edu.demian.stockmarket.dto.Company;
import edu.demian.stockmarket.dto.CompanyStockInformation;
import edu.demian.stockmarket.exception.WrongStatusCodeException;
import edu.demian.stockmarket.http.StockRetrievalService;
import edu.demian.stockmarket.service.CompanyStockInformationService;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@SpringBootApplication
public class StockMarketApplication implements CommandLineRunner {

  private final StockRetrievalService stockRetrievalService;
  private final CompanyStockInformationService companyStockInformationService;

  public StockMarketApplication(
      StockRetrievalService stockRetrievalService,
      CompanyStockInformationService companyStockInformationService) {
    this.stockRetrievalService = stockRetrievalService;
    this.companyStockInformationService = companyStockInformationService;
  }

  public static void main(String[] args) {
    SpringApplication.run(StockMarketApplication.class, args);
  }

  @Override
  public void run(String... args) throws ExecutionException, InterruptedException {
    ScheduledExecutorService executorService =
        Executors.newScheduledThreadPool(Runtime.getRuntime().availableProcessors());

    Executor delayed = CompletableFuture.delayedExecutor(10L, TimeUnit.MILLISECONDS);

    CompletableFuture<Company[]> futureCompanies = getCompaniesAsync(executorService);
    Company[] companies = futureCompanies.get();

    for (Company c : companies) {
      CompletableFuture<CompanyStockInformation> futureCompanyStockInformation =
          getCompanyStockInformationAsync(c, delayed);

      futureCompanyStockInformation
          .thenAcceptAsync(companyStockInformationService::save)
          .whenComplete(
              (result, ex) -> {
                if (null != ex) {
                  ex.printStackTrace();
                }
              });
    }

    while (true) {
      Thread.sleep(10000);

      List<CompanyStockInformation> all = companyStockInformationService.findAll();
      System.out.println("Retrieved " + all.size() + " instances");
      for (CompanyStockInformation csi : all) {
        System.out.println(csi);
      }
    }
  }

  public CompletableFuture<Company[]> getCompaniesAsync(ExecutorService executor) {
    return CompletableFuture.supplyAsync(
        () -> {
          ResponseEntity<Company[]> companies = stockRetrievalService.getCompanies();
          return checkStatusCodeAndReturnResponseBody(companies, HttpStatus.OK);
        },
        executor);
  }

  public CompletableFuture<CompanyStockInformation> getCompanyStockInformationAsync(
      Company company, Executor executor) {
    return CompletableFuture.supplyAsync(
        () -> {
          ResponseEntity<CompanyStockInformation> companyStockInformation =
              stockRetrievalService.getCompanyStockInformation(company);
          return checkStatusCodeAndReturnResponseBody(
              companyStockInformation, HttpStatus.OK);
        },
        executor);
  }

  private <T> T checkStatusCodeAndReturnResponseBody(
      ResponseEntity<T> responseEntity, HttpStatus expected) {
    HttpStatus actual = responseEntity.getStatusCode();
    if (actual == expected) {
      return responseEntity.getBody();
    }
    throw new WrongStatusCodeException(
        "Status code = " + actual + ", " + expected + " is expected");
  }
}
