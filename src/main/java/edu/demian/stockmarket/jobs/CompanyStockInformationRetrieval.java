package edu.demian.stockmarket.jobs;

import edu.demian.stockmarket.dto.Company;
import edu.demian.stockmarket.dto.CompanyStockInformation;
import edu.demian.stockmarket.dto.CompanyStockInformationLog;
import edu.demian.stockmarket.http.HttpUtils;
import edu.demian.stockmarket.http.StockRetrievalService;
import edu.demian.stockmarket.service.CompanyStockInformationLogService;
import edu.demian.stockmarket.service.CompanyStockInformationService;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Function;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class CompanyStockInformationRetrieval {

  private final StockRetrievalService stockRetrievalService;
  private final CompanyStockInformationService companyStockInformationService;
  private final CompanyStockInformationLogService companyStockInformationLogService;

  private static final int MAX_RETRIES_WHEN_CAUGHT_EXCEPTION = 10;
  private static final ExecutorService executorService =
      Executors.newFixedThreadPool(4 * Runtime.getRuntime().availableProcessors());

  public CompanyStockInformationRetrieval(
      StockRetrievalService stockRetrievalService,
      CompanyStockInformationService companyStockInformationService,
      CompanyStockInformationLogService companyStockInformationLogService) {
    this.stockRetrievalService = stockRetrievalService;
    this.companyStockInformationService = companyStockInformationService;
    this.companyStockInformationLogService = companyStockInformationLogService;
  }

  @Scheduled(fixedDelay = 1000)
  public void retrieveCompanyStockInformation() {
    System.out.println("In retrieve company stock information task");
    Company[] companies = AvailableCompaniesInformationRetrieval.companies;
    List<CompletableFuture<CompanyStockInformation>> completableFutures = new ArrayList<>();
    if (companies.length == 0) {
      return;
    }

    Arrays.stream(companies).parallel()
        .forEach(
            (c) -> {
              CompletableFuture<CompanyStockInformation> futureCompanyStockInformation =
                  getCompanyStockInformationAsync(c, executorService);

              futureCompanyStockInformation.thenAcceptAsync(this::saveCompanyStockInformation);
              completableFutures.add(futureCompanyStockInformation);
            });

    completableFutures.forEach(CompletableFuture::join);
  }

  public void saveCompanyStockInformation(CompanyStockInformation newCsi) {
    CompanyStockInformation csi = companyStockInformationService.findBySymbol(newCsi.getSymbol());
    if (csi == null) {
      companyStockInformationService.save(newCsi);
    } else {
      if (csi.equals(newCsi)) return;
      companyStockInformationService.replace(newCsi, csi.getId());

      CompanyStockInformationLog csil =
          CompanyStockInformationLog.builder()
              .companyName(newCsi.getCompanyName())
              .symbol(newCsi.getSymbol())
              .latestPrice(newCsi.getLatestPrice())
              .changePercent(newCsi.getChangePercent())
              .currency(newCsi.getCurrency())
              .avgTotalVolume(newCsi.getAvgTotalVolume())
              .latestVolume(newCsi.getLatestVolume())
              .previousVolume(newCsi.getPreviousVolume())
              .latestUpdate(newCsi.getLatestUpdate()).build();
      companyStockInformationLogService.save(csil);
    }

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

          return HttpUtils.checkStatusCodeAndReturnResponseBody(companyStockInformation, HttpStatus.OK);
        },
        executor);
  }

}
