package repository.custom.impl;

import model.DailySalesReport;
import model.MonthlySalesReport;
import model.SalesRangeReport;
import model.TopSellingMedicine;
import repository.custom.ReportRepository;
import util.CrudUtill;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ReportRepositoryImpl implements ReportRepository {

    @Override
    public List<DailySalesReport> getDailySalesReport(LocalDate date) {
        try {
            String sql = "SELECT " +
                    "    sb.date, " +
                    "    COUNT(sb.bill_id) as total_bills, " +
                    "    SUM(sb.total_amount) as total_sales, " +
                    "    AVG(sb.total_amount) as average_bill_amount, " +
                    "    SUM(od.quantity) as total_items_sold " +
                    "FROM sale_bill sb " +
                    "LEFT JOIN order_details od ON sb.bill_id = od.bill_id " +
                    "WHERE sb.date = ? " +
                    "GROUP BY sb.date";

            ResultSet resultSet = CrudUtill.execute(sql, date);
            List<DailySalesReport> reports = new ArrayList<>();

            while (resultSet.next()) {
                reports.add(new DailySalesReport(
                        resultSet.getDate("date").toLocalDate(),
                        resultSet.getInt("total_bills"),
                        resultSet.getDouble("total_sales"),
                        resultSet.getDouble("average_bill_amount"),
                        resultSet.getInt("total_items_sold")
                ));
            }

            if (reports.isEmpty()) {
                reports.add(new DailySalesReport(date, 0, 0.0, 0.0, 0));
            }

            return reports;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<DailySalesReport> getWeeklySalesReport(LocalDate startDate, LocalDate endDate) {
        try {
            String sql = "SELECT " +
                    "    sb.date, " +
                    "    COUNT(sb.bill_id) as total_bills, " +
                    "    SUM(sb.total_amount) as total_sales, " +
                    "    AVG(sb.total_amount) as average_bill_amount, " +
                    "    COALESCE(SUM(od.quantity), 0) as total_items_sold " +
                    "FROM sale_bill sb " +
                    "LEFT JOIN order_details od ON sb.bill_id = od.bill_id " +
                    "WHERE sb.date BETWEEN ? AND ? " +
                    "GROUP BY sb.date " +
                    "ORDER BY sb.date ASC";

            ResultSet resultSet = CrudUtill.execute(sql, startDate, endDate);
            List<DailySalesReport> reports = new ArrayList<>();

            while (resultSet.next()) {
                reports.add(new DailySalesReport(
                        resultSet.getDate("date").toLocalDate(),
                        resultSet.getInt("total_bills"),
                        resultSet.getDouble("total_sales"),
                        resultSet.getDouble("average_bill_amount"),
                        resultSet.getInt("total_items_sold")
                ));
            }

            return reports;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<MonthlySalesReport> getMonthlySalesReport(int year, int month) {
        try {
            String sql = "SELECT " +
                    "    YEAR(sb.date) as year, " +
                    "    MONTH(sb.date) as month, " +
                    "    COUNT(sb.bill_id) as total_bills, " +
                    "    SUM(sb.total_amount) as total_sales, " +
                    "    AVG(sb.total_amount) as average_bill_amount, " +
                    "    COALESCE(SUM(od.quantity), 0) as total_items_sold " +
                    "FROM sale_bill sb " +
                    "LEFT JOIN order_details od ON sb.bill_id = od.bill_id " +
                    "WHERE YEAR(sb.date) = ? AND MONTH(sb.date) = ? " +
                    "GROUP BY YEAR(sb.date), MONTH(sb.date)";

            ResultSet resultSet = CrudUtill.execute(sql, year, month);
            List<MonthlySalesReport> reports = new ArrayList<>();

            while (resultSet.next()) {
                int monthValue = resultSet.getInt("month");
                String monthName = java.time.Month.of(monthValue)
                        .getDisplayName(TextStyle.FULL, Locale.ENGLISH);

                reports.add(new MonthlySalesReport(
                        resultSet.getInt("year"),
                        monthValue,
                        monthName,
                        resultSet.getInt("total_bills"),
                        resultSet.getDouble("total_sales"),
                        resultSet.getDouble("average_bill_amount"),
                        resultSet.getInt("total_items_sold")
                ));
            }

            if (reports.isEmpty()) {
                String monthName = java.time.Month.of(month)
                        .getDisplayName(TextStyle.FULL, Locale.ENGLISH);
                reports.add(new MonthlySalesReport(year, month, monthName, 0, 0.0, 0.0, 0));
            }

            return reports;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<MonthlySalesReport> getYearlySalesReport(int year) {
        try {
            String sql = "SELECT " +
                    "    YEAR(sb.date) as year, " +
                    "    MONTH(sb.date) as month, " +
                    "    COUNT(sb.bill_id) as total_bills, " +
                    "    SUM(sb.total_amount) as total_sales, " +
                    "    AVG(sb.total_amount) as average_bill_amount, " +
                    "    COALESCE(SUM(od.quantity), 0) as total_items_sold " +
                    "FROM sale_bill sb " +
                    "LEFT JOIN order_details od ON sb.bill_id = od.bill_id " +
                    "WHERE YEAR(sb.date) = ? " +
                    "GROUP BY YEAR(sb.date), MONTH(sb.date) " +
                    "ORDER BY MONTH(sb.date) ASC";

            ResultSet resultSet = CrudUtill.execute(sql, year);
            List<MonthlySalesReport> reports = new ArrayList<>();

            while (resultSet.next()) {
                int monthValue = resultSet.getInt("month");
                String monthName = java.time.Month.of(monthValue)
                        .getDisplayName(TextStyle.FULL, Locale.ENGLISH);

                reports.add(new MonthlySalesReport(
                        resultSet.getInt("year"),
                        monthValue,
                        monthName,
                        resultSet.getInt("total_bills"),
                        resultSet.getDouble("total_sales"),
                        resultSet.getDouble("average_bill_amount"),
                        resultSet.getInt("total_items_sold")
                ));
            }

            return reports;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public SalesRangeReport getSalesRangeReport(LocalDate startDate, LocalDate endDate) {
        try {
            String sql = "SELECT " +
                    "    COUNT(sb.bill_id) as total_bills, " +
                    "    COALESCE(SUM(sb.total_amount), 0) as total_sales, " +
                    "    COALESCE(SUM(od.quantity), 0) as total_items_sold " +
                    "FROM sale_bill sb " +
                    "LEFT JOIN order_details od ON sb.bill_id = od.bill_id " +
                    "WHERE sb.date BETWEEN ? AND ?";

            ResultSet resultSet = CrudUtill.execute(sql, startDate, endDate);

            if (resultSet.next()) {
                int totalBills = resultSet.getInt("total_bills");
                double totalSales = resultSet.getDouble("total_sales");
                int totalItemsSold = resultSet.getInt("total_items_sold");

                long daysBetween = java.time.temporal.ChronoUnit.DAYS.between(startDate, endDate) + 1;
                int daysInRange = (int) daysBetween;
                double averageDailySales = daysInRange > 0 ? totalSales / daysInRange : 0.0;

                return new SalesRangeReport(
                        startDate,
                        endDate,
                        totalBills,
                        totalSales,
                        averageDailySales,
                        totalItemsSold,
                        daysInRange
                );
            }

            return new SalesRangeReport(startDate, endDate, 0, 0.0, 0.0, 0, 0);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<TopSellingMedicine> getTopSellingMedicines(int limit) {
        try {
            String sql = "SELECT " +
                    "    m.id as medicine_id, " +
                    "    m.medicine_name, " +
                    "    SUM(od.quantity) as total_quantity_sold, " +
                    "    SUM(od.quantity * od.unit_price) as total_revenue, " +
                    "    COUNT(DISTINCT od.bill_id) as number_of_orders " +
                    "FROM order_details od " +
                    "JOIN medicine m ON od.medicine_id = m.id " +
                    "GROUP BY m.id, m.medicine_name " +
                    "ORDER BY total_quantity_sold DESC " +
                    "LIMIT ?";

            ResultSet resultSet = CrudUtill.execute(sql, limit);
            List<TopSellingMedicine> topMedicines = new ArrayList<>();

            while (resultSet.next()) {
                topMedicines.add(new TopSellingMedicine(
                        resultSet.getInt("medicine_id"),
                        resultSet.getString("medicine_name"),
                        resultSet.getInt("total_quantity_sold"),
                        resultSet.getDouble("total_revenue"),
                        resultSet.getInt("number_of_orders")
                ));
            }

            return topMedicines;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<TopSellingMedicine> getTopSellingMedicinesByDateRange(LocalDate startDate, LocalDate endDate, int limit) {
        try {
            String sql = "SELECT " +
                    "    m.id as medicine_id, " +
                    "    m.medicine_name, " +
                    "    SUM(od.quantity) as total_quantity_sold, " +
                    "    SUM(od.quantity * od.unit_price) as total_revenue, " +
                    "    COUNT(DISTINCT od.bill_id) as number_of_orders " +
                    "FROM order_details od " +
                    "JOIN medicine m ON od.medicine_id = m.id " +
                    "JOIN sale_bill sb ON od.bill_id = sb.bill_id " +
                    "WHERE sb.date BETWEEN ? AND ? " +
                    "GROUP BY m.id, m.medicine_name " +
                    "ORDER BY total_quantity_sold DESC " +
                    "LIMIT ?";

            ResultSet resultSet = CrudUtill.execute(sql, startDate, endDate, limit);
            List<TopSellingMedicine> topMedicines = new ArrayList<>();

            while (resultSet.next()) {
                topMedicines.add(new TopSellingMedicine(
                        resultSet.getInt("medicine_id"),
                        resultSet.getString("medicine_name"),
                        resultSet.getInt("total_quantity_sold"),
                        resultSet.getDouble("total_revenue"),
                        resultSet.getInt("number_of_orders")
                ));
            }

            return topMedicines;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Double getTotalSalesForPeriod(LocalDate startDate, LocalDate endDate) {
        try {
            String sql = "SELECT COALESCE(SUM(total_amount), 0) as total " +
                    "FROM sale_bill " +
                    "WHERE date BETWEEN ? AND ?";

            ResultSet resultSet = CrudUtill.execute(sql, startDate, endDate);

            if (resultSet.next()) {
                return resultSet.getDouble("total");
            }

            return 0.0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Integer getTotalBillsForPeriod(LocalDate startDate, LocalDate endDate) {
        try {
            String sql = "SELECT COUNT(bill_id) as total " +
                    "FROM sale_bill " +
                    "WHERE date BETWEEN ? AND ?";

            ResultSet resultSet = CrudUtill.execute(sql, startDate, endDate);

            if (resultSet.next()) {
                return resultSet.getInt("total");
            }

            return 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<DailySalesReport> getLast30DaysSales() {
        try {
            String sql = "SELECT " +
                    "    sb.date, " +
                    "    COUNT(sb.bill_id) as total_bills, " +
                    "    COALESCE(SUM(sb.total_amount), 0) as total_sales, " +
                    "    COALESCE(AVG(sb.total_amount), 0) as average_bill_amount, " +
                    "    COALESCE(SUM(od.quantity), 0) as total_items_sold " +
                    "FROM sale_bill sb " +
                    "LEFT JOIN order_details od ON sb.bill_id = od.bill_id " +
                    "WHERE sb.date >= DATE_SUB(CURDATE(), INTERVAL 30 DAY) " +
                    "GROUP BY sb.date " +
                    "ORDER BY sb.date ASC";

            ResultSet resultSet = CrudUtill.execute(sql);
            List<DailySalesReport> reports = new ArrayList<>();

            while (resultSet.next()) {
                reports.add(new DailySalesReport(
                        resultSet.getDate("date").toLocalDate(),
                        resultSet.getInt("total_bills"),
                        resultSet.getDouble("total_sales"),
                        resultSet.getDouble("average_bill_amount"),
                        resultSet.getInt("total_items_sold")
                ));
            }

            return reports;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
