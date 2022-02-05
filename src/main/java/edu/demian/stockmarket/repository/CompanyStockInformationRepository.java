package edu.demian.stockmarket.repository;

import edu.demian.stockmarket.dto.CompanyStockInformation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompanyStockInformationRepository extends JpaRepository<CompanyStockInformation, Long> {

}
