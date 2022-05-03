package it.polimi.ingsw.Client;

import it.polimi.ingsw.Constants;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerHandler {
    private Socket server;
    private ObjectInputStream inputStream;
    private ObjectOutputStream outputStream;


    public ServerHandler() {
        try{
            this.server=new Socket("127.0.0.1",2000);
            //this.server=new Socket(Constants.getIP(),Constants.getPort());
            inputStream = new ObjectInputStream(server.getInputStream());
            outputStream= new ObjectOutputStream(server.getOutputStream());
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public Object read() throws IOException, ClassNotFoundException {
        try{
            return inputStream.readObject();
        }
        catch(Exception e){
            return null;
        }
    }
    public void write(Object msg) throws IOException {
        outputStream.reset();
        outputStream.writeObject(msg);
        outputStream.flush();
    }
}
