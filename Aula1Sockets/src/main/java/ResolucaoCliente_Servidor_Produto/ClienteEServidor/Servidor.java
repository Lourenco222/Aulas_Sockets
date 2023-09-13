package ResolucaoCliente_Servidor_Produto.ClienteEServidor;

import ResolucaoCliente_Servidor_Produto.Model.Produto;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Servidor {
    static   ArrayList<Produto> produtoList = new ArrayList<>();
    public static void main(String[] args) throws IOException {



        try (ServerSocket serverSocket = new ServerSocket(2335)) {
            System.out.println("Servidor Disponivel! ");
            Socket socket = serverSocket.accept();
            System.out.println("Servidor Conectado! ");

            DataInputStream dis = new DataInputStream(socket.getInputStream());
            DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());

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
                        Produto produto = (Produto) ois.readObject();
                        produtoList.add(produto);
                        dos.writeUTF("Produto Inserido com Sucesso! ");
                        break;
                    case 2:

                        oos.writeObject(produtoList);


                        break;
                    case 3:
                        Produto produtoProcurado = new Produto();
                        boolean verificador = Boolean.FALSE;
                       int id = dis.readInt();
                       for(Produto pro : produtoList) {
                           if (pro.getId() == id) {
                               produtoProcurado = pro;
                            verificador = Boolean.TRUE;
                           }
                       }
                       dos.writeBoolean(verificador);
                        if(verificador){
                          oos.writeObject(produtoProcurado);
                          Produto produtoPorActualizar = (Produto)ois.readObject();

                          produtoList.set(produtoList.indexOf(produtoProcurado),produtoPorActualizar);
                          dos.writeUTF("Produto Actualizado");
                        }
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
