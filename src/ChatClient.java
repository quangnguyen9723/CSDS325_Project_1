import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class ChatClient {

    public static void main(String[] args) throws IOException {

        if (args.length != 2) {
            System.err.println(
                    "Usage: java ChatClient <host name> <port number>");
            System.exit(1);
        }

        String hostName = args[0];
        int portNumber = Integer.parseInt(args[1]);

        try (
                Socket chatSocket = new Socket(hostName, portNumber);
                PrintWriter out = new PrintWriter(chatSocket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(chatSocket.getInputStream()));
                BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
        ) {
//            String serverOutput;
            String clientInput;

            while ((clientInput = stdIn.readLine()) != null) {
                out.println(clientInput);
                System.out.println(in.readLine());
            }

//            while (true) {
//                System.out.println("test");
//                clientInput = stdIn.readLine();
//                serverOutput = in.readLine();
//
//                if (clientInput != null) {
//                    out.println(clientInput);
//                }
//
//                if (serverOutput != null) {
//                    System.out.println(serverOutput);
//                }
//            }

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
