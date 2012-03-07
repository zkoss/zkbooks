package bigbank;

import org.springframework.security.access.annotation.Secured;


public interface BankServiceScenario2 {
	
	@Secured("IS_AUTHENTICATED_ANONYMOUSLY")
	public Account readAccount(Long id);
		
	@Secured("IS_AUTHENTICATED_ANONYMOUSLY")
	public Account[] findAccounts();
	
	@Secured("IS_AUTHENTICATED_ANONYMOUSLY")
	public Account post(Account account, double amount);
}
