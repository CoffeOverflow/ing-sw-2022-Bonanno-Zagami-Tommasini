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

public class CLI implements View, Runnable {

    private ServerHandler serverHandler;
    private VirtualModel vmodel;

    public CLI(ServerHandler serverHandler){
        this.serverHandler = serverHandler;
        this.vmodel=new VirtualModel();
    }
    public static void main(String[] args) {
        System.out.println("\n"+Constants.ERIANTYS);
        /*Scanner scanner = new Scanner(System.in);
        System.out.print("\nInsert the server IP address > ");
        String ip = scanner.nextLine();
        System.out.print("Insert the server port > ");
        int port = scanner.nextInt();*/
        Constants.setIP("127.0.0.1");
        Constants.setPort(2000);
        ServerHandler server = new ServerHandler();
        Thread cliThread = new Thread(new CLI(server));
        cliThread.start();
        /*Scanner scanner = null;
        try {
            boolean confirmation = false;
            do{
                ServerToClientMessage fromServer = server.read();
                if(fromServer instanceof  RequestNickname){
                    RequestNickname msg = (RequestNickname) fromServer;
                    System.out.print(msg.getMsg()+" > ");
                    scanner = new Scanner(System.in);
                    String nickname = scanner.nextLine();
                    server.write(new ChooseNickname(nickname));
                    fromServer.handle(this, server);
                }
                if(fromServer instanceof Error){
                    Error msg = (Error) fromServer;
                    System.err.println(msg.getMessage());
                }
                if(fromServer instanceof ActionValid){
                    ActionValid msg = (ActionValid) fromServer;
                    System.out.println(msg.getMsg());
                }
                if(fromServer instanceof ChooseMatch){
                    System.out.println("\nAvailable match:");
                    ChooseMatch msg = (ChooseMatch) fromServer;
                    for(String match : msg.getAvailableMatchs())
                        System.out.println(match);
                    System.out.print("\n"+msg.getMsg());
                    int game = scanner.nextInt();
                    server.write(new SelectMatch(game));
                }
                if (fromServer instanceof GenericMessage){
                    System.out.println(((GenericMessage) fromServer).getMessage());
                }
                if(fromServer instanceof WaitForOtherPlayer){
                    WaitForOtherPlayer msg = (WaitForOtherPlayer) fromServer;
                    System.out.println(msg.getMsg());
                    confirmation = true;
                }
            }while(!confirmation);
        } catch (IOException e) {
            System.out.println("Server unreachable :c");
            System.exit(-1);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        while(true){
            String fromInput = scanner.nextLine();
            if(fromInput.equals("Quit"))
                break;
        }*/

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
            System.out.print("Select a match to join or type 0 for create new match > ");
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
        serverHandler.close();
    }

    @Override
    public void otherPlayerWins(OtherPlayerWins msg){
        this.showMessage(msg.getMsg());
    }

    @Override
    public void selectAssistantCard(SelectAssistantCard msg){
        int value=0;
        do{
            this.showMessage(SelectAssistantCard.getMsg());
            int steps=0;
            for(String s: msg.getAvailableCards()){
                for(int i=0; i<vmodel.getClientPlayer().getAssistantCards().size();i++) {
                    if (vmodel.getClientPlayer().getAssistantCards().get(i).getName().equalsIgnoreCase(s)) {
                        value = vmodel.getClientPlayer().getAssistantCards().get(i).getValue();
                        steps = vmodel.getClientPlayer().getAssistantCards().get(i).getMothernatureSteps();
                        break;
                    }
                }
                this.showMessage("Card: "+ s + " value: " + value + " steps: " + steps+ "\n" );
            }
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
                colorTower=ANSI_GRAY;
            showSchool(p,colorTower);
        }

    }

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
                            studentOnIsland.append(ANSI_GRAY);
                            break;
                        case WHITE:
                            studentOnIsland.append(ANSI_WHITE);
                            break;

                    }
                    studentOnIsland.append(" "+ANSI_BACKGROUND_PURPLE+filledRect+ANSI_RESET);
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

        numColorStudents[0]=students.get(Color.GREEN);
        numColorStudents[1]=students.get(Color.RED);
        numColorStudents[2]=students.get(Color.YELLOW);
        numColorStudents[3]=students.get(Color.PINK);
        numColorStudents[4]=students.get(Color.BLUE);

        numColorEntryStudents[0]=entryStudents.get(Color.GREEN);
        numColorEntryStudents[1]=entryStudents.get(Color.RED);
        numColorEntryStudents[2]=entryStudents.get(Color.YELLOW);
        numColorEntryStudents[3]=entryStudents.get(Color.PINK);
        numColorEntryStudents[4]=entryStudents.get(Color.BLUE);

        this.showMessage(p.getNickname()+"'s board "); this.showMessage(" Coins: "+p.getMoney()+"\n");
        StringBuilder color=new StringBuilder();
        int num=0;
        for(int i=0;i<5;i++)
            num+=numColorEntryStudents[i];
        String[] ansiColor={ANSI_GREEN,ANSI_RED,ANSI_YELLOW,ANSI_PINK,ANSI_BLUE};
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 14; j++) {
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
                if(j>1 && j<12) {
                    if(j!=11)
                    {
                        color.append(ansiColor[i]);
                        color.append(boardElement[i][j]);
                    }
                    else
                    {
                        if(professor[i])
                            boardElement[i][j]=filledCircle;
                        else
                            boardElement[i][j]=emptyCircle;
                        color.append(ansiColor[i]);
                        color.append(boardElement[i][j]);
                    }
                }
                else if(j<=1){
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
                        if (numberOfTower > 0) {
                            boardElement[i][j] = filledRect;
                            numberOfTower--;
                        } else boardElement[i][j] = emptyRect;
                        color.append(WHITE_BACKGROUND + colorTower);
                        color.append(boardElement[i][j]);
                        if(j!=13)
                        color.append(" ");
                }
                if (j == 1 || j == 10 || j == 11 || j == 13) color.append(ANSI_RESET + "|");
            }
            color.append("\n");
        }
        this.showMessage(color.toString());
        color.setLength(0);
        this.showMessage("________________\n");

    }

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
        int n=0;
        if(message.getType()==OptionType.CHOOSECLOUD){
            showMessage(message.getMsg());
            System.out.print("> ");
            Scanner scanner = new Scanner(System.in);
            boolean checkIfNonEmptyCloud=false;
            do{
                try {
                    n = scanner.nextInt();
                }
                catch (InputMismatchException e){
                    this.showMessage("Please insert an integer value");
                }
                if(n>0 && n<=vmodel.getClouds().size()){
                    for(Color c :vmodel.getClouds().get(n-1).getStudents().keySet()){
                        if(vmodel.getClouds().get(n-1).getStudents().get(c)>0)
                            checkIfNonEmptyCloud=true;
                    }
                }
                if(!checkIfNonEmptyCloud)
                    System.out.print("choose a valid number for the cloud: \n> ");
            }while(!checkIfNonEmptyCloud);
            serverHandler.send(new ChooseCloud(n-1));
            this.vmodel.setUseCharacterCard(false);
        }else{
        do {
            Scanner scanner = new Scanner(System.in);
            if(message.isExpertMode() && this.vmodel.isUseCharacterCard()==false){
                String msg= message.getMsg();
                showMessage("Choose an option: \n 1."+msg+" 2.Play a character card \n" );
                System.out.print("> ");
                try {
                    n = scanner.nextInt();
                }
                catch (InputMismatchException e){
                    this.showMessage("Please insert an integer value\n");
                }

            }else n=1;
            switch (n) {
                case 1:
                    if (message.getType()==OptionType.MOVESTUDENTS) {
                            System.out.print("Choose where to move the student: \n1.school\n2.island \n> ");
                            int n2 = 0;
                            String col=null;
                            Color color=null;
                            boolean boolWhile;
                            do {
                                try {
                                    n2 = scanner.nextInt();
                                }catch (InputMismatchException e){
                                    this.showMessage("Please insert an integer value\n");
                                }

                                do{
                                    do{
                                        System.out.print("Choose the color of the student: \n> ");
                                        col=scanner.next();
                                        try{
                                            Color color2=Color.valueOf(col.toUpperCase());
                                            boolWhile=Arrays.asList(Color.values()).contains(color2);
                                        }catch(Exception e){
                                            boolWhile=false;
                                            System.out.print("Choose a valid color\n");
                                        }
                                    }while (!boolWhile);
                                    color=Color.valueOf(col.toUpperCase());
                                }while(!vmodel.getClientPlayer().getEntryStudents().containsKey(color) ||
                                        vmodel.getClientPlayer().getEntryStudents().get(color)==0);
                                if (n2 == 1) {
                                    serverHandler.send(new MoveStudent(MoveTo.SCHOOL,color,this.vmodel.getNumOfInstance()));
                                } else if (n2 == 2) {
                                    int islandPosition;
                                    do{
                                    System.out.print("Choose the number of the island: \n> ");
                                    islandPosition= scanner.nextInt();}while(islandPosition<=0 || islandPosition>vmodel.getIslands().size());
                                    serverHandler.send(new MoveStudent(MoveTo.ISLAND,Color.valueOf(col.toUpperCase()),islandPosition-1,this.vmodel.getNumOfInstance()));
                                    //vmodel.getClientPlayer().getEntryStudents().put(color,vmodel.getClientPlayer().getEntryStudents().get(color)-1);
                                }
                            } while (n2 != 1 && n2 != 2);
                    } else if (message.getType()==OptionType.MOVENATURE) {
                        this.vmodel.resetNumOfInstance();
                        int steps;
                        System.out.println("Choose the steps of mother nature");
                        steps = scanner.nextInt();
                        serverHandler.send(new MoveMotherNature(steps));
                    }
                    break;
                case 2:
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
                        System.out.print(">");
                        card= scanner.next();
                        if(!charcaterName.contains(card)){
                            choose=false;
                            System.out.println(ANSI_RED+"Choose a valid card"+ANSI_RESET);
                        }
                        else
                            choose=true;
                    } while (!choose);

                    List<String> characterStudentName = new ArrayList<>();
                    characterStudentName.add("innkeeper.jpg");//posizione isola e choosen student (un solo studente)
                    characterStudentName.add("thief.jpg");//choosen color (prendere 3 studenti dalla scuola e rimetterli nel sacchetto)
                    characterStudentName.add("clown.jpg");//2 mappe (choosen<=3 studenti dalla carta, entrance==choosen dall'ingresso)
                    characterStudentName.add("princess.jpg"); //choosen con un solo studente (preso da quelli sulla carta)
                    characterStudentName.add("storyteller.jpg");//choosen dalla scuola e scambia con entrance (<=2)
                    characterStudentName.add("lumberjack.jpg");//choosen color (da non considerare nell'influenza)
                    characterStudentName.add("auctioneer.jpg");//posizione isola
                    characterStudentName.add("herbalist.jpg");//posizione isola
                    sendCard(characterStudentName,card.toLowerCase()+".jpg");
                    break;
                default:
                    System.out.print("Option not valid, retry!\n");
                    break;
            }
        }while(n!=1 && n!=2);
        }
    }

    public void sendCard(List<String> characterStudentName, String card){
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
                if(characterStudentName.contains(card)){
                    switch (card){
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
                               this.showMessage("Scegli il colore dello studente dalla carta\n>");
                               String colorStudent=scanner.next();
                               try{
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
                                        e.printStackTrace();
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
        }
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
    public void run() {
        while(true){
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

    }
}
