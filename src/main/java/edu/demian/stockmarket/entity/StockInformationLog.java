package edu.demian.stockmarket.entity;

import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Builder;
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
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "company_stock_log")
public class StockInformationLog {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @EqualsAndHashCode.Exclude
  private long id;

  private String symbol;

  private String companyName;

  private BigDecimal latestPrice;

  private double changePercent;

  private String currency;

  private BigDecimal avgTotalVolume;

  private BigDecimal latestVolume;

  private BigDecimal previousVolume;

  @EqualsAndHashCode.Exclude
  private long latestUpdate;

  @EqualsAndHashCode.Exclude
  @CreationTimestamp
  @Temporal(TemporalType.TIMESTAMP)
  private Date createdAt;

}
