package com.Vlatko.springboot.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.Vlatko.springboot.domain.User;
import com.Vlatko.springboot.repository.UserRepository;
import com.Vlatko.springboot.service.UserServiceCrudImpl;

@org.springframework.stereotype.Controller
public class Controller {

	@Autowired
	private UserServiceCrudImpl userService;
	
	@Autowired
	private UserRepository userRepo;
	
	@GetMapping("")
	public String viewHomePage() {
		return "index";
	}
	
	@GetMapping("/register")
	public String showRegisterForm(Model model, User user) {
		model.addAttribute("user", user); 
		return "signup_form";
	}
	
	@PostMapping("/process_register")
	public String processRegisterForm(User user) {
		userService.register(user);
		return "register_success";
		
	}
	
	/*
	 * @GetMapping("/users") public String listUsers(Model model) { List<User>
	 * listUsers = userRepo.findAll(); model.addAttribute("listUsers", listUsers);
	 * 
	 * return "users"; }
	 */
	
	@GetMapping("/login")
	public String showLoginForm() {
		return "login";
	}
	
	@GetMapping("/delete/{id}")
	public String deleteUser(@PathVariable(value="id") Long id) {
		userService.deleteUserById(id);
		return "redirect:/";
	}
	
	@GetMapping("/update/{id}")
	public String showUpdateForm(@PathVariable(value="id") Long id, Model model) {
		User user = userService.getUserByid(id);
		model.addAttribute("user", user);
		return "update_form";
	}
	
	// valjda moze ako e nov user da se povika metod create a ako ne update
	@PostMapping("/saveUser")
	public String processUpdate(@ModelAttribute("user") User user) {
		userService.updateUser(user);
		return "redirect:/";
	}
	
	@GetMapping("/createUser")
	public String newUser(Model model) {
		model.addAttribute("user", new User());
		return "create_user";
	}
	
	
	@PostMapping("/saveNewUser")
	public String createNewUser(@ModelAttribute("user") User user) {
		userService.register(user);
		return "redirect:/";
	}
	
	// pagination and sorting
	
	// URI = /users/1?sortField=name&sortDir=asc
		@GetMapping("/users/{pageNo}")
		public String findPaginated(@PathVariable(value="pageNo") int pageNo,
				@RequestParam("sortField") String sortField,
				@RequestParam("sortDir") String sortDir,
				Model model) {
			int pageSize=5;
			Page<User> page = userService.findPaginated(pageNo, pageSize, sortField, sortDir);
			List<User> listUsers = page.getContent();
			
			model.addAttribute("currentPage", pageNo);
			model.addAttribute("totalPages", page.getTotalPages());
			model.addAttribute("totalItems", page.getTotalElements());
			
			model.addAttribute("sortField", sortField);
			model.addAttribute("sortDir", sortDir);
			model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");
			model.addAttribute("listUsers", listUsers);
			return "users";
			
		}
		@GetMapping("/users")
		public String viewHome(Model model) {
			return findPaginated(1,"firstName", "asc",  model);
		}
	
}
