package com.skilloVilla.DataLoader;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.skilloVilla.Entity.User;
import com.skilloVilla.Repository.UserRepository;

@Component
public class LibraryUser {

	/*
	 * This Class is used for per loading data in the data base of User 
	 * here 4 User are present in the data base 
	 * data base name :- libraryManagmentSystem 
	 */
	
	@Autowired
    private UserRepository userRepository;
	
	@PostConstruct
	public void loadUser() {
		
		User user1 = new User();
		user1.setUserName("Shanu Kumrawat");
		user1.setUserContact("8602987958");
		user1.setUserEmail("shanu.kumrawat@gmail.com");
		user1.setUserAddress("Dewas");
		userRepository.save(user1);
		
		User user2 = new User();
		user2.setUserName("Pawan Patil");
		user2.setUserContact("9907098795");
		user2.setUserEmail("pawan.patil@gmail.com");
		user2.setUserAddress("Dewas");
		userRepository.save(user2);
		
		User user3 = new User();
		user3.setUserName("Nidhi Chouhan");
		user3.setUserContact("7895246897");
		user3.setUserEmail("nidhi.chouhan@gmail.com");
		user3.setUserAddress("Dewas");
		userRepository.save(user3);
		
		User user4 = new User();
		user4.setUserName("Diksha Choudhary");
		user4.setUserContact("9997895231");
		user4.setUserEmail("diksha.choudhary@gmail.com");
		user4.setUserAddress("Dewas");
		userRepository.save(user4);

	}
}
