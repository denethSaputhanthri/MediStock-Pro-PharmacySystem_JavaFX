package repository;

import repository.custom.impl.MedicineRepositoryImpl;
import repository.custom.impl.OrderDetailsRepositoryImpl;
import repository.custom.impl.SaleBillRepositoryImpl;
import repository.custom.impl.SupplierRepositoryImpl;

import util.RepositoryType;

public class RepositoryFactory {
    private static RepositoryFactory instance;

    private RepositoryFactory(){}

    public static RepositoryFactory getInstance() {
        return instance==null ? instance=new RepositoryFactory():instance;
    }

    public <T extends SuperRepository>T getRepositoryType(RepositoryType type){
        switch (type){
            case MEDICINE : return (T) new MedicineRepositoryImpl();
            case SALEBILL : return (T) new SaleBillRepositoryImpl();
            case SUPPLIER : return (T) new SupplierRepositoryImpl();
            case ORDERDETAILS: return (T) new OrderDetailsRepositoryImpl();
        }
        return null;
    }
}
