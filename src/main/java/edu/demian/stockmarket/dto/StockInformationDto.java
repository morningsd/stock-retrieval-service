package edu.demian.stockmarket.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class StockInformationDto {

    private String symbol;

    private String companyName;

    private BigDecimal latestPrice;

    private double changePercent;

    private String currency;

    private BigDecimal avgTotalVolume;

    private BigDecimal latestVolume;

    private BigDecimal previousVolume;

    private long latestUpdate;

}
