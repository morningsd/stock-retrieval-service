package edu.demian.stockmarket.service;

import edu.demian.stockmarket.dto.CompanyDto;
import edu.demian.stockmarket.entity.StockInformation;
import edu.demian.stockmarket.util.HttpUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
public class StockRetrievalService {

    private final RestTemplate restTemplate;

    private static final String GET_COMPANIES_SYMBOLS =
            "https://sandbox.iexapis.com/stable/ref-data/symbols?token=Tpk_ee567917a6b640bb8602834c9d30e571&filter=symbol";
    private static final String GET_COMPANY_STOCK =
            "https://sandbox.iexapis.com/stable/stock/%s/quote?token=Tpk_ee567917a6b640bb8602834c9d30e571";

    public StockRetrievalService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }


    public ResponseEntity<List<CompanyDto>> getCompanies() {
        return restTemplate.exchange(GET_COMPANIES_SYMBOLS,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {
                });
    }

    public CompletableFuture<List<CompanyDto>> getCompaniesAsync() {
        return CompletableFuture.supplyAsync(
                () -> {
                    log.debug("Retrieving companies");
                    ResponseEntity<List<CompanyDto>> companies = getCompanies();
                    return HttpUtils.checkStatusCodeAndReturnResponseBody(companies, HttpStatus.OK);
                });
    }

    public ResponseEntity<StockInformation> getCompanyStockInformation(CompanyDto companyDto) {
        return restTemplate.getForEntity(getCompanyStockLink(companyDto.getSymbol()), StockInformation.class);
    }

    public String getCompanyStockLink(String companySymbol) {
        return String.format(GET_COMPANY_STOCK, companySymbol);
    }
}
