package ru.kata.spring.boot_security.demo.controller;

;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/")
public class AdminController {

    private final UserService userService;
    private final RoleService roleService;

    public AdminController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping("/admin")
    public String getAdminPage(Principal principal, Model model) {
        model.addAttribute("user", userService.findByEmail(principal.getName()));
        return "admin";
    }

    @GetMapping("/admin/allUsers")
    public String showAllUsers(Model model) {
        model.addAttribute("allUsers", userService.getAllUsers());
        model.addAttribute("allRoles", roleService.getAllRoles());
        model.addAttribute("newUser", new User());
        return "user-view";
    }

    @GetMapping("/admin/addNewUser")
    public String addNewUser(Model model) {
        model.addAttribute("rolesList", roleService.getAllRoles());
        return "user-info";
    }

    @PostMapping("/admin/addNewUser")
    public String saveUser(@ModelAttribute("newUser") @Valid User user, BindingResult bindingResult, @RequestParam(value = "checkedIdRoles") List<Integer> checkedIdRoles) {
        if (bindingResult.hasErrors()) {
            return "user-info";
        }
        List<Role> roles = new ArrayList<>();
        for (Integer id : checkedIdRoles) {
            roles.add(roleService.getRole(id));
            user.setRoles(roles);
        }
        userService.saveUser(user);
        return "redirect:/admin/allUsers";
    }
    @GetMapping("/admin/updateInfo")
    public String updateUser(@RequestParam("userId") long id, Model model) {
        User user = userService.getUser(id);
        model.addAttribute("user", user);
        model.addAttribute("rolesList", roleService.getAllRoles());
        return "user-info";
    }

    @GetMapping("/admin/deleteUser")
    public String deleteUser(@RequestParam("userId") long id) {
        userService.deleteUser(id);
        return "redirect:/admin/allUsers";
    }
}
