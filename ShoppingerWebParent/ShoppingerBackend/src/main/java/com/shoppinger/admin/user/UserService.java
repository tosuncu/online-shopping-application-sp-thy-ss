package com.shoppinger.admin.user;

import com.shoppinger.admin.paging.PagingAndSortingHelper;
import com.shoppinger.admin.paging.PagingAndSortingParam;
import com.shoppinger.common.exception.UserNotFoundException;
import com.shoppinger.common.entity.Role;
import com.shoppinger.common.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import javax.transaction.Transactional;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@Transactional
public class UserService {
    public static final int USERS_PER_PAGE = 6;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User getUserByEmail (String email) {
        return userRepository.getUserByEmail(email);
    }
    public List<User> listAll() {
        return (List<User>) userRepository.findAll(Sort.by("firstName").ascending());
    }

    public void listByPage(int pageNum, PagingAndSortingHelper helper){
       helper.listEntities(pageNum,USERS_PER_PAGE, userRepository);
    }

    public List<Role> listRoles() {
        return (List<Role>) roleRepository.findAll();
    }

    public User save(User user) {
        boolean isUpdatingUser = (user.getId() != null);
        if (isUpdatingUser) {
            User existingUser = userRepository.findById(user.getId()).get();

            if (user.getPassword().isEmpty()) {
                user.setPassword(existingUser.getPassword());
            } else {
                encodePassword(user);
            }
        } else {
            encodePassword(user);
        }

       return userRepository.save(user);
    }

    public User updateAccount (User userInForm) {
      User userInDb =  userRepository.findById(userInForm.getId()).get();

      if(!userInForm.getPassword().isEmpty()) {
          userInDb.setPassword(userInForm.getPassword());
          encodePassword(userInDb);
      }
      if (userInForm.getPhotos() != null) {
          userInDb.setPhotos(userInForm.getPhotos());
      }
      userInDb.setFirstName(userInForm.getFirstName());
      userInDb.setLastName(userInForm.getLastName());

      return userRepository.save(userInDb);

    }
    private void encodePassword(User user) {
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
    }

    public boolean isEmailUnique(Integer id, String email) {
        User userByEmail = userRepository.getUserByEmail(email);

        if (userByEmail == null) return true;

        boolean isCreatingNew = (id == null);

        if (isCreatingNew) {
            if (userByEmail != null) return false;
        } else {
            if (userByEmail.getId() != id) {
                return false;
            }
        }

        return true;
    }

    public User getUserById(Integer id) throws UserNotFoundException {
        try {
            return userRepository.findById(id).get();
        } catch (NoSuchElementException ex) {
            throw new UserNotFoundException("No user found with id: " + id);
        }

    }

    public void delete(Integer id) throws UserNotFoundException {
        Long countById = userRepository.countById(id);
        if (countById == null || countById == 0) {
            throw new UserNotFoundException("There is no user with that id: "+ id);
        }
        userRepository.deleteById(id);
    }

    public void updateUserEnabledStatus(Integer id, boolean enabled){
        userRepository.updateEnabledStatus(id,enabled);
    }
}
