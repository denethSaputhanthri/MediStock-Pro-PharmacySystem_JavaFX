package service.custom.impl;

import model.SaleBill;
import repository.RepositoryFactory;
import repository.custom.SaleBillRepository;
import service.custom.SaleBillService;
import util.RepositoryType;

import java.util.List;

public class SaleBillServiceImpl implements SaleBillService {
    SaleBillRepository saleBillRepository = RepositoryFactory.getInstance().getRepositoryType(RepositoryType.SALEBILL);

    @Override
    public List<SaleBill> getAll() {
        return saleBillRepository.getAll();
    }

    @Override
    public boolean addSaleBill(SaleBill saleBill) {
        System.out.println("Adding SaleBill: " + saleBill);
        if (saleBill == null) {
            return false;
        }
        if (saleBill.getTotalAmount() == null || saleBill.getTotalAmount() < 0) {
            return false;
        }
        if (saleBill.getCustomerName() == null || saleBill.getCustomerName().trim().isEmpty()) {
            return false;
        }
        return saleBillRepository.create(saleBill);
    }

    @Override
    public boolean updateSaleBill(SaleBill saleBill) {
        if (saleBill == null || saleBill.getBillId() == null) {
            return false;
        }
        if (saleBill.getTotalAmount() == null || saleBill.getTotalAmount() < 0) {
            return false;
        }
        if (saleBill.getCustomerName() == null || saleBill.getCustomerName().trim().isEmpty()) {
            return false;
        }
        return saleBillRepository.update(saleBill);
    }

    @Override
    public boolean deleteSaleBill(Integer billId) {
        if (billId == null || billId <= 0) {
            return false;
        }
        return saleBillRepository.deleteById(billId);
    }

    @Override
    public SaleBill searchById(Integer billId) {
        if (billId == null || billId <= 0) {
            return null;
        }
        return saleBillRepository.searchById(billId);
    }
}
