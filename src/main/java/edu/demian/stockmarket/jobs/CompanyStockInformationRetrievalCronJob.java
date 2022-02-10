package edu.demian.stockmarket.jobs;

import edu.demian.stockmarket.dto.Company;
import edu.demian.stockmarket.dto.CompanyStockInformation;
import edu.demian.stockmarket.exception.WrongStatusCodeException;
import edu.demian.stockmarket.http.StockRetrievalService;
import edu.demian.stockmarket.service.CompanyStockInformationService;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Function;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class CompanyStockInformationRetrievalCronJob {

  private final StockRetrievalService stockRetrievalService;
  private final CompanyStockInformationService companyStockInformationService;

  private static final int MAX_RETRIES_WHEN_CAUGHT_EXCEPTION = 10;
  private static final ExecutorService executorService =
      Executors.newFixedThreadPool(4 * Runtime.getRuntime().availableProcessors());
  private Company[] companies = new Company[0];

  public CompanyStockInformationRetrievalCronJob(
      StockRetrievalService stockRetrievalService,
      CompanyStockInformationService companyStockInformationService) {
    this.stockRetrievalService = stockRetrievalService;
    this.companyStockInformationService = companyStockInformationService;
  }

  @Scheduled(fixedDelay = 1000)
  public void retrieveCompanyStockInformation() throws ExecutionException, InterruptedException {
    List<CompletableFuture<CompanyStockInformation>> list = new ArrayList<>();
    System.out.println("In cron task");
    if (companies.length == 0) {
      CompletableFuture<Company[]> futureCompanies = getCompaniesAsync(executorService);
      companies = futureCompanies.get();
    }

    for (Company c : companies) {
      CompletableFuture<CompanyStockInformation> futureCompanyStockInformation =
          getCompanyStockInformationAsync(c, executorService);

      futureCompanyStockInformation.thenAcceptAsync(companyStockInformationService::save);
      list.add(futureCompanyStockInformation);
    }
    list.forEach(CompletableFuture::join);

    //    Arrays.stream(companies)/*.parallel()*/.forEach((c) -> {
    //      CompletableFuture<CompanyStockInformation> futureCompanyStockInformation =
    //          getCompanyStockInformationAsync(c, executorService);
    //
    //      futureCompanyStockInformation.thenAcceptAsync(companyStockInformationService::save);
    //    });
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
    CompletableFuture<CompanyStockInformation> future = retrieveCompanyStockInfo(company, executor);
    for (int i = 0; i < MAX_RETRIES_WHEN_CAUGHT_EXCEPTION; i++) {
      future =
          future
              .thenApply(CompletableFuture::completedFuture)
              .exceptionally(t -> retrieveCompanyStockInfo(company, executor))
              .thenCompose(Function.identity());
    }
    return future;
  }

  private CompletableFuture<CompanyStockInformation> retrieveCompanyStockInfo(
      Company company, Executor executor) {
    return CompletableFuture.supplyAsync(
        () -> {
          ResponseEntity<CompanyStockInformation> companyStockInformation =
              stockRetrievalService.getCompanyStockInformation(company);
          return checkStatusCodeAndReturnResponseBody(companyStockInformation, HttpStatus.OK);
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
