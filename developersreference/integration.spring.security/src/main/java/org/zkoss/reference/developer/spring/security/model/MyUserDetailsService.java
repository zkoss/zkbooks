/**
 * 
 */
package org.zkoss.reference.developer.spring.security.model;

import java.util.HashMap;
import java.util.Map;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


/**
 * A custom UserDetailsService implementation to adapt to extended User class.<br>
 * 
 * If you have your own user credential model and don't want to change it even a bit, 
 * then instead of swapping the UserDetailService of default AuthenticationProvider, 
 * you have to swap the AuthenticationProvider itself, and you'll have to deal with 
 * password hashing and salting by your own.<br>
 * 
 * 
 * @author Ian YT Tsai (zanyking)
 *
 */

@Service
public class MyUserDetailsService implements UserDetailsService {

	private static final Map<String, MyUser> USERS = new HashMap<String,MyUser>();
	private static void add(MyUser mu){
		USERS.put(mu.getUsername(), mu);
	}
	static{
		
		add(new MyUser("rod","81dc9bdb52d04dc20036dbd8313ed055", //password:1234 
			new String[]{"ROLE_USER", "ROLE_EDITOR"} ));
		
		add(new MyUser("dianne","81dc9bdb52d04dc20036dbd8313ed055", 
			new String[]{"ROLE_USER", "ROLE_EDITOR"} ));
		
		add(new MyUser("scott","81dc9bdb52d04dc20036dbd8313ed055", 
			new String[]{"ROLE_USER"} ));
		
		add(new MyUser("peter","81dc9bdb52d04dc20036dbd8313ed055", 
			new String[]{"ROLE_USER"} ));
	}
	
	// must return a value or throw UsernameNotFoundException
	public UserDetails loadUserByUsername(String username)
			throws UsernameNotFoundException {
		MyUser user = USERS.get(username);
		if(user==null){
			System.out.println(">>> cannot find user: "+username);
			throw new UsernameNotFoundException("cannot found user: "+username);
		}
		//Return a clone object to avoid a user's credentials data being erased after success authentication.
		//This behavior only happens from Spring Security 3.1 onwards.
		//please refer to http://static.springsource.org/spring-security/site/docs/3.1.x/reference/core-services.html#core-services-erasing-credentials
		return new MyUser(user.getUsername(), user.getPassword(), user.getAuthorities());
	}


}
