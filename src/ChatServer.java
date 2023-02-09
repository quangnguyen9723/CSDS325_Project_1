import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

//TODO: for now, the client may not need to send GREETING message
public class ChatServer {
    public static void main(String[] args) {

        if (args.length != 1) {
            System.err.println("Usage: java ChatServer <port number>");
            System.exit(1);
        }

        int portNumber = Integer.parseInt(args[0]);
        boolean listening = true;
        int clientCount = 0;

        // support for multiple clients using multi threads
        try (ServerSocket serverSocket = new ServerSocket(portNumber)) {
            System.out.println("Server initialized...");
            while (listening) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client " + ++clientCount + " accepted");
                new ChatServerThread(clientSocket).start();
            }
        } catch (IOException e) {
            System.err.println("Could not listen on port " + portNumber);
            System.exit(-1);
        }
    }
}
