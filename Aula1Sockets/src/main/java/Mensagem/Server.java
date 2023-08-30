package Mensagem;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Server {
    public static void main(String[] args) throws IOException {
        int portNumber = 12345;

        try (ServerSocket serverSocket = new ServerSocket(portNumber)) {
            System.out.println("Aguardando conexão do cliente...");
            Socket clientSocket = serverSocket.accept();
            System.out.println("Conexão estabelecida com: " + clientSocket.getInetAddress());

            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                System.out.println("Mensagem recebida do cliente: " + inputLine);

                Scanner sc = new Scanner(System.in);
                System.out.print("Escreva uma resposta: ");
                String response = sc.nextLine();
                out.println(response);
            }
        } catch (IOException e) {
            System.out.println("Erro no servidor: " + e.getMessage());
        }
    }

    private static String processMessage(String message) {
        return message.toUpperCase();
    }
}
