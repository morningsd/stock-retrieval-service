package edu.demian.stockmarket.http;

import edu.demian.stockmarket.dto.Company;
import edu.demian.stockmarket.dto.CompanyStockInformation;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

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

  public ResponseEntity<Company[]> getCompanies() {
    return restTemplate.getForEntity(GET_COMPANIES_SYMBOLS, Company[].class);
  }

  public ResponseEntity<CompanyStockInformation> getCompanyStockInformation(Company company) {
    return restTemplate.getForEntity(
        getCompanyStockLink(company.getSymbol()), CompanyStockInformation.class);
  }

  public String getCompanyStockLink(String companySymbol) {
    return String.format(GET_COMPANY_STOCK, companySymbol);
  }
}
