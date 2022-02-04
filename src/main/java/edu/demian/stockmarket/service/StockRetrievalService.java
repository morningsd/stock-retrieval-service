package edu.demian.stockmarket.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class StockRetrievalService {

  private final RestTemplate restTemplate;

  private static final String GET_COMPANIES = "https://sandbox.iexapis.com/stable/ref-data/symbols?token=Tpk_ee567917a6b640bb8602834c9d30e571";
  private static final String GET_COMPANY_STOCK = "https://sandbox.iexapis.com/stable/stock/%s/quote?token=Tpk_ee567917a6b640bb8602834c9d30e571";

  public StockRetrievalService(RestTemplate restTemplate) {
    this.restTemplate = restTemplate;
  }

  public ResponseEntity<String> getCompanies() {
    return restTemplate.getForEntity(GET_COMPANIES, String.class);
  }

  public ResponseEntity<String> getCompanyStockInformation(String companySymbol) {
    return restTemplate.getForEntity(getCompanyStockLink(companySymbol), String.class);
  }

  public String getCompanyStockLink(String companySymbol) {
    return String.format(GET_COMPANY_STOCK, companySymbol);
  }
}
