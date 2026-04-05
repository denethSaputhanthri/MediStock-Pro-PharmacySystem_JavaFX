package model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CartItem {
    private Integer medicineId;
    private String medicineName;
    private Integer quantity;
    private Double unitPrice;
    private Double total;
    private Double discount;
}
