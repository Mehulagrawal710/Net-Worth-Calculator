package app.NetWorth.Controller;

import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import app.NetWorth.Entity.User;
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

}
