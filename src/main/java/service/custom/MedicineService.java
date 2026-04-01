package service.custom;

import model.Medicine;
import service.ServiceFactory;
import service.SuperService;

import java.util.List;

public interface MedicineService extends SuperService {
    List<Medicine> getAll();
    boolean addMedicine(Medicine medicine);
    boolean updateMedicine(Medicine medicine);
    boolean deleteMedicine(String id);
    Medicine searchById(String id);
    List<String>getAllMedicineIDs();
}
