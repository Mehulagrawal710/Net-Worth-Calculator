package app.NetWorth.Service;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import app.NetWorth.Entity.Section;
import app.NetWorth.Entity.User;
import app.NetWorth.Repository.SectionRepository;

@Service
public class RecordService {

	@Autowired
	SectionRepository sectionRepository;

	public void generateDefaultSections(User user) {
		String userId = user.getId();
		ArrayList<Section> defaultSections = new ArrayList<>();
		// Asset Sections
		defaultSections.add(new Section("Cash Holdings", "asset", 0, userId, true));
		defaultSections.add(new Section("Stock Investments", "asset", 0, userId, true));
		defaultSections.add(new Section("Real Estate", "asset", 0, userId, true));
		defaultSections.add(new Section("Automobiles", "asset", 0, userId, true));
		// Liability Section
		defaultSections.add(new Section("Mortgages", "liability", 0, userId, true));

		sectionRepository.saveAll(defaultSections);
	}

}
