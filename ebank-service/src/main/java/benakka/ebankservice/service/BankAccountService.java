package benakka.ebankservice.service;

import benakka.ebankservice.entities.BankAccount;
import benakka.ebankservice.feign.CustomerRestClient;
import benakka.ebankservice.model.Customer;
import benakka.ebankservice.repository.BankAccountRepository;
import org.springframework.ai.mcp.annotation.McpTool;
import org.springframework.ai.mcp.annotation.McpToolParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class BankAccountService {
    BankAccountRepository bankAccountRepository;
    CustomerRestClient customerRestClient;

    public BankAccountService(BankAccountRepository bankAccountRepository, CustomerRestClient customerRestClient){
        this.bankAccountRepository = bankAccountRepository;
        this.customerRestClient = customerRestClient;
    }

    @McpTool(description = "Get All Bank Accounts")
    public List<BankAccount> getAllBankAccounts(){
        return bankAccountRepository.findAll();
    }

    @McpTool(description = "Get a Bank Account by id")
    public BankAccount getBankAccountById(@McpToolParam(description = "The bank account id") String id){
        BankAccount bankAccount = bankAccountRepository.findById(id)
                .orElseThrow(()->new RuntimeException("account not found"));
        bankAccount.setCustomer(customerRestClient.getCustomerById(bankAccount.getCustomerId()));
        return bankAccount;
    }

    @McpTool(description = "Save a new Bank Accounts")
    public BankAccount save(@McpToolParam(description = "The bank account to save (balance, type, customerId)") BankAccount bankAccount){
        try {

            Customer customer = customerRestClient.getCustomerById(bankAccount.getCustomerId());
            bankAccount.setId(UUID.randomUUID().toString());
            bankAccount.setCreatedAt(new Date());
            return bankAccountRepository.save(bankAccount);
        } catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }
}
