package ru.kata.spring.boot_security.demo.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;
import javax.validation.Valid;
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

//    @GetMapping("/admin")
//    public String getAdminPage(Principal principal, Model model) {
//        model.addAttribute("user", userService.findByEmail(principal.getName()));
//        return "admin";
//    }
    @GetMapping("/admin/allUsers")
    public String showAllUsers(Model model, @ModelAttribute("user") User user) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        model.addAttribute("user", userService.findByEmail(auth.getName()));
        model.addAttribute("allUsers", userService.getAllUsers());
        model.addAttribute("allRoles", roleService.getAllRoles());
        model.addAttribute("newUser", new User());
        return "bootstrap-admin-panel";
    }

//    @GetMapping("/admin/addNewUser")
//    public String addNewUser(@ModelAttribute("user") User user, Model model) {
//        model.addAttribute("rolesList", roleService.getAllRoles());
//        return "bootstrap-admin-panel";
//    }
//    return "user-add";
    @PostMapping("/admin/addNewUser")
    public String saveUser(@ModelAttribute("user") @Valid User user, BindingResult bindingResult, @RequestParam(value = "checkedIdRoles") List<Integer> checkedIdRoles) {
        if (bindingResult.hasErrors()) {
            return "bootstrap-admin-panel";
        }
        userService.saveUser(user, checkedIdRoles);
        return "redirect:/admin/allUsers";
    }
    @GetMapping("/admin/updateInfo/{id}")
    public String updateUser(@PathVariable("id") Long id, Model model) {
        model.addAttribute("user", userService.getUser(id));
        model.addAttribute("rolesList", roleService.getAllRoles());
        return "bootstrap-admin-panel";
    }
    @PostMapping("/admin/updateInform/{getId}")
    public String updateInformUser(@PathVariable("getId") Long id, @ModelAttribute("user") User user, @RequestParam("roleId") Integer roleId) {
        user.setId(id);
        user.setRoles(Collections.singletonList(roleService.getRole(roleId)));
        userService.updateUser(user);
        return "redirect:/admin/allUsers";
    }

    @GetMapping("/admin/deleteUser/{getId}")
    public String deleteUser(@PathVariable("getId") Long id) {
        userService.deleteUser(id);
        return "redirect:/admin/allUsers";
    }
}
