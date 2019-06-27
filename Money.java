import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.Set;

public class Money {

	static public void DisplayRequest(String customer, int amount, String bank) {
		System.out.println(customer + " requests a loan of " + amount + " dollar(s) from " + bank);

	}

	static public void DisplayRequestApprovals(String customer, int amount, String bank, boolean r) {
		if (r) {
			System.out.println(bank + " approves a loan of " + amount + " dollars from bob " + customer);
		} else {
			System.out.println(bank + " denies a loan of " + amount + " dollars from bob " + customer);
		}

	}

	static public void DisplayMessage(String cust, int amt, boolean flag) throws InterruptedException {
	CentralRepository.removeFromCustomer(cust);

		if (flag) {
			System.out.println(cust + " has reached the objective of " + amt + " dollar(s). Woo Hoo!");
		} else {
			System.out.println(cust + " was only able to borrow " + amt + " dollar(s). Boo Hoo!");
		}
		if (CentralRepository.customers.isEmpty()) {
			Thread.currentThread();
			Thread.sleep(1000);
			try {
				requestTerminate();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	static private void requestTerminate() throws UnknownHostException, IOException {
		ServerSocket s = null;
		Socket socket = null;
		DataOutputStream out = null;

		for (String key : CentralRepository.BanksData.keySet()) {
			int port = (int) CentralRepository.BanksData.get(key);
			socket = new Socket("localhost", port);
			s = new ServerSocket(0);
			out = new DataOutputStream(socket.getOutputStream());
			out.writeUTF("terminate#");
		}

	}

	static public void DisplayBalance(String name, int amount) {
		System.out.println(name + " has only " + amount + " dollars left.");
	}

	public void Display(File file) throws FileNotFoundException {
		Scanner sc = new Scanner(file);
		while (sc.hasNext()) {

			String input = sc.next();
			String[] data = input.substring(1, input.length() - 2).split(",");
			String name = data[0];
			int bal = Integer.parseInt(data[1]);
			System.out.println(name + ": " + bal);

		}
		sc.close();
	}

	@SuppressWarnings("resource")
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		CentralRepository cr = new CentralRepository();

		Money obj = new Money();
		File file = new File("banks.txt");
		File cfile = new File("customer.txt");

		try {

			System.out.println("** Customers and loan objectives **");
			obj.Display(cfile);
			System.out.println("** Banks and financial resources **");
			obj.Display(file);

			Scanner sc = new Scanner(file);
			while (sc.hasNext()) {

				String input = sc.next();
				String[] data = input.substring(1, input.length() - 2).split(",");
				String b_name = data[0];
				int bal = Integer.parseInt(data[1]);

				Thread bankthread = new Thread(new Banks(b_name, bal, cr));
				bankthread.setName(b_name);
				bankthread.start();
			}

			sc = new Scanner(cfile);

			while (sc.hasNext()) {

				String input = sc.next();
				String[] data = input.substring(1, input.length() - 2).split(",");
				String c_name = data[0];
				int bal = Integer.parseInt(data[1]);
				cr.addToCustData(c_name);
//				System.out.println("customer:" + c_name + "--" + "amt:" + bal);
				Thread bankthread = new Thread(new Customer(c_name, bal, cr));
				bankthread.setName(c_name);
				bankthread.start();
			}

			sc.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
