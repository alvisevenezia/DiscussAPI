package fr.alvisevenezia.Web.Socket;

import java.io.IOException;
import java.net.Socket;

public class ClientSocket {

    private String host;
    private int port;
    private Socket socket;

    public ClientSocket(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public Socket getSocket() {
        return socket;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    private void buildSocket(){

        try {
            socket = new Socket(host,port);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

   private void close(){

       try {
           socket.close();
       } catch (IOException e) {
           e.printStackTrace();
       }

   }


}
