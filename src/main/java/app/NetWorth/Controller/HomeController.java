package app.NetWorth.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import app.NetWorth.Entity.User;

@Controller
public class HomeController {

	@GetMapping("/")
	public String homepage(Model model) {
		System.out.println("at home");
		model.addAttribute("user", new User());
		return "getin";
	}

}
