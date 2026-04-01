package repository.custom.impl;

import model.SaleBill;
import repository.custom.SaleBillRepository;
import util.CrudUtill;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SaleBillRepositoryImpl implements SaleBillRepository {

    @Override
    public Boolean create(SaleBill saleBill) {
        try {
            return CrudUtill.execute("INSERT INTO sale_bill VALUES(?,?,?,?)",
                    saleBill.getBillId(),
                    saleBill.getDate(),
                    saleBill.getTotalAmount(),
                    saleBill.getCustomerName()
            );
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Boolean update(SaleBill saleBill) {
        try {
            return CrudUtill.execute("UPDATE sale_bill SET date = ?, total_amount = ?, customer_name = ? WHERE bill_id = ?",
                    saleBill.getDate(),
                    saleBill.getTotalAmount(),
                    saleBill.getCustomerName(),
                    saleBill.getBillId()
            );
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Boolean deleteById(Integer id) {
        try {
            return CrudUtill.execute("DELETE FROM sale_bill WHERE bill_id = ?", id);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public SaleBill searchById(Integer id) {
        try {
            ResultSet resultSet = CrudUtill.execute("SELECT * FROM sale_bill WHERE bill_id = ?", id);
            if (resultSet.next()) {
                return new SaleBill(
                        resultSet.getInt(1),
                        resultSet.getDate(2).toLocalDate(),
                        resultSet.getDouble(3),
                        resultSet.getString(4)
                );
            }
            return null;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<SaleBill> getAll() {
        try {
            ResultSet resultSet = CrudUtill.execute("SELECT * FROM sale_bill");
            ArrayList<SaleBill> saleBillsArrayList = new ArrayList<>();
            while (resultSet.next()) {
                saleBillsArrayList.add(
                        new SaleBill(
                                resultSet.getInt(1),
                                resultSet.getDate(2).toLocalDate(),
                                resultSet.getDouble(3),
                                resultSet.getString(4)
                        )
                );
            }
            return saleBillsArrayList;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
