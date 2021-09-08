package com.shoppinger.admin;

import com.shoppinger.admin.user.UserRepository;
import com.shoppinger.common.entity.Role;
import com.shoppinger.common.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.Rollback;


import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest(showSql = false)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(false)
public class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    @Test
    public void testCreateUserWithOneRole() {
        Role roleAdmin = testEntityManager.find(Role.class, 1);
        User userAbduraman = new User("tosuncu07@gmail.com", "tosuncu", "Abduraman", "Karakulak");
        userAbduraman.addRole(roleAdmin);

        User savedUser = userRepository.save(userAbduraman);
        assertThat(savedUser.getId()).isGreaterThan(0);

    }

    @Test
    public void testCreateUserWithTwoRoles() {
        User userTosun = new User("muhammethuseyintosun@gmail.com", "tosuncu123", "Muhammet", "Tosun");
        Role roleEditor = new Role(3);
        Role roleAssistant = new Role(5);
        userTosun.addRole(roleEditor);
        userTosun.addRole(roleAssistant);

        User savedUser = userRepository.save(userTosun);
        assertThat(savedUser.getId()).isGreaterThan(0);

    }

    @Test
    public void testListAllUsers() {
        Iterable<User> listUsers = userRepository.findAll();
        listUsers.forEach(user -> System.out.println(user));
    }

    @Test
    public void testGetUserById() {
        User userTosuncu =  userRepository.findById(1).get();
        System.out.println(userTosuncu);
        assertThat(userTosuncu).isNotNull();
    }

    @Test
    public void testUpdateUserDetails() {
        User userTosuncu =  userRepository.findById(1).get();
        userTosuncu.setEnabled(true);
        userTosuncu.setEmail("huseyin_tosun007@hotmail.com");

        userRepository.save(userTosuncu);
    }

    @Test
    public void testUpdateUserRoles(){
        User userTosuncu1 =  userRepository.findById(2).get();
        Role roleEditor = new Role(3);
        Role roleSalesperson = new Role(2);
        userTosuncu1.getRoles().remove(roleEditor);
        userTosuncu1.addRole(roleSalesperson);
        userRepository.save(userTosuncu1);
    }
    @Test
    public void testDeleteUser(){
        Integer userId = 2;
        userRepository.deleteById(userId);
    }

    @Test
    public void testGetUserByEmail(){
        String email = "tosuncu07@gmail.com";
        User user = userRepository.getUserByEmail(email);

        assertThat(user).isNotNull();
    }
    @Test
    public void testCountById(){
        Integer id = 3;
        Long countById = userRepository.countById(id);

        assertThat(countById).isNotNull().isGreaterThan(0);
    }

    @Test
    public void testDisableUser(){
        Integer id = 1;
        userRepository.updateEnabledStatus(id,false);
    }

    @Test
    public void testEnableUser(){
        Integer id = 1;
        userRepository.updateEnabledStatus(id,true);
    }

    @Test
    public void testListFirstPage() {
        int pageNumber = 1;
        int pageSize = 8;

        Pageable pageable = PageRequest.of(pageNumber,pageSize);
        Page<User> page = userRepository.findAll(pageable);

        List<User> userList = page.getContent();

        userList.forEach(user -> System.out.println(user));

        assertThat(userList.size()).isEqualTo(pageSize);
    }

    @Test
    public void testSearchUsers() {
        String keyword = "bruce";

        int pageNumber = 0;
        int pageSize = 4;
        Pageable pageable = PageRequest.of(pageNumber,pageSize);
        Page<User> page = userRepository.findAll(keyword,pageable);

        List<User> userList = page.getContent();

        userList.forEach(user -> System.out.println(user));
        assertThat(userList.size()).isGreaterThan(0);
    }
}
