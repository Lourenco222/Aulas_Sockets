package Mensagem;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) throws IOException {
        String hostName = "127.0.0.1";
        int portNumber = 12345;

        try (
                Socket socket = new Socket(hostName, portNumber);
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        ) {
            Scanner scanner = new Scanner(System.in);
            String userInput;

            while (true) {
                System.out.print("Digite uma mensagem para o servidor (ou 'sair' para encerrar): ");
                userInput = scanner.nextLine();

                if (userInput.equalsIgnoreCase("sair")) {
                    break;
                }

                out.println(userInput);

                String serverResponse = in.readLine();
                System.out.println("Resposta do servidor: " + serverResponse);
            }
        } catch (IOException e) {
            System.out.println("Erro no cliente: " + e.getMessage());
        }
    }
}