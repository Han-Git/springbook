package springbook.user.service;

import java.util.List;

import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;

import springbook.user.dao.UserDao;
import springbook.user.domain.Level;
import springbook.user.domain.User;

public class UserServiceImpl {
	public static final int MIN_LOGCOUNT_FOR_SILVER = 50;
	public static final int MIN_RECCOMEND_FOR_GOLD = 30;
	
	UserDao userDao;
	private MailSender mailSender;
	
	public void setMailSender(MailSender mailSender){
		this.mailSender = mailSender;
	}
	
	public void setUserDao(UserDao userDao){
		this.userDao = userDao;
	}
	
	public void upgradeLevels() throws Exception{
		try{
			List<User> users = userDao.getAll();
			for(User user : users){
				if(canUpgradeLevel(user)){
					upgradeLevel(user);
				}
			}
		}catch(RuntimeException e){
			throw e;
		}
	}
	
	private boolean canUpgradeLevel(User user){
		Level currentLevel = user.getLevel();
		switch(currentLevel){
			case BASIC: return (user.getLogin() >= MIN_LOGCOUNT_FOR_SILVER);
			case SILVER: return (user.getRecommend() >= MIN_RECCOMEND_FOR_GOLD);
			case GOLD: return false;
			default: throw new IllegalArgumentException("Unknown Level: "+currentLevel);
		}
	}
	
	protected void upgradeLevel(User user){
		user.upgradeLevel();
		userDao.update(user);
		sendUpgradeEMail(user);
	}
	
	private void sendUpgradeEMail(User user) {

		SimpleMailMessage mailMessage = new SimpleMailMessage();
		mailMessage.setTo(user.getEmail());
		mailMessage.setFrom("useradmin@ksug.org");
		mailMessage.setSubject("Upgrade �ȳ�");
		mailMessage.setText("������� ����� "+user.getLevel().name()+ "�� ���׷��̵� �Ǿ����ϴ�.");
		
		mailSender.send(mailMessage);
	}

	public void add(User user){
		if(user.getLevel() == null){
			user.setLevel(Level.BASIC);
		}
		userDao.add(user);
	}
}
