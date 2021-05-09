package app.NetWorth.Repository;

import java.util.ArrayList;

import org.springframework.data.mongodb.repository.MongoRepository;

import app.NetWorth.Entity.Entry;

public interface EntryRepository extends MongoRepository<Entry, String>{

	ArrayList<Entry> findByBelongsToSectionId(String sectionId);
	
}
