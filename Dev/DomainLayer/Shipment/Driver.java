package DomainLayer.Shipment;

import DomainLayer.Employee.BankAccount;
import DomainLayer.Employee.Employee;
import DomainLayer.Employee.Role;

import java.util.List;

public class Driver extends Employee {
    private  int id ;
    private String name ;
    private int  Driving_Licence ;
    private boolean isAvailable;

    public Driver(int _Driving_Licence , int _id, String _name, int storeBranch, BankAccount bankDetails, Double salary, List<Role> qualification, boolean isHRManager, String employeeTerms)
    {
        super(_id, _name, storeBranch, bankDetails, salary, qualification, isHRManager, employeeTerms);
        Driving_Licence= _Driving_Licence ;
        isAvailable= true;
    }

//    public int getId() {
//        return id;
//    }

//    public String getName(){
//        return name;
//    }

    public int getDriving_Licence(){
        return Driving_Licence;
    }

    public void settId(int _id){
        id= _id;
    }

//    public void setName(String _name){
//        name= _name ;
//    }

    public void  setDriving_Licence(int _Driving_Licence){
        Driving_Licence= _Driving_Licence ;
    }

    public boolean canDrive(Truck truck) {
        return truck.getLicense_number() <= Driving_Licence;
    }

    public String getLicense() {
        return String.valueOf(Driving_Licence);
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void assignToTransport() {
        isAvailable = false;
    }

    public void releaseFromTransport() {
        isAvailable = true;
    }

}

