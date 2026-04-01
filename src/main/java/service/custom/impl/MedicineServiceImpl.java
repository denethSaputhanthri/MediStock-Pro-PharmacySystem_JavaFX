package service.custom.impl;

import model.Medicine;
import repository.RepositoryFactory;
import repository.custom.MedicineRepository;
import service.ServiceFactory;
import service.custom.MedicineService;
import util.RepositoryType;

import java.util.List;

public class MedicineServiceImpl implements MedicineService {
    MedicineRepository medicineRepository = RepositoryFactory.getInstance().getRepositoryType(RepositoryType.MEDICINE);

    @Override
    public List<Medicine> getAll() {
        return medicineRepository.getAll();
    }

    @Override
    public boolean addMedicine(Medicine medicine) {
        return medicineRepository.create(medicine);
    }

    @Override
    public boolean updateMedicine(Medicine medicine) {
        return medicineRepository.update(medicine);
    }

    @Override
    public boolean deleteMedicine(String id) {
        return medicineRepository.deleteById(Integer.valueOf(id));
    }

    @Override
    public Medicine searchById(String id) {
        return null;
    }

    @Override
    public List<String> getAllMedicineIDs() {
        return List.of();
    }
}
