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
    private int medicine_id;
    private String name;
    private String brand;
    private LocalDate expiry_date;
    private Integer quantity;
    private Integer purchase_price;
    private Integer selling_price;
}
