package edu.demian.stockmarket.dto;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
public class CompanyStockInformation {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  private String symbol;

  private String companyName;

  private String latestPrice;

  private String changePercent;

  private String currency;

  private String avgTotalVolume;

  private String latestVolume;

  private String previousVolume;

  private String latestUpdate;

}
