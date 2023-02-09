import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;

public class ChatClient {

    public static void main(String[] args) {

        if (args.length != 2) {
            System.err.println("Usage: java ChatClient <host name> <port number>");
            System.exit(1);
        }

        String hostName = args[0];
        int portNumber = Integer.parseInt(args[1]);

        try (
                // socket
                Socket socket = new Socket(hostName, portNumber);
                // sends output
//                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                DataOutputStream out = new DataOutputStream(socket.getOutputStream());
                // takes input from server
//                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                DataInputStream in = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
                // takes input from terminal
                BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
        ) {
            System.out.println("Connected");

            // thread to receive message from server
            new Thread(() -> {
                while (true) {
                    String serverOutput;
                    try {
                        serverOutput = in.readUTF();
                        System.out.println(serverOutput);
                    } catch (IOException e) {
                        e.printStackTrace();
                        break;
                    }

                }
            }).start();

            String clientInput = "";

            while (!clientInput.equals(".")) {
                clientInput = stdIn.readLine();
//                out.println(clientInput);
                out.writeUTF(clientInput);
            }

        } catch (UnknownHostException e) {
            System.err.println("Don't know about host " + hostName);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to " +
                    hostName);
            System.exit(1);
        }

    }
}
