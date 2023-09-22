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
                "3. Actualizar produtos\n" +
                "4. Remover produto\n" +
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
            case 3:
                actualizarProdutos(socket, dos, dis);
                break;
            case 4:
                removerProduto(socket, dos, dis);
                break;


            default:
                System.out.println("Opção inválida");
                break;
        }

        menu(dis, dos, socket);
    }

    private static void removerProduto(Socket socket, DataOutputStream dos, DataInputStream dis) throws IOException, ClassNotFoundException {

        System.out.print("ID do Produto a ser removido: ");
        int id = sc.nextInt();
        dos.writeInt(id);
        System.out.println(dis.readUTF());

    }


    public static void actualizarProdutos(Socket socket, DataOutputStream dos, DataInputStream dis) throws IOException, ClassNotFoundException {
        ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
        ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());


        System.out.print("ID: ");
        int id = sc.nextInt();

        dos.writeInt(id);
        boolean verificador = dis.readBoolean();

        if (verificador) {
            Produto produto = (Produto) ois.readObject();
            System.out.println("O que deseja actualizar??\n 1.Nome\n 2.Quantidade\n 3.Preco");
            int escolha = sc.nextInt();
            switch (escolha) {
                case 1:
                    System.out.print("Digite Nome: ");
                    produto.setNome(sc.next());
                    oos.writeObject(produto);
                    System.out.println(dis.readUTF());
                    break;
                case 2:
                    System.out.println("Quantidade");
                    produto.setQuantidade(sc.nextInt());
                    oos.writeObject(produto);
                    System.out.println(dis.readUTF());
                    break;
                case 3:
                    System.out.println("Preco");
                    produto.setPreco(sc.nextDouble());
                    oos.writeObject(produto);
                    System.out.println(dis.readUTF());
                    break;
                default:
                    System.out.println("Opcao inexistente");
                    break;
            }

        } else {
            System.out.println("ID nao encontrado");
        }
    }

    public static void cadastrarProduto(DataInputStream dis, DataOutputStream dos, Socket socket) throws IOException {
        Produto produto = new Produto();

        System.out.print("Digite o ID: ");
        produto.setId(sc.nextInt());
        sc.nextLine();
        System.out.print("Digite o Nome: ");
        produto.setNome(sc.nextLine());

        System.out.print("Digite a quantidade: ");
        produto.setQuantidade(sc.nextInt());
        sc.nextLine();
        System.out.print("Digite o preco: ");
        produto.setPreco(sc.nextDouble());
        sc.nextLine();


        ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
        oos.writeObject(produto);
        System.out.println("Produto enviado ao Servidor! ");
        System.out.println(dis.readUTF());
    }

    public static void listarProdutos(Socket socket) throws IOException, ClassNotFoundException {
        ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
        ArrayList<Produto> produtoList = (ArrayList<Produto>) ois.readObject();

        for (Produto produto : produtoList) {
            System.out.println(produto.toString());
        }
    }
}
