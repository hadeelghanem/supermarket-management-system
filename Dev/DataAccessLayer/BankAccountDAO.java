package DataAccessLayer;

import DTO.BankAccountDTO;
import DTO.DriverDTO;

import java.sql.SQLException;

public interface BankAccountDAO {
    BankAccountDTO insert(BankAccountDTO account) throws SQLException;
    void delete(int id) throws SQLException;
    BankAccountDTO findById(int id) throws SQLException;
}
