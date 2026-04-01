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

    public CartItem(Integer medicineId, String medicineName, Integer quantity, Double unitPrice) {
        this.medicineId = medicineId;
        this.medicineName = medicineName;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.total = quantity * unitPrice;
    }

    public void calculateTotal() {
        this.total = this.quantity * this.unitPrice;
    }
}
