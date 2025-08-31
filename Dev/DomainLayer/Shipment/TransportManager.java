package DomainLayer.Shipment;

import DTO.*;
import DataAccessLayer.*;
import DomainLayer.API.EmployeeShipmentAPI;
import DomainLayer.Employee.BankAccount;
import DomainLayer.Employee.Role;
import DomainLayer.Employee.ShiftType;
import DomainLayer.Employee.StoreBranch;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

public class TransportManager {
    private final DriverManager driverManager;
    private final TruckManager truckManager;
    private List<ReportForDes> pendingReports;
    private HashMap<Area, List<ReportForDes>> areaGroupedReports;
    private static int transportIdCounter = 1;
    private static int ReprtIdCounter = 1;

    private static StoreBranch source ;
    private List<Transport> completedTransports;
    private static TransportManager instance = null;
    private  EmployeeShipmentAPI api;
    private JdbcTransportDAO jdbcTransportDAO;
    private JdbcDriverDAO jdbcDriverDAO;
    private JdbcDriversAssignedDAO jdbcDriversAssignedDAO;
    private JdbcTruckDAO jdbcTruckDAO;
    private JdbcPendingReportsDAO jdbcPendingReportsDAO;
    private JdbcEmployeeDAO jdbcEmployeeDAO;
    private JdbcEmployeeToRoleDAO jdbcEmployeeToRoleDAO;
    private JdbcEmployeeToShiftScheduledDAO jdbcEmployeeToShiftScheduledDAO;



    public TransportManager() {
        driverManager = new DriverManager();
        truckManager = new TruckManager();
        pendingReports = new ArrayList<>();
        areaGroupedReports = new HashMap<>();
        completedTransports = new ArrayList<>();
         api=new EmployeeShipmentAPI();
        jdbcTransportDAO = new JdbcTransportDAO();
        jdbcDriverDAO = new JdbcDriverDAO();
        jdbcDriversAssignedDAO = new JdbcDriversAssignedDAO();
        jdbcTruckDAO = new JdbcTruckDAO();
        jdbcPendingReportsDAO = new JdbcPendingReportsDAO();
        jdbcEmployeeDAO = new JdbcEmployeeDAO();
        jdbcEmployeeToRoleDAO = new JdbcEmployeeToRoleDAO();
        jdbcEmployeeToShiftScheduledDAO = new JdbcEmployeeToShiftScheduledDAO();
    }



    public  void setSource(String branchName)
    {
        source=api.getStoreByName(branchName);

    }
    public  StoreBranch getStore(String branchName) {

        return api.getStoreByName(branchName);
    }

    public EmployeeShipmentAPI getApi() {
        return api;
    }
    public void setDrivers()
    {
        LocalDate date= LocalDate.now();
        LocalTime time= LocalTime.now();
        List<Driver> drivers=api.getScheduledDrivers(date, time, source.getId());
        driverManager.setAvailableDrivers(drivers);
    }

    public List<Transport> createTransportsWithSplit(LocalDate date, LocalTime time, List<ReportForDes> allReports, StoreBranch source) {


        List<Transport> transports = new ArrayList<>();
        List<ReportForDes> remainingReports = new ArrayList<>(allReports);
        while (!remainingReports.isEmpty()){ // there are orders to be placed
            //warehouseKeeper
            if (!api.shiftHasWarehouseKeeper(date, time, source.getId())) {
                System.out.println("No warehouse keeper in source.");
                break;
            }
            remainingReports=wareHouseSublist(remainingReports, date,  time);
            if (remainingReports.isEmpty())
            {
                System.out.println("No  warehouse keeper in destination.");
                break;
            }
            ////
            int totalWeight = calculateTotalWeight(remainingReports);
            Truck truck = truckManager.assignTruck(totalWeight); //
            if (truck == null) { //No truck can handle this  totalWeight
                // Try to split the list
                List<ReportForDes> subList = findFittingSublist(remainingReports, maxTruckTDRiver(getTruckManager().getAvailableTrucks(), getDriverManager().getAvailableDrivers()));
                if (subList.isEmpty()) {
                    System.out.println("No fitting sublist found for the remaining reports.");
                    break;
                }
                truck = truckManager.assignTruck(calculateTotalWeight(subList)); // assume that we check that  there an is empty truck
                if (truck == null) break;
                Driver driver = driverManager.assignDriver(truck);
                if (driver == null) {
                    System.out.println("No available drivers for the truck.");
                    truckManager.releaseTruck(truck);
                    break;
                }

                Transport transport = new Transport(transportIdCounter, date, time, subList, source, calculateTotalWeight(subList));
                TransportDTO transportDTO = new TransportDTO(transportIdCounter, date.toString(), time.toString(), source.getAddress(), transport.DestinationList(), calculateTotalWeight(subList));


                try {
                    jdbcTransportDAO.insert(transportDTO);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                transportIdCounter++;
                transport.assignTruck(truck);
                transport.assignDriver(driver);
                transports.add(transport);
                try {
                    for (ReportForDes reportForDes : subList) {
                        jdbcPendingReportsDAO.delete(reportForDes.getReportId());
                    }
                    jdbcTruckDAO.delete(truck.getId_number());
                    jdbcDriverDAO.delete(driver.getId());
                    jdbcDriversAssignedDAO.delete(driver.getId());
                    jdbcEmployeeDAO.delete(driver.getId());
                    jdbcEmployeeToRoleDAO.delete(driver.getId());
                    jdbcEmployeeToShiftScheduledDAO.delete(driver.getId());
//                    api.getStoreByName(source.getName()).getShiftScheduled().get(0).removeEmployeeRequired(driver.getId());
//                    api.getStoreByName(source.getName()).getShiftScheduled().get(0).removeDriverAssigned(driver.getId());
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                remainingReports.removeAll(subList);
                remainingReports.removeIf(report1 -> report1.getProductsForDes().isEmpty());

            } else {
                Driver driver = driverManager.assignDriver(truck);
                if (driver == null) {
                    truckManager.releaseTruck(truck);
                    break;
                }
                Transport transport = new Transport(transportIdCounter, date, time, remainingReports, source, totalWeight);
                TransportDTO transportDTO = new TransportDTO(transportIdCounter, date.toString(), time.toString(), source.getAddress(), transport.DestinationList(), totalWeight);

                transportIdCounter++;
                transport.assignTruck(truck);
                transport.assignDriver(driver);
                transports.add(transport);
                try {
                    for (ReportForDes remainingReport : remainingReports) {
                        jdbcPendingReportsDAO.delete(remainingReport.getReportId());
                    }
                    jdbcTruckDAO.delete(truck.getId_number());
                    jdbcDriverDAO.delete(driver.getId());
                    jdbcDriversAssignedDAO.delete(driver.getId());
                    jdbcEmployeeDAO.delete(driver.getId());
                    jdbcEmployeeToRoleDAO.delete(driver.getId());
                    jdbcEmployeeToShiftScheduledDAO.delete(driver.getId());

//                    api.getStoreByName(source.getName()).getShiftScheduled().get(0).removeEmployeeRequired(driver.getId());
//                    api.getStoreByName(source.getName()).getShiftScheduled().get(0).removeDriverAssigned(driver.getId());
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                try {
                    jdbcTransportDAO.insert(transportDTO);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                break;
            }
        }

        return transports;
    }

    private List<ReportForDes> wareHouseSublist(List<ReportForDes> reports, LocalDate date, LocalTime time)
    {
        List<ReportForDes> ToReturn = new ArrayList<>();
        for (ReportForDes temp : reports) {
            if( api.shiftHasWarehouseKeeper(date, time, temp.getDestination().getId()))
            {
                ToReturn.add(temp);
            }
        }
        return ToReturn;
    }


    private int calculateTotalWeight(List<ReportForDes> reports) {
        int total = 0;
        for (ReportForDes report : reports) {
            total += report.weightForDes();
        }
        return total;
    }

    private List<ReportForDes> findFittingSublist(List<ReportForDes> reports, int WeightCanTake) {
        List<ReportForDes> ToReturn = new ArrayList<>();
        boolean canTake;
            for (ReportForDes temp : reports) {
                canTake = true;
                while (canTake && WeightCanTake > 0){
                    ReportForDes tempReport;
                    tempReport = extractFittingPart(temp, WeightCanTake);
                        if (tempReport == null) {
                            canTake = false;
                        }
                        else{
                            int weightOfTemp = tempReport.weightForDes();
                            if (weightOfTemp == 0) {
                                canTake = false;
                            }else{
                                ToReturn.add(tempReport);
                                WeightCanTake -= weightOfTemp;
                            }
                        }
                }
            }
        return ToReturn;
    }

    private ReportForDes extractFittingPart(ReportForDes report, int maxWeight) {
        List<Product> toSend = new ArrayList<>();
        List<Product> toSendCompleted = new ArrayList<>();
        List<Product> toSendPartial = new ArrayList<>();
        int currentWeight = 0;
        for (Product product : report.getProductsForDes()) {
            int productWeight = product.getWeight() * product.getQuantity();/////////all the Quantity for one product
            if (currentWeight + productWeight <= maxWeight) {////////////// if yes , take the all
                toSendCompleted.add(product);
                currentWeight += productWeight;
            } else if (currentWeight < maxWeight) {
                int quantityThatFits = (maxWeight - currentWeight) / product.getWeight();///////////////////////////////////////// how  much Quantity I can take for 1 product
                if (quantityThatFits > 0) {
                    toSendPartial.add(new Product(product.getWeight(), product.getType(), quantityThatFits));
                    UpdateQuantity(report, product, product.getQuantity() - quantityThatFits);
                    currentWeight += quantityThatFits * product.getWeight();
                }
            }
        }
        for (Product product : toSendCompleted) {
            toSend.add(product);
        }
        for (Product product : toSendPartial) {
            toSend.add(product);
        }
        if (toSend.isEmpty()) return null;
        report.deleteProducts(toSendCompleted);
        ReportForDes tmp = new ReportForDes(ReprtIdCounter,report.getDestination(), toSend);
        ReprtIdCounter++;
        return tmp;
    }

    public void manageAllDeliveries() {
        LocalDate date = LocalDate.now();
        LocalTime time = LocalTime.now();

        groupReportsByArea();
        // First: Try grouped areas
        for (Area area : areaGroupedReports.keySet()) {
            List<ReportForDes> reports = areaGroupedReports.get(area);
            if (reports.size() > 1) {
                List<Transport> alreadyTransport = createTransportsWithSplit(date, time, reports, source);
                if(alreadyTransport==null || alreadyTransport.isEmpty()) {
                    return;
                }
                for (Transport t : alreadyTransport) {
                    completedTransports.add(t);
                    pendingReports.removeAll(t.getDes());
                    }
                }
            }
            // Then: Try sending the remaining reports together (even if from different areas)
            if (!pendingReports.isEmpty()) {
                List<Transport> alreadyTransport = createTransportsWithSplit(date, time, new ArrayList<>(pendingReports), source);
                if(alreadyTransport==null || alreadyTransport.isEmpty()) {
                    return;
                }
                // Remove delivered ones from pendingReports
                for (Transport t : alreadyTransport) {
                    completedTransports.add(t);
                    pendingReports.removeAll(t.getDes());
                }
            }
        pendingReports.removeIf(report -> report.getProductsForDes().isEmpty());
        }

    private void groupReportsByArea() {
        areaGroupedReports.clear();
        for (ReportForDes report : pendingReports) {
            areaGroupedReports.putIfAbsent(report.getDestination().getArea(), new ArrayList<>());
            areaGroupedReports.get(report.getDestination().getArea()).add(report);
        }
    }

    public void addReport(ReportForDes report) {
        pendingReports.add(report);
    }

    public List<ReportForDes> getPendingReports() {
        return pendingReports;
    }

    public DriverManager getDriverManager() {
        return driverManager;
    }

    public TruckManager getTruckManager() {
        return truckManager;
    }

    public void UpdateQuantity(ReportForDes tempReport, Product tempProduct, int QuantityToUpdate) {
        for (Product t : tempReport.getProductsForDes()) {
            if (t.equals(tempProduct)) {
                t.setQuantity(QuantityToUpdate);
            }
        }
    }

    public List<Transport> getCompletedTransports() {
        return completedTransports;
    }

    public int maxTruckTDRiver(List<Truck> availableTrucks, List<Driver> availableDrivers) {
        int Maximum_weightToDRiver = 0;
            if (!availableTrucks.isEmpty()) {
                for (Truck tempTruck : availableTrucks) {
                    if (!availableDrivers.isEmpty()) {
                        for (Driver tempDriver : availableDrivers) {
                            if (tempDriver.canDrive(tempTruck) && (tempTruck.getMaximum_weight() > Maximum_weightToDRiver)) {
                                Maximum_weightToDRiver = tempTruck.getMaximum_weight();
                            }
                        }
                    }
                }
            }
        return Maximum_weightToDRiver;
    }


    public StoreBranch getsource() {
        return source;
    }

    public List<String> getAllBranchesNames(boolean source) {
        List<String> branches = api.getStoresNames();
        List<String> ans = new ArrayList<>();
        //new EmployeeShipmentAPI().shiftHasWarehouseKeeper
        if (!source)
        {
            for (String branch : branches) {
                if (!branch.equals(this.source.getName())) {
                    ans.add(branch);
                }
            }
            return ans;
        }
        return branches;
    }

    public boolean shipmentExists(StoreBranch storeBranch, LocalDate date, ShiftType type, boolean isDriver) {
        if(completedTransports==null || completedTransports.isEmpty()) {
            return false;
        }
        for (Transport t : completedTransports) {
            // 1) date must match
            if (!t.getTransport_date().equals(date)) {
                continue;
            }

            // 2) shift type must match departure time
            LocalTime dep = t.getDeparture_time();
            boolean isMorning = dep.isBefore(LocalTime.NOON);  // 00:00–11:59
            if (type == ShiftType.MORNING && !isMorning) {
                continue;
            }
            if (type == ShiftType.NIGHT  &&  isMorning) {
                continue;
            }

            // 3) branch must be either source or one of the destinations
            if (isDriver && t.getSrc().getId()==storeBranch.getId())
            {
                return true;
            }
            if(t.getDes()==null || t.getDes().isEmpty()) {
                return false;
            }
            if (!isDriver && t.getDes().stream()
                    .map(ReportForDes::getDestination)
                    .anyMatch(dest -> dest.equals(storeBranch))) {
                return true;
            }
        }
        return false;

    }





    public void  LoadPendingReportsDTO() {
        List<ReportForDes> pendingReportsDTO = new ArrayList<>();
       List<PendingReportsDTO> pendingReportsDTOS = new ArrayList<>();
        try {
            pendingReportsDTOS = jdbcPendingReportsDAO.findAll();
            for (PendingReportsDTO pendingReport : pendingReportsDTOS) {

                ReportForDes report = new ReportForDes(
                        pendingReport.ReportId(),
                        api.getStoreByName(pendingReport.destination()),
                        StringproductsForDesToList(pendingReport.productsForDes())
                );
                pendingReports.add(report);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        }





    private List<Product> StringproductsForDesToList(String productsForDes) {
        List<Product> products = new ArrayList<>();
        if (productsForDes == null || productsForDes.trim().isEmpty()) {
            return products;
        }
        String[] productStrings = productsForDes.split("\n");
        for (String productString : productStrings) {
            String[] parts = productString.split(", ");
            if (parts.length == 3) {
                String type = parts[0].split(": ")[1];
                int weight = Integer.parseInt(parts[1].split(": ")[1]);
                int quantity = Integer.parseInt(parts[2].split(": ")[1]);
                products.add(new Product(weight, type, quantity));
            }
        }
        return products;
    }

    public void LoadAvailableDriversFromDB() {
        List<DriverDTO> driversFromDB = new ArrayList<>();
        List<EmployeeDTO> employeeDTOS = new ArrayList<>();
        List<Driver> drivers = new ArrayList<>();
        try {
            driversFromDB = jdbcDriverDAO.findAll();
            employeeDTOS = jdbcEmployeeDAO.findAll();
            for (EmployeeDTO current : employeeDTOS) {
                List<Role> qualifications = new ArrayList<>();
                EmployeeToRoleDTO EmpRole = jdbcEmployeeToRoleDAO.findById(current.id());
                if (EmpRole.DELIVERY_MAN()){
                    qualifications.add(Role.DELIVERY_MAN);
                    Driver driver = new Driver(current.licenseNumber(), current.id(), current.name(), current.storeBranchId(),
                            new BankAccount(current.bankAccountName(), 0, current.bankAccountNumber(), current.name()), (double) current.salary(),
                            qualifications,false,current.employeeTerms());
                    drivers.add(driver);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }



        driverManager.setAvailableDrivers(drivers);
    }



}