import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

class ChatServerThread extends Thread {
    private static List<Socket> socketList = new ArrayList<>();
    private Socket socket;

    //TODO
//    private DataInputStream inp = null;
    private BufferedReader inp = null;

    public ChatServerThread(Socket clientSocket) {
        this.socket = clientSocket;
        socketList.add(socket);
    }

    @Override
    public void run() {
        initInputStream();

        String line = "";
        while (line != null && !line.equals(".")) {
            try {
                //TODO
//                line = inp.readUTF();
                line = inp.readLine();

                sendMessageToClients(line);
            } catch (IOException e) {
                System.out.println("error while reading input from client");
                e.printStackTrace();
                break;
            }
        }

        closeConnection();
    }

    private void initInputStream() {
        try {
            //TODO
//            inp = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
            inp = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            System.out.println("error while creating input stream");
        }
    }

    private void sendMessageToClients(String message) throws IOException {
        for (Socket other : socketList) {
            //TODO
//            DataOutputStream output = new DataOutputStream(other.getOutputStream());
            PrintWriter output = new PrintWriter(other.getOutputStream(), true);

            //format message to include sender's information
            String address = socket.getLocalAddress().toString().substring(1);
            int port = socket.getPort();
            String formattedMessage = String.format("<From %s:%d>: %s", address, port, message);

            //TODO
//            output.writeUTF(formattedMessage);
            output.println(formattedMessage);
        }
    }

    private void closeConnection() {
        System.out.format("Closing Connection to %s\n", socket.getLocalAddress().toString());
        try {
            socket.close();
        } catch (IOException e) {
            System.out.println("error while closing connection");
        }
    }
}
