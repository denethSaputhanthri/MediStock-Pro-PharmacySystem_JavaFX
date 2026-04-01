package model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class MonthlySalesReport {
    private Integer year;
    private Integer month;
    private String monthName;
    private Integer totalBills;
    private Double totalSales;
    private Double averageBillAmount;
    private Integer totalItemsSold;
}
