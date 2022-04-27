package it.polimi.ingsw.Client.CLI;

import it.polimi.ingsw.Client.ClientToServer.ChooseNickname;
import it.polimi.ingsw.Constants;
import it.polimi.ingsw.Server.ServerToClient.RequestNickname;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

public class CLI {
    public static void main(String[] args) {
        System.out.println("\n"+Constants.ERIANTYS);
       /* Scanner scanner = new Scanner(System.in);
        System.out.print("\nInsert the server IP address > ");
        String ip = scanner.nextLine();
        System.out.print("Insert the server port > ");
        int port = scanner.nextInt();
        Constants.setIP(ip);
        Constants.setPort(port);*/
        Socket server = null;
        try {
            server = new Socket("127.0.0.1", 2000);
            ObjectOutputStream outputStream= new ObjectOutputStream(server.getOutputStream());
            ObjectInputStream inputStream = new ObjectInputStream(server.getInputStream());
            RequestNickname msg = (RequestNickname) inputStream.readObject();
            System.out.print(msg.getMsg()+" > ");
            Scanner scanner = new Scanner(System.in);
            String nickname = scanner.nextLine();
            outputStream.writeObject(new ChooseNickname(nickname));
            outputStream.flush();


        } catch (IOException e) {
            System.out.println("Server unreachable :c");
            System.exit(-1);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }


    }
}
