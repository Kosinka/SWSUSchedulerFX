import java.io.*;
import java.util.HashMap;
import java.util.TimerTask;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Controller {
    private HashMap hashMap;
    private String filepath,email,note;
    @FXML
    TextField EmailTextField;
    @FXML
    TextField NoteTextField;
    @FXML
    TextField FileTextField;
    @FXML
    Text emailTextid;

    public Controller() throws IOException, ClassNotFoundException {
        FileInputStream fis = new FileInputStream("settings");
        ObjectInputStream ois = new ObjectInputStream(fis);
        hashMap = (HashMap) ois.readObject();
        email = (String) hashMap.get("email");
        note = (String) hashMap.get("note");
        String file = (String) hashMap.get("file");

        ois.close();
        fis.close();

        new java.util.Timer().schedule(
                new TimerTask() {
                    public void run() {
                        EmailTextField.setText(email);
                        NoteTextField.setText(note);
                        FileTextField.setText(file);
                    }
                },
                500 );

        if(file.equals("")) {
            filepath = null;
        } else {
            filepath = file;
        }

   }


    public void SendNowButton(ActionEvent event) throws IOException {
        EmailSender();
    }

    public void Send5MinButton(ActionEvent event) {
        String email = (String) hashMap.get("email");
        String note = (String) hashMap.get("note");
        System.out.println(email + note + filepath);
        new java.util.Timer().schedule(
                new TimerTask() {
                    public void run() {
                        EmailSender();
                    }
                },
                300000 );
    }

    public void SendXMinButton(ActionEvent event) throws IOException {
        Stage XtimeStage = new Stage();
        XtimeController.STAGE = XtimeStage;
        Parent root1 = (Parent) FXMLLoader.load(this.getClass().getResource("Xtime.fxml"));
        XtimeStage.setTitle("SchedulerFx - XTime");
        XtimeStage.setResizable(false);
        XtimeStage.setScene(new Scene(root1, 300.0D, 170.0D));
        String file = (String) hashMap.get("file");
        if(file.equals("")) {
            filepath = null;
        } else {
            filepath = file;
        }
        email = (String) hashMap.get("email");
        note = (String) hashMap.get("note");
        XtimeController.email = email;
        XtimeController.file = filepath;
        XtimeController.note = note;
        XtimeStage.show();
    }

    public void SettingsOK(ActionEvent event) throws IOException {
        FileOutputStream fos = new FileOutputStream("settings");
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        if (EmailCheck(EmailTextField.getText())) {
            hashMap.put("email",EmailTextField.getText());
            emailTextid.setText("Email принят");
            emailTextid.setFill(Color.valueOf("#00000099"));
            new java.util.Timer().schedule(
                    new TimerTask() {
                        public void run() {
                            emailTextid.setText("Куда отправлять e-mail");
                        }
                    },
                    2000 );
        } else {
            emailTextid.setText("Ошибка ввода email.");
            emailTextid.setFill(Color.valueOf("red"));
        }

        hashMap.put("note",NoteTextField.getText());
        hashMap.put("file",FileTextField.getText());
        String file = (String) hashMap.get("file");
        if(file.equals("")) {
            filepath = null;
        } else {
            filepath = file;
        }
        email = (String) hashMap.get("email");
        note = (String) hashMap.get("note");
        oos.writeObject(hashMap);
        oos.close();
        fos.close();
    }

    public void EmailSender(){
        SendEmail sendEmail = new SendEmail(email, "test", filepath);
        sendEmail.sendMessage("Application status: development\n" + note);
    }

    private static boolean EmailCheck(String email){
        boolean result = false;
        char[] emailCharArray = email.toCharArray();
        boolean sobaka = false,to4ka = false;
        for (int i = 0; i <= emailCharArray.length - 1; i++){

            int sobakaInt = 0, to4kaInt = 0;
            if (emailCharArray[i] == '@') {
                sobaka = true;
                sobakaInt = i;
            }
            if (emailCharArray[i] == '.') {
                to4ka = true;
                to4kaInt = i;
            }
            if((to4kaInt > sobakaInt) && (sobaka && to4ka)){
                result = true;
            }
        }
        return result;
    }

}