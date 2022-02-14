package edu.demian.stockmarket.repository;

import edu.demian.stockmarket.dto.CompanyStockInformationLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompanyStockInformationLogRepository extends JpaRepository<CompanyStockInformationLog, Long> {

}
