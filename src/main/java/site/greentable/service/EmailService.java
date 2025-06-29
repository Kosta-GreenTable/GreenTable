package site.greentable.service;

import java.io.InputStream;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 * 이메일 발송 서비스
 */
public class EmailService {

    private Properties config;

    public EmailService() {
        loadConfig();
    }

    /**
     * 설정 파일 로드 (env.properties)
     */
    private void loadConfig() {
        config = new Properties();
        try {
            InputStream input = getClass().getClassLoader().getResourceAsStream("env.properties");
            if (input != null) {
                config.load(input);
                input.close();
            } else {
                System.err.println("env.properties 파일을 찾을 수 없습니다.");
            }
        } catch (Exception e) {
            System.err.println("설정 파일 로드 오류: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 이메일 인증번호 발송
     * 
     * @param toEmail          수신자 이메일
     * @param verificationCode 인증번호
     * @return 발송 성공 여부
     */
    public boolean sendVerificationEmail(String toEmail, String verificationCode) {
        try {
            // SMTP 설정을 properties에서 읽어옴
            Properties props = new Properties();
            props.put("mail.smtp.host", config.getProperty("mail.smtp.host", "smtp.gmail.com"));
            props.put("mail.smtp.port", config.getProperty("mail.smtp.port", "587"));
            props.put("mail.smtp.auth", config.getProperty("mail.smtp.auth", "true"));
            props.put("mail.smtp.starttls.enable", config.getProperty("mail.smtp.starttls.enable", "true"));
            props.put("mail.smtp.ssl.trust", config.getProperty("mail.smtp.ssl.trust", "smtp.gmail.com"));

            // 이메일 계정 정보
            final String username = config.getProperty("mail.username");
            final String password = config.getProperty("mail.password");

            if (username == null || password == null) {
                System.err.println("이메일 계정 정보가 설정되지 않았습니다.");
                return false;
            }

            // 인증 정보
            Authenticator auth = new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(username, password);
                }
            };

            // 세션 생성
            Session session = Session.getInstance(props, auth);

            // 메시지 생성
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username, "Green Table"));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(toEmail));
            message.setSubject("[Green Table] 이메일 인증번호", "UTF-8");

            // 이메일 내용
            String emailContent = String.format(
                    "<html><body style='font-family: Arial, sans-serif;'>" +
                            "<div style='max-width: 600px; margin: 0 auto; padding: 20px;'>" +
                            "<h2 style='color: #28a745; text-align: center;'>🥗 Green Table 이메일 인증</h2>" +
                            "<p>안녕하세요! Green Table 회원가입을 위한 이메일 인증번호입니다.</p>" +
                            "<div style='background-color: #f8f9fa; padding: 30px; border-radius: 10px; text-align: center; margin: 20px 0;'>"
                            +
                            "<p style='margin: 0; font-size: 16px; color: #666;'>인증번호</p>" +
                            "<h1 style='color: #28a745; font-size: 36px; margin: 10px 0; letter-spacing: 5px;'>%s</h1>"
                            +
                            "</div>" +
                            "<p>위 인증번호를 회원가입 페이지에 입력해주세요.</p>" +
                            "<p style='color: #dc3545;'><strong>※ 인증번호는 5분간 유효합니다.</strong></p>" +
                            "<p>감사합니다.</p>" +
                            "<hr style='margin: 30px 0; border: none; border-top: 1px solid #eee;'>" +
                            "<p style='font-size: 12px; color: #999; text-align: center;'>" +
                            "본 메일은 발신전용입니다. 문의사항은 고객센터를 이용해주세요.<br>" +
                            "Green Table | 신선한 식재료, 건강한 한 끼" +
                            "</p>" +
                            "</div>" +
                            "</body></html>",
                    verificationCode);

            message.setContent(emailContent, "text/html; charset=UTF-8");

            // 이메일 발송
            Transport.send(message);

            System.out.println("이메일 발송 성공: " + toEmail + " -> " + verificationCode);
            return true;

        } catch (Exception e) {
            System.err.println("이메일 발송 실패: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}
