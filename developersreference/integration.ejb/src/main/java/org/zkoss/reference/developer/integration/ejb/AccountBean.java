package org.zkoss.reference.developer.integration.ejb;

import javax.ejb.Local;
import javax.ejb.Remove;
import javax.ejb.Stateful;

@Stateful(name="Account")
@Local(Account.class)  
public class AccountBean implements Account {
	float balance = 0;

	public float deposit(float amount){
		balance += amount;
		return balance;
	}
	public float withdraw(float amount){
		balance -= amount;
		return balance;
	}
	@Remove  
	public void remove() {
		balance = 0;
	}
}
