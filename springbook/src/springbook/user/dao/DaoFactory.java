package springbook.user.dao;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;

@Configuration
public class DaoFactory {
	
	@Bean
	public UserDao userDao(){
		//ConnectionMaker connectionMaker = new DConnectionMaker();	//#5
		//UserDao userDao = new UserDao(connectionMaker);
		//return userDao;
		//return new UserDao(new DConnectionMaker());	// #6
		//return new UserDao(connectionMaker());	//#124
		//return new UserDao();	// #8
		UserDao userDao = new UserDao();
		//userDao.setConnectionMaker(connectionMaker());	// #p138
		userDao.setDataSource(dataSource());	// #p138
		return userDao;
	}
	
	@Bean
	public DataSource dataSource(){	// p138
		SimpleDriverDataSource dataSource = new SimpleDriverDataSource();	// p138	
		
		dataSource.setDriverClass(com.mysql.jdbc.Driver.class); 	// p138
		dataSource.setUrl("jdbc:mysql://localhost:3306/springbook");	// p138
		dataSource.setUsername("jmh");	// p138
		dataSource.setPassword("jmh");	// p138
		
		return dataSource;	// p138
	}
	
	/* p138	
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
	*/
	
}
