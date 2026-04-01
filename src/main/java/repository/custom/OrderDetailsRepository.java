package repository.custom;

import model.OrderDetails;
import repository.CrudRepository;

import java.util.List;

public interface OrderDetailsRepository extends CrudRepository<OrderDetails, Integer> {
    Boolean createBatch(List<OrderDetails> orderDetailsList);
    List<OrderDetails> getByBillId(Integer billId);
    Boolean deleteByBillId(Integer billId);
}
