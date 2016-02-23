package springbook.user.dao;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DaoFactory {
	
	@Bean
	public UserDao userDao(){
		//ConnectionMaker connectionMaker = new DConnectionMaker();	//#5
		//UserDao userDao = new UserDao(connectionMaker);
		//return userDao;
		//return new UserDao(new DConnectionMaker());	// #6
		return new UserDao(connectionMaker());
		//return new UserDao();	// #8
	}
	
	@Bean
	public AccountDao accountDao(){
		//return new AccountDao(new DConnectionMaker());	// #6
		return new AccountDao(connectionMaker());	// #6
	}
	
	@Bean
	public MessageDao messageDao(){
		//return new MessageDao(new DConnectionMaker());	// #6
		return new MessageDao(connectionMaker());	// #6
	}
	
	@Bean
	public ConnectionMaker connectionMaker(){
		return new DConnectionMaker();
	}
}
