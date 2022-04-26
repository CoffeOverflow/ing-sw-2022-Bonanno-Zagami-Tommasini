package it.polimi.ingsw;

public class Constants {
    private static String ip;
    private static int port;
    private static String eriantys = "███████╗██████╗ ██╗ █████╗ ███╗   ██╗████████╗██╗   ██╗███████╗\n"+
                            "██╔════╝██╔══██╗██║██╔══██╗████╗  ██║╚══██╔══╝╚██╗ ██╔╝██╔════╝\n"+
                            "█████╗  ██████╔╝██║███████║██╔██╗ ██║   ██║    ╚████╔╝ ███████╗\n"+
                            "██╔══╝  ██╔══██╗██║██╔══██║██║╚██╗██║   ██║     ╚██╔╝  ╚════██║\n"+
                            "███████╗██║  ██║██║██║  ██║██║ ╚████║   ██║      ██║   ███████║\n"+
                            "╚══════╝╚═╝  ╚═╝╚═╝╚═╝  ╚═╝╚═╝  ╚═══╝   ╚═╝      ╚═╝   ╚══════╝\n";


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


    public static String getEriantys() {
        return eriantys;
    }
}
