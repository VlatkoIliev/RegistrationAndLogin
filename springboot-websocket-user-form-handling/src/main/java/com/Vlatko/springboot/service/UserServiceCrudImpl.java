package com.Vlatko.springboot.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import com.Vlatko.springboot.domain.User;
import com.Vlatko.springboot.repository.UserRepository;

@Service
public class UserServiceCrudImpl implements UserServiceCrud {
	
	@Autowired
	private UserRepository userRepo;

	@Override
	public void deleteUserById(Long id) {
		userRepo.deleteById(id);
		
	}

	@Override
	public void updateUser(User user) {
		user.setFirstName(user.getFirstName());
		user.setLastName(user.getLastName());
		user.setEmail(user.getEmail());
		userRepo.save(user);
		
	}

	@Override
	public User getUserByid(Long id) {
		Optional<User> useropt = userRepo.findById(id);
		User user = null;
		if(useropt.isPresent()) {
			user = useropt.get();
		}
		else {
			throw new RuntimeException("User not found");
		}
		return user;
	}
	
	public void register(User user) {
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		String encodedPassword = passwordEncoder.encode(user.getPassword());
		user.setPassword(encodedPassword);
		userRepo.save(user);
	}

	@Override
	public Page<User> findPaginated(int pageNo, int pageSize, String sortField, String sortDirection) {
		Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending() :
			Sort.by(sortField).descending();
		Pageable pageable = PageRequest.of(pageNo-1, pageSize, sort);
		return userRepo.findAll(pageable);
	}
	
	
	

	/*
	 * @Override public void createUser(User user) { BCryptPasswordEncoder
	 * passwordEncoder = new BCryptPasswordEncoder(); String encodedPassword =
	 * passwordEncoder.encode(user.getPassword());
	 * user.setPassword(encodedPassword); userRepo.save(user); }
	 */

}
