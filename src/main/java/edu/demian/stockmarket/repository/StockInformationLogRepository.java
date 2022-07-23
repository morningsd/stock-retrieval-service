package edu.demian.stockmarket.repository;

import edu.demian.stockmarket.entity.StockInformationLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StockInformationLogRepository extends JpaRepository<StockInformationLog, Long> {

}
