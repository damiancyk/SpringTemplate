package com.damiancyk.security;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.damiancyk.beans.Operator;

@Service
public class UserDetailsServiceCustom implements UserDetailsService {

	@Autowired
	Operator operator;

	public UserDetails loadUserByUsername(String username)
			throws UsernameNotFoundException {
		try {

			boolean enabled = true;
			boolean accountNonExpired = true;
			boolean credentialsNonExpired = true;
			boolean accountNonLocked = true;

			List<GrantedAuthority> grantedAuthorities = new ArrayList<GrantedAuthority>();
			grantedAuthorities.add(new SimpleGrantedAuthority("ADMIN_ROLE"));

			return new User("admin", "21232f297a57a5a743894a0e4a801fc3",
					enabled, accountNonExpired, credentialsNonExpired,
					accountNonLocked, grantedAuthorities);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
}