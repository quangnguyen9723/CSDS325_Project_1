import java.io.*;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

class ChatServerThread extends Thread {
    // list of client sockets
    private static final List<Socket> socketList = new LinkedList<>();
    // socket that this thread handle
    private final Socket clientSocket;
    // process message from client
    private BufferedReader input = null;

    public ChatServerThread(Socket clientSocket) {
        this.clientSocket = clientSocket;
        socketList.add(this.clientSocket);
    }

    @Override
    public void run() {
        initInputStream();

        String line;
        try {
            while ((line = input.readLine()) != null) {
                sendMessageToClients(line);
            }
        } catch (IOException e) {
            System.out.println("error while reading input from client");
        }

        closeConnection();
    }

    private void initInputStream() {
        try {
            input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        } catch (IOException e) {
            System.out.println("error while creating input stream");
            System.exit(-1);
        }
    }

    private void sendMessageToClients(String message) {
        ListIterator<Socket> iterator = socketList.listIterator();

        while (iterator.hasNext()) {
            Socket socket = iterator.next();

            if (socket.isClosed()) {
                iterator.remove();
                continue;
            }

            try (PrintWriter output = new PrintWriter(socket.getOutputStream(), true)) {
                //format message to include sender's information
                String address = clientSocket.getLocalAddress().toString().substring(1);
                int port = clientSocket.getPort();
                String formattedMessage = String.format("<From %s:%d>: %s", address, port, message);
                // broadcast message
                output.println(formattedMessage);
            } catch (IOException e) {
                System.out.println("error: cannot send message to client");
            }

        }
    }

    private void closeConnection() {
        try {
            input.close();
            clientSocket.close();
        } catch (IOException e) {
            System.out.println("error while closing connection");
            System.exit(1);
        }
    }
}
