package it.polimi.ingsw.Client;

import it.polimi.ingsw.Client.ClientToServer.ClientHeartbeat;
import it.polimi.ingsw.Constants;
import it.polimi.ingsw.Server.ServerToClient.ServerToClientMessage;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import static it.polimi.ingsw.Constants.*;

/***
 * Server Handler Class
 * @author Angelo Zagami
 */
public class ServerHandler {
    private Socket server;
    private ObjectInputStream inputStream;
    private ObjectOutputStream outputStream;


    /***
     * Class construct. The method create the server socket and retrieve input and output stream for recive and send messages.
     * It also starts the Heartbeat thread.
     */
    public ServerHandler() {
        try{
            this.server = new Socket(Constants.getIP(),Constants.getPort());
            inputStream = new ObjectInputStream(server.getInputStream());
            outputStream = new ObjectOutputStream(server.getOutputStream());
            this.server.setSoTimeout(timeout);
        }catch (Exception e){
            throw new RuntimeException(e);
        }
        new Thread(() -> { //Client to Server
            while(!endGame){
                try {
                    Thread.sleep(halfTimeout);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                try{
                    sendHeartbeat();
                }catch (RuntimeException e){
                    if(!endGame){
                        System.out.println(ANSI_RED+"\nConnection error, maybe one player left the match. The app will now close!"+ANSI_RESET);
                        System.exit(-1);
                    }
                }
            }
        }).start();
    }

    /***
     * Recive a message from server
     * @return The message received
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public ServerToClientMessage read() throws IOException, ClassNotFoundException {
        try{
            return (ServerToClientMessage) inputStream.readObject();
        }
        catch(Exception e){
           throw new RuntimeException(e);
        }
    }

    /***
     * Send a message to Server
     * @param message The message to send
     */
    public synchronized void send(Object message){
        try {
            outputStream.reset();
            outputStream.writeObject(message);
            outputStream.flush();
        }catch(Exception e){
            throw new RuntimeException(e);
        }
    }

    /***
     * Send a Heartbeat to the Server
     */
    public void sendHeartbeat(){
        send(new ClientHeartbeat());
    }

    /***
     * Close server connection
     */
    public void close(){
        try {
            server.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
