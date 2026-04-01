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
public class SaleAndBill {
    private Integer invoiceId;
    private String medicineName;
    private Double unitPrice;
    private Double discount;
    private Integer quantity;
    private LocalDate saleDate;
    private Double totalPrice;
}
