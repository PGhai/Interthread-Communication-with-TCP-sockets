import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Random;

public class Customer implements Runnable {

	String custName;
	int loanAmount;
	CentralRepository cr;

	private Socket socket = null;
	private DataOutputStream out = null;
	ServerSocket s = null;

	public Customer(String custName, int loanAmount, CentralRepository cr) {
		this.custName = custName;
		this.loanAmount = loanAmount;
		this.cr = cr;
		cr.addToCustData(custName);

	}

	public void run() {
		int balance = loanAmount;

		// pick random amount

		try {
			ArrayList<String> arr = new ArrayList<>(cr.getBanksList());
			while (balance != 0 && !arr.isEmpty()) {
				int requestAmount = new Random().nextInt(50) + 1;
				
				if (requestAmount > balance) {
					requestAmount = balance;
				}
				
				int index; 
                if(arr.size() >1) {
                index  = new Random().nextInt(arr.size());}
                else {
                 index =0;
                }
				int port = (int) CentralRepository.BanksData.get(arr.get(index));
				Money.DisplayRequest(custName,requestAmount,arr.get(index));
				socket = new Socket("localhost", port);
				s = new ServerSocket(0);
				out = new DataOutputStream(socket.getOutputStream());
				Thread.currentThread().sleep(new Random().nextInt(100));
				out.writeUTF(custName + "#" + requestAmount + "#");
				DataInputStream in = new DataInputStream(socket.getInputStream());
				String response = in.readUTF();
				if (response.equals("Approved")) {
					balance = balance - requestAmount;
				} else {
					arr.remove(arr.get(index));
				}
//				System.out.println("New Balance is "+ balance);

			}

			if(balance == 0) {
			
				Money.DisplayMessage(custName,loanAmount, true);
			}else {
				
				Money.DisplayMessage(custName,loanAmount, false);
			}
           
			socket.close();
		} catch (IOException i) {
			System.out.println(i);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
