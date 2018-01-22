package cc.zzr.service;

import cc.zzr.annotation.Service;
import cc.zzr.annotation.Transaction;
import cc.zzr.helper.DatabaseHelper;
import cc.zzr.model.Customer;

import java.util.List;
import java.util.Map;

/**
 * 提供客户数据服务
 */
@Service
public class CustomerSerivce {
    /**
     * 获取客户列表
     *
     * @return
     */
    public List<Customer> getCustomerList() {
        String sql = "select * from customer";
        return DatabaseHelper.queryEntityList(Customer.class, sql);
    }

    /**
     * 获取客户
     *
     * @param id
     * @return
     */
    public Customer getCustomerById(long id) {
        String sql = "select * from customer where id = ?";
        return DatabaseHelper.getEntityById(Customer.class, sql, id);
    }

    /**
     * 创建客户
     *
     * @param fieldMap
     * @return
     */
    @Transaction
    public boolean createCustomer(Map<String, Object> fieldMap) {
        return DatabaseHelper.insertEntity(Customer.class, fieldMap);
    }

    /**
     * 更新客户
     *
     * @param id
     * @param fieldMap
     * @return
     */
    @Transaction
    public boolean updateCustomer(long id, Map<String, Object> fieldMap) {
        return DatabaseHelper.updateEntity(Customer.class, id, fieldMap);
    }

    /**
     * 删除客户
     *
     * @param id
     * @return
     */
    @Transaction
    public boolean deleteCustomer(long id) {
        return DatabaseHelper.deleteEntity(Customer.class, id);
    }
}
