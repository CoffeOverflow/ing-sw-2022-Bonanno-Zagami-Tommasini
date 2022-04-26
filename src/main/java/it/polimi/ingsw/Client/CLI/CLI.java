package it.polimi.ingsw.Client.CLI;

import it.polimi.ingsw.Constants;

import java.util.Scanner;

public class CLI {
    public static void main(String[] args) {
        System.out.println(Constants.getEriantys());
        Scanner scanner = new Scanner(System.in);
        System.out.print("\nInsert the server IP address > ");
        String ip = scanner.nextLine();
        System.out.print("Insert the server port > ");
        int port = scanner.nextInt();
        Constants.setIP(ip);
        Constants.setPort(port);
    }
}
