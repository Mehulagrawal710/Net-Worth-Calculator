package app.NetWorth.Repository;

import java.util.ArrayList;

import org.springframework.data.mongodb.repository.MongoRepository;

import app.NetWorth.Entity.Section;

public interface SectionRepository extends MongoRepository<Section, String> {

	ArrayList<Section> findByBelongsToUserId(String userId);

}
