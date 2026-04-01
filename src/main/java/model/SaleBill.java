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
public class SaleBill {
    private Integer billId;
    private LocalDate date;
    private Double totalAmount;
    private String customerName;
}
