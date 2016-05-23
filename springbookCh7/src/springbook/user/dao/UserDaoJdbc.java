package springbook.user.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import springbook.user.domain.Level;
import springbook.user.domain.User;

public class UserDaoJdbc implements UserDao{
	private JdbcTemplate jdbcTemplate;	//#p260
	private DataSource dataSource;
	
	private Map<String, String> sqlMap;
	
	public void setSqlMap(Map<String, String> sqlMap) {
		this.sqlMap = sqlMap;
	}
	
	private RowMapper<User> userMapper = new RowMapper<User>(){
											public User mapRow(ResultSet rs, int rowNum)throws SQLException{
												User user = new User();
												user.setId(rs.getString("id"));
												user.setName(rs.getString("name"));
												user.setPassword(rs.getString("password"));
												user.setLevel(Level.valueOf(rs.getInt("level")));
												user.setLogin(rs.getInt("login"));
												user.setRecommend(rs.getInt("recommend"));
												user.setEmail(rs.getString("email"));
												return user;
											}
										};
	public void setDataSource(DataSource dataSource){
		this.jdbcTemplate = new JdbcTemplate(dataSource);	//#p260
		this.dataSource = dataSource;
	}

	public User get(String id){	//#p274
		return this.jdbcTemplate.queryForObject( this.sqlMap.get("get")
					, new Object[] {id}
					, this.userMapper	//#p273
				);
	}

	public List<User> getAll(){
		return this.jdbcTemplate.query(this.sqlMap.get("getAll"),
				this.userMapper	//#p273
		);
	}
	
	public void add(final User user){	// p274
		this.jdbcTemplate.update(this.sqlMap.get("add"),
				user.getId(),user.getName(),user.getPassword(),user.getEmail(),
				user.getLevel().intValue(),user.getLogin(),	user.getRecommend());
	}
	
	public void deleteAll(){
		this.jdbcTemplate.update(this.sqlMap.get("deleteAll"));	// #p261
	}
	
	public int getCount(){	//#p274
		return this.jdbcTemplate.queryForInt(this.sqlMap.get("getCount"));	//#p264
	}
	
	public void update(User user) {
		//this.jdbcTemplate.update("update users set name = ?, password = ?, level = ?, login = ?,recommend = ?, email = ?where id = ?",
		this.jdbcTemplate.update(this.sqlMap.get("update"),
				user.getName(),user.getPassword(),
				user.getLevel().intValue(), user.getLogin(),user.getRecommend(),user.getEmail(),
				user.getId()
				);
	}
	
}
