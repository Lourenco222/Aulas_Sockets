package ResolucaoCliente_Servidor_Produto.ClienteEServidor;

import ResolucaoCliente_Servidor_Produto.Model.Produto;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Servidor {
    public static void main(String[] args) throws IOException {

        ArrayList<Produto> produtoList = new ArrayList<>();

        try (ServerSocket serverSocket = new ServerSocket(2335)) {
            System.out.println("Servidor Disponivel! ");
            Socket socket = serverSocket.accept();
            System.out.println("Servidor Conectado! ");

            DataInputStream dis = new DataInputStream(socket.getInputStream());
            DataOutputStream dos = new DataOutputStream(socket.getOutputStream());

            while (true) {

                int opcao = dis.readInt();
                switch (opcao) {
                    case 0:
                        System.out.println("Servidor encerrado.");
                        socket.close();
                        serverSocket.close();
                        System.exit(0);
                        break;
                    case 1:
                        ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                        Produto produto = (Produto) ois.readObject();
                        produtoList.add(produto);
                        dos.writeUTF("Produto Inserido com Sucesso! ");
                        break;
                    case 2:
                        ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                        oos.writeObject(produtoList);
                        break;
                    default:
                        dos.writeUTF("Opção inválida! Tente novamente.");
                        break;
                }
            }

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
