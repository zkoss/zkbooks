package org.zkoss.reference.developer.integration.ejb;

import javax.ejb.Local;
import javax.ejb.Remove;

@Local
public interface Account {

	public float deposit(float amount);
	public float withdraw(float amount);
	@Remove 
	public void remove();
}
