package cc.zzr;

import cc.zzr.model.Customer;
import cc.zzr.service.CustomerService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomerServiceTest {
    public final CustomerService customerService;

    public CustomerServiceTest() {
        customerService = new CustomerService();
    }

    @Before
    public void init() {

    }

    @Test
    public void getCustomerListTest() throws  Exception{
        List<Customer> customers = customerService.getCustomerList();
        Assert.assertEquals(2,customers.size());
    }

    @Test
    public void getCustomerByIdTest(){
        Customer customer = customerService.getCustomerById(1);
        Assert.assertNotNull(customer);
    }

    @Test
    public void createCustomerTest(){
        Map<String,Object> fieldMap = new HashMap<>();
        fieldMap.put("name","customer100");
        fieldMap.put("contact","John");
        fieldMap.put("telephone","17777777777");
        boolean result = customerService.createCustomer(fieldMap);
        Assert.assertTrue(result);
    }

    @Test
    public void updateCustomerTest(){
        Map<String,Object> fieldMap = new HashMap<>();
        fieldMap.put("name","customer101");
        fieldMap.put("contact","John");
        fieldMap.put("telephone","17777777778");
        boolean result = customerService.updateCustomer(1,fieldMap);
        Assert.assertTrue(result);
    }

    @Test
    public void deleteCustomerTest(){
        boolean result = customerService.deleteCustomer(1);
        Assert.assertTrue(result);
    }


}
