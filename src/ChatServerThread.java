import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ChatServerThread extends Thread {

    private static List<PrintWriter> clients = new ArrayList<>();
    private Socket clientSocket = null;

    public ChatServerThread(Socket socket) {
        super("ChatServerThread");
        try {
            clients.add(new PrintWriter(socket.getOutputStream(), true));
        } catch (IOException e) {
            throw new RuntimeException(e);
        };
        this.clientSocket = socket;
    }

    @Override
    public void run() {
        try (
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        ) {
            String messageIn, messageOut;

            while ((messageIn = in.readLine()) != null) {
                String senderIP = clientSocket.getLocalAddress().toString().substring(1);
                int senderPort = clientSocket.getPort();
                messageOut = String.format("<From %s:%d>: %s", senderIP, senderPort, messageIn);
                broadcast(messageOut);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void broadcast(String message) {
        for (PrintWriter client : clients) {
                client.println(message);
        }
    }
}
