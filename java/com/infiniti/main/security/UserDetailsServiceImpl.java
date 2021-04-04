package com.infiniti.main.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.infiniti.main.modal.Admin;
import com.infiniti.main.repo.AdminRepo;

@Service
public class UserDetailsServiceImpl implements UserDetailsService{

	@Autowired AdminRepo myrepo;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Admin admin = myrepo.getUserByUserName(username);
		if(admin==null)
		{
			throw new UsernameNotFoundException("could not found user!!");
		}
		MyUserDetails userDetails = new MyUserDetails(admin);
		return userDetails;
	}
	
}
