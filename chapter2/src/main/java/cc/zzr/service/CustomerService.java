package cc.zzr.service;

import cc.zzr.helper.DatabaseHelper;
import cc.zzr.model.Customer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

public class CustomerService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomerService.class);

    public List<Customer> getCustomerList() {
//        Connection connection = null;
        /*try {*/
//            List<Customer> customers = null;
        String sql = "select * from customer";
//            connection = DatabaseHelper.getConnection();
           /* PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                Customer customer = new Customer();
                customer.setId(rs.getLong("id"));
                customer.setName(rs.getString("name"));
                customer.setContact(rs.getString("contact"));
                customer.setTelephone(rs.getString("telephone"));
                customer.setEmail(rs.getString("email"));
                customer.setRemark(rs.getString("remark"));
                customers.add(customer);
            }*/
        return DatabaseHelper.queryEntityList(Customer.class, sql);
//            return  customers;
       /* } catch (Exception e) {
            LOGGER.error("execute sql failure",e);
        } finally {
            DatabaseHelper.close();
        }

        return null;*/
    }

    public Customer getCustomerById(long id) {
        String sql = "select * from customer where id = ? ";
        return DatabaseHelper.getEntityById(Customer.class, sql, id);
    }

    public boolean createCustomer(Map<String, Object> fieldMap) {
        return DatabaseHelper.insertEntity(Customer.class, fieldMap);
    }

    public boolean updateCustomer(long id, Map<String, Object> fieldMap) {
        return DatabaseHelper.updateEntity(Customer.class, id, fieldMap);
    }

    public boolean deleteCustomer(long id) {
        return DatabaseHelper.deleteEntity(Customer.class, id);
    }

}
