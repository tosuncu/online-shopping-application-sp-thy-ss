package com.shoppinger.admin.customer;

import com.shoppinger.admin.paging.PagingAndSortingHelper;
import com.shoppinger.admin.paging.PagingAndSortingParam;
import com.shoppinger.common.entity.*;
import com.shoppinger.common.exception.CustomerNotFoundException;
import com.shoppinger.common.exception.ProductNotFoundException;
import com.shoppinger.common.exception.UserNotFoundException;
import org.dom4j.rule.Mode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class CustomerController {
    @Autowired
    private CustomerService customerService;


    @GetMapping("/customers")
    public String listFirsPage() {
        return "redirect:/customers/page/1?sortField=firstName&sortDir=asc";
    }

    @GetMapping("/customers/page/{pageNum}")
    public String listByPage(@PagingAndSortingParam(listName = "customerList", moduleURL = "/customers") PagingAndSortingHelper helper,
                             @PathVariable(name = "pageNum") int pageNum) {
        customerService.listByPage(pageNum, helper);
        System.out.println("From Controller");
        return "customers/customers";
    }

    @GetMapping("/customers/edit/{id}")
    public String editUser(@PathVariable(name = "id") Integer id, Model model, RedirectAttributes redirectAttributes) {
        try {
            Customer customer = customerService.get(id);
            List<Country> countryList = customerService.listAllCountries();

            model.addAttribute("countryList", countryList);
            model.addAttribute("customer", customer);
            model.addAttribute("pageTitle", "Edit Customer (ID: " + id + ")");
            return "customers/customer_form";
        } catch (CustomerNotFoundException ex) {
            redirectAttributes.addFlashAttribute("message", ex.getMessage());
            return "redirect:/customers";
        }
    }

    @GetMapping("customers/{id}/enabled/{status}")
    public String updateCustomerEnabledStatus(@PathVariable("id") Integer id, @PathVariable("status") boolean enabled, RedirectAttributes redirectAttributes) {
        customerService.updateCustomerEnabledStatus(id, enabled);
        String status = enabled ? "enabled" : "disabled";
        String message = "The Customer ID " + id + "has been " + status;
        redirectAttributes.addFlashAttribute("message", message);

        return "redirect:/customers";
    }

    @GetMapping("/customers/delete/{id}")
    public String deleteUser(@PathVariable(name = "id") Integer id, Model model, RedirectAttributes redirectAttributes) {
        try {
            customerService.delete(id);
            redirectAttributes.addFlashAttribute("message", "The customer ID " + id + "has been deleted succesfully");
        } catch (CustomerNotFoundException exception) {
            redirectAttributes.addFlashAttribute("message", exception.getMessage());
        }
        return "redirect:/customers";
    }

    @GetMapping("/customers/detail/{id}")
    public String viewCustomerDetails(@PathVariable("id") Integer id, Model model, RedirectAttributes redirectAttributes) {
        try {
            Customer customer = customerService.get(id);
            List<Country> countryList = customerService.listAllCountries();

            model.addAttribute("customer", customer);
            model.addAttribute("countryList", countryList);

            model.addAttribute("pageTitle", String.format("Edit Customer (ID : %d)", id));
            return "customers/customer_detail_modal";

        } catch (CustomerNotFoundException exception) {
            redirectAttributes.addFlashAttribute("message", exception.getMessage());
            return "redirect:/customers";
        }
    }

    @PostMapping("/customers/save")
    public String saveCustomer(Customer customer, Model model, RedirectAttributes redirectAttributes) {
        customerService.save(customer);
        redirectAttributes.addFlashAttribute("message", "The customer ID " + customer.getId() + "has been updated");
        return "redirect:/customers";
    }


}
