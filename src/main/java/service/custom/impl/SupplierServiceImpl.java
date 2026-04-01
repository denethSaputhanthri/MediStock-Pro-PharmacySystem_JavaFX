package service.custom.impl;

import model.Supplier;
import repository.RepositoryFactory;
import repository.custom.SupplierRepository;
import service.custom.SupplierService;
import util.RepositoryType;

import java.util.List;

public class SupplierServiceImpl implements SupplierService {
    SupplierRepository supplierRepository = RepositoryFactory.getInstance().getRepositoryType(RepositoryType.SUPPLIER);

    @Override
    public boolean addSupplier(Supplier supplier) {
        return supplierRepository.create(supplier);
    }

    @Override
    public boolean updateSupplier(Supplier supplier) {
        return supplierRepository.update(supplier);
    }

    @Override
    public boolean deleteSupplier(String name) {
        return supplierRepository.deleteById(name);
    }

    @Override
    public List<Supplier> getAll() {
        return supplierRepository.getAll();
    }
}
