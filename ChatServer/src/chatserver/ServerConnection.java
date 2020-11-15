package chatserver;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ServerConnection extends Thread {

    Socket socket;
    DataInputStream din;
    DataOutputStream dout;
    boolean serverStatus = true;

    public ServerConnection(Socket socket) {
        this.socket = socket;
    }

    public void sendToClient(String message) {
        try {
            dout.writeUTF(message);
            dout.flush();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void sendToAllClient(String mesaage) {
        for (int i = 0; i < ServerMain.connections.size(); i++) {
            ServerConnection sc = ServerMain.connections.get(i);
            sc.sendToClient(mesaage);
        }
    }

    public void run() {
        try {
            din = new DataInputStream(socket.getInputStream());
            dout = new DataOutputStream(socket.getOutputStream());

            while (serverStatus) {
                while (din.available() == 0) {
                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                String text = din.readUTF();
                System.out.println("MSG ::: " + text);

                MsgControl msgcontrol = new MsgControl();
                msgcontrol.stringParcala(text);
                msgcontrol.yazdir();

                String list = "xk-" + String.valueOf(ServerMain.clientList.size()) + "-";
                for (String str : ServerMain.clientList) {
                    list = list + str + "-";
                }
                sendToAllClient(list);
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
