package service.custom.impl;

import model.OrderDetails;
import repository.RepositoryFactory;
import repository.custom.OrderDetailsRepository;
import service.custom.OrderDetailsService;
import util.RepositoryType;

import java.util.List;

public class OrderDetailsServiceImpl implements OrderDetailsService {
    OrderDetailsRepository orderDetailsRepository = RepositoryFactory.getInstance().getRepositoryType(RepositoryType.ORDERDETAILS);

    @Override
    public List<OrderDetails> getAll() {
        return orderDetailsRepository.getAll();
    }

    @Override
    public boolean addOrderDetail(OrderDetails orderDetails) {
        if (!validateOrderDetails(orderDetails)) {
            return false;
        }
        return orderDetailsRepository.create(orderDetails);
    }

    @Override
    public boolean addOrderDetailsBatch(List<OrderDetails> orderDetailsList) {
        if (orderDetailsList == null || orderDetailsList.isEmpty()) {
            return false;
        }

        for (OrderDetails orderDetails : orderDetailsList) {
            if (!validateOrderDetails(orderDetails)) {
                return false;
            }
        }

        return orderDetailsRepository.createBatch(orderDetailsList);
    }

    @Override
    public boolean updateOrderDetail(OrderDetails orderDetails) {
        if (orderDetails == null || orderDetails.getOrderDetailId() == null) {
            return false;
        }
        if (!validateOrderDetails(orderDetails)) {
            return false;
        }
        return orderDetailsRepository.update(orderDetails);
    }

    @Override
    public boolean deleteOrderDetail(Integer orderDetailId) {
        if (orderDetailId == null || orderDetailId <= 0) {
            return false;
        }
        return orderDetailsRepository.deleteById(orderDetailId);
    }

    @Override
    public boolean deleteOrderDetailsByBillId(Integer billId) {
        if (billId == null || billId <= 0) {
            return false;
        }
        return orderDetailsRepository.deleteByBillId(billId);
    }

    @Override
    public OrderDetails searchById(Integer orderDetailId) {
        if (orderDetailId == null || orderDetailId <= 0) {
            return null;
        }
        return orderDetailsRepository.searchById(orderDetailId);
    }

    @Override
    public List<OrderDetails> getOrderDetailsByBillId(Integer billId) {
        if (billId == null || billId <= 0) {
            return null;
        }
        return orderDetailsRepository.getByBillId(billId);
    }

    @Override
    public Double calculateTotalForBill(Integer billId) {
        if (billId == null || billId <= 0) {
            return 0.0;
        }

        List<OrderDetails> orderDetailsList = orderDetailsRepository.getByBillId(billId);
        if (orderDetailsList == null || orderDetailsList.isEmpty()) {
            return 0.0;
        }

        double total = 0.0;
        for (OrderDetails orderDetails : orderDetailsList) {
            if (orderDetails.getQuantity() != null && orderDetails.getUnitPrice() != null) {
                total += orderDetails.getQuantity() * orderDetails.getUnitPrice();
            }
        }

        return total;
    }

    private boolean validateOrderDetails(OrderDetails orderDetails) {
        if (orderDetails == null) {
            return false;
        }
        if (orderDetails.getBillId() == null || orderDetails.getBillId() <= 0) {
            return false;
        }
        if (orderDetails.getMedicineId() == null || orderDetails.getMedicineId() <= 0) {
            return false;
        }
        if (orderDetails.getQuantity() == null || orderDetails.getQuantity() <= 0) {
            return false;
        }
        if (orderDetails.getUnitPrice() == null || orderDetails.getUnitPrice() < 0) {
            return false;
        }
        return true;
    }
}
