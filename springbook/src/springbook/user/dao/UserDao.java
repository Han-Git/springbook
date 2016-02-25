package springbook.user.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.dao.EmptyResultDataAccessException;

import springbook.user.domain.User;

public class UserDao {
	//private SimpleConnectionMaker simpleConnectionMaker;
	private ConnectionMaker connectionMaker;
	private Connection c;	// #7
	private User user;	// #7
	private DataSource dataSource;
	
//	public UserDao(ConnectionMaker connectionMaker) {
//	//public UserDao(ConnectionMaker connectionMaker) {
//		//simpleConnectionMaker = new SimpleConnectionMaker();	//#2
//		//connectionMaker = new DConnectionMaker();	//#3
//		this.connectionMaker = connectionMaker;	//#4
//	}
	
//	public UserDao(){
////		DaoFactory daoFactory = new DaoFactory();	// #8
////		this.connectionMaker = daoFactory.connectionMaker();	// #8
//		
//		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(DaoFactory.class);	// #9
//		this.connectionMaker = context.getBean("connectionMaker", ConnectionMaker.class);	// #9
//	}
	
	// #p137
	public void setDataSource(DataSource dataSource){
		this.dataSource = dataSource;
	}
	
	// #p127
	public void setConnectionMaker(ConnectionMaker connectionMaker){
		this.connectionMaker = connectionMaker;
	}
	
	public void add(User user) throws ClassNotFoundException, SQLException{
		//Connection c = simpleConnectionMaker.makeNewConnection();	//#2
		//Connection c = connectionMaker.makeConnection();	// #7
		Connection c = dataSource.getConnection();	// #137
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
		//this.c = connectionMaker.makeConnection();	// #7
		this.c = dataSource.getConnection();	// #137
		PreparedStatement ps = c.prepareStatement("select * from users where id = ?");
		ps.setString(1, id);
		ResultSet rs = ps.executeQuery();
//		rs.next();
//		User user = new User();	//#4
//		user.setId(rs.getString("id"));
//		user.setName(rs.getString("name"));
//		user.setPassword(rs.getString("password"));
		
		/*
		this.user = new User();	// #7
		this.user.setId(rs.getString("id"));
		this.user.setName(rs.getString("name"));
		this.user.setPassword(rs.getString("password"));
		*/
		
		User user = null;	//#p173
		if(rs.next()){
			user = new User();	//#p173
			user.setId(rs.getString("id"));	//#p173
			user.setName(rs.getString("name"));	//#p173
			user.setPassword(rs.getString("password"));	//#p173
		}
		
		rs.close();
		ps.close();
		c.close();
		
		if(user == null){
			throw new EmptyResultDataAccessException(1);
		}
		//return this.user;
		return user; 	//#p173
	}
	
	public void deleteAll() throws SQLException{
		Connection c = dataSource.getConnection();	//#p164
		
		PreparedStatement ps = c.prepareStatement("delete from users");	//#p164
		ps.executeUpdate();	//#p164
		
		ps.close();	//#p164
		c.close();	//#p164
	}
	
	public int getCount() throws SQLException{
		Connection c = dataSource.getConnection();
		
		PreparedStatement ps = c.prepareStatement("select count(*) from users");
		
		ResultSet rs = ps.executeQuery();
		rs.next();
		int count = rs.getInt(1);
		
		rs.close();
		ps.close();
		c.close();
		
		return count;
	}
}
