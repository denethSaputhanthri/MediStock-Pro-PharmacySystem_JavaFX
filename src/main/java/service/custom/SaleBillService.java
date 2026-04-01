package service.custom;

import model.SaleBill;
import service.SuperService;

import java.util.List;

public interface SaleBillService extends SuperService {
    List<SaleBill> getAll();
    boolean addSaleBill(SaleBill saleBill);
    boolean updateSaleBill(SaleBill saleBill);
    boolean deleteSaleBill(Integer billId);
    SaleBill searchById(Integer billId);
}
