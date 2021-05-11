package app.NetWorth.Controller;

import java.util.ArrayList;
import java.util.Date;
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
import app.NetWorth.Entity.NetWorthTarget;
import app.NetWorth.Entity.NetWorthTimeSeries;
import app.NetWorth.Entity.Section;
import app.NetWorth.Entity.User;
import app.NetWorth.Repository.EntryRepository;
import app.NetWorth.Repository.NetWorthTargetRepository;
import app.NetWorth.Repository.NetWorthTimeSeriesRepository;
import app.NetWorth.Repository.SectionRepository;
import app.NetWorth.Repository.UserRepository;
import app.NetWorth.Service.RecordService;

@Controller
@RequestMapping("/record/{username}")
public class RecordController {

	@Autowired
	UserRepository userRepository;
	@Autowired
	SectionRepository sectionRepository;
	@Autowired
	EntryRepository entryRepository;
	@Autowired
	NetWorthTimeSeriesRepository netWorthTimeSeriesRepository;
	@Autowired
	NetWorthTargetRepository netWorthTargetRepository;
	@Autowired
	RecordService recordService;

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
		model.addAttribute("entryMap", sectionIdToEntryListMap);
		model.addAttribute("netWorth", netWorth);
		NetWorthTimeSeries netWorthTimeSeries = netWorthTimeSeriesRepository.findOneByBelongsToUserId(userId);
		model.addAttribute("netWorthTimeSeries", netWorthTimeSeries);
		NetWorthTarget netWorthTarget = netWorthTargetRepository.findOneByBelongsToUserId(userId);
		ArrayList<Date> extrapolatedLabelDate = new ArrayList<Date>();
		extrapolatedLabelDate.add(new Date());
		model.addAttribute("extrapolatedLabelDate", extrapolatedLabelDate);
		if (netWorthTarget == null) {
			model.addAttribute("isTargetSet", false);
			model.addAttribute("newTarget", new NetWorthTarget());
			model.addAttribute("targetCheckpoint", null);
		} else {
			model.addAttribute("isTargetSet", true);
			model.addAttribute("netWorthTarget", netWorthTarget);
			ArrayList<Integer> targetCheckpoint = recordService.getTargetCheckpoint(netWorthTarget, netWorthTimeSeries,
					extrapolatedLabelDate);
			model.addAttribute("targetCheckpoint", targetCheckpoint);
		}
		return "record";
	}

	@PostMapping("/section")
	public String addSection(@PathVariable("username") String username, @ModelAttribute Section newSection) {
		User user = userRepository.findOneByUsername(username);
		String userId = user.getId();
		newSection.setBelongsToUserId(userId);
		newSection.setTotalAmount(0);
		newSection.setIsDefault(false);
		sectionRepository.save(newSection);
		return "redirect:/record/" + username;
	}

	@PostMapping("/entry")
	public String addEntry(@PathVariable("username") String username, @ModelAttribute Entry newEntry) {

		String sectionId = newEntry.getBelongsToSectionId();
		Section section = sectionRepository.findById(sectionId).orElse(null);
		section.setTotalAmount(section.getTotalAmount() + newEntry.getAmount());
		sectionRepository.save(section);

		newEntry.setCreatedOn(new Date());
		entryRepository.save(newEntry);

		String entryType = section.getType();
		Date entryTimeStamp = newEntry.getCreatedOn();
		recordService.updateNetWorthTimeSeries(username, entryType, entryTimeStamp, newEntry.getAmount());

		return "redirect:/record/" + username;
	}

	@PostMapping("/target")
	public String setTarget(@PathVariable("username") String username, @ModelAttribute NetWorthTarget target) {

		User user = userRepository.findOneByUsername(username);
		target.setBelongsToUserId(user.getId());
		target.setTargetCreatedOn(new Date());
		netWorthTargetRepository.save(target);

		return "redirect:/record/" + username;
	}

	@PostMapping("/removetarget")
	public String removeTarget(@PathVariable("username") String username, @ModelAttribute NetWorthTarget target) {

		User user = userRepository.findOneByUsername(username);
		netWorthTargetRepository.deleteByBelongsToUserId(user.getId());

		return "redirect:/record/" + username;
	}

}
