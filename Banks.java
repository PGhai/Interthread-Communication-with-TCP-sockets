import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;

public class Banks implements Runnable {

	String bankName;
	int amount;
	int port;
	CentralRepository cr;
	ServerSocket s = null;
	private Socket socket = null;
	private Socket newsocket = null;
	private DataInputStream in = null;
	private DataOutputStream out = null;

	public Banks(String BankName, int Amount, CentralRepository cr) throws IOException {
		bankName = BankName;
		amount = Amount;
		s = new ServerSocket(0);
		this.cr = cr;

//		System.out.println("listening on port: " + s.getLocalPort());
		cr.addToBanksData(bankName, s.getLocalPort());
	}

	public void sendAcknowledgement(String message) throws IOException {
		out = new DataOutputStream(socket.getOutputStream());
		out.writeUTF(message);
	}

	public void run() {

		String line = "";
		boolean flag = true;
		HashSet Cust = (HashSet) cr.getCustSet();
		while (flag) {
			try {
				socket = s.accept();

				in = new DataInputStream(new BufferedInputStream(socket.getInputStream()));

				line = in.readUTF();

				String[] data = line.split("#");
				
				if (!data[0].equals("terminate")) {
					// System.out.println("I have "+amount+" money.");
					if (Integer.parseInt(data[1]) > amount) {

						sendAcknowledgement("Not Approved");
				
						Money.DisplayRequestApprovals(data[0], Integer.parseInt(data[1]), bankName, false);

					} else {
						synchronized (this) {
							amount = amount - Integer.parseInt(data[1]);
						}
						// System.out.println(bankName+" Now, I have "+amount+" money.");
						sendAcknowledgement("Approved");
						Money.DisplayRequestApprovals(data[0], Integer.parseInt(data[1]), bankName, true);

					}
				} else {
					Money.DisplayBalance(bankName, amount);
                    
				}

			} catch (Exception e) {

			}

		}
	}

}
