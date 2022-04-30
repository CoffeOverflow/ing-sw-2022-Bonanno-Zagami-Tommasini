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


    public ServerHandler() throws IOException {
        this.server=new Socket(Constants.getIP(),Constants.getPort());
        inputStream = new ObjectInputStream(server.getInputStream());
        outputStream= new ObjectOutputStream(server.getOutputStream());
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
        outputStream.write((byte[]) msg);
        outputStream.flush();
    }
}
