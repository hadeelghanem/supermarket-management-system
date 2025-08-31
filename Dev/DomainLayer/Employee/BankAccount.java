package DomainLayer.Employee;

public class BankAccount {
    private String bankName;
    private int branchNumber;
    private int accountNumber;
    private String accountOwnerName;

    public BankAccount(String bankName, int branchNumber, int accountNumber, String accountOwnerName) {
        this.bankName = bankName;
        this.branchNumber = branchNumber;
        this.accountNumber = accountNumber;
        this.accountOwnerName = accountOwnerName;
    }



}
