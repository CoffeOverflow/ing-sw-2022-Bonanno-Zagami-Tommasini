package it.polimi.ingsw;


import java.util.Scanner;

public class Eriantys
{
    public static void main( String[] args )
    {
        Scanner scanner = new Scanner(System.in);
        System.out.println( "Welcome to Eriantys!\n" +
                "0. SERVER\n1. CLIENT (CLI INTERFACE)\n2. CLIENT (GUI INTERFACE)\n>"
        );
        int choose = scanner.nextInt();
        switch (choose)
    }
}
