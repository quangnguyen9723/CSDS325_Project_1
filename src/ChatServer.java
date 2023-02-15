import java.io.IOException;
import java.net.ServerSocket;

public class ChatServer {
    public static void main(String[] args) {

        if (args.length != 1) {
            System.err.println("Usage: java ChatServer <port number>");
            System.exit(1);
        }

        int portNumber = Integer.parseInt(args[0]);
        boolean listening = true;

        // init server and accept multiple clients
        try (ServerSocket serverSocket = new ServerSocket(portNumber)) {
            System.out.println("Server Initialized...\n");
            while (listening) {
                new ChatServerThread(serverSocket.accept()).start();
            }
        } catch (IOException e) {
            System.out.format("error: server cannot listen on port %d", portNumber);
            System.exit(-1);
        }
    }
}