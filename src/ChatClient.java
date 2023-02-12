import java.io.*;
import java.net.Socket;

public class ChatClient {
    private Socket socket = null;
    private BufferedReader input;
    private PrintWriter output = null;

    public ChatClient(String address, int port) {
        //establish connection
        establishConnection(address, port);
        createRecipientThread();
        createPersistentOutput();
    }

    private void establishConnection(String address, int port) {
        try {
            socket = new Socket(address, port);
            //takes input from terminal
            input = new BufferedReader(new InputStreamReader(System.in));
            //sends output to the socket (then to server)
            output = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException e) {
            System.out.format("Cannot connect to address %s at port %d", address, port);
        }
        System.out.println("Connected");
    }

    // RECEIVER
    private void createRecipientThread() {
        //Here create a thread to receive message from server.
        BufferedReader inp;
        try {
            inp = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            System.out.println("error creating input stream reader");
            throw new RuntimeException(e);
        }

        new Thread(() -> {
            String line;
            try {
                while ((line = inp.readLine()) != null) {
                    System.out.println(line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }, "Client Receiver.").start();
    }

    // SENDER
    private void createPersistentOutput() {
        //string to read message from input
        String line;

        //keep reading until "Over" is input
        try {
            while ((line = input.readLine()) != null) {
//                System.out.println("type: " + line);
                output.println(line);
            }
        } catch (IOException e) {
            System.out.println("error while reading input");
        }
        //close the connection
        try {
            input.close();
            output.close();
            socket.close();
        } catch (IOException e) {
            System.out.println("error while cleaning resources");
        }
    }

    public static void main(String[] args) {
        new ChatClient(args[0], Integer.parseInt(args[1]));
    }
}