package Soma;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

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
                String[] values = inputLine.split(",");
                if (values.length != 2) {
                    out.println("Formato incorreto. Envie dois valores separados por vírgula.");
                    continue;
                }

                try {
                    double num1 = Double.parseDouble(values[0]);
                    double num2 = Double.parseDouble(values[1]);
                    double result = num1 + num2;
                    out.println(Double.toString(result));
                } catch (NumberFormatException e) {
                    out.println("Valores inválidos. Certifique-se de enviar números.");
                }
            }
        } catch (IOException e) {
            System.out.println("Erro no servidor: " + e.getMessage());
        }
    }
}
