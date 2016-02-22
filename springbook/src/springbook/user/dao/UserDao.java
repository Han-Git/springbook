package springbook.user.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import springbook.user.domain.User;

public class UserDao {
	//private SimpleConnectionMaker simpleConnectionMaker;
	private ConnectionMaker connectionMaker;
	private Connection c;	// #7
	private User user;	// #7
//	
//	public UserDao(ConnectionMaker connectionMaker) {
//	//public UserDao(ConnectionMaker connectionMaker) {
//		//simpleConnectionMaker = new SimpleConnectionMaker();	//#2
//		//connectionMaker = new DConnectionMaker();	//#3
//		this.connectionMaker = connectionMaker;	//#4
//	}
//	
	public UserDao(){
//		DaoFactory daoFactory = new DaoFactory();	// #8
//		this.connectionMaker = daoFactory.connectionMaker();	// #8
		
		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(DaoFactory.class);	// #9
		this.connectionMaker = context.getBean("connectionMaker", ConnectionMaker.class);	// #9
	}
	
	public void add(User user) throws ClassNotFoundException, SQLException{
		//Connection c = simpleConnectionMaker.makeNewConnection();	//#2
		Connection c = connectionMaker.makeConnection();
		PreparedStatement ps = c.prepareStatement("insert into users(id, name, password) values(?,?,?)");
		ps.setString(1, user.getId());
		ps.setString(2, user.getName());
		ps.setString(3, user.getPassword());
		
		ps.executeUpdate();
		
		ps.close();
		c.close();
	}
	
	public User get(String id) throws ClassNotFoundException, SQLException{
		//Connection c = simpleConnectionMaker.makeNewConnection();	//#2
		//Connection c = connectionMaker.makeConnection();	// #4
		this.c = connectionMaker.makeConnection();	// #7
		PreparedStatement ps = c.prepareStatement("select * from users where id = ?");
		ps.setString(1, id);
		ResultSet rs = ps.executeQuery();
		rs.next();
//		User user = new User();	//#4
//		user.setId(rs.getString("id"));
//		user.setName(rs.getString("name"));
//		user.setPassword(rs.getString("password"));
		
		this.user = new User();	// #7
		this.user.setId(rs.getString("id"));
		this.user.setName(rs.getString("name"));
		this.user.setPassword(rs.getString("password"));
		
		
		rs.close();
		ps.close();
		c.close();
		return this.user;
	}
	
}
