package springbook.user.dao;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.sql.SQLException;

import javax.sql.DataSource;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import springbook.user.domain.User;


@RunWith(SpringJUnit4ClassRunner.class)	// #p185
//@ContextConfiguration(locations="/applicationContext.xml")	// #p185
@ContextConfiguration(locations="/test-applicationContext.xml")	// #p194
//@DirtiesContext		//#p191
public class UserDaoTest {
	
	@Autowired
	UserDao dao;	//#p188
//	private ApplicationContext context;	//#p185
	
	//private UserDao dao;	// #p180
	private User user1;	//#p183
	private User user2;	//#p183
	private User user3;	//#p183
	
	@Before
	public void setUp(){
//		ApplicationContext context = new GenericXmlApplicationContext("applicationContext.xml");	// #p180
//		this.dao = context.getBean("userDao", UserDao.class);	// #p180
//		DataSource dataSource = new SingleConnectionDataSource("jdbc:mysql://localhost:3306/testdb", "jmh","jmh", true);
//		dao.setDataSource(dataSource);
		this.user1 = new User("gyumee", "박성철","springno1");	//#p183
		this.user2 = new User("jhm", "정명한","springno2");	//#p183
		this.user3 = new User("lyg", "이용규","springno3");	//#p183
	}
	
	@Test
	public void count() throws SQLException, ClassNotFoundException{
//		ApplicationContext context = new GenericXmlApplicationContext("applicationContext.xml");	// #p169
//		UserDao dao = context.getBean("userDao", UserDao.class);	// #p169
//		User user1 = new User("gyumee", "박성철","springno1");
//		User user2 = new User("jhm", "정명한","springno2");
//		User user3 = new User("lyg", "이용규","springno3");
		
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
//		ApplicationContext context = new GenericXmlApplicationContext("applicationContext.xml");	// #p158
//		UserDao dao = context.getBean("userDao", UserDao.class);	// #p158
	
//		User user = new User();	// #p158
//		user.setId("gyumee");	// #p158
//		user.setName("박성철");	// #p158
//		user.setPassword("springno1");	// #p158
		
//		User user1 = new User("gyumee", "박성철","springno1");		//#171
//		User user2 = new User("leegw700", "이길원","springno2");	//#171
		
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
		
		/*
		dao.add(user);	// #p158
		
		User user2 = dao.get(user.getId());	// #p158
		
		assertThat(user2.getName(), is(user.getName()));	// #p158
		assertThat(user2.getPassword(), is(user.getPassword()));	// #p158
		*/
	}
	
	@Test(expected=EmptyResultDataAccessException.class)
	public void getUserFailure() throws SQLException, ClassNotFoundException{
//		ApplicationContext context = new GenericXmlApplicationContext("applicationContext.xml");
//		UserDao dao = context.getBean("userDao",UserDao.class);
		
		dao.deleteAll();
		assertThat(dao.getCount(),is(0));
		
		dao.get("unknown_id");
	}
	
	public static void main(String[] args)throws ClassNotFoundException, SQLException {
		
		JUnitCore.main("springbook.user.dao.UserDaoTest");
		/*	#p160
		//ConnectionMaker connectionMaker = new DConnectionMaker();	//#4
		//UserDao dao = new UserDao(connectionMaker);	// #4
		
		//UserDao dao = new DaoFactory().userDao();	// #5
		
		//ApplicationContext context = new AnnotationConfigApplicationContext(DaoFactory.class);	// #6
		ApplicationContext context = new GenericXmlApplicationContext("applicationContext.xml");	// #p135
		UserDao dao = context.getBean("userDao", UserDao.class);	// #6
		
		User user = new User();
		user.setId("whiteship");
		user.setName("백기선");
		user.setPassword("married");
		
		dao.add(user);
		
		System.out.println(user.getId() + "등록 성공!");
		
		User user2 = dao.get(user.getId());
		*/
		
		/* #p154
		System.out.println(user2.getName());
		System.out.println(user2.getPassword());
		System.out.println(user2.getId() + "조회 성공!");
		*/
		
		/*	#p160
		// #p154
		if(!user.getName().equals(user2.getName())){
			System.out.println("테스트 실패 (name)");
		}else if(!user.getPassword().equals(user2.getPassword())){
			System.out.println("테스트 실패 (password)");
		}else{
			System.out.println("조회 테스트 성공!!");
		}
		*/
	}
}
