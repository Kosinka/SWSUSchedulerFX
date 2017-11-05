import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;

import java.util.TimerTask;

public class XtimeController {

    public static String email;
    public static String file;
    public static String note;

    @FXML
    public static Stage STAGE;

    @FXML
    Button XtimeCancelButtonId;
    @FXML
    ToggleGroup TimeRadioButtonGroup;
    @FXML
    TextField XtimeTextFieldXid;
    public void XtimeOkButton(ActionEvent actionEvent) {
        String toggleStatus = TimeRadioButtonGroup.getSelectedToggle().toString();
        int c1 = toggleStatus.indexOf("'") + 1;
        int c2 = toggleStatus.indexOf("'", c1);
        toggleStatus = toggleStatus.substring(c1,c2);
        int time = Integer.valueOf(XtimeTextFieldXid.getText());
        int timeProc = 0;
        if(toggleStatus.equals("Min")){
            timeProc = time * 60 * 1000;
        }
        if(toggleStatus.equals("Sec")){
            timeProc = time * 1000;
        }
        if(toggleStatus.equals("Hrs")){
            timeProc = time * 60 * 60 * 1000;
        }
        new java.util.Timer().schedule(
                new TimerTask() {
                    public void run() {
                        EmailSender();
                    }
                },
                timeProc );
        STAGE.close();
    }

    public void XtimeCancelButton(ActionEvent actionEvent) {
        STAGE.close();
    }
    public void EmailSender(){
        SendEmail sendEmail = new SendEmail(email, "test", file);
        sendEmail.sendMessage("Application status: development\n" + note);
    }
}