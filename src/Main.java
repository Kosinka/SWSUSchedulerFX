import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.*;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Main extends Application {

    public void start(Stage primaryStage) throws Exception {
        Parent root = (Parent) FXMLLoader.load(this.getClass().getResource("sample.fxml"));
        primaryStage.setTitle("SchedulerFx");
        primaryStage.setResizable(false);
        primaryStage.setScene(new Scene(root, 600.0D, 300.0D));
        primaryStage.show();
    }

    public static void main(String[] args) throws IOException {
        File file = new File("settings");
        if (!file.exists()) {
            Date date = new Date();
            try {
                FileOutputStream fos = new FileOutputStream("settings");
                ObjectOutputStream oos = new ObjectOutputStream(fos);
                Map<String, String> hashMap = new HashMap<String, String>();
                hashMap.put("email","kosinka4@yandex.ru");
                hashMap.put("note","Default note!");
                hashMap.put("file","");
                oos.writeObject(hashMap);
                oos.close();
                fos.close();

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            }
        launch(args);
    }
}