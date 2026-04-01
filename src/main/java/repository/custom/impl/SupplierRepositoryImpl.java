package repository.custom.impl;

import model.Medicine;
import model.Supplier;
import repository.custom.SupplierRepository;
import util.CrudUtill;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SupplierRepositoryImpl implements SupplierRepository {
    @Override
    public Boolean create(Supplier supplier) {
        try {
            return CrudUtill.execute("INSERT INTO supplier VALUES(?,?,?,?,?)",
                    supplier.getId(),
                    supplier.getName(),
                    supplier.getContactNumber(),
                    supplier.getEmail(),
                    supplier.getAddress()
            );
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Boolean update(Supplier supplier) {
        try {
            return CrudUtill.execute("UPDATE supplier SET contact_number = ?, email = ?, address = ? WHERE name = ?",
                    supplier.getContactNumber(),
                    supplier.getEmail(),
                    supplier.getAddress(),
                    supplier.getName()
            );
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Boolean deleteById(String s) {
        try {
            return CrudUtill.execute("DELETE FROM supplier WHERE name=?", s);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Supplier searchById(String s) {
        return null;
    }

    @Override
    public List<Supplier> getAll() {
        try {
            ResultSet resultSet = CrudUtill.execute("SELECT * FROM supplier");
            ArrayList<Supplier> suppliersArrayList = new ArrayList<>();
            while (resultSet.next()){
                suppliersArrayList.add(
                        new Supplier(
                                resultSet.getInt(1),
                                resultSet.getString(2),
                                resultSet.getString(3),
                                resultSet.getString(4),
                                resultSet.getString(5)
                        )
                );
            }
            return suppliersArrayList;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
