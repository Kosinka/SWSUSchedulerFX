import java.util.Properties;
import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Address;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.Message.RecipientType;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class SendEmail {
    private Message message = null;
    protected static String SMTP_SERVER = "smtp.gmail.com";
    protected static String SMTP_Port = "465";
    protected static String SMTP_AUTH_USER = "kosinovtestmail@gmail.com";
    protected static String SMTP_AUTH_PWD = "kosinka46";
    protected static String EMAIL_FROM = "kosinka4@yandex.ru";
    protected static String FILE_PATH = null;
    protected static String REPLY_TO = "skosinka@yandex.ru";

    public SendEmail(String emailTo, String thema, String filepath) {
        Properties properties = new Properties();
        properties.put("mail.smtp.host", SMTP_SERVER);
        properties.put("mail.smtp.port", SMTP_Port);
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.ssl.enable", "true");
        properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        FILE_PATH = filepath;

        try {
            Authenticator auth = new EmailAuthenticator(SMTP_AUTH_USER, SMTP_AUTH_PWD);
            Session session = Session.getDefaultInstance(properties, auth);
            session.setDebug(false);
            InternetAddress email_from = new InternetAddress(EMAIL_FROM);
            InternetAddress email_to = new InternetAddress(emailTo);
            InternetAddress reply_to = REPLY_TO != null?new InternetAddress(REPLY_TO):null;
            this.message = new MimeMessage(session);
            this.message.setFrom(email_from);
            this.message.setRecipient(RecipientType.TO, email_to);
            this.message.setSubject(thema);
            if(reply_to != null) {
                this.message.setReplyTo(new Address[]{reply_to});
            }
        } catch (AddressException var9) {
            System.err.println(var9.getMessage());
        } catch (MessagingException var10) {
            System.err.println(var10.getMessage());
        }

    }

    public boolean sendMessage(String text) {
        boolean result = false;

        try {
            Multipart mmp = new MimeMultipart();
            MimeBodyPart bodyPart = new MimeBodyPart();
            bodyPart.setContent(text, "text/plain; charset=utf-8");
            mmp.addBodyPart(bodyPart);
            if(FILE_PATH != null) {
                MimeBodyPart mbr = this.createFileAttachment(FILE_PATH);
                mmp.addBodyPart(mbr);
            }

            this.message.setContent(mmp);
            Transport.send(this.message);
            result = true;
        } catch (MessagingException var6) {
            System.err.println(var6.getMessage());
        }

        return result;
    }

    private MimeBodyPart createFileAttachment(String filepath) throws MessagingException {
        MimeBodyPart mbp = new MimeBodyPart();
        FileDataSource fds = new FileDataSource(filepath);
        mbp.setDataHandler(new DataHandler(fds));
        mbp.setFileName(fds.getName());
        return mbp;
    }
}
