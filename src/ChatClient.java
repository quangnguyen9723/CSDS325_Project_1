import java.io.*;
import java.net.Socket;

public class ChatClient {
    // client socket
    private Socket socket = null;
    // to process input from terminal
    private BufferedReader stdin;
    // to send message to server
    private PrintWriter output = null;
    // to process message from server
    private BufferedReader input;

    public ChatClient(String address, int port) {
        establishConnection(address, port);
        createRecipientThread();
        createPersistentOutput();
    }

    // initialize connection to server
    private void establishConnection(String address, int port) {
        try {
            // create socket
            socket = new Socket(address, port);
            //takes input from terminal
            stdin = new BufferedReader(new InputStreamReader(System.in));
            //sends output to the socket (then to server)
            output = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException e) {
            System.out.format("Cannot connect to address %s at port %d", address, port);
            System.exit(1);
        }
    }

    // RECEIVER: create a thread to receive message from server
    private void createRecipientThread() {
        try {
            input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            System.out.println("error creating input stream reader");
            throw new RuntimeException(e);
        }

        new Thread(() -> {
            String line;
            try {
                while ((line = input.readLine()) != null) {
                    System.out.println(line);
                }
            } catch (IOException e) {
                System.out.println("Error: cannot read message from server");
            }
        }, "Client Receiver.").start();
    }

    // SENDER
    private void createPersistentOutput() {
        //string to read message from terminal
        String line;
        try {
            while ((line = stdin.readLine()) != null) {
                output.println(line);
            }
        } catch (IOException e) {
            System.out.println("error: cannot read input from terminal");
        }
        //close the connection
        try {
            stdin.close();
            output.close();
            socket.close();
        } catch (IOException e) {
            System.out.println("error while cleaning resources");
            System.exit(1);
        }
    }

    public static void main(String[] args) {
        new ChatClient(args[0], Integer.parseInt(args[1]));
    }
}