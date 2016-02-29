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
	
	//public void add(User user) throws ClassNotFoundException, SQLException{
	public void add(final User user) throws ClassNotFoundException, SQLException{	// p229
		//Connection c = simpleConnectionMaker.makeNewConnection();	//#2
		//Connection c = connectionMaker.makeConnection();	// #7
		/*
		Connection c = dataSource.getConnection();	// #137
		PreparedStatement ps = c.prepareStatement("insert into users(id, name, password) values(?,?,?)");
		ps.setString(1, user.getId());
		ps.setString(2, user.getName());
		ps.setString(3, user.getPassword());
		
		ps.executeUpdate();
		
		ps.close();
		c.close();
		*/
		
		class AddStatement implements StatementStrategy{
			/*
			User user;	//#p227
			
			public AddStatement(User user) {
				this.user = user;	//#p227
			}
			*/
			public PreparedStatement makePreparedStatement(Connection c) throws SQLException {
				PreparedStatement ps = c.prepareStatement("insert into users(id, name, password) values(?,?,?)");	//#p227
				ps.setString(1, user.getId());	//#p227
				ps.setString(1, user.getName());	//#p227
				ps.setString(1, user.getPassword());	//#p227
				
				return ps;	//#p227
			}
		}
		
//		StatementStrategy st = new AddStatement(user);	// #p226
		StatementStrategy st = new AddStatement();	// #p229
		jdbcContextWithStatementStrategy(st);	// #p226
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
		
//		this.user = new User();	// #7
//		this.user.setId(rs.getString("id"));
//		this.user.setName(rs.getString("name"));
//		this.user.setPassword(rs.getString("password"));
		
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
		/*
		Connection c = dataSource.getConnection();	//#p164
		
		PreparedStatement ps = c.prepareStatement("delete from users");	//#p164
		ps.executeUpdate();	//#p164
		
		ps.close();	//#p164
		c.close();	//#p164
		*/
		
		/*
		Connection c = null;	//#p211
		PreparedStatement ps = null;	//#p211
		
		try{
			c = dataSource.getConnection();	//#p211
			ps = c.prepareStatement("delete from users");	//#p211
			ps.executeUpdate();	//#p211
		}catch(SQLException e){
			throw e;	//#p211
		}finally {
			if(ps != null){
				try{
					ps.close();	//#p211
				}catch(SQLException e){
					
				}
			}
			if(c != null){
				try{
					c.close();	//#p211
				}catch(SQLException e){
					
				}
			}
		}
		*/
		
		StatementStrategy st = new DeleteAllStatement();
		jdbcContextWithStatementStrategy(st);
	}
	
	public int getCount() throws SQLException{
		Connection c = null;	//#p213
		PreparedStatement ps = null;	//#p213
		ResultSet rs = null;	//#p213
		
		try{
			c = dataSource.getConnection();	//#p213
			
			ps = c.prepareStatement("select count(*) from users");	//#p213
			
			rs = ps.executeQuery();	//#p213
			rs.next();	//#p213
			return rs.getInt(1);	//#p213
			
		}catch(SQLException e){
			throw e;	//#p213
		}finally {
			if(rs != null){
				try{
					rs.close();
				}catch(SQLException e){
					
				}
			}
			if(ps != null){
				try{
					ps.close();	//#p213
				}catch(SQLException e){
					
				}
			}
			if(c != null){
				try{
					c.close();	//#p213
				}catch(SQLException e){
					
				}
			}
		}
		
		/*
		Connection c = dataSource.getConnection();
		
		PreparedStatement ps = c.prepareStatement("select count(*) from users");
		
		ResultSet rs = ps.executeQuery();
		rs.next();
		int count = rs.getInt(1);
		
		rs.close();
		ps.close();
		c.close();
		
		return count;
		*/
	}
	
	public void jdbcContextWithStatementStrategy(StatementStrategy stmt) throws SQLException{
		Connection c = null;	//#223
		PreparedStatement ps = null;	//#223
		
		try{
			c = dataSource.getConnection();	//#223
			ps = stmt.makePreparedStatement(c);	//#223
			
			ps.executeUpdate();	//#223
		}catch(SQLException e){
			throw e;	//#223
		}finally {
			if(ps != null){
				try{
					ps.close();	//#223
				}catch(SQLException e){
					
				}
			}
			if(c != null){
				try{
					c.close();	//#223
				}catch(SQLException e){
					
				}
			}
		}
	}
}
