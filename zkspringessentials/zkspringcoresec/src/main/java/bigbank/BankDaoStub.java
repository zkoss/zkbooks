package bigbank;

import java.util.HashMap;
import java.util.Map;

public class BankDaoStub implements BankDao {
	private long id = 0;
	@SuppressWarnings("unchecked")
	private Map accounts = new HashMap();
	
	@SuppressWarnings("unchecked")
	public void createOrUpdateAccount(Account account) {
		if (account.getId() == -1) {
			id++;
			account.setId(id);
		}
		accounts.put(new Long(account.getId()), account);
		System.out.println("SAVE: " + account);
	}

	@SuppressWarnings("unchecked")
	public Account[] findAccounts() {
		Account[] a = (Account[]) accounts.values().toArray(new Account[] {});
		System.out.println("Returning " + a.length + " account(s):");
		for (int i = 0; i < a.length; i++) {
			System.out.println(" > " + a[i]);
		}
		return a;
	}

	public Account readAccount(Long id) {
		return (Account) accounts.get(id);
	}

}
