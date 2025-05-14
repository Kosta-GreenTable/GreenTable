package site.greentable.service;

import java.security.SecureRandom;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import site.greentable.dao.UserDAO;
import site.greentable.dao.UserDAOImpl;
import site.greentable.dto.UserDTO;
import site.greentable.exception.AddException;
import site.greentable.exception.EmailVerifyException;
import site.greentable.exception.NotFoundException;
import site.greentable.exception.ServerException;
import site.greentable.util.Env;

public class UserServiceImpl implements UserService {

	private UserDAO userDao = new UserDAOImpl();

	@Override
	public UserDTO login(String userEmail, String userPwd) throws NotFoundException, ServerException {
		UserDTO userDto = userDao.selectUserByEmail(userEmail, userPwd);
		if (userDto == null)
			throw new NotFoundException("<script>alert('아이디 혹은 비밀번호가 틀렸습니다');history.back()</script>");

		return userDto;
	}

	@Override
	public void register(UserDTO userDto) throws AddException {
		// TODO Auto-generated method stub

	}

	@Override
	public String verifyEmail(String email) throws EmailVerifyException {
		Properties props = new Properties();
		props.put("mail.smtp.host", Env.pr.getProperty("mail.smtp.host"));
		props.put("mail.smtp.port", Env.pr.getProperty("mail.smtp.port"));
		props.put("mail.smtp.auth", Env.pr.getProperty("mail.smtp.auth"));
		props.put("mail.smtp.starttls.enable", Env.pr.getProperty("mail.smtp.starttls.enable"));
		
		
		Session session = Session.getInstance(props,
			    new Authenticator() {
			        protected PasswordAuthentication getPasswordAuthentication() {
			            return new PasswordAuthentication(Env.pr.getProperty("verifyEmail"), Env.pr.getProperty("verifyEmailPassword"));
			        }
			    });
		try {
			 SecureRandom random = new SecureRandom();
		     int number = random.nextInt(1000000); // 0 ~ 999999
		     String verifyCode = String.format("%06d", number);
			
		    Message message = new MimeMessage(session);
		    message.setFrom(new InternetAddress(Env.pr.getProperty("verifyEmail")));
		    message.setRecipients(
		        Message.RecipientType.TO,
		        InternetAddress.parse(email)
		    );
		    message.setSubject("[greentable] 회원 가입 인증 번호");
		    message.setText("회원가입 인증번호["+verifyCode+"]");

		    Transport.send(message);
		    System.out.println("이메일 전송 성공");
		    return verifyCode;
		} catch (MessagingException e) {
		    e.printStackTrace();
		    throw new EmailVerifyException("이메일 전송에 실패하였습니다");
		}
	}

	@Override
	public String findUserEmail(String name, String phone) throws NotFoundException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void findUserPwd(String name, String email) throws NotFoundException {
		// TODO Auto-generated method stub

	}

	@Override
	public UserDTO loginKakao(String code) throws AddException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void kakaoJoin(UserDTO userDto) throws AddException {
		// TODO Auto-generated method stub

	}

	@Override
	public UserDTO loginGoogle(String code) throws AddException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void googleJoin(UserDTO userDto) throws AddException {
		// TODO Auto-generated method stub

	}

}
