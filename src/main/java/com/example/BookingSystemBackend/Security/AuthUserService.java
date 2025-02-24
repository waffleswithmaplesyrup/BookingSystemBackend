package com.example.BookingSystemBackend.Security;

import com.example.BookingSystemBackend.Model.User;
import com.example.BookingSystemBackend.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AuthUserService implements UserDetailsService {

	private UserRepository userRepository;

	@Autowired
	public AuthUserService(UserRepository userRepository) {
		super();
		this.userRepository = userRepository;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepository.findByEmail(username).orElseThrow(
				()-> new UsernameNotFoundException("Email " + username + " not found"));
		return new AuthUser(user);
	}

}
