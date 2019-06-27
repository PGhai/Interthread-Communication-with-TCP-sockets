import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class CentralRepository {

	static HashMap<String, Integer> BanksData = new HashMap<String, Integer>();
	static HashSet<String> customers = new HashSet<String>();

	public synchronized void  addToCustData(String Name) {

		customers.add(Name);

	}
	
	public Set<String> getCustSet() {
		return customers;
	}

	public synchronized void addToBanksData(String Bank_Name, Integer socket) {

		BanksData.put(Bank_Name, socket);

	}
	static public synchronized void removeFromCustomer(String customer) {
		customers.remove(customer);
	}

	public Set<String> getBanksList() {
		return BanksData.keySet();
	}

}
