package model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class TopSellingMedicine {
    private Integer medicineId;
    private String medicineName;
    private Integer totalQuantitySold;
    private Double totalRevenue;
    private Integer numberOfOrders;
}
