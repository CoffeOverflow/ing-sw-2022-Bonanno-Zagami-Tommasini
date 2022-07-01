package it.polimi.ingsw.Client.CLI;

import it.polimi.ingsw.Client.ClientToServer.*;
import it.polimi.ingsw.Client.ServerHandler;
import it.polimi.ingsw.Client.View;
import it.polimi.ingsw.Client.VirtualModel;
import it.polimi.ingsw.Constants;
import it.polimi.ingsw.Controller.State.MoveTo;
import it.polimi.ingsw.Model.*;
import it.polimi.ingsw.Server.ServerToClient.*;
import it.polimi.ingsw.Server.ServerToClient.ServerHeartbeat;

import java.io.IOException;
import java.util.*;

import static it.polimi.ingsw.Constants.*;

/***
 * CLI implementation of UI
 * @author Giuseppe Bonanno, Federica Tommasini, Angelo Zagami
 */
public class CLI implements View, Runnable {

    private ServerHandler serverHandler;
    private VirtualModel vmodel;

    /***
     * CLI constructor
     * @author Federica Tommasini, Angelo Zagami
     * @param serverHandler Server Handler object used for server communications
     */
    public CLI(ServerHandler serverHandler){
        this.serverHandler = serverHandler;
        this.vmodel=new VirtualModel();
    }

    @Override
    public VirtualModel getVmodel() {
        return vmodel;
    }

    /***
     * Main class of CLI, ask for server IP and port then start the CLI
     * @author Giuseppe Bonanno, Angelo Zagami
     * @param args
     */
    public static void main(String[] args) {
        System.out.println("\n"+Constants.ERIANTYS);
        Scanner scanner = new Scanner(System.in);
        System.out.print("\nInsert the server IP address > ");
        String ip = scanner.nextLine();
        int port = 0;
        do {
            System.out.print("Insert the server port > ");
            try{
                port = scanner.nextInt();
                if(port < 1024){
                    System.out.print(ANSI_RED+"Please insert a value > 1024!\n"+ANSI_RESET);
                }
            }
            catch (InputMismatchException e){
                scanner.nextLine();
                System.out.print(ANSI_RED+"Please insert a number!\n"+ANSI_RESET);
            }

        }while(port<1024);
        Constants.setIP(ip);
        Constants.setPort(port);
        ServerHandler server = null;
        try{
            server = new ServerHandler();
        }catch (RuntimeException e){
            System.out.println(ANSI_RED + "Server at " + ANSI_YELLOW + Constants.getIP() + ":" + Constants.getPort() + ANSI_RED + " is offline or the insert IP and port are not valid.\nThe app will now close!" + ANSI_RESET);
            System.exit(-1);
        }
        Thread cliThread = new Thread(new CLI(server));
        cliThread.start();

    }

    @Override
    public void requestNickname() throws IOException {
        System.out.print("Choose a nickname > ");
        Scanner scanner = new Scanner(System.in);
        String nickname = scanner.nextLine();
        serverHandler.send(new ChooseNickname(nickname));
    }

    @Override
    public void showError(String error) {
        System.out.println(ANSI_RED + error +  ANSI_RESET);
    }

    @Override
    public void actionValid(String message) {
        System.out.println(ANSI_GREEN + message +  ANSI_RESET);
    }

    @Override
    public void chooseMatch(String games) throws IOException {
        System.out.println("\nAvailable match:");
        System.out.println(games);
        boolean confirmation = false;
        do{
            System.out.print("Select a match to join or type 0 for create new match or -1 to refresh> ");
            Scanner scanner = new Scanner(System.in);
            try{
                int game = scanner.nextInt();
                serverHandler.send(new SelectMatch(game));
                confirmation = true;
            }catch (InputMismatchException e){
                showError("Please insert a number!");
            }
        }while(!confirmation);


    }

    @Override
    public void showMessage(String message) {
        System.out.print(message);
    }

    @Override
    public void requestSetup() throws IOException {
        Scanner scanner = new Scanner(System.in);
        int num = 0;
        String expert = "";
        do {
            showMessage("Select the number of player (2 or 3) > ");
            scanner = new Scanner(System.in);
            try{
                num = scanner.nextInt();
                if(num < 2 || num > 3){
                    showError("Please insert 2 or 3!");
                }
            }
            catch (InputMismatchException e){
                showError("Please insert a number!");
            }

        }while(num < 2 || num>3);
        do {
            showMessage("Expert mode? [y/n] > ");
            expert =  scanner.next();
            if(!expert.equalsIgnoreCase("y") && !expert.equalsIgnoreCase("n")){
                showError("Please insert y or n!");
            }
        }while(!expert.equalsIgnoreCase("y") && !expert.equalsIgnoreCase("n"));
        serverHandler.send(new SelectModeAndPlayers(num, expert.equalsIgnoreCase("y")));
    }

    @Override
    public void matchCreated(MatchCreated msg) {
        this.vmodel.setIslandsAndMotherNature(msg);
    }

    @Override
    public void playersInfo(PlayersInfo msg){
        this.vmodel.setPlayersInfo(msg);
    }

    @Override
    public void setUpCharacterCard(SetUpCharacterCard msg){
        this.vmodel.setCharacterCards(msg);
    }

    @Override
    public void setUpSchoolStudent(SetUpSchoolStudent msg){
        this.vmodel.setSchoolStudents(msg);
    }

    @Override
    public void isTurnOfPlayer(String msg){
        this.showMessage(msg);
        vmodel.setTakeProfessorWhenTie(false);
    }

    @Override
    public void youWin() {
        endGame = true;
        this.showMessage("YOU WIN!");
    }

    @Override
    public void otherPlayerWins(OtherPlayerWins msg){
        endGame = true;
        this.showMessage(msg.getMsg());
    }

    @Override
    public void selectAssistantCard(SelectAssistantCard msg){
        int value=0;
        do{
            this.showMessage(SelectAssistantCard.getMsg());
            int steps=0;
            /*
             * show the names of the assistant cards, the values and the steps
             */
            for(String s: msg.getAvailableCards()){
                for(int i=0; i<vmodel.getClientPlayer().getAssistantCards().size();i++) {
                    if (vmodel.getClientPlayer().getAssistantCards().get(i).getName().equalsIgnoreCase(s)) {
                        value = vmodel.getClientPlayer().getAssistantCards().get(i).getValue();
                        steps = vmodel.getClientPlayer().getAssistantCards().get(i).getMothernaturesteps();
                        break;
                    }
                }
                this.showMessage("Card: "+ s + " value: " + value + " steps: " + steps+ "\n" );
            }
            /*
             * let the player insert the name of the card that he wants to play
             */
            System.out.print("> ");
            Scanner scanner = new Scanner(System.in);
            String card = scanner.next();
            value = -1;
            for(int i=0; i<vmodel.getClientPlayer().getAssistantCards().size();i++) {
                if (vmodel.getClientPlayer().getAssistantCards().get(i).getName().equalsIgnoreCase(card)) {
                    for(String s: msg.getAvailableCards()){
                        if(card.equalsIgnoreCase(s)){
                            value = vmodel.getClientPlayer().getAssistantCards().get(i).getValue();
                            break;
                        }
                    }
                }
            }
            if(value == -1)
                showError("Please insert a valid card!");
        }while(value == -1);
        serverHandler.send(new PlayAssistantCard(value));
        

    }

    @Override
    public void update(UpdateMessage msg){
        vmodel.update(msg);
        this.showBoard();
    }

    @Override
    public void chooseWizard(SelectWizard message) throws IOException {
        boolean confirmation = false;
        do{
            showMessage(message.getMsg());
            for(Wizards wizard: message.getAvailableWizards()){
                showMessage("> "+wizard.getName()+"\n");
            }
            System.out.print("> ");
            Scanner scanner = new Scanner(System.in);
            String wizard = scanner.nextLine();
            wizard = wizard.replaceAll(" ", "").toUpperCase();
            try {
                serverHandler.send(new ChooseWizard(Wizards.valueOf(Wizards.class,wizard)));
                confirmation = true;
            }
            catch (IllegalArgumentException e){
                showError("Please insert a valid Wizard!");
            }
        }while(!confirmation);



    }

    /**
     * Show the board on cli
     */
    @Override
    public void showBoard(){
        System.out.println("");
        System.out.println("___________ISLANDS___________");
        showIsland();
        System.out.println("");
        System.out.println("___________CLOUDS___________");
        showClouds();
        if(vmodel.getCharacterCards()!=null){
            System.out.println("");
            System.out.println("___________CHARACTER CARDS___________");
            System.out.println("");
            showCharacterCard();
        }
        System.out.println("");
        System.out.println("___________BOARDS___________");
        System.out.println("");

        for(Player p:this.vmodel.getPlayers())
        {
            Tower tower=p.getTower();
            String colorTower;
            if(tower.equals(Tower.WHITE))
                colorTower=ANSI_WHITE;
            else if(tower.equals(Tower.BLACK))
                colorTower=ANSI_BLACK;
            else
                colorTower=ANSI_PURPLE;
            showSchool(p,colorTower);
        }

    }

    /**
     * Show the character cards with their costs and if there are any students on the card or no entry cards
     */

    public void showCharacterCard(){
        List<CharacterCard> characterCards=this.vmodel.getCharacterCards();
        if(!characterCards.equals(null)) {
            String[] characterCardsName;
            for (int i = 0; i < 3; i++) {
                characterCardsName = characterCards.get(i).getAsset().split("\\.");
                System.out.print("Card: " + characterCardsName[0]);
                System.out.print(" cost: " + characterCards.get(i).getCost());
                int k = 0;
                int numStudentsColor = 0;
                String[] ansiColor = { ANSI_PINK, ANSI_RED, ANSI_YELLOW, ANSI_BLUE, ANSI_GREEN };
                List<String> characterStudentName = new ArrayList<>();
                characterStudentName.add("innkeeper.jpg");
                characterStudentName.add("clown.jpg");
                characterStudentName.add("princess.jpg");
                if (characterStudentName.contains(characterCards.get(i).getAsset())) {
                    System.out.print(" students:");
                    for (Color c : Color.values()) {
                        if (characterCards.get(i).getStudents().get().get(c) != 0) {
                            numStudentsColor = characterCards.get(i).getStudents().get().get(c);
                            while (numStudentsColor > 0) {
                                System.out.print(ansiColor[k] + filledCircle + ANSI_RESET);
                                numStudentsColor--;
                            }
                        }
                        k++;
                    }
                }
                if (characterCards.get(i).getAsset().equals("herbalist.jpg")) {
                    System.out.print(" no entry titles: " + characterCards.get(i).getNoEntryTiles().get());
                }
                System.out.println("");
            }
        }
    }

    /**
     * shows the islands with the students, the no entry cards and the towers on them.
     * The filled colored circles represent the students
     * The towers are represented by colored squares (white, black or purple). We had to replace the gray of the towers with purple.
     */
    public void showIsland(){

        EnumMap<Color,Integer> students;
        StringBuilder studentOnIsland=new StringBuilder();
        int numberOfTower=0;
        Optional<Tower> tower;
        int num=0;
        for(int i=0;i<this.vmodel.getIslands().size();i++)
        {
            students=this.vmodel.getIslands().get(i).getStudents();
            tower=this.vmodel.getIslands().get(i).getTower();
            for(Color c: Color.values())
            {
                num=students.get(c);
                for(int j=0;j<num;j++){
                    switch(c){
                        case BLUE:
                            studentOnIsland.append(ANSI_BLUE+filledCircle+ANSI_RESET);
                            break;
                        case PINK:
                            studentOnIsland.append(ANSI_PINK+filledCircle+ANSI_RESET);
                            break;
                        case GREEN:
                            studentOnIsland.append(ANSI_GREEN+filledCircle+ANSI_RESET);
                            break;
                        case RED:
                            studentOnIsland.append(ANSI_RED+filledCircle+ANSI_RESET);
                            break;
                        case YELLOW:
                            studentOnIsland.append(ANSI_YELLOW+filledCircle+ANSI_RESET);
                            break;
                    }
                }

            }
            if(tower.isPresent()) {
                numberOfTower=this.vmodel.getIslands().get(i).getNumberOfTowers();
                for(int k=0;k<numberOfTower;k++)
                {
                    switch (tower.get()){
                        case BLACK:
                            studentOnIsland.append(ANSI_BLACK);
                            break;
                        case GRAY:
                            studentOnIsland.append(ANSI_PURPLE);
                            break;
                        case WHITE:
                            studentOnIsland.append(ANSI_WHITE);
                            break;

                    }
                    studentOnIsland.append(" "+CYAN_BACKGROUND+filledRect+ANSI_RESET);
                 }
            }

            int entryCardPerIsland=this.vmodel.getIslands().get(i).getNoEntryCard();
            for(int m=0;m<entryCardPerIsland;m++)
                studentOnIsland.append(ANSI_RED+dashedCircle+ANSI_RESET);

            if(i==this.vmodel.getMotherNaturePosition())
            {
                studentOnIsland.append(" "+ANSI_YELLOW+filledRect+ANSI_RESET);
            }

            if(i>=9)
                this.showMessage("Island "+(i+1)+": "+studentOnIsland +'\n');
            else
                this.showMessage("Island "+(i+1)+":  "+studentOnIsland+'\n');


            studentOnIsland.setLength(0);
        }
    }

    /**
     * Show the schools of each player with the relative nickname.
     * @param p
     * @param colorTower
     */
    public void showSchool(Player p,String colorTower){

        char[][] boardElement = new char[5][14];

        EnumMap<Color,Integer> students=new EnumMap<Color, Integer>(Color.class);
        EnumMap<Color,Integer> entryStudents=new EnumMap<Color, Integer>(Color.class);
        entryStudents=p.getEntryStudents();
        students=p.getStudents();
        int numColorStudents[]=new int[5];
        int numColorEntryStudents[]=new int[5];
        boolean[] professor={p.isPresentProfessor(Color.GREEN),p.isPresentProfessor(Color.RED),p.isPresentProfessor(Color.YELLOW),p.isPresentProfessor(Color.PINK),p.isPresentProfessor(Color.BLUE)};
        int numberOfTower=p.getNumberOfTower();

        /**
         * initializes the array with the number of students on the school for each color
         */
        numColorStudents[0]=students.get(Color.GREEN);
        numColorStudents[1]=students.get(Color.RED);
        numColorStudents[2]=students.get(Color.YELLOW);
        numColorStudents[3]=students.get(Color.PINK);
        numColorStudents[4]=students.get(Color.BLUE);

        /**
         * initializes the array with the number of entrance students for each color
         */
        numColorEntryStudents[0]=entryStudents.get(Color.GREEN);
        numColorEntryStudents[1]=entryStudents.get(Color.RED);
        numColorEntryStudents[2]=entryStudents.get(Color.YELLOW);
        numColorEntryStudents[3]=entryStudents.get(Color.PINK);
        numColorEntryStudents[4]=entryStudents.get(Color.BLUE);

        /**
         * Show coins and nickname
         */

        this.showMessage(p.getNickname()+"'s board "); this.showMessage(" Coins: "+p.getMoney()+"\n");
        StringBuilder color=new StringBuilder();
        int num=0;
        for(int i=0;i<5;i++)
            num+=numColorEntryStudents[i];
        /**
         * Use the ansiColor array to correctly print students with their colors
         */
        String[] ansiColor={ANSI_GREEN,ANSI_RED,ANSI_YELLOW,ANSI_PINK,ANSI_BLUE};
        /**
         * i=rows
         * j=columns
         */
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 14; j++) {
                /**
                 * elements on [0][0] is a dashed circle
                 */
                if (i == 0 && j == 0) boardElement[i][j] = dashedCircle;
                else if (j == 12 || j == 13) boardElement[i][j] = emptyRect;
                else {
                    if(j>1 && j<12)
                        if (numColorStudents[i] > 0) {
                            boardElement[i][j] = filledCircle;
                            numColorStudents[i]--;
                        }
                        else boardElement[i][j] = emptyCircle;
                    else
                        boardElement[i][j]=emptyCircle;

                }
            }

        }
        int k=0;
        for(int i=0;i<5;i++) {
            for (int j = 0; j < 14; j++) {
                if(j>1 && j<12) { //columns between 2 and 11 are the students on the school
                    if(j!=11)
                    {
                        color.append(ansiColor[i]);
                        color.append(boardElement[i][j]);
                    }
                    else
                    {
                        /**
                         * If j==11 print the professor
                         */
                        if(professor[i])
                            boardElement[i][j]=filledCircle;
                        else
                            boardElement[i][j]=emptyCircle;
                        color.append(ansiColor[i]);
                        color.append(boardElement[i][j]);
                    }
                }
                else if(j<=1){
                    /**
                     * For j<=1 print the entrance student
                     */
                    if(i==0 && j==0)boardElement[i][j]=dashedCircle;
                    else if(num>0){
                        while (numColorEntryStudents[k]==0)
                                k++;
                        color.append(ansiColor[k]);
                        numColorEntryStudents[k]--;
                        boardElement[i][j]=filledCircle;
                        num--;

                    }else boardElement[i][j]=emptyCircle;

                    color.append(boardElement[i][j]);
                    if(num==0)color.append(ANSI_RESET);
                }
                else{
                    /**
                     * If j>=12 print the tower of the player on the board
                     * Filled rect if it is present on the board, otherwise print an empty rect
                     */
                        if (numberOfTower > 0) {
                            boardElement[i][j] = filledRect;
                            numberOfTower--;
                        } else boardElement[i][j] = emptyRect;
                        color.append(WHITE_BACKGROUND + colorTower);
                        color.append(boardElement[i][j]);
                        if(j!=13)
                        color.append(" ");
                }
                if (j == 1 || j == 10 || j == 11 || j == 13) color.append(ANSI_RESET + "|"); //separator |
            }
            color.append("\n");
        }
        this.showMessage(color.toString());
        color.setLength(0);
        this.showMessage("________________\n");

    }

    /***
     * This method shows the clouds on the CLI
     */
    public void showClouds() {
        List<Cloud> clouds = this.vmodel.getClouds();
        EnumMap<Color, Integer> students;
        StringBuilder studentOnClouds = new StringBuilder();
        StringBuilder cloudsStrings = new StringBuilder();
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < clouds.size(); j++) {
                //cloudsStrings.append(cloud[i]);
                if (i == 2) {
                    //cloudsStrings.append(cloud[i]);
                    students = clouds.get(j).getStudents();
                    int numberOfStudentsOnCloud = 0;
                    for (Color c : Color.values()) {
                        for (int y = 0; y < students.get(c); y++) {
                            switch (c) {
                                case BLUE:
                                    studentOnClouds.append(ANSI_BLUE + filledCircle + ANSI_RESET);
                                    break;
                                case PINK:
                                    studentOnClouds.append(ANSI_PINK + filledCircle + ANSI_RESET);
                                    break;
                                case GREEN:
                                    studentOnClouds.append(ANSI_GREEN + filledCircle + ANSI_RESET);
                                    break;
                                case RED:
                                    studentOnClouds.append(ANSI_RED + filledCircle + ANSI_RESET);
                                    break;
                                case YELLOW:
                                    studentOnClouds.append(ANSI_YELLOW + filledCircle + ANSI_RESET);
                                    break;
                            }
                            numberOfStudentsOnCloud++;
                        }
                    }
                    if(numberOfStudentsOnCloud != clouds.size()+1){
                        for (int k = 0; k < clouds.size()+1; k++){
                            studentOnClouds.append(" ");
                        }
                    }
                    if(clouds.size() == 2)
                        studentOnClouds.append(" ");
                    cloudsStrings.append(cloud[i]+studentOnClouds+cloud[i+1]);
                    studentOnClouds.setLength(0);

                }
                else{
                    cloudsStrings.append(cloud[i]);
                }
            }
            cloudsStrings.append("\n");
            if(i == 2)
                i++;
        }
        this.showMessage("\n"+cloudsStrings+"\n\n");
        cloudsStrings.setLength(0);
    }

    @Override
    public void chooseOption(ChooseOption message){
        int characterOrMove=0;
        Scanner scanner=new Scanner(System.in);
        /*
         * make the player choose the number of the cloud that he wants to take the students from
         * at the end of his turn
         */
        if(message.getType()==OptionType.CHOOSECLOUD){

            boolean checkIfNonEmptyCloud=false;
            do{
                try {
                    showMessage(message.getMsg());
                    System.out.print("> ");
                    characterOrMove = scanner.nextInt();
                    if(characterOrMove>0 && characterOrMove<=vmodel.getClouds().size()){
                        for(Color c :vmodel.getClouds().get(characterOrMove-1).getStudents().keySet()){
                            if(vmodel.getClouds().get(characterOrMove-1).getStudents().get(c)>0)
                                checkIfNonEmptyCloud=true;
                        }
                    }
                    if(!checkIfNonEmptyCloud)
                    {
                        showError("choose a valid number for the cloud: ");
                        showMessage("> ");
                    }

                }
                catch (InputMismatchException e){
                    showError("Please insert an integer value");
                }
                scanner.nextLine();
            }while(!checkIfNonEmptyCloud);
            serverHandler.send(new ChooseCloud(characterOrMove-1));
            this.vmodel.setUseCharacterCard(false);
        } else{

            do {
                /*
                 * if the expert mode is set, depending on the phase of the game, make the player choose between
                 * playing a character card or moving the students and moving mother nature
                 * the message contains the valid first option in the specific phase, either "Move students" or
                 * "Move mother nature"
                 */
                if(message.isExpertMode() && this.vmodel.isUseCharacterCard()==false){
                    String msg= message.getMsg();
                    showMessage("Choose an option: \n 1."+msg+" 2.Play a character card \n" );
                    System.out.print("> ");
                    try {

                        characterOrMove = scanner.nextInt();
                    }
                    catch (InputMismatchException e){
                        this.showError("Please insert an integer value");
                    }
                }else characterOrMove=1;
                scanner.nextLine();
            }while(characterOrMove!=1 && characterOrMove!=2);

            switch (characterOrMove) {
                case 1:
                    /*
                     * the player selects the first option
                     */
                    if (message.getType()==OptionType.MOVESTUDENTS) {
                        /*
                         * the first option, according to the game phase, allows the player to move a student at a time
                         */
                            int islandOrSchool = 0;
                            String col=null;
                            Color color=null;
                            boolean boolWhile;
                            boolean breakDoWhile=false;
                            do {
                                /*
                                 * choose whether to move the students to an island or to the dining hall of the school
                                 * if the number is not a valid choice (1 or 2) or it is not an integer value, the
                                 * player has to provide an input again
                                 */
                                try {
                                    System.out.print("Choose where to move the student: \n 1.school\n 2.island \n> ");
                                    islandOrSchool = scanner.nextInt();
                                }catch (InputMismatchException e){
                                    this.showError("Please insert an integer value");
                                }
                                scanner.nextLine();
                            }while (islandOrSchool != 1 && islandOrSchool != 2);

                            do{
                                do{
                                    /*
                                     * choose the color of the student to move, if the string is not a valid color for
                                     * a student, or if such student is not present in the player's school entrance,
                                     * the player has to provide an input again
                                     */
                                    System.out.print("Choose the color of the student: \n> ");
                                    col=scanner.next();
                                    try{
                                        Color color2=Color.valueOf(col.toUpperCase());
                                        boolWhile=Arrays.asList(Color.values()).contains(color2);
                                    }catch(Exception e){
                                        boolWhile=false;
                                        this.showError("Choose a valid color");
                                    }
                                }while (!boolWhile);
                                color=Color.valueOf(col.toUpperCase());
                                if(!vmodel.getClientPlayer().getEntryStudents().containsKey(color))
                                    this.showError("Choose a color that is present in your entrance");
                            }while(!vmodel.getClientPlayer().getEntryStudents().containsKey(color) ||
                                    vmodel.getClientPlayer().getEntryStudents().get(color)==0);


                            if (islandOrSchool == 1) {
                                /*
                                 * move the student to the school dining hall
                                 */
                                serverHandler.send(new MoveStudent(MoveTo.SCHOOL,color,this.vmodel.getNumOfInstance()));
                            } else if (islandOrSchool == 2) {
                                /*
                                 * choose the number of the island to which move the student, if the value is not valid
                                 * the player has to provide an inout again
                                 */
                                int islandPosition=-1;
                                do{
                                    System.out.print("Choose the number of the island: \n> ");
                                    try{
                                        islandPosition= scanner.nextInt();
                                    }catch (InputMismatchException e){
                                        showError("Please insert an integer value");
                                    }
                                    scanner.nextLine();
                                }while(islandPosition<=0 || islandPosition>vmodel.getIslands().size());
                                serverHandler.send(new MoveStudent(MoveTo.ISLAND,Color.valueOf(col.toUpperCase()),islandPosition-1,this.vmodel.getNumOfInstance()));
                            }
                    } else if (message.getType()==OptionType.MOVENATURE) {
                        /*
                         * the first option, according to the game phase, allows the player to move mother nature,
                         * indicating the steps, if he doesn't provide an integer value, he has to provide an input
                         * again
                         */
                        this.vmodel.resetNumOfInstance();
                        int steps=-1;
                        boolean mothernatureInput=false;
                        do{
                            System.out.println("Choose the steps of mother nature");
                            try {
                                steps = scanner.nextInt();
                                mothernatureInput=false;
                            }catch (InputMismatchException e){
                                mothernatureInput=true;
                                scanner.nextLine();
                                showError("Please insert an integer value");
                            }
                        }while (mothernatureInput);
                        serverHandler.send(new MoveMotherNature(steps));
                    }
                    break;
                case 2:
                    /*
                     * the player chooses the second option, i.e. to use a character card, he has to indicate the name
                     * of the card he wants to play, if he insert a string which does not correspond to a card, he
                     * has to provide an input again
                     */
                    List<CharacterCard> characterCards=this.vmodel.getCharacterCards();
                    String[] characterCardsName;
                    List<String> charcaterName=new ArrayList<>();
                    String card;
                    boolean choose;
                    this.vmodel.setUseCharacterCard(true);

                    for(int i=0;i<3;i++) {
                        characterCardsName=characterCards.get(i).getAsset().split("\\.");
                        System.out.println("Card: "+characterCardsName[0]);
                        charcaterName.add(characterCardsName[0]);
                    }
                    do{
                        System.out.println("Choose a card ");
                        System.out.print("> ");
                        card= scanner.next();
                        if(!charcaterName.contains(card)){
                            choose=false;
                            showError("Choose a valid card");
                        }
                        else
                            choose=true;
                    } while (!choose);
                    sendCard(card.toLowerCase()+".jpg");
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * To use character cards and set the value to use them
     * @param card asset of the card
     */
    public void sendCard(String card){
        String asset=card;
        Integer posIsland=null;
        EnumMap<Color,Integer> choosenStudent=null;
        EnumMap<Color,Integer> entranceStudent=null;
        Color color=null;
        boolean boolWhile;
        Color color2=null;
        int numStudent=0;
        Scanner scanner = new Scanner(System.in);

        List<CharacterCard> characterCards=this.vmodel.getCharacterCards();
        for(CharacterCard c:characterCards){
            if(c.getAsset().equals(card)){
                switch (card){
                    /**
                     * Innkeper: Choose a student's color from the card and the island to place it on
                     */
                    case "innkeeper.jpg":
                        do{
                            this.showMessage("Scegli la posizione dell'isola \n>");
                            try{
                                posIsland=scanner.nextInt()-1;
                                if(posIsland<0 || posIsland>this.vmodel.getIslands().size()-1)
                                    this.showMessage(ANSI_RED+" Invalid input\n" +ANSI_RESET);
                            }catch(Exception e){
                                this.showMessage("Insert a valid value\n");
                                this.showMessage(">");
                            }
                        }while (posIsland<0 || posIsland>this.vmodel.getIslands().size()-1);
                        choosenStudent=new EnumMap<Color, Integer>(Color.class);
                        for(Color c1:color.values())
                            choosenStudent.put(c1,0);
                       do{
                           this.showMessage("Choose a student's color from the card\n>");
                           String colorStudent=scanner.next();
                           try{
                               //Check if the color is present on the card
                               color2=Color.valueOf(colorStudent.toUpperCase());
                               boolWhile=Arrays.asList(Color.values()).contains(color2);
                               if(!c.getStudents().get().containsKey(color2) || c.getStudents().get().get(color2)<=0)
                               {
                                   boolWhile=false;
                                   System.out.print(ANSI_RED+"Choose a color that is present on the card\n"+ANSI_RESET);
                                   this.showMessage(">");
                               }
                           }catch(Exception e){
                               boolWhile=false;
                               System.out.print(ANSI_RED+"Choose a valid color\n"+ANSI_RESET);
                               this.showMessage(">");
                           }
                       }while (!boolWhile);
                        for(Color c1:color.values())
                            choosenStudent.put(c1,0);
                        choosenStudent=new EnumMap<Color, Integer>(Color.class);
                        choosenStudent.put(color2,1);
                        break;
                    case "thief.jpg":
                        do {
                            /**
                             * Choose a color and all players will have to remove 3 students (or less if not present) from the entrance
                             */
                            this.showMessage("Choose the color of students to put on the bag \n>");
                            String colorStudent = scanner.next();
                            try {
                                color2 = Color.valueOf(colorStudent.toUpperCase());
                                boolWhile = Arrays.asList(Color.values()).contains(color2);
                            } catch (Exception e) {
                                boolWhile = false;
                                System.out.print(ANSI_RED + "Choose a valid color\n" + ANSI_RESET);
                                this.showMessage(">");
                            }
                        } while (!boolWhile);
                        color=color2;
                        break;
                    case "clown.jpg":
                        do{
                            /**
                             * change up to 3 students between those on the card and those in your entrance
                             */
                            this.showMessage("how many students do you want to change?");
                            try{
                                numStudent= scanner.nextInt();
                            }catch (Exception e){
                                this.showMessage("Insert a valid value\n");
                                this.showMessage(">");
                            }
                            if(numStudent<=0 || numStudent>3) {
                                this.showMessage("Choose a number from 1 to 3");
                                this.showMessage(">");
                            }
                        }while(numStudent<=0 || numStudent>3);

                        choosenStudent=new EnumMap<Color, Integer>(Color.class);
                        for(Color c1:Color.values())
                            choosenStudent.put(c1,0);
                        for(int i=0;i<numStudent;i++) {
                            do {
                                this.showMessage("Choose a student from the card\n>");
                                String colorStudent = scanner.next();
                                try {
                                    /**
                                     * Check if the color is present on the card
                                     */
                                    color2 = Color.valueOf(colorStudent.toUpperCase());
                                    boolWhile = Arrays.asList(Color.values()).contains(color2);
                                    if (!c.getStudents().get().containsKey(color2) || c.getStudents().get().get(color2) <= 0) {
                                        boolWhile = false;
                                        System.out.print(ANSI_RED + "Choose a color that is present on the card\n"
                                                + ANSI_RESET);
                                        this.showMessage(">");
                                    }
                                } catch (Exception e) {
                                    boolWhile = false;
                                    System.out.print(ANSI_RED + "Choose a valid color\n" + ANSI_RESET);
                                    this.showMessage(">");
                                }
                            } while (!boolWhile);
                            choosenStudent.put(color2,choosenStudent.get(color2)+1);
                        }
                        entranceStudent=new EnumMap<Color, Integer>(Color.class);
                        for(Color c2: Color.values())
                            entranceStudent.put(c2,0);
                        for(int i=0;i<numStudent;i++) {
                            do {
                                this.showMessage("Choose a student from your entrance \n>");
                                String colorStudent = scanner.next();
                                try {
                                    color2 = Color.valueOf(colorStudent.toUpperCase());
                                    boolWhile = Arrays.asList(Color.values()).contains(color2);
                                    if(!this.vmodel.getClientPlayer().getEntryStudents().containsKey(color2) || this.vmodel.getClientPlayer().getEntryStudents().get(color2)<=0 ){
                                        boolWhile = false;
                                        System.out.print(ANSI_RED + "Choose a color that is present at the entrance\n"
                                                + ANSI_RESET);
                                        this.showMessage(">");
                                    }
                                } catch (Exception e) {
                                    boolWhile = false;
                                    System.out.print(ANSI_RED + "Choose a valid color\n" + ANSI_RESET);
                                    this.showMessage(">");
                                }
                            } while (!boolWhile);
                            entranceStudent.put(color2,entranceStudent.get(color2)+1);
                        }
                        break;

                    case "princess.jpg":
                        do {
                            /**
                             * Take a student and place him in your school
                             */
                            this.showMessage("Choose a color of the student from the card\n>");
                            String colorStudent = scanner.next();
                            try {
                                color2 = Color.valueOf(colorStudent.toUpperCase());
                                boolWhile = Arrays.asList(Color.values()).contains(color2);
                                if (!c.getStudents().get().containsKey(color2) || c.getStudents().get().get(color2) <= 0) {
                                    boolWhile = false;
                                    System.out.print(ANSI_RED + "Choose a color that is present on the card\n"
                                            + ANSI_RESET);
                                    this.showMessage(">");
                                }
                            } catch (Exception e) {
                                boolWhile = false;
                                System.out.print(ANSI_RED + "Choose a valid color\n" + ANSI_RESET);
                                this.showMessage(">");
                            }
                        } while (!boolWhile);
                        choosenStudent=new EnumMap<Color, Integer>(Color.class);
                        for(Color c1:Color.values())
                            choosenStudent.put(c1,0);
                        choosenStudent.put(color2,1);
                        break;
                    case "storyteller.jpg":
                        do{
                            /**
                             * choose up to two students to exchange between your school and the entrances
                             */
                            this.showMessage("how many students do you want to change?");
                            try{
                                numStudent= scanner.nextInt();
                            }catch(Exception e){
                                this.showMessage("Insert a valid value \n>");
                            }
                            if(numStudent<=0 || numStudent>2)
                                this.showMessage("Choose a number from 1 to 2");
                        }while(numStudent<=0 || numStudent>2);

                        choosenStudent=new EnumMap<Color, Integer>(Color.class);
                        for(Color c1:Color.values())
                            choosenStudent.put(c1,0);
                        for(int i=0;i<numStudent;i++) {
                            do {
                                this.showMessage("Choose a color of the student from your school \n>");
                                String colorStudent = scanner.next();
                                try {
                                    color2 = Color.valueOf(colorStudent.toUpperCase());
                                    boolWhile = Arrays.asList(Color.values()).contains(color2);
                                    if (!this.vmodel.getClientPlayer().getStudents().containsKey(color2) || this.vmodel.getClientPlayer().getStudents().get(color2) <= 0) {
                                        boolWhile = false;
                                        System.out.print(ANSI_RED + "Choose a color that is present on your school \n"
                                                + ANSI_RESET);
                                        this.showMessage(">");
                                    }
                                } catch (Exception e) {
                                    boolWhile = false;
                                    System.out.print(ANSI_RED + "Choose a valid color\n" + ANSI_RESET);
                                    this.showMessage(">");
                                }
                            } while (!boolWhile);

                            choosenStudent.put(color2,choosenStudent.get(color2)+1);
                        }

                        entranceStudent=new EnumMap<Color, Integer>(Color.class);
                        for(Color c2:Color.values())
                            entranceStudent.put(c2,0);
                        for(int i=0;i<numStudent;i++) {
                            do {
                                this.showMessage("Choose a student from your entrance \n>");
                                String colorStudent = scanner.next();
                                try {
                                    color2 = Color.valueOf(colorStudent.toUpperCase());
                                    boolWhile = Arrays.asList(Color.values()).contains(color2);
                                    if(!this.vmodel.getClientPlayer().getEntryStudents().containsKey(color2) || this.vmodel.getClientPlayer().getEntryStudents().get(color2)<=0 ){
                                        boolWhile = false;
                                        System.out.print(ANSI_RED + "Choose a color that is present in your entrance\n"
                                                + ANSI_RESET);
                                        this.showMessage(">");
                                    }
                                } catch (Exception e) {
                                    boolWhile = false;
                                    System.out.print(ANSI_RED + "Choose a valid color\n" + ANSI_RESET);
                                    this.showMessage(">");
                                }
                            } while (!boolWhile);

                            entranceStudent.put(color2,entranceStudent.get(color2)+1);
                        }
                        break;

                    case "auctioneer.jpg":
                    case "herbalist.jpg":
                        do{
                            /**
                             * Choose the island position
                             */
                            this.showMessage("Choose the island position \n>");
                            try{
                                posIsland=scanner.nextInt()-1;
                            }catch (Exception e){
                                this.showMessage("Insert a valid value \n >");
                            }
                            if(posIsland<0 || posIsland>this.vmodel.getIslands().size()-1)
                                this.showMessage(ANSI_RED+" Invalid input\n" +ANSI_RESET);
                        }while (posIsland<0 || posIsland>this.vmodel.getIslands().size()-1);
                        break;
                    case "lumberjack.jpg":
                        do{
                            /**
                             * The choosen color will not be considered
                             */
                            this.showMessage("Choose the color not to be considered when calculating the influence \n>");
                            String colorStudent=scanner.next();
                            try{
                                color2=Color.valueOf(colorStudent.toUpperCase());
                                boolWhile=Arrays.asList(Color.values()).contains(color2);
                            }catch(Exception e){
                                boolWhile=false;
                                System.out.print(ANSI_RED+"Choose a valid color"+ANSI_RESET);
                            }
                        }while (!boolWhile);
                        color=color2;
                        break;
                    default:
                        break;
                }
            }
        }
        /**
         * Send the message with the parameters set according to the card, those not set will remain null
         */
        serverHandler.send(new UseCharacterCard(asset,posIsland,choosenStudent,entranceStudent,color));
        asset=null;
        posIsland=null;
        choosenStudent=null;
        entranceStudent=null;
        color=null;
    }

    @Override
    public void setUseCharcaterCard(){
        this.vmodel.setUseCharacterCard(false);
    }

    @Override
    public void playerPlayAssistantCard(int playerID, AssistantCard card) {
        showMessage("Player "+getVmodel().getPlayerByID(playerID).getNickname()+" played card "+card.getName()+"\n");
    }

    /***
     * Main loop of the CLI, the method recives the message from the server and launch the handle.
     */
    @Override
    public void run() {
        while(!endGame){
            ServerToClientMessage fromServer = null;
            try {
                fromServer = serverHandler.read();
                if(!(fromServer instanceof ServerHeartbeat))
                    fromServer.handle(this);

            } catch (IOException | ClassNotFoundException | RuntimeException e) {
                showError("\nConnection error, maybe one player left the match. The app will now close!");
                System.exit(-1);
            }
        }
        serverHandler.close();
        System.exit(0);
    }
}
