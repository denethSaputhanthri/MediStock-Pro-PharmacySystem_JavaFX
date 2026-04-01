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
public class Medicine {
    private Integer id;
    private String name;
    private String brand;
    private LocalDate expiry_date;
    private Integer quantity;
    private double purchase_price;
    private double selling_price;
    private String supplier_id;



}
