package it.polimi.ingsw;

import java.lang.reflect.Array;

public class Constants {
    private static String ip;
    private static int port;
    public static String ERIANTYS =
                            "███████╗██████╗ ██╗ █████╗ ███╗   ██╗████████╗██╗   ██╗███████╗\n"+
                            "██╔════╝██╔══██╗██║██╔══██╗████╗  ██║╚══██╔══╝╚██╗ ██╔╝██╔════╝\n"+
                            "█████╗  ██████╔╝██║███████║██╔██╗ ██║   ██║    ╚████╔╝ ███████╗\n"+
                            "██╔══╝  ██╔══██╗██║██╔══██║██║╚██╗██║   ██║     ╚██╔╝  ╚════██║\n"+
                            "███████╗██║  ██║██║██║  ██║██║ ╚████║   ██║      ██║   ███████║\n"+
                            "╚══════╝╚═╝  ╚═╝╚═╝╚═╝  ╚═╝╚═╝  ╚═══╝   ╚═╝      ╚═╝   ╚══════╝\n";



    public static final String ANSI_UNDERLINE = "\033[4m";
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\033[35m";
    public static final String ANSI_CYAN = "\033[36m";
    public static final String ANSI_WHITE = "\033[37m";
    public static final String ANSI_BLACK = "\033[37m";
    public static final String ANSI_GRAY = "\033[246m";
    public static final String ANSI_BACKGROUND_BLACK = "\033[40m";
    public static final String ANSI_BACKGROUND_PURPLE = "\033[45m";
    public static final String ANSI_PINK="\033[38;5;206m";
    public static final String[] cloud = {"   __   _    ", " _(  )_( )_  ", "(_   _", "_) ", "  (_) (__)   "};
    public static void setIP(String ip) {
        Constants.ip = ip;
    }

    public static String getIP() {
        return ip;
    }

    public static int getPort() {
        return port;
    }

    public static void setPort(int port) {
        Constants.port = port;
    }



}
