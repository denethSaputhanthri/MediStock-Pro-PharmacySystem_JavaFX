package repository.custom.impl;


import model.Medicine;
import repository.custom.MedicineRepository;
import util.CrudUtill;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MedicineRepositoryImpl implements MedicineRepository {


    @Override
    public Boolean create(Medicine medicine) {
        try {
            return CrudUtill.execute("INSERT INTO medicine VALUES(?,?,?,?,?,?,?,?)",
                medicine.getId(),
                medicine.getName(),
                medicine.getBrand(),
                medicine.getExpiry_date(),
                medicine.getQuantity(),
                medicine.getPurchase_price(),
                medicine.getSelling_price(),
                medicine.getSupplier_id()
            );
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Boolean update(Medicine medicine) {
        try {
            return CrudUtill.execute("UPDATE medicine SET  brand = ?, expiry_date = ?, quantity = ?, purchase_price = ?, selling_price = ?, supplier_id = ? WHERE medicine_name = ?",
                    medicine.getBrand(),
                    medicine.getExpiry_date(),
                    medicine.getQuantity(),
                    medicine.getPurchase_price(),
                    medicine.getSelling_price(),
                    medicine.getSupplier_id(),
                    medicine.getName()
            );
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Boolean deleteById(Integer id) {
        try {
            return CrudUtill.execute("DELETE FROM medicine WHERE medicine_name=?", id);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Medicine searchById(Integer integer) {
        return null;
    }

    @Override
    public List<Medicine> getAll() {
        try {
            ResultSet resultSet = CrudUtill.execute("SELECT * FROM medicine");
            ArrayList<Medicine> medicinesArrayList = new ArrayList<>();
            while (resultSet.next()){
                medicinesArrayList.add(
                        new Medicine(
                                resultSet.getInt(1),
                                resultSet.getString(2),
                                resultSet.getString(3),
                                resultSet.getDate(4).toLocalDate(),
                                resultSet.getInt(5),
                                resultSet.getDouble(6),
                                resultSet.getDouble(7),
                                resultSet.getString(8)
                        )
                );
            }
            return medicinesArrayList;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
