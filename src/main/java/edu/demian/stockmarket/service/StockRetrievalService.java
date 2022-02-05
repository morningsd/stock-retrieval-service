package edu.demian.stockmarket.service;

import edu.demian.stockmarket.dto.Company;
import edu.demian.stockmarket.dto.CompanyStockInformation;
import java.util.List;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class StockRetrievalService {

  private final RestTemplate restTemplate;

  private static final String GET_COMPANIES_SYMBOLS = "https://sandbox.iexapis.com/stable/ref-data/symbols?token=Tpk_ee567917a6b640bb8602834c9d30e571&filter=symbol";
  private static final String GET_COMPANY_STOCK = "https://sandbox.iexapis.com/stable/stock/%s/quote?token=Tpk_ee567917a6b640bb8602834c9d30e571";

  public StockRetrievalService(RestTemplate restTemplate) {
    this.restTemplate = restTemplate;
  }

  public ResponseEntity<List<Company>> getCompanies() {
    return restTemplate.exchange(GET_COMPANIES_SYMBOLS, HttpMethod.GET, null,
        new ParameterizedTypeReference<List<Company>>() {});
  }

  public ResponseEntity<CompanyStockInformation> getCompanyStockInformation(String companySymbol) {
    return restTemplate.getForEntity(getCompanyStockLink(companySymbol), CompanyStockInformation.class);
  }

  public String getCompanyStockLink(String companySymbol) {
    return String.format(GET_COMPANY_STOCK, companySymbol);
  }
}
