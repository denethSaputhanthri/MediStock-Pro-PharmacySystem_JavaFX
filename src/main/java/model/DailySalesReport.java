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
public class DailySalesReport {
    private LocalDate date;
    private Integer totalBills;
    private Double totalSales;
    private Double averageBillAmount;
    private Integer totalItemsSold;
}
