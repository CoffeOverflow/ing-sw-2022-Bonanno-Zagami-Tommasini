package it.polimi.ingsw;

import java.lang.reflect.Array;

/***
 * Class that contains constants used in the whole project.
 * @author Giuseppe Bonanno, Federica Tommasini, Angelo Zagami
 */
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



    public static final String ANSI_UNDERLINE = "\u001B[4m";
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[97m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_GRAY = "\u001B[246m";
    public static final String ANSI_BACKGROUND_BLACK = "\u001B[40m";
    public static final String ANSI_BACKGROUND_PURPLE = "\u001B[45m";
    public static final String ANSI_PINK="\u001B[38;5;206m";

    // Reset
    public static final String RESET = "\u001B[0m";  // Text Reset

    // Regular Colors
    public static final String BLACK = "\u001B[30m";   // BLACK
    public static final String RED = "\u001B[31m";     // RED
    public static final String GREEN = "\u001B[32m";   // GREEN
    public static final String YELLOW = "\u001B[33m";  // YELLOW
    public static final String BLUE = "\u001B[34m";    // BLUE
    public static final String PURPLE = "\u001B[35m";  // PURPLE
    public static final String CYAN = "\u001B[36m";    // CYAN
    public static final String WHITE = "\u001B[37m";   // WHITE

    // Bold
    public static final String BLACK_BOLD = "\u001B[30m";  // BLACK
    public static final String RED_BOLD = "\u001B[31m";    // RED
    public static final String GREEN_BOLD = "\u001B[32m";  // GREEN
    public static final String YELLOW_BOLD = "\u001B[33m"; // YELLOW
    public static final String BLUE_BOLD = "\u001B[34m";   // BLUE
    public static final String PURPLE_BOLD = "\u001B[35m"; // PURPLE
    public static final String CYAN_BOLD = "\u001B[36m";   // CYAN
    public static final String WHITE_BOLD = "\u001B[37m";  // WHITE

    // Underline
    public static final String BLACK_UNDERLINED = "\u001B[30m";  // BLACK
    public static final String RED_UNDERLINED = "\u001B[31m";    // RED
    public static final String GREEN_UNDERLINED = "\u001B[32m";  // GREEN
    public static final String YELLOW_UNDERLINED = "\u001B[33m"; // YELLOW
    public static final String BLUE_UNDERLINED = "\u001B[34m";   // BLUE
    public static final String PURPLE_UNDERLINED = "\u001B[35m"; // PURPLE
    public static final String CYAN_UNDERLINED = "\u001B[36m";   // CYAN
    public static final String WHITE_UNDERLINED = "\u001B[37m";  // WHITE

    // Background
    public static final String BLACK_BACKGROUND = "\u001B[40m";  // BLACK
    public static final String RED_BACKGROUND = "\u001B[41m";    // RED
    public static final String GREEN_BACKGROUND = "\u001B[42m";  // GREEN
    public static final String YELLOW_BACKGROUND = "\u001B[43m"; // YELLOW
    public static final String BLUE_BACKGROUND = "\u001B[44m";   // BLUE
    public static final String PURPLE_BACKGROUND = "\u001B[45m"; // PURPLE
    public static final String CYAN_BACKGROUND = "\u001B[46m";   // CYAN
    public static final String WHITE_BACKGROUND = "\u001B[47m";  // WHITE

    // High Intensity
    public static final String BLACK_BRIGHT = "\u001B[90m";  // BLACK
    public static final String RED_BRIGHT = "\u001B[91m";    // RED
    public static final String GREEN_BRIGHT = "\u001B[92m";  // GREEN
    public static final String YELLOW_BRIGHT = "\u001B[93m"; // YELLOW
    public static final String BLUE_BRIGHT = "\u001B[94m";   // BLUE
    public static final String PURPLE_BRIGHT = "\u001B[95m"; // PURPLE
    public static final String CYAN_BRIGHT = "\u001B[96m";   // CYAN
    public static final String WHITE_BRIGHT = "\u001B[97m";  // WHITE

    // Bold High Intensity
    public static final String BLACK_BOLD_BRIGHT = "\u001B[90m"; // BLACK
    public static final String RED_BOLD_BRIGHT = "\u001B[91m";   // RED
    public static final String GREEN_BOLD_BRIGHT = "\u001B[92m"; // GREEN
    public static final String YELLOW_BOLD_BRIGHT = "\u001B[93m";// YELLOW
    public static final String BLUE_BOLD_BRIGHT = "\u001B[94m";  // BLUE
    public static final String PURPLE_BOLD_BRIGHT = "\u001B[95m";// PURPLE
    public static final String CYAN_BOLD_BRIGHT = "\u001B[96m";  // CYAN
    public static final String WHITE_BOLD_BRIGHT = "\u001B[97m"; // WHITE

    // High Intensity backgrounds
    public static final String BLACK_BACKGROUND_BRIGHT = "\u001B[100m";// BLACK
    public static final String RED_BACKGROUND_BRIGHT = "\u001B[101m";// RED
    public static final String GREEN_BACKGROUND_BRIGHT = "\u001B[102m";// GREEN
    public static final String YELLOW_BACKGROUND_BRIGHT = "\u001B[103m";// YELLOW
    public static final String BLUE_BACKGROUND_BRIGHT = "\u001B[104m";// BLUE
    public static final String PURPLE_BACKGROUND_BRIGHT = "\u001B[105m"; // PURPLE
    public static final String CYAN_BACKGROUND_BRIGHT = "\u001B[06m";  // CYAN
    public static final String WHITE_BACKGROUND_BRIGHT = "\u001B[107m";   // WHITE
    public static final char emptyCircle ='○';
    public static final char filledCircle='●';
    public static final char dashedCircle='◌';
    public static final char emptyRect='□';
    public static final char filledRect='■';

    public static final String[] cloud = {"   __   _    ", " _(  )_( )_  ", "(_   _", "_) ", "  (_) (__)   "};
    public static final int timeout = 14000;
    public static final int halfTimeout = 7000;

    /***
     * Set the server IP
     * @param ip The IP of the server
     */
    public static void setIP(String ip) {
        Constants.ip = ip;
    }

    /***
     * Get the server IP
     * @return The server IP
     */
    public static String getIP() {
        return ip;
    }

    /***
     * Get the server port
     * @return The server port
     */
    public static int getPort() {
        return port;
    }

    /***
     * Set the server port
     * @param port The port of the server
     */
    public static void setPort(int port) {
        Constants.port = port;
    }



}
