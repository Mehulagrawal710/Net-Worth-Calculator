package app.NetWorth.Repository;

import java.util.ArrayList;

import org.springframework.data.mongodb.repository.MongoRepository;

import app.NetWorth.Entity.User;

public interface UserRepository extends MongoRepository<User, String> {
	
	User findOneByUsername(String username);
	
	ArrayList<User> findByUsername(String username);

	ArrayList<User> findByEmail(String email);

}