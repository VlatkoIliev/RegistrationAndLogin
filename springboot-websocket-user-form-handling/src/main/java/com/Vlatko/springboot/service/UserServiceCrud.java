package com.Vlatko.springboot.service;

import org.springframework.data.domain.Page;


import com.Vlatko.springboot.domain.User;

public interface UserServiceCrud {
	
	public void deleteUserById(Long id);
	
	public void updateUser(User user);
	
	public User getUserByid(Long id);
	
	Page<User> findPaginated(int pageNo, int pageSize, String sortField, String sortDirection);
	
	

}
