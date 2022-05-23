package it.polimi.ingsw.Client.CLI;

import it.polimi.ingsw.Client.ClientToServer.*;
import it.polimi.ingsw.Client.ServerHandler;
import it.polimi.ingsw.Client.View;
import it.polimi.ingsw.Client.VirtualModel;
import it.polimi.ingsw.Constants;
import it.polimi.ingsw.Controller.Action;
import it.polimi.ingsw.Controller.State.MoveTo;
import it.polimi.ingsw.Model.*;
import it.polimi.ingsw.Server.ServerToClient.*;

import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
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
       /* Scanner scanner = new Scanner(System.in);
        System.out.print("\nInsert the server IP address > ");
        String ip = scanner.nextLine();
        System.out.print("Insert the server port > ");
        int port = scanner.nextInt();
        Constants.setIP(ip);
        Constants.setPort(port);*/
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
        System.out.print("Select a match to join or type 0 for create new match > ");
        Scanner scanner = new Scanner(System.in);
        int game = scanner.nextInt();
        serverHandler.send(new SelectMatch(game));
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
            num = scanner.nextInt();
            if(num < 2 || num > 3){
                showError("Please insert 2 or 3!");
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
        System.out.println("mother nature position: "+ vmodel.getMotherNaturePosition());
        //this.showBoard();
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
    }

    @Override
    public void youWin() {
        this.showMessage(YouWin.getMsg());
    }

    @Override
    public void otherPlayerWins(OtherPlayerWins msg){
        this.showMessage(msg.getMsg());
    }

    @Override
    public void selectAssistantCard(SelectAssistantCard msg){
        this.showMessage(SelectAssistantCard.getMsg());
        int value=0;
        int steps=0;
        for(String s: msg.getAvailableCards()){
            for(int i=0; i<vmodel.getClientPlayer().getAssistantCards().size();i++) {
                if (vmodel.getClientPlayer().getAssistantCards().get(i).getName().equals(s)) {
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
        for(int i=0; i<vmodel.getClientPlayer().getAssistantCards().size();i++) {
            if (vmodel.getClientPlayer().getAssistantCards().get(i).getName().equals(card)) {
                value = vmodel.getClientPlayer().getAssistantCards().get(i).getValue();
                break;
            }
        }
        serverHandler.send(new PlayAssistantCard(value));
        

    }

    @Override
    public void update(UpdateMessage msg){

        BoardChange bchange=msg.getChange();
        switch(bchange.getChange()){
            case CONQUER:
                System.out.println("conquer");
                this.vmodel.getIslands().get(bchange.getConquerIsland()).setTower(bchange.getConquerorTower());
                break;
            case MOVESTUDENT:
                if(bchange.getMoveTo().equals(MoveTo.ISLAND)){
                    this.vmodel.getIslands().get(bchange.getIslandPosition()).addStudents(bchange.getStudentColor(),1);
                    for(Player p: vmodel.getPlayers()){
                        if(p.getPlayerID() == bchange.getPlayer()){
                            p.getEntryStudents().put(bchange.getStudentColor(),p.getEntryStudents().get(bchange.getStudentColor())-1);
                        }
                    }
                }
                else if(bchange.getMoveTo().equals(MoveTo.SCHOOL)){
                    for(Player p: this.vmodel.getPlayers())
                    {
                        if(p.getPlayerID()==bchange.getPlayer()){
                            this.vmodel.moveToSchool(p.getPlayerID(),bchange.getStudentColor());
                        }
                    }
                }
                break;
            case MERGE:
                System.out.println("merge");
                this.vmodel.getIslands().get(bchange.getConquerIsland()).setTower(bchange.getConquerorTower());
                //this.vmodel.mergeIslands(bchange.getMergedIsland1(), bchange.getMergedIsland2());
                break;
            case MOTHERNATURE:
                this.vmodel.moveMotherNature(bchange.getMotherNatureSteps());
                break;
            case CLOUD:
                this.vmodel.fillClouds(bchange);
                break;
        }
        this.showBoard();
    }

    @Override
    public void chooseWizard(SelectWizard message) throws IOException {
        showMessage(message.getMsg());
        for(Wizards wizard: message.getAvailableWizards()){
            showMessage("> "+wizard.getName()+"\n");
        }
        System.out.print("> ");
        Scanner scanner = new Scanner(System.in);
        String wizard = scanner.nextLine();
        wizard = wizard.replaceAll(" ", "").toUpperCase();
        serverHandler.send(new ChooseWizard(wizard));

    }

    @Override
    public void showBoard(){
        System.out.println("");
        System.out.println("___________ISLANDS___________");
        showIsland();
        System.out.println("");
        System.out.println("___________CLOUDS___________");
        showClouds();
        System.out.println("");
        System.out.println("___________CHARACTER CARDS___________");
        System.out.println("");
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
        System.out.println("");
        System.out.println("___________BOARDS___________");
        System.out.println("");
        int i=0;
        for(Player p:this.vmodel.getPlayers())
            showSchool(p,i++);

    }



    @Override
    public void showIsland(){

        EnumMap<Color,Integer> students;
        StringBuilder studentOnIsland=new StringBuilder();
        int numberOfTower=0;
        Optional<Tower> tower;
        int num=0;
        for(int i=0;i<12;i++)
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
                    studentOnIsland.append(" "+filledRect);
                 }
            }
            if(i==this.vmodel.getMotherNaturePosition())
            {
                studentOnIsland.append(" "+ANSI_YELLOW+filledRect+ANSI_RESET);
            }

            if(i==9 || i==10 ||i==11)
                this.showMessage("Island "+(i+1)+": "+studentOnIsland +'\n');
            else
                this.showMessage("Island "+(i+1)+":  "+studentOnIsland+'\n');
            studentOnIsland.setLength(0);
        }
    }

    @Override
    public void showSchool(Player p,int numColor){

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

        this.showMessage(p.getNickname()+"'s board \n");
        StringBuilder color=new StringBuilder();
        int num=0;
        for(int i=0;i<5;i++)
            num+=numColorEntryStudents[i];
        String[] ansiColor={ANSI_GREEN,ANSI_RED,ANSI_YELLOW,ANSI_PINK,ANSI_BLUE};
        String[] ansiTower={WHITE_BRIGHT,ANSI_BLACK,ANSI_GRAY};
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
                        color.append(WHITE_BACKGROUND + ansiTower[numColor]);
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

    @Override
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
    }

    @Override
    public void chooseOption(ChooseOption message){
        showMessage(message.getMsg());
        System.out.print("> ");
        int n=0;
        if(message.getType()==OptionType.CHOOSECLOUD){
            Scanner scanner = new Scanner(System.in);
            do{
                n = scanner.nextInt();
            }while(n<=0 || n>vmodel.getClouds().size());
            serverHandler.send(new ChooseCloud(n-1));
        }else{
        do {
            Scanner scanner = new Scanner(System.in);
            if(message.isExpertMode()){
                n = scanner.nextInt();
            }else n=1;
            switch (n) {
                case 1:
                    if (message.getType()==OptionType.MOVESTUDENTS) {
                        for (int i = 0; i < 3; i++) {
                            System.out.print("Choose where to move the student: \n1.school\n2.island \n> ");
                            int n2 = 0;
                            String col=null;
                            Color color=null;
                            boolean boolWhile;
                            do {
                                n2 = scanner.nextInt();
                                do{
                                    do{
                                        System.out.print("Choose the color of the student: \n> ");
                                        col=scanner.next();
                                        try{
                                            Color color2=Color.valueOf(col.toUpperCase());
                                            boolWhile=Arrays.asList(Color.values()).contains(color2);
                                        }catch(Exception e){
                                            boolWhile=false;
                                            System.out.print("Choose a valid color");
                                        }
                                    }while (!boolWhile);
                                    color=Color.valueOf(col.toUpperCase());
                                }while(!vmodel.getClientPlayer().getEntryStudents().containsKey(color) ||
                                        vmodel.getClientPlayer().getEntryStudents().get(color)==0);
                                if (n2 == 1) {
                                    serverHandler.send(new MoveStudent(MoveTo.SCHOOL,color,i));
                                } else if (n2 == 2) {
                                    int islandPosition;
                                    do{
                                    System.out.print("Choose the number of the island: \n> ");
                                    islandPosition= scanner.nextInt();}while(islandPosition<=0 || islandPosition>vmodel.getIslands().size());
                                    serverHandler.send(new MoveStudent(MoveTo.ISLAND,Color.valueOf(col.toUpperCase()),islandPosition-1,i));
                                    //vmodel.getClientPlayer().getEntryStudents().put(color,vmodel.getClientPlayer().getEntryStudents().get(color)-1);
                                    vmodel.getIslands().get(islandPosition-1).addStudents(color,1);
                                } else {
                                    System.out.print("Option not valid, retry!\n");
                                }
                            } while (n2 != 1 && n2 != 2);
                        }
                    } else if (message.getType()==OptionType.MOVENATURE) {
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
                            System.out.println("Choose a valid card");
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
                    System.out.print("Option not valid, retry!");
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
        Scanner scanner = new Scanner(System.in);

        List<CharacterCard> characterCards=this.vmodel.getCharacterCards();
        for(CharacterCard c:characterCards){
            if(c.getAsset().equals(card)){
                if(characterStudentName.contains(card)){
                    switch (card){
                        case "inkeeper.jpg":
                            do{
                                this.showMessage("Scegli la posizione dell'isola \n>");
                                posIsland=scanner.nextInt()-1;
                                if(posIsland<0 || posIsland>this.vmodel.getIslands().size()-1)
                                    this.showMessage(ANSI_RED+" Invalid input\n" +ANSI_RESET);
                            }while (posIsland<0 || posIsland>this.vmodel.getIslands().size()-1);
                            for(Color c1:color.values())
                                choosenStudent.put(c1,0);
                            Color color2=null;
                           do{
                               this.showMessage("Scegli il colore dello studente dalla carta\n>");
                               String colorStudent=scanner.next();
                               try{
                                   color2=Color.valueOf(colorStudent.toUpperCase());
                                   boolWhile=Arrays.asList(Color.values()).contains(color2);
                                   if(c.getStudents().get().get(color2)>0)
                                       c.getStudents().get().put(color2,c.getStudents().get().get(color2)-1);
                                   else {
                                       boolWhile=false;
                                       System.out.print(ANSI_RED+"Choose color that is present on the card\n"+ANSI_RESET);
                                       this.showMessage(">");
                                   }
                               }catch(Exception e){
                                   boolWhile=false;
                                   System.out.print(ANSI_RED+"Choose a valid color\n"+ANSI_RESET);
                                   this.showMessage(">");
                               }
                           }while (!boolWhile);
                           choosenStudent.put(color2,1);
                           break;
                        // TO DO
                        case "thief.jpg":

                            break;
                        case "clown.jpg":
                            break;

                        case "princess,jpg":
                            break;
                        case "storyteller.jpg":
                            break;

                        case "auctioneer.jpg":
                        case "herbalist.jpg":
                            do{
                                this.showMessage("Scegli la posizione dell'isola \n>");
                                posIsland=scanner.nextInt()-1;
                                if(posIsland<0 || posIsland>this.vmodel.getIslands().size()-1)
                                    this.showMessage(ANSI_RED+" Invalid input\n" +ANSI_RESET);
                            }while (posIsland<0 || posIsland>this.vmodel.getIslands().size()-1);
                            break;
                        case "lumberjack.jpg":
                            do{
                                this.showMessage("Scegli il colore da non calcolare nell'influenza\n>");
                                String colorStudent=scanner.next();
                                try{
                                    color2=Color.valueOf(colorStudent.toUpperCase());
                                    boolWhile=Arrays.asList(Color.values()).contains(color2);
                                }catch(Exception e){
                                    boolWhile=false;
                                    System.out.print(ANSI_RED+"Choose a valid color"+ANSI_RESET);
                                }
                            }while (!boolWhile);
                            break;
                    }
                }
            }
        }

        serverHandler.send(new UseCharacterCard(asset,posIsland,choosenStudent,entranceStudent,color));
    }

    @Override
    public void run() {
        while(true){
            ServerToClientMessage fromServer = null;
            try {
                fromServer = serverHandler.read();
                fromServer.handle(this);
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }

    }
}
