package app.NetWorth.Controller;

import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import app.NetWorth.Entity.User;
import app.NetWorth.Repository.NetWorthTimeSeriesRepository;
import app.NetWorth.Repository.UserRepository;
import app.NetWorth.Service.RecordService;
import app.NetWorth.UtilityClasses.Alert;

@Controller
@RequestMapping("/user")
public class UserController {

	@Autowired
	UserRepository userRepository;
	@Autowired
	RecordService recordService;

	@GetMapping("/all")
	public ResponseEntity<ArrayList<User>> getAllUsers() {
		try {
			ArrayList<User> users = new ArrayList<User>();

			userRepository.findAll().forEach(users::add);

			if (users.isEmpty()) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}

			return new ResponseEntity<>(users, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("/register")
	public String registerUser(@ModelAttribute User user, Model model) {
		String username = user.getUsername();
		ArrayList<User> userListByUsername = userRepository.findByUsername(username);
		if (!userListByUsername.isEmpty()) {
			model.addAttribute("alert",
					new Alert("a user is already registered with this username, kindly choose another one", "warning"));
			model.addAttribute("formToOpen", new String("signup"));
		} else {
			userRepository.save(user);
			recordService.generateDefaultSections(user);
			recordService.initiateNetWorthTimeSeries(user);
			model.addAttribute("alert", new Alert("signup successful, you can login now", "success"));
		}
		return "getin";
	}

	@PostMapping("/login")
	public String loginUser(@ModelAttribute User user, RedirectAttributes redirectAttributes, Model model) {
		String username = user.getUsername();
		String password = user.getPassword();
		ArrayList<User> userListByUsername = userRepository.findByUsername(username);
		if (userListByUsername.size() == 1) {
			User thisUser = userListByUsername.get(0);
			if (thisUser.getPassword().equals(password)) {
				redirectAttributes.addFlashAttribute("user", thisUser);
				return String.format("redirect:/record/%s", username);
			}
			model.addAttribute("alert", new Alert("wrong password", "danger"));
			return "getin";
		}
		model.addAttribute("alert", new Alert("username not found", "danger"));
		return "getin";
	}

	@PostMapping("/{username}/logout")
	public String logoutUser() {
		return "redirect:/";
	}

	@GetMapping("/{username}")
	public String getUserProfile(@PathVariable("username") String username, Model model) {
		User user = userRepository.findOneByUsername(username);
		model.addAttribute("user", user);
		return "userprofile";
	}

	@PostMapping("/{username}/username")
	public String changeUsername(@PathVariable("username") String username,
			@RequestParam("newUsername") String newUsername, Model model) {
		User userInDb = userRepository.findOneByUsername(username);
		if (userInDb.getUsername().equals(newUsername)) {
			model.addAttribute("user", userInDb);
			model.addAttribute("alert", new Alert("new username cannot be same as current one", "warning"));
			return "userprofile";
		}
		User userByNewUsername = userRepository.findOneByUsername(newUsername);
		if (userByNewUsername != null) {
			model.addAttribute("user", userInDb);
			model.addAttribute("alert",
					new Alert("a user is already registered with this username, kindly choose another one", "warning"));
			return "userprofile";
		} else {
			userInDb.setUsername(newUsername);
			userRepository.save(userInDb);
			model.addAttribute("user", userInDb);
			model.addAttribute("alert", new Alert("username changed", "success"));
		}
		return "userprofile";
	}

	@PostMapping("/{username}/password")
	public String changePassword(@PathVariable("username") String username,
			@RequestParam("currentPassword") String currentPassword, @RequestParam("newPassword") String newPassword,
			Model model) {
		User userInDb = userRepository.findOneByUsername(username);
		if (!userInDb.getPassword().equals(currentPassword)) {
			model.addAttribute("alert", new Alert("current password input is wrong", "danger"));
		} else if (currentPassword.equals(newPassword)) {
			model.addAttribute("alert", new Alert("new password cannot be same as current one", "warning"));
		} else {
			userInDb.setPassword(newPassword);
			userRepository.save(userInDb);
			model.addAttribute("alert", new Alert("password changed", "success"));
		}
		model.addAttribute("user", userInDb);
		return "userprofile";
	}

	@PostMapping("/{username}/email")
	public String changeEmail(@PathVariable("username") String username, @RequestParam("newEmail") String newEmail,
			Model model) {
		User userInDb = userRepository.findOneByUsername(username);
		if (userInDb.getEmail().equals(newEmail)) {
			model.addAttribute("alert", new Alert("new email cannot be same as current one", "warning"));
		} else {
			userInDb.setEmail(newEmail);
			userRepository.save(userInDb);
			model.addAttribute("alert", new Alert("Email changed", "success"));
		}
		model.addAttribute("user", userInDb);
		return "userprofile";
	}

}
