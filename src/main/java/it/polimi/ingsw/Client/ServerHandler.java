package it.polimi.ingsw.Client;

import it.polimi.ingsw.Client.ClientToServer.ClientHeartbeat;
import it.polimi.ingsw.Constants;
import it.polimi.ingsw.Server.ServerToClient.ServerToClientMessage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import static it.polimi.ingsw.Constants.halfTimeout;
import static it.polimi.ingsw.Constants.timeout;

public class ServerHandler {
    private Socket server;
    private ObjectInputStream inputStream;
    private ObjectOutputStream outputStream;


    public ServerHandler() {
        try{
            //this.server=new Socket("127.0.0.1",2000);
            this.server=new Socket(Constants.getIP(),Constants.getPort());
            this.server.setSoTimeout(timeout);
            inputStream = new ObjectInputStream(server.getInputStream());
            outputStream= new ObjectOutputStream(server.getOutputStream());
        }catch (Exception e){
            e.printStackTrace();
        }


        new Thread(() -> { //Server to Client
            while(true){
                sendHeartbeat();
                try {
                    Thread.sleep(halfTimeout);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }

        }).start();
    }

    public ServerToClientMessage read() throws IOException, ClassNotFoundException {
        try{
            return (ServerToClientMessage) inputStream.readObject();
        }
        catch(Exception e){
            return null;
        }
    }
    public void send(Object msg){
        try {
            outputStream.reset();
            outputStream.writeObject(msg);
            outputStream.flush();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void sendHeartbeat(){
        try {
            outputStream.reset();
            outputStream.writeObject(new ClientHeartbeat());
            outputStream.flush();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void close(){
        try {
            server.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
