package app.NetWorth.Controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import app.NetWorth.Entity.Entry;
import app.NetWorth.Entity.Section;
import app.NetWorth.Entity.User;
import app.NetWorth.Repository.EntryRepository;
import app.NetWorth.Repository.SectionRepository;
import app.NetWorth.Repository.UserRepository;

@Controller
@RequestMapping("/record/{username}")
public class RecordController {

	@Autowired
	UserRepository userRepository;
	@Autowired
	SectionRepository sectionRepository;
	@Autowired
	EntryRepository entryRepository;

	@GetMapping
	public String getUserRecord(@PathVariable("username") String username, Model model) {
		User user = userRepository.findOneByUsername(username);
		String userId = user.getId();
		ArrayList<Section> sections = sectionRepository.findByBelongsToUserId(userId);
		model.addAttribute("user", user);
		model.addAttribute("sections", sections);
		model.addAttribute("newSection", new Section());
		model.addAttribute("newEntry", new Entry());
		Map<String, ArrayList<Entry>> sectionIdToEntryListMap = new HashMap<>();
		int netWorth = 0;
		for (Section section : sections) {
			String sectionId = section.getId();
			ArrayList<Entry> entriesOfThisSection = entryRepository.findByBelongsToSectionId(sectionId);
			sectionIdToEntryListMap.put(sectionId, entriesOfThisSection);
			if (section.getType().equals("asset")) {
				netWorth += section.getTotalAmount();
			} else {
				netWorth -= section.getTotalAmount();
			}
		}
		System.out.println(netWorth);
		model.addAttribute("entryMap", sectionIdToEntryListMap);
		model.addAttribute("netWorth", netWorth);
		return "record";
	}

	@PostMapping("/section")
	public String addSection(@PathVariable("username") String username, @ModelAttribute Section newSection,
			Model model) {
		User user = userRepository.findOneByUsername(username);
		String userId = user.getId();
		newSection.setBelongsToUserId(userId);
		newSection.setTotalAmount(0);
		newSection.setIsDefault(false);
		sectionRepository.save(newSection);
		return "redirect:/record/" + username;
	}

	@PostMapping("/entry")
	public String addEntry(@PathVariable("username") String username, @ModelAttribute Entry newEntry, Model model) {
		String sectionId = newEntry.getBelongsToSectionId();
		Section section = sectionRepository.findById(sectionId).orElse(null);
		section.setTotalAmount(section.getTotalAmount() + newEntry.getAmount());
		sectionRepository.save(section);
		entryRepository.save(newEntry);
		return "redirect:/record/" + username;
	}

}
