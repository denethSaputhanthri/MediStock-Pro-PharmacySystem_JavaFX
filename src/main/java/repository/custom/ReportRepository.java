package repository.custom;

import model.DailySalesReport;
import model.MonthlySalesReport;
import model.SalesRangeReport;
import model.TopSellingMedicine;
import repository.SuperRepository;

import java.time.LocalDate;
import java.util.List;

public interface ReportRepository extends SuperRepository {
    List<DailySalesReport> getDailySalesReport(LocalDate date);
    List<DailySalesReport> getWeeklySalesReport(LocalDate startDate, LocalDate endDate);
    List<MonthlySalesReport> getMonthlySalesReport(int year, int month);
    List<MonthlySalesReport> getYearlySalesReport(int year);
    SalesRangeReport getSalesRangeReport(LocalDate startDate, LocalDate endDate);
    List<TopSellingMedicine> getTopSellingMedicines(int limit);
    List<TopSellingMedicine> getTopSellingMedicinesByDateRange(LocalDate startDate, LocalDate endDate, int limit);
    Double getTotalSalesForPeriod(LocalDate startDate, LocalDate endDate);
    Integer getTotalBillsForPeriod(LocalDate startDate, LocalDate endDate);
    List<DailySalesReport> getLast30DaysSales();
}
