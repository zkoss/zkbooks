package bigbank;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.util.Assert;

@Qualifier("bankServiceScenario2Impl")
public class BankServiceScenario2Impl implements BankServiceScenario2 {
	private BankDao bankDao;

	public BankServiceScenario2Impl(BankDao bankDao) {
		Assert.notNull(bankDao);
		this.bankDao = bankDao;
	}

	public Account[] findAccounts() {
		return this.bankDao.findAccounts();
	}

	public Account post(Account account, double amount) {
		Assert.notNull(account);
		Assert.notNull(account.getId());
		
		// We read account bank from DAO so it reflects the latest balance
		Account a = bankDao.readAccount(account.getId());
		if (account == null) {
			throw new IllegalArgumentException("Couldn't find requested account");
		}
		
		a.setBalance(a.getBalance() + amount);
		bankDao.createOrUpdateAccount(a);
		return a;
	}

	public Account readAccount(Long id) {
		return bankDao.readAccount(id);
	}
}
