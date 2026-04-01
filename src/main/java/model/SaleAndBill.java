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
    private Integer id;
    private Integer quantity_sold;
    private LocalDate sale_date;
    private boolean total_price;
}
