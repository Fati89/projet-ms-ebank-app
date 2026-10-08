package benakka.customerservice.service;

import benakka.customerservice.entities.Customer;
import benakka.customerservice.repostory.CustomerRepository;
import org.springframework.ai.mcp.annotation.McpTool;
import org.springframework.ai.mcp.annotation.McpToolParam;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {
    private CustomerRepository customerRepository;
    public CustomerService(CustomerRepository customerRepository){
        this.customerRepository = customerRepository;
    }

    @McpTool(description = "Get All Customers")
    public List<Customer> getAllCustomers(){
        return customerRepository.findAll();
    }

    @McpTool(description = "Find a Customer by id")
    public Customer findCustomerById(@McpToolParam(description = "The Customer id") Long id){
        return customerRepository.findById(id)
                .orElseThrow(()->new RuntimeException("Customer not found"));
    }

    @McpTool(description = "Save a new customer")
    public Customer saveCustomer(@McpToolParam(description = "The Customer to save (name, email)") Customer customer){
        return customerRepository.save(customer);
    }
}
