package chatclient;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import java.io.DataOutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class SampleController {

    SecondController sc;
    DataOutputStream dout;

    @FXML
    private Button button;

    @FXML
    private Label label;

    @FXML
    private JFXPasswordField txtPass;

    @FXML
    private JFXTextField txtUser;

    @FXML
    private JFXButton btnLogin;

    public static String userName, pass;
    private static boolean login = false;

    @FXML
    void btnLogin_click(ActionEvent event) {
        userName = txtUser.getText();
        pass = txtPass.getText();
        try {
            Class.forName("org.postgresql.Driver");
            String url = "jdbc:postgresql://localhost:5432/dbchat?user=postgres&password=123456&ssl=false";
            Connection conn = DriverManager.getConnection(url);
            System.out.println("connected");
            PreparedStatement ps = conn.prepareStatement("select * from table_user where username='" + userName + "' and password='" + pass + "'");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                System.out.println("login success");
                login = true;
            }
            if (login) {
                FXMLLoader fxloader = new FXMLLoader(getClass().getResource("Second.fxml"));
                Parent parent2 = (Parent) fxloader.load();
                Stage stage = new Stage();
                stage.setTitle("MyChat");
                stage.setScene(new Scene(parent2));
                stage.show();
                sc = fxloader.getController();
                sc.getUserName(userName);

                Node source = (Node) event.getSource();
                Stage stage1 = (Stage) source.getScene().getWindow();
                stage1.close();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
