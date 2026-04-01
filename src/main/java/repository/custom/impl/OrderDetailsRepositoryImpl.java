package repository.custom.impl;

import db.DBConnection;
import model.OrderDetails;
import repository.custom.OrderDetailsRepository;
import util.CrudUtill;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OrderDetailsRepositoryImpl implements OrderDetailsRepository {

    @Override
    public Boolean create(OrderDetails orderDetails) {
        try {
            return CrudUtill.execute("INSERT INTO order_details VALUES(?,?,?,?,?)",
                    orderDetails.getOrderDetailId(),
                    orderDetails.getBillId(),
                    orderDetails.getMedicineId(),
                    orderDetails.getQuantity(),
                    orderDetails.getUnitPrice()
            );
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Boolean createBatch(List<OrderDetails> orderDetailsList) {
        if (orderDetailsList == null || orderDetailsList.isEmpty()) {
            return false;
        }

        Connection connection = null;
        PreparedStatement psTm = null;

        try {
            connection = DBConnection.getInstance().getConnection();
            connection.setAutoCommit(false);

            String sql = "INSERT INTO order_details VALUES(?,?,?,?,?)";
            psTm = connection.prepareStatement(sql);

            for (OrderDetails orderDetails : orderDetailsList) {
                psTm.setInt(1, orderDetails.getOrderDetailId());
                psTm.setInt(2, orderDetails.getBillId());
                psTm.setInt(3, orderDetails.getMedicineId());
                psTm.setInt(4, orderDetails.getQuantity());
                psTm.setDouble(5, orderDetails.getUnitPrice());
                psTm.addBatch();
            }

            int[] results = psTm.executeBatch();
            connection.commit();

            for (int result : results) {
                if (result <= 0) {
                    return false;
                }
            }
            return true;

        } catch (SQLException e) {
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException rollbackException) {
                    throw new RuntimeException(rollbackException);
                }
            }
            throw new RuntimeException(e);
        } finally {
            if (psTm != null) {
                try {
                    psTm.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
            if (connection != null) {
                try {
                    connection.setAutoCommit(true);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    @Override
    public Boolean update(OrderDetails orderDetails) {
        try {
            return CrudUtill.execute("UPDATE order_details SET bill_id = ?, medicine_id = ?, quantity = ?, unit_price = ? WHERE order_detail_id = ?",
                    orderDetails.getBillId(),
                    orderDetails.getMedicineId(),
                    orderDetails.getQuantity(),
                    orderDetails.getUnitPrice(),
                    orderDetails.getOrderDetailId()
            );
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Boolean deleteById(Integer id) {
        try {
            return CrudUtill.execute("DELETE FROM order_details WHERE order_detail_id = ?", id);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Boolean deleteByBillId(Integer billId) {
        try {
            return CrudUtill.execute("DELETE FROM order_details WHERE bill_id = ?", billId);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public OrderDetails searchById(Integer id) {
        try {
            ResultSet resultSet = CrudUtill.execute("SELECT * FROM order_details WHERE order_detail_id = ?", id);
            if (resultSet.next()) {
                return new OrderDetails(
                        resultSet.getInt(1),
                        resultSet.getInt(2),
                        resultSet.getInt(3),
                        resultSet.getInt(4),
                        resultSet.getDouble(5)
                );
            }
            return null;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<OrderDetails> getByBillId(Integer billId) {
        try {
            ResultSet resultSet = CrudUtill.execute("SELECT * FROM order_details WHERE bill_id = ?", billId);
            ArrayList<OrderDetails> orderDetailsArrayList = new ArrayList<>();
            while (resultSet.next()) {
                orderDetailsArrayList.add(
                        new OrderDetails(
                                resultSet.getInt(1),
                                resultSet.getInt(2),
                                resultSet.getInt(3),
                                resultSet.getInt(4),
                                resultSet.getDouble(5)
                        )
                );
            }
            return orderDetailsArrayList;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<OrderDetails> getAll() {
        try {
            ResultSet resultSet = CrudUtill.execute("SELECT * FROM order_details");
            ArrayList<OrderDetails> orderDetailsArrayList = new ArrayList<>();
            while (resultSet.next()) {
                orderDetailsArrayList.add(
                        new OrderDetails(
                                resultSet.getInt(1),
                                resultSet.getInt(2),
                                resultSet.getInt(3),
                                resultSet.getInt(4),
                                resultSet.getDouble(5)
                        )
                );
            }
            return orderDetailsArrayList;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
