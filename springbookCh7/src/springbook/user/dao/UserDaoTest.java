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
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import springbook.user.domain.Level;
import springbook.user.domain.User;


@RunWith(SpringJUnit4ClassRunner.class)	// #p185
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
		this.user1 = new User("jkonoury", "장지홍","springno1", Level.BASIC , 1,0,"test1@naver.com");
		this.user2 = new User("jmh", "정명한","springno2", Level.SILVER , 55,10,"test2@naver.com");
		this.user3 = new User("lyg", "이용규","springno3", Level.GOLD , 100,40,"test3@naver.com");
	}
	
	@Test
	public void update(){
		dao.deleteAll();
		
		dao.add(user1);
		dao.add(user2);
		
		user1.setName("오민규");
		user1.setPassword("spring06");
		user1.setLevel(Level.GOLD);
		user1.setLogin(1000);
		user1.setRecommend(999);
		dao.update(user1);
		
		User user1update = dao.get(user1.getId());
		checkSameUser(user1, user1update);
		
		User user2same = dao.get(user2.getId());
		checkSameUser(user2, user2same);
	}
	
	@Test
	public void addAndGet() throws SQLException, ClassNotFoundException{
		dao.deleteAll();
		
		dao.add(user1);
		dao.add(user2);
		
		User userget1 = dao.get(user1.getId());
		checkSameUser(userget1, user1);
		
		User userget2 = dao.get(user2.getId());
		checkSameUser(userget2, user2);
	}
	
	private void checkSameUser(User user1, User user2){
		assertThat(user1.getId(),is(user2.getId()));
		assertThat(user1.getName(),is(user2.getName()));
		assertThat(user1.getPassword(),is(user2.getPassword()));
		assertThat(user1.getLevel(),is(user2.getLevel()));
		assertThat(user1.getLogin(),is(user2.getLogin()));
		assertThat(user1.getRecommend(),is(user2.getRecommend()));
	}
	
	public static void main(String[] args)throws ClassNotFoundException, SQLException {
		JUnitCore.main("springbook.user.dao.UserDaoTest");
	}
}
