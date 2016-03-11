package springbook.user.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import springbook.user.domain.User;

public class UserDao {
	private JdbcTemplate jdbcTemplate;	//#p260
	
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
	
}
