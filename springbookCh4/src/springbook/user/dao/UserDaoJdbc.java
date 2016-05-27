package springbook.user.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import com.mysql.jdbc.MysqlErrorNumbers;

import springbook.user.domain.User;

public class UserDaoJdbc implements UserDao{
	private JdbcTemplate jdbcTemplate;	//#p260
	private DataSource dataSource;
	
	private RowMapper<User> userMapper = new RowMapper<User>(){
											public User mapRow(ResultSet rs, int rowNum)throws SQLException{
												User user = new User();
												user.setId(rs.getString("id"));
												user.setName(rs.getString("name"));
												user.setPassword(rs.getString("password"));
												return user;
											}
										};
	public void setDataSource(DataSource dataSource){
		this.jdbcTemplate = new JdbcTemplate(dataSource);	//#p260
		this.dataSource = dataSource;
	}

	public User get(String id){	//#p274
		return this.jdbcTemplate.queryForObject("select * from users where id = ?"
					, new Object[] {id}
					, this.userMapper	//#p273
				);
	}

	public List<User> getAll(){
		return this.jdbcTemplate.query("select * from users order by id",
				this.userMapper	//#p273
		);
	}
	
	public void add(final User user){	// p274
		this.jdbcTemplate.update("insert into users (id,name,password) values(?,?,?)",user.getId(),user.getName(),user.getPassword());	//#p262
	}
	
	public void deleteAll(){
		this.jdbcTemplate.update("delete from users");	// #p261
	}
	
	public int getCount(){	//#p274
		return this.jdbcTemplate.queryForInt("select count(*) from users");	//#p264
	}
	
	
	public void addExceptionTest() throws DuplicateUserIdException{
		try{
			Connection c = dataSource.getConnection();	// #137
			PreparedStatement ps = c.prepareStatement("insert into users(id, name, password) values(?,?,?)");
			ps.setString(1, "jmh");
			ps.setString(2, "정명한");
			ps.setString(3, "springno2");
			
			ps.executeUpdate();
			
			ps.setString(1, "jmh");
			ps.setString(2, "정명한");
			ps.setString(3, "springno2");
			
			ps.executeUpdate();
			
			ps.close();
			c.close();
		}catch(SQLException e){
			if(e.getErrorCode()== MysqlErrorNumbers.ER_DUP_ENTRY){
				throw new DuplicateUserIdException(e);	// 에외 전환
			}else{
				throw new RuntimeException(e);	// 예외 포장
			}
		}
	}
	

	public void addTest(User user) throws DuplicateKeyException{	// p302
		try{
			this.jdbcTemplate.update("insert into users (id,name,password) values(?,?,?)",user.getId(),user.getName(),user.getPassword());	//#p262
		}catch(DuplicateKeyException e){
			throw new DuplicateUserIdException(e);
		}
		
	}
	
}
