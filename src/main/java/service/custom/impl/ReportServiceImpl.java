package service.custom.impl;

import model.DailySalesReport;
import model.MonthlySalesReport;
import model.SalesRangeReport;
import model.TopSellingMedicine;
import repository.RepositoryFactory;
import repository.custom.ReportRepository;
import service.custom.ReportService;
import util.RepositoryType;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.List;

public class ReportServiceImpl implements ReportService {
    ReportRepository reportRepository = RepositoryFactory.getInstance().getRepositoryType(RepositoryType.REPORT);

    @Override
    public List<DailySalesReport> getDailySalesReport(LocalDate date) {
        if (date == null) {
            date = LocalDate.now();
        }
        return reportRepository.getDailySalesReport(date);
    }

    @Override
    public List<DailySalesReport> getWeeklySalesReport(LocalDate startDate, LocalDate endDate) {
        if (startDate == null || endDate == null) {
            return getCurrentWeekSales();
        }
        
        if (startDate.isAfter(endDate)) {
            LocalDate temp = startDate;
            startDate = endDate;
            endDate = temp;
        }
        
        return reportRepository.getWeeklySalesReport(startDate, endDate);
    }

    @Override
    public List<MonthlySalesReport> getMonthlySalesReport(int year, int month) {
        if (year < 2000 || year > 2100) {
            year = LocalDate.now().getYear();
        }
        if (month < 1 || month > 12) {
            month = LocalDate.now().getMonthValue();
        }
        return reportRepository.getMonthlySalesReport(year, month);
    }

    @Override
    public List<MonthlySalesReport> getYearlySalesReport(int year) {
        if (year < 2000 || year > 2100) {
            year = LocalDate.now().getYear();
        }
        return reportRepository.getYearlySalesReport(year);
    }

    @Override
    public SalesRangeReport getSalesRangeReport(LocalDate startDate, LocalDate endDate) {
        if (startDate == null || endDate == null) {
            return getTodaySalesReport();
        }
        
        if (startDate.isAfter(endDate)) {
            LocalDate temp = startDate;
            startDate = endDate;
            endDate = temp;
        }
        
        return reportRepository.getSalesRangeReport(startDate, endDate);
    }

    @Override
    public List<TopSellingMedicine> getTopSellingMedicines(int limit) {
        if (limit <= 0) {
            limit = 10;
        }
        if (limit > 100) {
            limit = 100;
        }
        return reportRepository.getTopSellingMedicines(limit);
    }

    @Override
    public List<TopSellingMedicine> getTopSellingMedicinesByDateRange(LocalDate startDate, LocalDate endDate, int limit) {
        if (startDate == null || endDate == null) {
            return getTopSellingMedicines(limit);
        }
        
        if (startDate.isAfter(endDate)) {
            LocalDate temp = startDate;
            startDate = endDate;
            endDate = temp;
        }
        
        if (limit <= 0) {
            limit = 10;
        }
        if (limit > 100) {
            limit = 100;
        }
        
        return reportRepository.getTopSellingMedicinesByDateRange(startDate, endDate, limit);
    }

    @Override
    public Double getTotalSalesForPeriod(LocalDate startDate, LocalDate endDate) {
        if (startDate == null || endDate == null) {
            return 0.0;
        }
        
        if (startDate.isAfter(endDate)) {
            LocalDate temp = startDate;
            startDate = endDate;
            endDate = temp;
        }
        
        return reportRepository.getTotalSalesForPeriod(startDate, endDate);
    }

    @Override
    public Integer getTotalBillsForPeriod(LocalDate startDate, LocalDate endDate) {
        if (startDate == null || endDate == null) {
            return 0;
        }
        
        if (startDate.isAfter(endDate)) {
            LocalDate temp = startDate;
            startDate = endDate;
            endDate = temp;
        }
        
        return reportRepository.getTotalBillsForPeriod(startDate, endDate);
    }

    @Override
    public List<DailySalesReport> getLast30DaysSales() {
        return reportRepository.getLast30DaysSales();
    }

    @Override
    public List<DailySalesReport> getCurrentWeekSales() {
        LocalDate today = LocalDate.now();
        LocalDate startOfWeek = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        LocalDate endOfWeek = today.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));
        
        return reportRepository.getWeeklySalesReport(startOfWeek, endOfWeek);
    }

    @Override
    public List<MonthlySalesReport> getCurrentMonthSales() {
        LocalDate today = LocalDate.now();
        return reportRepository.getMonthlySalesReport(today.getYear(), today.getMonthValue());
    }

    @Override
    public SalesRangeReport getTodaySalesReport() {
        LocalDate today = LocalDate.now();
        return reportRepository.getSalesRangeReport(today, today);
    }
}
