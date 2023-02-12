import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

class ChatServerThread extends Thread {
    private static final List<Socket> socketList = new LinkedList<>();
    private final Socket clientSocket;

    private BufferedReader inp = null;

    public ChatServerThread(Socket clientSocket) {
        this.clientSocket = clientSocket;
        socketList.add(this.clientSocket);
    }

    @Override
    public void run() {
        initInputStream();

        String line;
        try {
            while ((line = inp.readLine()) != null) {
//                System.out.println("received: " + line);
                sendMessageToClients(line);
            }
        } catch (IOException e) {
            System.out.println("error while reading input from client");
            e.printStackTrace();
        }

        closeConnection();
    }

    private void initInputStream() {
        try {
            inp = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        } catch (IOException e) {
            System.out.println("error while creating input stream");
        }
    }

    private void sendMessageToClients(String message) throws IOException {
        ListIterator<Socket> iterator = socketList.listIterator();

        while (iterator.hasNext()) {
            Socket socket = iterator.next();
            if (socket.isClosed()) {
                iterator.remove();
                continue;
            }
            PrintWriter output = new PrintWriter(socket.getOutputStream(), true);

            //format message to include sender's information
            String address = clientSocket.getLocalAddress().toString().substring(1);
            int port = clientSocket.getPort();
            String formattedMessage = String.format("<From %s:%d>: %s", address, port, message);

            output.println(formattedMessage);
        }
    }

    private void closeConnection() {
        System.out.format("Closing Connection to %s\n", clientSocket.getLocalAddress().toString());
        try {
            clientSocket.close();
        } catch (IOException e) {
            System.out.println("error while closing connection");
        }
    }
}
