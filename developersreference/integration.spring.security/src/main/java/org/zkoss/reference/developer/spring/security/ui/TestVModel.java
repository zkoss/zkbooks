/**
 * 
 */
package org.zkoss.reference.developer.spring.security.ui;

import org.springframework.security.access.AccessDeniedException;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.reference.developer.spring.security.SecurityUtil;

/**
 * @author Ian YT Tsai (zanyking)
 *
 */
public class TestVModel {

	private String firstName = "";
	private String lastName = "";
	

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getFullName(){
		return firstName+ " "+lastName;
	}
	
	@Command
    @NotifyChange("fullName")
	public void doChange(){
		if(SecurityUtil.isNoneGranted("ROLE_EDITOR")){
			throw new AccessDeniedException("you are not an editor!");
		}
	}
}
