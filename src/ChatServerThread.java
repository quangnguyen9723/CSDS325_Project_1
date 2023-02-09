import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ChatServerThread extends Thread {

    private static List<Socket> clients = new ArrayList<>();
    private Socket clientSocket = null;

    public ChatServerThread(Socket socket) {
        super("ChatServerThread");
        clients.add(socket);
        this.clientSocket = socket;
    }

    @Override
    public void run() {
        try (
//                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                DataInputStream in = new DataInputStream(new BufferedInputStream(clientSocket.getInputStream()))
        ) {
            String line = "";
            String formattedMessage;

            while (!line.equals(".")) {
//                line = in.readLine();
                line = in.readUTF();
                // format message
                String senderIP = clientSocket.getLocalAddress().toString().substring(1);
                int senderPort = clientSocket.getPort();
                formattedMessage = String.format("<From %s:%d>: %s", senderIP, senderPort, line);
                // broadcast formatted message
                broadcastMessage(formattedMessage);
            }
            // terminates connection
            System.out.println("Connection closed...");
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void broadcastMessage(String message) throws IOException {
        System.out.println(message);
        for (Socket client : clients) {
            System.out.println("sending multiple messages" + message);
            PrintWriter out = new PrintWriter(client.getOutputStream());
            out.println(message);
        }
    }
}
