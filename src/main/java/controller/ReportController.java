package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.print.PrinterJob;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.WritableImage;

import javafx.scene.text.Text;
import model.*;
import service.ServiceFactory;
import service.custom.ReportService;
import util.ServiceType;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

public class ReportController implements Initializable {

    ReportService reportService = ServiceFactory.getInstance().getServiceType(ServiceType.REPORT);

    @FXML
    private LineChart<String, Number> salesTrendChart;

    @FXML
    private BarChart<String, Number> salesBarChart;

    @FXML
    private PieChart salesPieChart;

    @FXML
    private TableView<DailySalesReport> dailyReportTable;

    @FXML
    private TableColumn<DailySalesReport, LocalDate> colDate;

    @FXML
    private TableColumn<DailySalesReport, Integer> colTotalBills;

    @FXML
    private TableColumn<DailySalesReport, Double> colTotalSales;

    @FXML
    private TableColumn<DailySalesReport, Double> colAverageBill;

    @FXML
    private TableColumn<DailySalesReport, Integer> colItemsSold;

    @FXML
    private TableView<MonthlySalesReport> monthlyReportTable;

    @FXML
    private TableColumn<MonthlySalesReport, String> colMonth;

    @FXML
    private TableColumn<MonthlySalesReport, Integer> colMonthBills;

    @FXML
    private TableColumn<MonthlySalesReport, Double> colMonthSales;

    @FXML
    private TableColumn<MonthlySalesReport, Double> colMonthAverage;

    @FXML
    private TableView<TopSellingMedicine> topMedicinesTable;

    @FXML
    private TableColumn<TopSellingMedicine, String> colMedicineName;

    @FXML
    private TableColumn<TopSellingMedicine, Integer> colQuantitySold;

    @FXML
    private TableColumn<TopSellingMedicine, Double> colRevenue;

    @FXML
    private TableColumn<TopSellingMedicine, Integer> colOrders;

    @FXML
    private DatePicker startDatePicker;

    @FXML
    private DatePicker endDatePicker;

    @FXML
    private Label totalSalesLabel;

    @FXML
    private Label totalBillsLabel;

    @FXML
    private Label averageBillLabel;

    @FXML
    private Label totalItemsLabel;

    @FXML
    private Label periodLabel;

    @FXML
    private ComboBox<Integer> yearComboBox;

    @FXML
    private ComboBox<String> monthComboBox;

    private ObservableList<DailySalesReport> dailyReportData = FXCollections.observableArrayList();
    private ObservableList<MonthlySalesReport> monthlyReportData = FXCollections.observableArrayList();
    private ObservableList<TopSellingMedicine> topMedicinesData = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setupTables();
        setupDatePickers();
        setupComboBoxes();
        loadDailyReport(null);
        refreshAnalytics(null);
    }

    private void setupTables() {
        if (colDate != null) {
            colDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        }
        if (colTotalBills != null) {
            colTotalBills.setCellValueFactory(new PropertyValueFactory<>("totalBills"));
        }
        if (colTotalSales != null) {
            colTotalSales.setCellValueFactory(new PropertyValueFactory<>("totalSales"));
            colTotalSales.setCellFactory(column -> new TableCell<DailySalesReport, Double>() {
                @Override
                protected void updateItem(Double item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText(null);
                    } else {
                        setText(String.format("$%.2f", item));
                    }
                }
            });
        }
        if (colAverageBill != null) {
            colAverageBill.setCellValueFactory(new PropertyValueFactory<>("averageBillAmount"));
            colAverageBill.setCellFactory(column -> new TableCell<DailySalesReport, Double>() {
                @Override
                protected void updateItem(Double item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText(null);
                    } else {
                        setText(String.format("$%.2f", item));
                    }
                }
            });
        }
        if (colItemsSold != null) {
            colItemsSold.setCellValueFactory(new PropertyValueFactory<>("totalItemsSold"));
        }

        if (colMonth != null) {
            colMonth.setCellValueFactory(new PropertyValueFactory<>("monthName"));
        }
        if (colMonthBills != null) {
            colMonthBills.setCellValueFactory(new PropertyValueFactory<>("totalBills"));
        }
        if (colMonthSales != null) {
            colMonthSales.setCellValueFactory(new PropertyValueFactory<>("totalSales"));
            colMonthSales.setCellFactory(column -> new TableCell<MonthlySalesReport, Double>() {
                @Override
                protected void updateItem(Double item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText(null);
                    } else {
                        setText(String.format("$%.2f", item));
                    }
                }
            });
        }
        if (colMonthAverage != null) {
            colMonthAverage.setCellValueFactory(new PropertyValueFactory<>("averageBillAmount"));
            colMonthAverage.setCellFactory(column -> new TableCell<MonthlySalesReport, Double>() {
                @Override
                protected void updateItem(Double item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText(null);
                    } else {
                        setText(String.format("$%.2f", item));
                    }
                }
            });
        }

        if (colMedicineName != null) {
            colMedicineName.setCellValueFactory(new PropertyValueFactory<>("medicineName"));
        }
        if (colQuantitySold != null) {
            colQuantitySold.setCellValueFactory(new PropertyValueFactory<>("totalQuantitySold"));
        }
        if (colRevenue != null) {
            colRevenue.setCellValueFactory(new PropertyValueFactory<>("totalRevenue"));
            colRevenue.setCellFactory(column -> new TableCell<TopSellingMedicine, Double>() {
                @Override
                protected void updateItem(Double item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText(null);
                    } else {
                        setText(String.format("$%.2f", item));
                    }
                }
            });
        }
        if (colOrders != null) {
            colOrders.setCellValueFactory(new PropertyValueFactory<>("numberOfOrders"));
        }

        if (dailyReportTable != null) {
            dailyReportTable.setItems(dailyReportData);
        }
        if (monthlyReportTable != null) {
            monthlyReportTable.setItems(monthlyReportData);
        }
        if (topMedicinesTable != null) {
            topMedicinesTable.setItems(topMedicinesData);
        }
    }

    private void setupDatePickers() {
        if (startDatePicker != null) {
            startDatePicker.setValue(LocalDate.now().minusDays(7));
        }
        if (endDatePicker != null) {
            endDatePicker.setValue(LocalDate.now());
        }
    }

    private void setupComboBoxes() {
        if (yearComboBox != null) {
            ObservableList<Integer> years = FXCollections.observableArrayList();
            int currentYear = LocalDate.now().getYear();
            for (int i = currentYear - 5; i <= currentYear + 1; i++) {
                years.add(i);
            }
            yearComboBox.setItems(years);
            yearComboBox.setValue(currentYear);
        }

        if (monthComboBox != null) {
            ObservableList<String> months = FXCollections.observableArrayList();
            for (int i = 1; i <= 12; i++) {
                months.add(java.time.Month.of(i).getDisplayName(TextStyle.FULL, Locale.ENGLISH));
            }
            monthComboBox.setItems(months);
            monthComboBox.setValue(java.time.Month.of(LocalDate.now().getMonthValue())
                    .getDisplayName(TextStyle.FULL, Locale.ENGLISH));
        }
    }

    @FXML
    public void loadDailyReport(ActionEvent actionEvent) {
        try {
            LocalDate today = LocalDate.now();
            List<DailySalesReport> reports = reportService.getDailySalesReport(today);
            
            dailyReportData.clear();
            dailyReportData.addAll(reports);

            if (periodLabel != null) {
                periodLabel.setText("Daily Report - " + today.format(DateTimeFormatter.ofPattern("dd MMM yyyy")));
            }

            updateSummaryLabels(reports);
            updateDailyChart();

            showAlert("Success", "Daily report loaded successfully", Alert.AlertType.INFORMATION);
        } catch (Exception e) {
            showAlert("Error", "Failed to load daily report: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    public void loadWeeklyReport(ActionEvent actionEvent) {
        try {
            LocalDate today = LocalDate.now();
            LocalDate startOfWeek = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
            LocalDate endOfWeek = today.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));

            List<DailySalesReport> reports = reportService.getWeeklySalesReport(startOfWeek, endOfWeek);
            
            dailyReportData.clear();
            dailyReportData.addAll(reports);

            if (periodLabel != null) {
                periodLabel.setText("Weekly Report - " + 
                    startOfWeek.format(DateTimeFormatter.ofPattern("dd MMM")) + " to " +
                    endOfWeek.format(DateTimeFormatter.ofPattern("dd MMM yyyy")));
            }

            updateSummaryLabels(reports);
            updateWeeklyChart();

            showAlert("Success", "Weekly report loaded successfully", Alert.AlertType.INFORMATION);
        } catch (Exception e) {
            showAlert("Error", "Failed to load weekly report: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    public void loadMonthlyReport(ActionEvent actionEvent) {
        try {
            int year = yearComboBox != null && yearComboBox.getValue() != null ? 
                      yearComboBox.getValue() : LocalDate.now().getYear();
            
            String monthName = monthComboBox != null && monthComboBox.getValue() != null ? 
                             monthComboBox.getValue() : 
                             java.time.Month.of(LocalDate.now().getMonthValue())
                                 .getDisplayName(TextStyle.FULL, Locale.ENGLISH);
            
            int month = getMonthNumber(monthName);

            List<MonthlySalesReport> reports = reportService.getMonthlySalesReport(year, month);
            
            monthlyReportData.clear();
            monthlyReportData.addAll(reports);

            if (periodLabel != null) {
                periodLabel.setText("Monthly Report - " + monthName + " " + year);
            }

            updateSummaryLabelsFromMonthly(reports);
            updateMonthlyChart(year);

            showAlert("Success", "Monthly report loaded successfully", Alert.AlertType.INFORMATION);
        } catch (Exception e) {
            showAlert("Error", "Failed to load monthly report: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    public void customRangeReport(ActionEvent actionEvent) {
        try {
            LocalDate startDate = startDatePicker != null ? startDatePicker.getValue() : LocalDate.now().minusDays(7);
            LocalDate endDate = endDatePicker != null ? endDatePicker.getValue() : LocalDate.now();

            if (startDate == null || endDate == null) {
                showAlert("Validation Error", "Please select both start and end dates", Alert.AlertType.WARNING);
                return;
            }

            if (startDate.isAfter(endDate)) {
                showAlert("Validation Error", "Start date must be before end date", Alert.AlertType.WARNING);
                return;
            }

            SalesRangeReport rangeReport = reportService.getSalesRangeReport(startDate, endDate);
            List<DailySalesReport> reports = reportService.getWeeklySalesReport(startDate, endDate);
            
            dailyReportData.clear();
            dailyReportData.addAll(reports);

            if (periodLabel != null) {
                periodLabel.setText("Custom Range - " + 
                    startDate.format(DateTimeFormatter.ofPattern("dd MMM")) + " to " +
                    endDate.format(DateTimeFormatter.ofPattern("dd MMM yyyy")));
            }

            if (totalSalesLabel != null) {
                totalSalesLabel.setText(String.format("$%.2f", rangeReport.getTotalSales()));
            }
            if (totalBillsLabel != null) {
                totalBillsLabel.setText(String.valueOf(rangeReport.getTotalBills()));
            }
            if (averageBillLabel != null) {
                double avgBill = rangeReport.getTotalBills() > 0 ? 
                                rangeReport.getTotalSales() / rangeReport.getTotalBills() : 0.0;
                averageBillLabel.setText(String.format("$%.2f", avgBill));
            }
            if (totalItemsLabel != null) {
                totalItemsLabel.setText(String.valueOf(rangeReport.getTotalItemsSold()));
            }

            updateCustomRangeChart(reports);

            showAlert("Success", "Custom range report loaded successfully", Alert.AlertType.INFORMATION);
        } catch (Exception e) {
            showAlert("Error", "Failed to load custom range report: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    public void refreshAnalytics(ActionEvent actionEvent) {
        try {
            List<TopSellingMedicine> topMedicines = reportService.getTopSellingMedicines(10);
            topMedicinesData.clear();
            topMedicinesData.addAll(topMedicines);

            updatePieChart(topMedicines);
            updateBarChart(topMedicines);

            if (actionEvent != null) {
                showAlert("Success", "Analytics refreshed successfully", Alert.AlertType.INFORMATION);
            }
        } catch (Exception e) {
            showAlert("Error", "Failed to refresh analytics: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    public void printReport(ActionEvent actionEvent) {
        try {
            StringBuilder reportContent = new StringBuilder();
            reportContent.append("========================================\n");
            reportContent.append("      SALES REPORT\n");
            reportContent.append("========================================\n\n");
            
            if (periodLabel != null) {
                reportContent.append(periodLabel.getText()).append("\n\n");
            }

            reportContent.append("Summary:\n");
            reportContent.append("----------------------------------------\n");
            if (totalBillsLabel != null) {
                reportContent.append("Total Bills: ").append(totalBillsLabel.getText()).append("\n");
            }
            if (totalSalesLabel != null) {
                reportContent.append("Total Sales: ").append(totalSalesLabel.getText()).append("\n");
            }
            if (averageBillLabel != null) {
                reportContent.append("Average Bill: ").append(averageBillLabel.getText()).append("\n");
            }
            if (totalItemsLabel != null) {
                reportContent.append("Total Items: ").append(totalItemsLabel.getText()).append("\n");
            }

            reportContent.append("\n\nDaily Breakdown:\n");
            reportContent.append("----------------------------------------\n");
            
            for (DailySalesReport report : dailyReportData) {
                reportContent.append(report.getDate().format(DateTimeFormatter.ofPattern("dd MMM yyyy")))
                           .append(" - Bills: ").append(report.getTotalBills())
                           .append(", Sales: $").append(String.format("%.2f", report.getTotalSales()))
                           .append("\n");
            }

            reportContent.append("\n========================================\n");

            Text text = new Text(reportContent.toString());
            PrinterJob printerJob = PrinterJob.createPrinterJob();

            if (printerJob != null && printerJob.showPrintDialog(dailyReportTable.getScene().getWindow())) {
                boolean success = printerJob.printPage(text);
                if (success) {
                    printerJob.endJob();
                    showAlert("Success", "Report printed successfully", Alert.AlertType.INFORMATION);
                }
            }
        } catch (Exception e) {
            showAlert("Error", "Printing failed: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    public void exportReportPDF(ActionEvent actionEvent) {
        showAlert("Info", "PDF export functionality requires external library (iText/PDFBox). Export to CSV instead.", 
                 Alert.AlertType.INFORMATION);
    }

    @FXML
    public void exportCharts(ActionEvent actionEvent) {
        try {
            if (salesTrendChart != null) {
                WritableImage image = salesTrendChart.snapshot(null, null);
                File file = new File("sales_trend_chart.png");
                ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);
            }

            if (salesBarChart != null) {
                WritableImage image = salesBarChart.snapshot(null, null);
                File file = new File("sales_bar_chart.png");
                ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);
            }

            if (salesPieChart != null) {
                WritableImage image = salesPieChart.snapshot(null, null);
                File file = new File("sales_pie_chart.png");
                ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);
            }

            showAlert("Success", "Charts exported successfully to current directory", Alert.AlertType.INFORMATION);
        } catch (IOException e) {
            showAlert("Error", "Failed to export charts: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void updateSummaryLabels(List<DailySalesReport> reports) {
        double totalSales = 0.0;
        int totalBills = 0;
        int totalItems = 0;

        for (DailySalesReport report : reports) {
            totalSales += report.getTotalSales();
            totalBills += report.getTotalBills();
            totalItems += report.getTotalItemsSold();
        }

        if (totalSalesLabel != null) {
            totalSalesLabel.setText(String.format("$%.2f", totalSales));
        }
        if (totalBillsLabel != null) {
            totalBillsLabel.setText(String.valueOf(totalBills));
        }
        if (averageBillLabel != null) {
            double avgBill = totalBills > 0 ? totalSales / totalBills : 0.0;
            averageBillLabel.setText(String.format("$%.2f", avgBill));
        }
        if (totalItemsLabel != null) {
            totalItemsLabel.setText(String.valueOf(totalItems));
        }
    }

    private void updateSummaryLabelsFromMonthly(List<MonthlySalesReport> reports) {
        double totalSales = 0.0;
        int totalBills = 0;
        int totalItems = 0;

        for (MonthlySalesReport report : reports) {
            totalSales += report.getTotalSales();
            totalBills += report.getTotalBills();
            totalItems += report.getTotalItemsSold();
        }

        if (totalSalesLabel != null) {
            totalSalesLabel.setText(String.format("$%.2f", totalSales));
        }
        if (totalBillsLabel != null) {
            totalBillsLabel.setText(String.valueOf(totalBills));
        }
        if (averageBillLabel != null) {
            double avgBill = totalBills > 0 ? totalSales / totalBills : 0.0;
            averageBillLabel.setText(String.format("$%.2f", avgBill));
        }
        if (totalItemsLabel != null) {
            totalItemsLabel.setText(String.valueOf(totalItems));
        }
    }

    private void updateDailyChart() {
        if (salesTrendChart == null) return;

        salesTrendChart.getData().clear();
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Daily Sales");

        for (DailySalesReport report : dailyReportData) {
            String date = report.getDate().format(DateTimeFormatter.ofPattern("dd MMM"));
            series.getData().add(new XYChart.Data<>(date, report.getTotalSales()));
        }

        salesTrendChart.getData().add(series);
    }

    private void updateWeeklyChart() {
        if (salesTrendChart == null) return;

        salesTrendChart.getData().clear();
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Weekly Sales");

        for (DailySalesReport report : dailyReportData) {
            String date = report.getDate().getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.ENGLISH);
            series.getData().add(new XYChart.Data<>(date, report.getTotalSales()));
        }

        salesTrendChart.getData().add(series);
    }

    private void updateMonthlyChart(int year) {
        if (salesTrendChart == null) return;

        List<MonthlySalesReport> yearlyReport = reportService.getYearlySalesReport(year);

        salesTrendChart.getData().clear();
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Monthly Sales " + year);

        for (MonthlySalesReport report : yearlyReport) {
            String month = report.getMonthName().substring(0, 3);
            series.getData().add(new XYChart.Data<>(month, report.getTotalSales()));
        }

        salesTrendChart.getData().add(series);
    }

    private void updateCustomRangeChart(List<DailySalesReport> reports) {
        if (salesTrendChart == null) return;

        salesTrendChart.getData().clear();
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Sales Trend");

        for (DailySalesReport report : reports) {
            String date = report.getDate().format(DateTimeFormatter.ofPattern("dd MMM"));
            series.getData().add(new XYChart.Data<>(date, report.getTotalSales()));
        }

        salesTrendChart.getData().add(series);
    }

    private void updatePieChart(List<TopSellingMedicine> topMedicines) {
        if (salesPieChart == null) return;

        salesPieChart.getData().clear();
        
        for (TopSellingMedicine medicine : topMedicines) {
            PieChart.Data slice = new PieChart.Data(
                medicine.getMedicineName(), 
                medicine.getTotalRevenue()
            );
            salesPieChart.getData().add(slice);
        }
    }

    private void updateBarChart(List<TopSellingMedicine> topMedicines) {
        if (salesBarChart == null) return;

        salesBarChart.getData().clear();
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Top Selling Medicines");

        for (TopSellingMedicine medicine : topMedicines) {
            series.getData().add(new XYChart.Data<>(
                medicine.getMedicineName(), 
                medicine.getTotalQuantitySold()
            ));
        }

        salesBarChart.getData().add(series);
    }

    private int getMonthNumber(String monthName) {
        for (int i = 1; i <= 12; i++) {
            if (java.time.Month.of(i).getDisplayName(TextStyle.FULL, Locale.ENGLISH).equals(monthName)) {
                return i;
            }
        }
        return LocalDate.now().getMonthValue();
    }

    private void showAlert(String title, String content, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
