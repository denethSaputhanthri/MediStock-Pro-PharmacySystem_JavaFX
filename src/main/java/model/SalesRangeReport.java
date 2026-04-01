package model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class SalesRangeReport {
    private LocalDate startDate;
    private LocalDate endDate;
    private Integer totalBills;
    private Double totalSales;
    private Double averageDailySales;
    private Integer totalItemsSold;
    private Integer daysInRange;
}
