import java.io.*;
import java.net.Socket;

public class ChatClient {
    private Socket socket = null;
    private BufferedReader input;
    //TODO
//    private DataOutputStream output = null;
    private PrintWriter output = null;

    public ChatClient(String address, int port) {
        //establish connection
        establishConnection(address, port);
        createRecipientThread();
        createPersistentConnection();
    }

    private void establishConnection(String address, int port) {
        try {
            socket = new Socket(address, port);
            //takes input from terminal
            input = new BufferedReader(new InputStreamReader(System.in));
            //sends output to the socket (then to server)
            //TODO
//            output = new DataOutputStream(socket.getOutputStream());
            output = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException e) {
            System.out.format("Cannot connect to address %s at port %d", address, port);
        }
        System.out.println("Connected");
    }

    // RECEIVER
    private void createRecipientThread() {
        //Here create a thread to receive message from server.
        // TOOD
//        DataInputStream inp;
        BufferedReader inp;
        try {
            //TODO
//            inp = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
            inp = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            System.out.println("error creating input stream reader");
            throw new RuntimeException(e);
        }

        new Thread(() -> {
            while (true) {
                String str;
                try {
                    //TODO
//                    str = inp.readUTF();
                    str = inp.readLine();
                    System.out.println(str);
                } catch (IOException e) {
                    e.printStackTrace();//error.
                    break;
                }
            }
        }, "Client Receiver.").start();
    }

    // SENDER
    private void createPersistentConnection() {
        //string to read message from input
        String line = "";

        //keep reading until "Over" is input
        while (!line.equals(".")) {
            try {
                line = input.readLine();
            } catch (IOException e) {
                System.out.println("error while reading input");
            }

//            try {
                //TODO
//                output.writeUTF(line);
                output.println(line);
//            } catch (IOException e) {
//                System.out.println("error while sending message to server");
//            }
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

    public static void main(String[] args) throws IOException {
        new ChatClient(args[0], Integer.parseInt(args[1]));
    }
}