package ru.kata.spring.boot_security.demo.controller;
;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import ru.kata.spring.boot_security.demo.dao.RoleRepository;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.service.UserService;

import javax.validation.Valid;
import java.security.Principal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@org.springframework.stereotype.Controller
@RequestMapping("/")
public class Controller {

    private final UserService userService;
    private final RoleRepository roleRepository;

    public Controller(UserService userService, RoleRepository roleRepository) {
        this.userService = userService;
        this.roleRepository = roleRepository;
    }
    @GetMapping("/admin")
    public String getAdminPage(Principal principal, Model model) {
        model.addAttribute("user", userService.findByEmail(principal.getName()));
        return "admin";
    }

    @GetMapping ("/admin/allUsers")
    public String showAllUsers(Model model) {
        List<User> allUsers = userService.getAllUsers();
        model.addAttribute("allUsers", allUsers);
        return "user-view";
    }
    @GetMapping("/admin/addNewUser")
    public String addNewUser(Model model) {
        User user = new User();
        model.addAttribute("user", user);
        return "user-info";
    }
    @PostMapping("/admin/addNewUser")
    public String saveUser(@ModelAttribute("user") @Valid User user, BindingResult bindingResult, @RequestParam(value = "roles", required = false) List<String> roles) {
        if (bindingResult.hasErrors()) {
            return "user-info";
        }
        for (String roleName : roles) {
            Role role = roleRepository.findByName(roleName);
            if (role == null) {
                role = new Role(roleName);
                roleRepository.save(role);
            }
            user.getRoles().add(role);
        }
        userService.saveUser(user);
        return "redirect:/admin/allUsers";
    }
//    @PostMapping("/admin/addNewUser")
//    public ModelAndView newUser() {
//        User user = new User();
//        ModelAndView mav = new ModelAndView("user-info");
//        mav.addObject("user", user);
//        List<Role> roles = roleRepository.findAll();
//        mav.addObject("allRoles", roles);
//        return mav;
//    }
    @PostMapping("/admin/updateInfo")
    public String updateUser(@RequestParam("userId") long id, Model model) {
        User user = userService.getUser(id);
        model.addAttribute("user", user);
        return "user-info";
    }
    @DeleteMapping ("/admin/deleteUser")
    public String deleteUser(@RequestParam("userId") long id) {
        userService.deleteUser(id);
        return "redirect:/admin/allUsers";
    }
    @GetMapping("/user")
    public String getUserPage(Principal principal, Model model) {
        model.addAttribute("user", userService.findByEmail(principal.getName()));
        return "user";
    }
}
