package springbook.user.dao;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.jdbc.support.SQLErrorCodeSQLExceptionTranslator;
import org.springframework.jdbc.support.SQLExceptionTranslator;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import springbook.user.domain.User;


@RunWith(SpringJUnit4ClassRunner.class)	// #p185
//@ContextConfiguration(locations="/applicationContext.xml")	// #p185
@ContextConfiguration(locations="/test-applicationContext.xml")	// #p194
public class UserDaoTest {
	
	@Autowired
	private UserDao dao;	//#p188
	@Autowired
	private DataSource dataSource;
	
	private User user1;	//#p183
	private User user2;	//#p183
	private User user3;	//#p183
	
	@Before
	public void setUp(){
		ApplicationContext context = new GenericXmlApplicationContext("test-applicationContext.xml");	// #p180
		this.dao = context.getBean("userDao", UserDaoJdbc.class);	// #p180
		DataSource dataSource = new SingleConnectionDataSource("jdbc:mysql://localhost:3306/testdb", "jmh","jmh", true);
		//dao.setDataSource(dataSource);
		this.user1 = new User("jkonoury", "장지홍","springno1");	//#p183
		this.user2 = new User("jmh", "정명한","springno2");	//#p183
		this.user3 = new User("lyg", "이용규","springno3");	//#p183
	}
	
	@Test
	public void count() throws SQLException, ClassNotFoundException{
		dao.deleteAll();
		assertThat(dao.getCount(),is(0));	// #p167
		
		dao.add(user1);	// #p169
		assertThat(dao.getCount(),is(1));	// #p167
		dao.add(user2);	// #p169
		assertThat(dao.getCount(),is(2));	// #p169
		dao.add(user3);	// #p169
		assertThat(dao.getCount(),is(3));	// #p169
	}
	
	@Test
	public void addAndGet() throws SQLException, ClassNotFoundException{
		dao.deleteAll();	//#171
		assertThat(dao.getCount(),is(0));	// #p167
		
		dao.add(user1);
		dao.add(user2);
		assertThat(dao.getCount(),is(2));
		
		User userget1 = dao.get(user1.getId());
		assertThat(userget1.getName(), is(user1.getName()));
		assertThat(userget1.getPassword(), is(user1.getPassword()));
		
		User userget2 = dao.get(user2.getId());
		assertThat(userget2.getName(), is(user2.getName()));
		assertThat(userget2.getPassword(), is(user2.getPassword()));
		
	}
	
	@Test(expected=EmptyResultDataAccessException.class)
	public void getUserFailure() throws SQLException, ClassNotFoundException{
		dao.deleteAll();
		assertThat(dao.getCount(),is(0));
		
		dao.get("unknown_id");
	}
	
	@Test
	public void getAll(){
		dao.deleteAll();
		
		List<User> users0 = dao.getAll();
		assertThat(users0.size(), is(0));
		
		dao.add(user1);	// id : gyumee
		List<User> users1 = dao.getAll();
		assertThat(users1.size(), is(1));
		checkSameUser(user1, users1.get(0));
		
		dao.add(user2);;	// id :jmh
		List<User> users2 = dao.getAll();
		assertThat(users2.size(), is(2));
		checkSameUser(user1, users2.get(0));
		checkSameUser(user2, users2.get(1));
		
		dao.add(user3);;	// id :lyg
		List<User> users3 = dao.getAll();
		assertThat(users3.size(), is(3));
		checkSameUser(user1, users3.get(0));
		checkSameUser(user2, users3.get(1));
		checkSameUser(user3, users3.get(2));
		
	}
	
	private void checkSameUser(User user1, User user2){
		assertThat(user1.getId(),is(user2.getId()));
		assertThat(user1.getName(),is(user2.getName()));
		assertThat(user1.getPassword(),is(user2.getPassword()));
	}
	
	@Test
	public void sqlExceptionTranslate(){	//p314
		dao.deleteAll();
		try{
			dao.add(user1);
			dao.add(user1);
		}catch(DuplicateKeyException ex){
			SQLException sqlEx = (SQLException) ex.getRootCause();
			SQLExceptionTranslator set = new SQLErrorCodeSQLExceptionTranslator(this.dataSource);
			assertThat(set.translate(null, null, sqlEx),is(DuplicateKeyException.class));
		}
	}
	
	public static void main(String[] args)throws ClassNotFoundException, SQLException {
		JUnitCore.main("springbook.user.dao.UserDaoTest");
	}
}
