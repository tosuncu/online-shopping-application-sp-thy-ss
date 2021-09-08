package com.shoppinger.admin.user.controller;

import com.shoppinger.admin.FileUploadUtil;
import com.shoppinger.admin.security.ShoppingerUserDetails;
import com.shoppinger.admin.user.UserService;
import com.shoppinger.common.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;

@Controller
public class AccountController {
    @Autowired
    private UserService userService;

    @GetMapping("/account")
    public String viewDetails(@AuthenticationPrincipal ShoppingerUserDetails loggedUser, Model model) {
    String email = loggedUser.getUsername();
     User user = userService.getUserByEmail(email);
     model.addAttribute("user", user);
     return "users/account_form";
    }

    @PostMapping("/account/update")
    public String saveDetails(User user, RedirectAttributes redirectAttributes, @RequestParam("image") MultipartFile multipartFile, @AuthenticationPrincipal ShoppingerUserDetails loggedInUser) throws IOException {
        if(!multipartFile.isEmpty()) {
            String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
            user.setPhotos(fileName);
            User savedUser = userService.updateAccount(user);
            String uploadDir = "user-photos/"+savedUser.getId();
            FileUploadUtil.cleanDir(uploadDir);
            FileUploadUtil.saveFile(uploadDir,fileName,multipartFile);
        } else {
            if(user.getPhotos().isEmpty()) user.setPhotos(null);
            userService.updateAccount(user);
        }
        loggedInUser.setFirstName(user.getFirstName());
        loggedInUser.setLastName(user.getLastName());

        redirectAttributes.addFlashAttribute("message", "Your account updated succesfully");
        return "redirect:/account";

    }

}
