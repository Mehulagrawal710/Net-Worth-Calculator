package app.NetWorth.Controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ErrorHandling {

	@GetMapping("/error")
	public String errorHandling() {
		return "Error fallback";
	}

}
