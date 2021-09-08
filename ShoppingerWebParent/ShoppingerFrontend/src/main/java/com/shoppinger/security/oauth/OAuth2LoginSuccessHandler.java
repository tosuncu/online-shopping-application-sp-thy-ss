package com.shoppinger.security.oauth;

import com.shoppinger.common.entity.AuthenticationType;
import com.shoppinger.common.entity.Customer;
import com.shoppinger.customer.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
@Component
public class OAuth2LoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {
    @Autowired
    private CustomerService customerService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException {
         CustomerOAuth2User oauth2User  = (CustomerOAuth2User) authentication.getPrincipal();
         String name = oauth2User.getName();
         String email = oauth2User.getEmail();
         String countryCode = request.getLocale().getCountry();
         String clientName = oauth2User.getClientName();
        System.out.println(clientName);
        AuthenticationType authenticationType = getAuthenticationType(clientName);

        Customer customerByEmail = customerService.getCustomerByEmail(email);
        if (customerByEmail == null) {
            customerService.addNewCustomerUponOAuthLogin(name, email, countryCode,authenticationType);
        } else {
            customerService.updateAuthenticationType(customerByEmail, authenticationType);
        }

        super.onAuthenticationSuccess(request, response, authentication);
    }

    private AuthenticationType getAuthenticationType(String clientName){
        if (clientName.equals("Google")){
            return AuthenticationType.GOOGLE;
        } else if(clientName.equals("Facebook")){
            return AuthenticationType.FACEBOOK;
        } else {
            return AuthenticationType.DATABASE;
        }
    }
}
