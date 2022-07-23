package edu.demian.stockmarket.repository;

import edu.demian.stockmarket.entity.StockInformation;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface StockInformationRepository extends JpaRepository<StockInformation, Long> {

  @Query(value = "SELECT * FROM company_stock cs WHERE cs.latest_price IS NOT NULL ORDER BY cs.latest_price DESC LIMIT 5", nativeQuery = true)
  List<StockInformation> findTop5ByOrderByLatestPriceDesc();

  @Query(value = "SELECT * FROM company_stock cs WHERE cs.change_percent IS NOT NULL ORDER BY cs.change_percent DESC LIMIT 5", nativeQuery = true)
  List<StockInformation> findTop5ByOrderByChangePercentDesc();

  StockInformation findCompanyStockInformationBySymbol(String symbol);

}
