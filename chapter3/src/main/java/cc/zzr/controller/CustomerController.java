package cc.zzr.controller;

import cc.zzr.annotation.Action;
import cc.zzr.annotation.Controller;
import cc.zzr.annotation.Inject;
import cc.zzr.bean.Param;
import cc.zzr.bean.View;
import cc.zzr.model.Customer;
import cc.zzr.service.CustomerSerivce;

import java.util.List;

@Controller
public class CustomerController {
    @Inject
    private CustomerSerivce customerSerivce;

    @Action(value = "get:/customer")
    public View index(Param param){
        List<Customer> customers = customerSerivce.getCustomerList();
        return new View("customer.jsp").addModel("customers",customers);
    }
}
