package com.shoppinger.customer;

import com.shoppinger.Utility;
import com.shoppinger.common.entity.Country;
import com.shoppinger.common.entity.Customer;
import com.shoppinger.setting.EmailSettingBag;
import com.shoppinger.setting.SettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.List;

@Controller
public class CustomerController {

    @Autowired
    private CustomerService customerService;
    @Autowired
    private SettingService settingService;

    @GetMapping("/register")
    public String showRegisterForm(Model model){
        List<Country> countryList = customerService.listAllCountries();
        model.addAttribute("countryList",countryList);
        model.addAttribute("pageTitle","Customer Registration");
        model.addAttribute("customer",new Customer());
        return "register/register_form";
    }
    @PostMapping("/create_customer")
    public String createCustomer(Customer customer, Model model, HttpServletRequest request) throws MessagingException, UnsupportedEncodingException {
        customerService.registerCustomer(customer);
        sendVerificationEmail(request,customer);
        model.addAttribute("pageTitle" ,"Registration Succeded");
        return "/register/register_success";
    }

    private void sendVerificationEmail(HttpServletRequest request, Customer customer) throws MessagingException, UnsupportedEncodingException {
        EmailSettingBag emailSettings = settingService.getEmailSettings();
        JavaMailSenderImpl mailSender = Utility.prepareMailSender(emailSettings);

        String toAdress = customer.getEmail();
        String subject = emailSettings.getCustomerVerifySubject();
        String content = emailSettings.getCustomerVerifyContent();

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom(emailSettings.getMailFrom(),emailSettings.getSenderName());
        helper.setTo(toAdress);
        helper.setSubject(subject);

        content = content.replace("[[name]]", customer.getFullName());

        String verifyURL = Utility.getSiteURL(request) +  "/verify?code=" + customer.getVerificationCode();

        content = content.replace("[[URL]]", verifyURL);
        helper.setText(content,true);
        mailSender.send(message);

        System.out.println("to address : "+ toAdress);
        System.out.println("verify URl :  " + verifyURL);

    }

    @GetMapping("/verify")
    public String verifyAccount(@Param("code") String code){
        boolean verified = customerService.verify(code);

        return "register/"+ (verified ? "verify_success" : "verify_fail");
    }
}
