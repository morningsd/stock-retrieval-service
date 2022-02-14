package edu.demian.stockmarket.dto;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@Table(name = "company_stock")
public class CompanyStockInformation {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  private String symbol;

  private String companyName;

  private BigDecimal latestPrice;

  private double changePercent;

  private String currency;

  private BigDecimal avgTotalVolume;

  private BigDecimal latestVolume;

  private BigDecimal previousVolume;

  private long latestUpdate;

  @CreationTimestamp
  @Temporal(TemporalType.TIMESTAMP)
  private Date createdAt;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CompanyStockInformation that = (CompanyStockInformation) o;
    return Double.compare(that.changePercent, changePercent) == 0
        && Objects.equals(symbol, that.symbol) && Objects.equals(companyName,
        that.companyName) && Objects.equals(latestPrice, that.latestPrice)
        && Objects.equals(currency, that.currency) && Objects.equals(
        avgTotalVolume, that.avgTotalVolume) && Objects.equals(latestVolume,
        that.latestVolume) && Objects.equals(previousVolume, that.previousVolume);
  }

  @Override
  public int hashCode() {
    return Objects.hash(symbol, companyName, latestPrice, changePercent, currency, avgTotalVolume,
        latestVolume, previousVolume);
  }
}
