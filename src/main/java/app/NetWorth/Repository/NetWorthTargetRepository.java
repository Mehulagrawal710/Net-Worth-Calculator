package app.NetWorth.Repository;

import org.springframework.data.mongodb.repository.DeleteQuery;
import org.springframework.data.mongodb.repository.MongoRepository;

import app.NetWorth.Entity.NetWorthTarget;

public interface NetWorthTargetRepository extends MongoRepository<NetWorthTarget, String> {

	NetWorthTarget findOneByBelongsToUserId(String userId);

	@DeleteQuery
	void deleteByBelongsToUserId(String userId);

}