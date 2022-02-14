package edu.demian.stockmarket.repository;

import edu.demian.stockmarket.dto.CompanyStockInformation;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CompanyStockInformationRepository extends JpaRepository<CompanyStockInformation, Long> {

  @Query(value = "SELECT * FROM company_stock cs WHERE cs.latest_volume IS NOT NULL ORDER BY cs.latest_volume DESC LIMIT 5", nativeQuery = true)
  List<CompanyStockInformation> findTop5ByOrderByLatestVolumeDesc();

  @Query(value = "SELECT * FROM company_stock cs WHERE cs.change_percent IS NOT NULL ORDER BY cs.change_percent DESC LIMIT 5", nativeQuery = true)
  List<CompanyStockInformation> findTop5ByOrderByChangePercentDesc();

  CompanyStockInformation findCompanyStockInformationBySymbol(String symbol);

}
