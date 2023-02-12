import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ChatServer {
    public static void main(String[] args) {
        ServerSocket serverSocket = null;
        Socket socket = null;

        //starts the server
        try {
            serverSocket = new ServerSocket(Integer.parseInt(args[0]));
            System.out.println("Server Initialized...\n");
        } catch (IOException e) {
            System.out.println("error creating server socket");
            System.exit(1);
        }

        //while loop to accept multiple clients
        while (true) {
            try {
                socket = serverSocket.accept();
            } catch (IOException e) {
                System.out.println("error accepting client socket");
            }

            //starts the server thread
            new ChatServerThread(socket).start();
        }
    }
}