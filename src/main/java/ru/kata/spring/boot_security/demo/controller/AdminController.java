package ru.kata.spring.boot_security.demo.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;
import javax.validation.Valid;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Collections;
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
        return "user-view";
    }

    @GetMapping("/admin/addNewUser")
    public String addNewUser(@ModelAttribute("user") User user, Model model) {
        model.addAttribute("rolesList", roleService.getAllRoles());
        return "user-info";
    }
    @PostMapping("/admin/addNewUser")
    public String saveUser(@ModelAttribute("user") @Valid User user, BindingResult bindingResult, @RequestParam(value = "checkedIdRoles") List<Integer> checkedIdRoles) {
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
    @GetMapping("/admin/updateInfo/{id}")
    public String updateUser(@PathVariable("id") Long id, Model model) {
        model.addAttribute("user", userService.getUser(id));
        model.addAttribute("rolesList", roleService.getAllRoles());
        return "user-update";
    }
    @PostMapping("/admin/updateInform/{getId}")
    public String updateInformUser(@PathVariable("getId") Long id, @ModelAttribute("user") User user, @RequestParam("roleId") Integer roleId) {
        user.setId(id);
        Role role = roleService.getRole(roleId);
        user.setRoles(Collections.singletonList(role));
        userService.saveUser(user);
        return "redirect:/admin/allUsers";
    }

    @GetMapping("/admin/deleteUser")
    public String deleteUser(@RequestParam("userId") long id) {
        userService.deleteUser(id);
        return "redirect:/admin/allUsers";
    }
}
