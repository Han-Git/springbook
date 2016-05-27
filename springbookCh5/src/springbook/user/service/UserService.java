package springbook.user.service;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import springbook.user.dao.UserDao;
import springbook.user.domain.Level;
import springbook.user.domain.User;

public class UserService {
	public static final int MIN_LOGCOUNT_FOR_SILVER = 50;
	public static final int MIN_RECCOMEND_FOR_GOLD = 30;
	
	UserDao userDao;
	// private DataSource dataSource;	// p374
	private PlatformTransactionManager transactionManager;
	
	private MailSender mailSender;
	
	public void setMailSender(MailSender mailSender){
		this.mailSender = mailSender;
	}
	
	public void setTransactionManager(PlatformTransactionManager transactionManager){
		this.transactionManager = transactionManager;
	}
	
	public void setUserDao(UserDao userDao){
		this.userDao = userDao;
	}
	
//	public void setDataSource(DataSource dataSource){	// p374
//		this.dataSource = dataSource;	// p374
//	}	// p374

	// p363
//	public void upgradeLevels(){
//		/* P338 removed because of Refactoring
//		List<User> users = userDao.getAll();
//		for(User user : users){
//			Boolean changed = null;
//			if(user.getLevel()== Level.BASIC && user.getLogin() >=50){
//				user.setLevel(Level.SILVER);
//				changed = true;
//			}
//			else if(user.getLevel()==Level.SILVER && user.getRecommend()>=30){
//				user.setLevel(Level.GOLD);
//				changed = true;
//			}else if(user.getLevel()== Level.GOLD){
//				changed = false;
//			}else{
//				changed = false;
//			}
//			if(changed){
//				userDao.update(user);
//			}
//		}
//		*/
//		
//		List<User> users = userDao.getAll();
//		for(User user : users){
//			if(canUpgradeLevel(user)){
//				upgradeLevel(user);
//			}
//		}
//	}
	
	/* p370 removed because of abstract transaction
	public void upgradeLevels() throws Exception{
		TransactionSynchronizationManager.initSynchronization();
		Connection c = DataSourceUtils.getConnection(dataSource);
		c.setAutoCommit(false);
		
		try{
			List<User> users = userDao.getAll();
			for(User user : users){
				if(canUpgradeLevel(user)){
					upgradeLevel(user);
				}
			}
			c.commit();
		}catch(Exception e){
			c.rollback();
			throw e;
		}finally{
			DataSourceUtils.releaseConnection(c, dataSource);
			TransactionSynchronizationManager.unbindResource(this.dataSource);
			TransactionSynchronizationManager.clearSynchronization();
		}
	}
	*/
	
	public void upgradeLevels() throws Exception{
		//PlatformTransactionManager transactionManager = new DataSourceTransactionManager(dataSource);
		//TransactionStatus status = transactionManager.getTransaction(new DefaultTransactionDefinition());
		
		TransactionStatus status = this.transactionManager.getTransaction(new DefaultTransactionDefinition());
		
		try{
			List<User> users = userDao.getAll();
			for(User user : users){
				if(canUpgradeLevel(user)){
					upgradeLevel(user);
				}
			}
			//transactionManager.commit(status);
			this.transactionManager.commit(status);
		}catch(RuntimeException e){
			//transactionManager.rollback(status);
			this.transactionManager.rollback(status);
			throw e;
		}
	}
	
	private boolean canUpgradeLevel(User user){
		Level currentLevel = user.getLevel();
		switch(currentLevel){
			//case BASIC: return (user.getLogin() >= 50);	//p347
			//case SILVER: return (user.getRecommend() >= 30);	//p347
			case BASIC: return (user.getLogin() >= MIN_LOGCOUNT_FOR_SILVER);
			case SILVER: return (user.getRecommend() >= MIN_RECCOMEND_FOR_GOLD);
			case GOLD: return false;
			default: throw new IllegalArgumentException("Unknown Level: "+currentLevel);
		}
	}
	
	//private void upgradeLevel(User user){	// #p350 
	protected void upgradeLevel(User user){
		/* p343 refactoring
		if(user.getLevel()== Level.BASIC){
			user.setLevel(Level.SILVER);
		}else if(user.getLevel()== Level.SILVER){
			user.setLevel(Level.GOLD);
		}
		*/
		user.upgradeLevel();
		userDao.update(user);
		sendUpgradeEMail(user);
	}
	
	private void sendUpgradeEMail(User user) {

		//p387 removed because of DI
//		JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
//		mailSender.setHost("mail.server.com");
		
		SimpleMailMessage mailMessage = new SimpleMailMessage();
		mailMessage.setTo(user.getEmail());
		mailMessage.setFrom("useradmin@ksug.org");
		mailMessage.setSubject("Upgrade 안내");
		mailMessage.setText("사용자의 등급이 "+user.getLevel().name()+ "로 업그레이드 되었습니다.");
		
		mailSender.send(mailMessage);
		
		/*	//p385 
		Properties props = new Properties();
		props.put("mail.smtp.host", "mail.ksug.org");
		Session s = Session.getInstance(props, null);
		
		MimeMessage message = new MimeMessage(s);
		
		try{
			message.setFrom(new InternetAddress("useradmin@ksug.org"));
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(user.getEmail()));
			message.setSubject("Upgrade 안내");
			message.setText("사용자님의 등급이 "+ user.getLevel().name()+ "로 업그레이드 되었습니다.");
			
			Transport.send(message);
		}catch(AddressException e){
			throw new RuntimeException(e);
		}catch(MessagingException e){
			throw new RuntimeException(e);
		}
		*/
	}

	public void add(User user){
		if(user.getLevel() == null){
			user.setLevel(Level.BASIC);
		}
		userDao.add(user);
	}
}
