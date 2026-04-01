package service;

import service.custom.impl.MedicineServiceImpl;
import service.custom.impl.OrderDetailsServiceImpl;
import service.custom.impl.ReportServiceImpl;
import service.custom.impl.SaleBillServiceImpl;
import service.custom.impl.SupplierServiceImpl;
import util.ServiceType;

public class ServiceFactory {
    private static ServiceFactory instance;

    private ServiceFactory(){};

    public static ServiceFactory getInstance() {
        return instance==null? instance=new ServiceFactory():instance;
    }

    public <T extends SuperService>T getServiceType(ServiceType type){
        switch (type){
            case MEDICINE: return (T) new MedicineServiceImpl();
            case SALEBILL:return (T) new SaleBillServiceImpl();
            case ORDERDETAILS:return (T) new OrderDetailsServiceImpl();
            case SUPPLIER: return (T) new SupplierServiceImpl();
            case REPORT: return (T) new ReportServiceImpl();
        }
        return null;
    }
}
