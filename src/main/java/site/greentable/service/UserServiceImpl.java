package site.greentable.service;

import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.SQLException;
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
import site.greentable.dto.UserInfoDTO;
import site.greentable.exception.AddException;
import site.greentable.exception.EmailVerifyException;
import site.greentable.exception.NotFoundException;
import site.greentable.exception.ServerException;
import site.greentable.util.DbUtil;
import site.greentable.util.Env;

public class UserServiceImpl implements UserService {

	private UserDAO userDao = new UserDAOImpl();

	@Override
	public UserDTO login(String userEmail, String userPwd) throws NotFoundException, ServerException {
		UserDTO userDto = userDao.selectUserByEmail(userEmail, userPwd);
		if (userDto != null) {
			UserInfoDTO userInfo = userDao.findUserInfoByUserId(userDto.getUserId());
	        userDto.setUserInfoDto(userInfo); 
		} else
			throw new NotFoundException("<script>alert('아이디 혹은 비밀번호가 틀렸습니다');history.back()</script>");

		return userDto;
	}

	@Override
	public void register(UserDTO userDto) throws AddException {
	    Connection con = null;
	    System.out.println("==== register 진입 ====");

	    try {
	        con = DbUtil.getConnection();
	        con.setAutoCommit(false); // 트랜잭션 시작
	        
	        System.out.println("*************** con 객체 상태: " + con);

	        UserDAO userDAO = new UserDAOImpl();

	     // 1. 기본 사용자 정보 insert -> user_id 생성
	        int userId = userDAO.insertUser(userDto, con);

	        // 2. userInfoDto 가져와서 userId 세팅
	        UserInfoDTO userInfoDto = userDto.getUserInfoDto();
	        if (userInfoDto == null) {
	        	throw new AddException("UserInfoDTO가 null입니다. 회원 상세정보가 필요합니다.");
	        }
	        userInfoDto.setUserId(userId);

	        // 3. 상세 정보 insert
	        userDAO.insertUserInfo(userInfoDto, con);

	        con.commit();

	    } catch (Exception e) {
	        e.printStackTrace();
	        if (con != null) {
	            try {
	                con.rollback();
	                System.out.println("회원가입 트랜잭션 롤백");
	            } catch (SQLException se) {
	                se.printStackTrace();
	            }
	        }
	        throw new AddException("회원가입 실패: " + e.getMessage());
	    } finally {
	        DbUtil.dbClose(con, null);
	        System.out.println("DB 연결 종료");
	    }
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
		UserDTO dto = userDao.selectUserByName(name, phone);
        return dto.getEmail();
	}

	@Override
	public void findUserPwd(String name, String email) throws NotFoundException {
		UserDTO dto = userDao.selectUserByEmailAndName(email, name);
        if (dto == null) {
            throw new NotFoundException("해당 사용자 정보를 찾을 수 없습니다.");
        }

        // 보안상 실제 비밀번호를 보내면 안되므로, 비밀번호 재설정 링크를 이메일로 보내는 방식 권장
        // 여기에 이메일 발송 로직이 들어갈 수 있음

	}

	@Override
	public UserDTO loginKakao(String code) throws AddException {
		// 걷어내기
		return null;
	}

	@Override
	public void kakaoJoin(UserDTO userDto) throws AddException {
		// 걷어내기

	}

	@Override
	public UserDTO loginGoogle(String code) throws AddException {
		// 걷어내기
		return null;
	}

	@Override
	public void googleJoin(UserDTO userDto) throws AddException {
		// 걷어내기

	}

	@Override
	public UserInfoDTO getUserInfoByUserId(int userId) {
		return userDao.findUserInfoByUserId(userId);
	}

}
