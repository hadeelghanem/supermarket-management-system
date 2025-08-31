package PresentationLayer.Employee;

import DomainLayer.Employee.Employee;
import PresentationLayer.Employee.EmployeePL;
import PresentationLayer.Employee.ManagerPL;
import PresentationLayer.Employee.StoreControllerPL;

import java.util.Scanner;

public class StorePL {
    private  final StoreControllerPL storesController;
    private static Scanner scanner;
    private final EmployeePL employeePL;
    private final ManagerPL managerPL;

    public StorePL(StoreControllerPL storeControllerPL) {
        this.storesController = storeControllerPL;
        scanner = new Scanner(System.in);
        this.employeePL = new EmployeePL(storesController);
        this.managerPL = new ManagerPL(storesController);
    }
    public  void logIn() {
        System.out.println("log in worker ");
        int id = enterID();
        String password = enterPassword();
        if(storesController.login(id, password))
        {
            workerMenu();
        }
    }

    public  void register() {
       System.out.println("register worker ");
        int id = enterID();
        String password = enterPassword();
        if(storesController.register(id, password))
        {
            workerMenu();
        }

    }
    private void workerMenu() {
        if(isManager())
        {
            this.managerPL.managerMenu();
        }
        else
        {
            this.employeePL.employeeMenu();
        }
    }
    private boolean isManager() {
        Employee employee = storesController.getEmployeeLoggedIn();
        return employee.isHRManager();
    }

    private int enterID()
    {  boolean flag = true;
        int id = -1;
        System.out.println("Please enter your ID:");
        while (flag) {
            flag = false;
            //id = scanner.nextInt();
            String idString = scanner.next();
            try {
                id = Integer.parseInt(idString);
            } catch (NumberFormatException e) {
                System.out.println("Invalid ID. Please enter a valid number.");
                flag = true;
                continue;
            }
            if (id < 0) {
                System.out.println("Invalid ID. Please try again.");
                flag = true;
            }
        }
        return id;
    }
    private String enterPassword()
    {  boolean flag = true;
        String password = "";
        System.out.println("Please enter your password:");
        while (flag)
        {
            flag = false;
            password = scanner.next();
            if(password.isEmpty()){
                System.out.println("Invalid password. Please try again.");
                flag = true;
            }
        }
        return password;
    }

    public void newWeek() {
       storesController.newWeek();


    }


    public void registerHRManager() {
        if(storesController.getEmployeeController().getHrManager()!=null)

        {
            System.out.println("❌  HR Manager already exists.");
            return;
        }
        AddEmployePL addEmployePL = new AddEmployePL(storesController);
        addEmployePL.addHr();
    }
}