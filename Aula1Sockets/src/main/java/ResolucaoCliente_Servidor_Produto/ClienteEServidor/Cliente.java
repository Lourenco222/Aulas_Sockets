package ResolucaoCliente_Servidor_Produto.ClienteEServidor;

import ResolucaoCliente_Servidor_Produto.Model.Produto;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class Cliente {

    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) throws IOException {

        try (
                Socket socket = new Socket("localhost", 2335);
                DataInputStream dis = new DataInputStream(socket.getInputStream());
                DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
        ) {

            menu(dis, dos, socket);
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Erro no cliente " + e.getMessage());
        }
    }

    public static void menu(DataInputStream dis, DataOutputStream dos, Socket socket) throws IOException, ClassNotFoundException {
        System.out.println("=============================================");
        System.out.println("                    MENU PRINCIPAL");
        System.out.println("1. Cadastrar Produto \n" +
                "2. Listar Produtos \n" +
                "0. Terminar");
        int opcao = sc.nextInt();
        dos.writeInt(opcao);
        switch (opcao) {
            case 0:
                dos.writeInt(0);
                System.exit(0);
                break;
            case 1:
                cadastrarProduto(dis, dos, socket);
                break;
            case 2:
                listarProdutos(socket);
                break;
            default:
                System.out.println("Opção inválida");
                break;
        }

        menu(dis, dos, socket);
    }


    public static void cadastrarProduto(DataInputStream dis, DataOutputStream dos, Socket socket) throws IOException {
        Produto produto = new Produto();

        System.out.print("Digite o ID: ");
        produto.setId(sc.nextInt());
        sc.nextLine();
        System.out.print("Digite o Nome: ");
        produto.setNome(sc.nextLine());

        ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
        oos.writeObject(produto);
        System.out.println("Produto enviado ao Servidor! ");
        System.out.println(dis.readUTF());
    }

    public static void listarProdutos(Socket socket) throws IOException, ClassNotFoundException {
        ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
        ArrayList <Produto>  produtoList = (ArrayList<Produto>) ois.readObject();

        for (Produto produto: produtoList) {
            System.out.println(produto.toString());
        }
    }
}
