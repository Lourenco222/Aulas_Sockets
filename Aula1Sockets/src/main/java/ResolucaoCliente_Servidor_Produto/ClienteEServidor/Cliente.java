package ResolucaoCliente_Servidor_Produto.ClienteEServidor;

import ResolucaoCliente_Servidor_Produto.Model.Produto;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class Cliente {
 static int aux =0;

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
                "3. Actualizar Produto \n " +
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
                actualizar(socket ,dos , dis);
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
        ArrayList <Produto>  produtoList = (ArrayList<Produto>) ois.readObject();

        for (Produto produto: produtoList) {
            System.out.println(produto.toString());
        }
    }
    public static void Retornar_Produto(Socket socket)  throws IOException, ClassNotFoundException{
        ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
        ArrayList <Produto>  produtoList = (ArrayList<Produto>)ois.readObject();

        System.out.print("Digite o ID do produto: ");
        int ID = sc.nextInt();
        aux=ID;
        for (int i=0; i<produtoList.size(); i++){
            if (ID == produtoList.get(i).getId()){
                System.out.println(produtoList.get(i));
            }
        }


    }

    public static void actualizar(Socket socket, DataOutputStream dos ,DataInputStream dis) throws IOException, ClassNotFoundException {
        ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
        ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());

        System.out.println("Selecione o Produto a actualizar");
        listarProdutos(socket);
        System.out.print("ID: ");
        int id = sc.nextInt();

        dos.writeInt(id);
        boolean verificador = dis.readBoolean();

        if(verificador){
             Produto produto = (Produto) ois.readObject();
            System.out.print("Digite Nome: ");
            produto.setNome(sc.next());
            oos.writeObject(produto);

            System.out.println(dis.readUTF());

        }else{
            System.out.println("ID nao encontrado");
        }

    }


}

