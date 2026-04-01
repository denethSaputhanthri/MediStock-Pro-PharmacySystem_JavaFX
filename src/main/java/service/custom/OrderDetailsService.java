package service.custom;

import model.OrderDetails;
import service.SuperService;

import java.util.List;

public interface OrderDetailsService extends SuperService {
    List<OrderDetails> getAll();
    boolean addOrderDetail(OrderDetails orderDetails);
    boolean addOrderDetailsBatch(List<OrderDetails> orderDetailsList);
    boolean updateOrderDetail(OrderDetails orderDetails);
    boolean deleteOrderDetail(Integer orderDetailId);
    boolean deleteOrderDetailsByBillId(Integer billId);
    OrderDetails searchById(Integer orderDetailId);
    List<OrderDetails> getOrderDetailsByBillId(Integer billId);
    Double calculateTotalForBill(Integer billId);
}
