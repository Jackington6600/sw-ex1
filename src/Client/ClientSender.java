package Client;

import java.io.*;

// Repeatedly reads recipient's nickname and text from the user in two
// separate lines, sending them to the server (read by ServerReceiver
// thread).

public class ClientSender extends Thread {

	private String nickname;
	private PrintStream server;
	private ClientReceiver receiver;

	ClientSender(String nickname, PrintStream server, ClientReceiver receiver) {
		this.nickname = nickname;
		this.server = server;
		this.receiver = receiver;
	}

	public void run() {
		// So that we can use the method readLine:
		BufferedReader user = new BufferedReader(new InputStreamReader(System.in));

		try {
			// Tell the server what my nickname is:
			server.println(nickname);
			
			server.println("refresh:" + nickname);

			// Then loop forever sending messages to recipients via the server:
			while (true) {
				String input = user.readLine();
				String splitInput[] = input.split(":");
				if(receiver.isChallenged()) {
					if(input.equals("y")) {
						server.println("accept:" + receiver.getChallenger());
						//System.out.println("accept:" + receiver.getChallenger());
						receiver.setChallenged(false);
					}
					else if (input.equals("n")) {
						server.println("decline" + receiver.getChallenger());
						receiver.setChallenged(false);
					}
				}
				else if (splitInput[0].equals("challenge")){
					server.println(input);
				}
				else if (splitInput[0].equals("refresh")){
					server.println(input);
				}
				else if (splitInput[0].equals("move")) {
					server.println(input);
				}
				else if (splitInput[0].equals("end")) {
					server.println(input);
				}
				else {
					System.out.println("Invalid command.");
				}
				

			}
		} catch (IOException e) {
			System.err.println("Communication broke in ClientSender" + e.getMessage());
			System.exit(1);
		}
	}
	
	public PrintStream getServer() {
		return server;
	}
}
