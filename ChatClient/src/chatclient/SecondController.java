package chatclient;

import java.awt.EventQueue;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextField;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;


public class SecondController implements Initializable {

    Socket socket;
    DataInputStream din;
    DataOutputStream dout;
    private static Integer userNumber;
    private static String[] parts;
    private static String selectedText;
    private static FileOutputStream fos;
    private static FileOutputStream fos1;
    private static FileInputStream fis;

    @FXML
    private Label lblUserName;

    @FXML
    private JFXTextField txtMessage;

    @FXML
    private TextArea txtHistory;

    @FXML
    private JFXButton btnSend;

    @FXML
    private JFXListView<Label> listview;

    public void getUserName(String usname) {
        lblUserName.setText(usname);
    }
    private void stringShred(String text) {
        parts = text.split("-");
        if (parts[0].equals("xk")) {
            userNumber = Integer.valueOf(parts[1]);
            System.out.println("Number of user = " + userNumber);
        }
        stringKontrol();
    }
    private void stringKontrol() {
        if (parts[0].equals("xk")) {
            listview.getItems().clear();
            int clientSayisi = Integer.valueOf(parts[1]);
            for (int i = 2; i <= clientSayisi + 1; i++) {
                System.out.println(parts[i]);

                Label label = new Label(parts[i]);

                listview.getItems().add(label);

            }
        } else if (parts[0].equals("msg")) {
            txtHistory.appendText(parts[1] + " : " + parts[2] + "\n");
        }
    }

    private void connect() {
        SampleController sampc = new SampleController();

        try {

            socket = new Socket("localhost", 6068);
            din = new DataInputStream(socket.getInputStream());
            dout = new DataOutputStream(socket.getOutputStream());
            dout.writeUTF("xun-" + sampc.userName);
            dout.flush();

            String msgin = "";
            while (!msgin.equals("exit")) {

                msgin = din.readUTF();
                System.out.println("client :::" + msgin);

                stringShred(msgin);
                //saveMessage();

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void saveMessage() {
        try {
            fos = new FileOutputStream(lblUserName.getText() + "-" + getSelectedItemText() + ".txt");
            fos1 = new FileOutputStream(getSelectedItemText() + "-" + lblUserName.getText() + ".txt");

        } catch (FileNotFoundException ex) {
            System.out.println("File Not Save.");
        }

        OutputStreamWriter osw = new OutputStreamWriter(fos, StandardCharsets.UTF_8);
        OutputStreamWriter osw1 = new OutputStreamWriter(fos1, StandardCharsets.UTF_8);
        try {
            osw.write(txtHistory.getText());
            osw.flush();
            osw.close();
            osw1.write(txtHistory.getText());
            osw1.flush();
            osw1.close();
        } catch (IOException ex) {
            System.out.println("Failed to Clean File.");
        }

    }

    public String getSelectedItemText() {
        listview.setOnMouseClicked(new EventHandler() {
            @Override
            public void handle(Event event) {
                int seciliIndex = listview.getSelectionModel().getSelectedIndex();
                selectedText = listview.getItems().get(seciliIndex).getText();
                System.out.println("Selected index = " + selectedText);
                try {
                    fis = new FileInputStream(getFileName());
                } catch (FileNotFoundException ex) {
                    System.out.println("File Not Found.");
                }
                txtHistory.setText("");
                BufferedReader br = new BufferedReader(new InputStreamReader(fis, StandardCharsets.UTF_8));
                String line;
                try {
                    while ((line = br.readLine()) != null) {
                        txtHistory.appendText(line);
                        txtHistory.appendText("\n");
                    }
                } catch (IOException ex) {
                    System.out.println("Conversation Failed to Attach to File.");
                }
            }
        });
        return selectedText;
    }

    private String getFileName() {
        String user1 = lblUserName.getText();
        String fileName = getSelectedItemText() + "-" + user1 + ".txt";
        System.out.println("getFileName" + fileName);
        return fileName;
    }

    public void sendMessage(String message) throws IOException {
        try {
            dout.writeUTF("msg-" + lblUserName.getText() + "-" + getSelectedItemText() + "-" + message);
            dout.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }

        txtHistory.appendText(lblUserName.getText() + " : " + message + "\n");

        saveMessage();
    }

    @FXML
    void btnSend_click(ActionEvent event) {
        try {
            sendMessage(txtMessage.getText());
            txtMessage.setText("");
        } catch (IOException ex) {
            Logger.getLogger(SecondController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {
                connect();
            }
        });

        getSelectedItemText();

    }

}
