package it.polimi.ingsw.Client.CLI;

import it.polimi.ingsw.Client.ClientToServer.*;
import it.polimi.ingsw.Client.ServerHandler;
import it.polimi.ingsw.Client.View;
import it.polimi.ingsw.Client.VirtualModel;
import it.polimi.ingsw.Constants;
import it.polimi.ingsw.Controller.State.MoveTo;
import it.polimi.ingsw.Model.*;
import it.polimi.ingsw.Server.ServerToClient.*;

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
                this.vmodel.getIslands().get(bchange.getConquerIsland()).setTower(bchange.getConquerorTower());
                break;
            case MOVESTUDENT:
                if(bchange.getMoveTo().equals(MoveTo.ISLAND)){
                    this.vmodel.getIslands().get(bchange.getIslandPosition()).addStudents(bchange.getStudentColor(),1);
                }
                else if(bchange.getMoveTo().equals(MoveTo.SCHOOL)){
                    for(Player p: this.vmodel.getPlayers())
                    {
                        if(p.equals(bchange.getPlayer())){
                            p.addStudentOf(bchange.getStudentColor());
                        }
                    }
                }
                break;
            case MERGE:
                this.vmodel.mergeIslands(bchange.getMergedIsland1(), bchange.getMergedIsland2());
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
            showIsland();
            showClouds();

            for(Player p:this.vmodel.getPlayers())
                showSchool(p);
    }



    @Override
    public void showIsland(){

        EnumMap<Color,Integer> students;
        StringBuilder studentOnIsland=new StringBuilder();
        int num=0;
        for(int i=0;i<12;i++)
        {
            students=this.vmodel.getIslands().get(i).getStudents();
            for(Color c: Color.values())
            {
                num=students.get(c);
                for(int j=0;j<num;j++){
                    switch(c){
                        case BLUE:
                            studentOnIsland.append(ANSI_BLUE+"○"+ANSI_RESET);
                            break;
                        case PINK:
                            studentOnIsland.append(ANSI_PINK+"○"+ANSI_RESET);
                            break;
                        case GREEN:
                            studentOnIsland.append(ANSI_GREEN+"○"+ANSI_RESET);
                            break;
                        case RED:
                            studentOnIsland.append(ANSI_RED+"○"+ANSI_RESET);
                            break;
                        case YELLOW:
                            studentOnIsland.append(ANSI_YELLOW+"○"+ANSI_RESET);
                            break;
                    }
                }

            }
            if(i==9 || i==10 ||i==11)
                this.showMessage("Island "+(i+1)+": "+studentOnIsland +'\n');
            else
                this.showMessage("Island "+(i+1)+":  "+studentOnIsland+'\n');
            studentOnIsland.setLength(0);
        }
    }

    @Override
    public void showSchool(Player p){
        char emptyCircle ='⚪';
        char filledCircle='⚫';
        char emptyRect='▯';
        char filledRect='▮';

        char[][] boardElement = new char[5][14];
        String[] titleElement = new String[4];

        EnumMap<Color,Integer> students=p.getStudents();
        EnumMap<Color,Integer> entryStudents=p.getEntryStudents();

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

        StringBuilder color=new StringBuilder();
        int num=0;
        for(int i=0;i<5;i++)
            num+=numColorEntryStudents[i];
        String[] ansiColor={ANSI_GREEN,ANSI_RED,ANSI_YELLOW,ANSI_PINK,ANSI_BLUE};

        for (int i = 0; i < 5; i++) {

            for (int j = 0; j < 14; j++) {
                if (i == 0 && j == 0) boardElement[i][j] = '⦵';
                else if (j == 12 || j == 13) boardElement[i][j] = '▯';
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
                    if(i==0 && j==0)boardElement[i][j]='⦵';
                    else if(num>0){
                        if(numColorEntryStudents[k]>1)
                        {
                            color.append(ansiColor[k]);
                            numColorEntryStudents[k]--;
                        }
                        else
                        {color.append(ansiColor[k]);numColorEntryStudents[k]--;
                            k++;}
                        boardElement[i][j]=filledCircle;
                        num--;

                    }else boardElement[i][j]=emptyCircle;

                    color.append(boardElement[i][j]);
                    if(num==0)color.append(ANSI_RESET);
                }
                else{
                    if(numberOfTower>0)
                    {
                        boardElement[i][j]=filledRect;
                        numberOfTower--;
                    }
                    else
                        boardElement[i][j]=emptyRect;
                    color.append(boardElement[i][j]);
                }
                if (j == 1 || j == 10 || j == 11 || j == 13) color.append(ANSI_RESET + "|");
            }
            color.append("\n");
        }
        this.showMessage(color.toString());
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
                                    studentOnClouds.append(ANSI_BLUE + "*" + ANSI_RESET);
                                    break;
                                case PINK:
                                    studentOnClouds.append(ANSI_PINK + "*" + ANSI_RESET);
                                    break;
                                case GREEN:
                                    studentOnClouds.append(ANSI_GREEN + "*" + ANSI_RESET);
                                    break;
                                case RED:
                                    studentOnClouds.append(ANSI_RED + "*" + ANSI_RESET);
                                    break;
                                case YELLOW:
                                    studentOnClouds.append(ANSI_YELLOW + "*" + ANSI_RESET);
                                    break;
                            }
                        }
                    }
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
