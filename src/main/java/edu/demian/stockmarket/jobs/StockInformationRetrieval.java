package edu.demian.stockmarket.jobs;

import edu.demian.stockmarket.dto.CompanyDto;
import edu.demian.stockmarket.entity.StockInformation;
import edu.demian.stockmarket.service.StockInformationLogService;
import edu.demian.stockmarket.service.StockInformationService;
import edu.demian.stockmarket.service.StockRetrievalService;
import edu.demian.stockmarket.util.HttpUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.*;
import java.util.stream.Collectors;

@Slf4j
@Component
public class StockInformationRetrieval {

    private static final ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    private final StockRetrievalService stockRetrievalService;
    private final StockInformationService stockInformationService;
    private final StockInformationLogService stockInformationLogService;

    public StockInformationRetrieval(
            StockRetrievalService stockRetrievalService,
            StockInformationService stockInformationService,
            StockInformationLogService stockInformationLogService) {
        this.stockRetrievalService = stockRetrievalService;
        this.stockInformationService = stockInformationService;
        this.stockInformationLogService = stockInformationLogService;
    }

    @Scheduled(fixedDelay = 1000)
    public void retrieveCompanyStockInformation() throws ExecutionException, InterruptedException {
        System.out.println("In retrieve company stock information task");
        CompletableFuture<List<CompanyDto>> companiesFuture = stockRetrievalService.getCompaniesAsync();
        long start = System.currentTimeMillis();
        List<CompanyDto> companies = companiesFuture.get();
        long finish = System.currentTimeMillis();
        log.debug("Companies retrieval took {} seconds", (finish - start) / 1000);
        System.out.println("Companies retrieval took " + (finish - start) / 1000 + " seconds, companies.size() == " + companies.size());

        if (companies.size() == 0) {
            return;
        }

        System.out.println("Before retrieving stock information");

        List<CompletableFuture<StockInformation>> completableFutures = companies.stream().map(company -> getCompanyStockInformationAsync(company, executorService)).collect(Collectors.toList());
        CompletableFuture<Void> allFutures = CompletableFuture.allOf(completableFutures.toArray(new CompletableFuture[0]));
        CompletableFuture<List<StockInformation>> allCompletableFuture = allFutures.thenApply(future -> completableFutures.stream()
                .map(CompletableFuture::join)
                .collect(Collectors.toList()));

        start = System.currentTimeMillis();
        List<StockInformation> stockInformationList = allCompletableFuture.get().stream().filter(Objects::nonNull).collect(Collectors.toList());
        finish = System.currentTimeMillis();
        System.out.println("Stock information retrieval took " + (finish - start) / 1000 + " seconds, stockInformationList.size() == " + stockInformationList.size());

        stockInformationService.saveAll(stockInformationList);
        System.out.println("Saved = " + stockInformationList.size());
    }

//    public void saveCompanyStockInformation(StockInformation newCsi) {
//        StockInformation csi = stockInformationService.findBySymbol(newCsi.getSymbol());
//        if (csi == null) {
//            stockInformationService.save(newCsi);
//        } else {
//            if (csi.equals(newCsi)) return;
//            stockInformationService.replace(newCsi, csi.getId());
//
//            StockInformationLog csil =
//                    StockInformationLog.builder()
//                            .companyName(newCsi.getCompanyName())
//                            .symbol(newCsi.getSymbol())
//                            .latestPrice(newCsi.getLatestPrice())
//                            .changePercent(newCsi.getChangePercent())
//                            .currency(newCsi.getCurrency())
//                            .avgTotalVolume(newCsi.getAvgTotalVolume())
//                            .latestVolume(newCsi.getLatestVolume())
//                            .previousVolume(newCsi.getPreviousVolume())
//                            .latestUpdate(newCsi.getLatestUpdate()).build();
//            stockInformationLogService.save(csil);
//        }
//    }

    public CompletableFuture<StockInformation> getCompanyStockInformationAsync(CompanyDto companyDto, Executor executor) {
        return retrieveCompanyStockInfo(companyDto, executor).exceptionally(t -> null);
    }

    private CompletableFuture<StockInformation> retrieveCompanyStockInfo(CompanyDto companyDto, Executor executor) {
        return CompletableFuture.supplyAsync(
                () -> {
                    ResponseEntity<StockInformation> companyStockInformation =
                            stockRetrievalService.getCompanyStockInformation(companyDto);

                    return HttpUtils.checkStatusCodeAndReturnResponseBody(companyStockInformation, HttpStatus.OK);
                },
                executor);
    }

}
