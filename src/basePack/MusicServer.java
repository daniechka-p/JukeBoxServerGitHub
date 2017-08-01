package basePack;

import java.io.*;
import java.net.*;
import java.util.*;

public class MusicServer
{
    ArrayList clientOutputStreams;

    public class ClientHandler implements Runnable {
        ObjectInputStream in;
        Socket sock;
        public ClientHandler(Socket clientSocket) {
            try {
                sock = clientSocket;
                in = new ObjectInputStream(sock.getInputStream());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        public void run() {
            Object o1;
            Object o2;
            try {
                while ((o1 = in.readObject()) != null) {
                    o2 = in.readObject();
                    System.out.println("Read two objects");
                    tellEveryone(o1, o2);
                }
            } catch (Exception ex) {
                System.out.println("Disconnected");
            }
        }
    }

    public void go() {
        clientOutputStreams = new ArrayList();
        try {
            ServerSocket serverSock = new ServerSocket(4242);
            while(true) {
                Socket clientSocket = serverSock.accept();
                ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream());
                clientOutputStreams.add(out);
                Thread thread = new Thread(new ClientHandler(clientSocket));
                thread.start();
                System.out.println("Got a connection");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    public void tellEveryone(Object one, Object two) {
        Iterator it = clientOutputStreams.iterator();
        while (it.hasNext()) {
            try {
                ObjectOutputStream out = (ObjectOutputStream) it.next();
                out.writeObject(one);
                out.writeObject(two);
            } catch (Exception ex) {
//              System.out.println("Smth does");
                ex.printStackTrace();
            }
        }
    }
}
