package springbook.user.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import springbook.user.domain.Level;
import springbook.user.domain.User;

public class UserDaoJdbc implements UserDao{
	private JdbcTemplate jdbcTemplate;	//#p260
	private DataSource dataSource;
	private String sqlAdd;
	private String sqlGet;
	private String sqlGetAll;
	private String sqlDeleteAll;
	private String sqlGetCount;
	private String sqlUpdate;
	
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
		//return this.jdbcTemplate.queryForObject("select * from users where id = ?"
		return this.jdbcTemplate.queryForObject( this.sqlGet
					, new Object[] {id}
					, this.userMapper	//#p273
				);
	}

	public List<User> getAll(){
		//return this.jdbcTemplate.query("select * from users order by id",
		return this.jdbcTemplate.query(this.sqlGetAll,
				this.userMapper	//#p273
		);
	}
	
	public void add(final User user){	// p274
		//this.jdbcTemplate.update("insert into users (id,name,password,level,login,recommend,email) values(?,?,?,?,?,?,?)",user.getId(),user.getName(),user.getPassword(),user.getLevel().intValue(),user.getLogin(),user.getRecommend(),user.getEmail());	//#p262
		this.jdbcTemplate.update(this.sqlAdd,
				user.getId(),user.getName(),user.getPassword(),user.getEmail(),
				user.getLevel().intValue(),user.getLogin(),	user.getRecommend());
	}
	
	public void deleteAll(){
		//this.jdbcTemplate.update("delete from users");	// #p261
		this.jdbcTemplate.update(this.sqlDeleteAll);	// #p261
	}
	
	public int getCount(){	//#p274
		//return this.jdbcTemplate.queryForInt("select count(*) from users");	//#p264
		return this.jdbcTemplate.queryForInt(this.sqlGetCount);	//#p264
	}
	
	public void update(User user) {
		//this.jdbcTemplate.update("update users set name = ?, password = ?, level = ?, login = ?,recommend = ?, email = ?where id = ?",
		this.jdbcTemplate.update(this.sqlUpdate,
				user.getName(),user.getPassword(),
				user.getLevel().intValue(), user.getLogin(),user.getRecommend(),user.getEmail(),
				user.getId()
				);
	}
	
	public void setSqlAdd(String sqlAdd){
		this.sqlAdd = sqlAdd;
	}

	public void setSqlGet(String sqlGet) {
		this.sqlGet = sqlGet;
	}

	public void setSqlGetAll(String sqlGetAll) {
		this.sqlGetAll = sqlGetAll;
	}

	public void setSqlDeleteAll(String sqlDeleteAll) {
		this.sqlDeleteAll = sqlDeleteAll;
	}

	public void setSqlGetCount(String sqlGetCount) {
		this.sqlGetCount = sqlGetCount;
	}

	public void setSqlUpdate(String sqlUpdate) {
		this.sqlUpdate = sqlUpdate;
	}

}
