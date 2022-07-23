package edu.demian.stockmarket.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@Table(name = "company_stock")
public class StockInformation {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @EqualsAndHashCode.Exclude
    private long id;

    private String symbol;

    private String companyName;

    private BigDecimal latestPrice;

    private double changePercent;

    private String currency;

    @EqualsAndHashCode.Exclude
    private long latestUpdate;

    @Override
    public String toString() {
        return "Stock[" + symbol + "]: " +
                companyName +
                " --- latest price=" + latestPrice +
                " " + currency +
                ", change percent=" + changePercent;
    }
}
