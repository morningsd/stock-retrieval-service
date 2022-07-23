package edu.demian.stockmarket.entity;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@EqualsAndHashCode
@Table(name = "company_stock")
public class
StockInformation {

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
