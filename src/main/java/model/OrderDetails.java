package model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class OrderDetails {
    private Integer orderDetailId;
    private Integer billId;
    private Integer medicineId;
    private Integer quantity;
    private Double unitPrice;
}
