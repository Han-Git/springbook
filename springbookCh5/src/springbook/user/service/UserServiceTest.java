package springbook.user.service;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.List;

import javax.sql.DataSource;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import springbook.user.dao.UserDao;
import springbook.user.domain.Level;
import springbook.user.domain.User;

import static springbook.user.service.UserService.MIN_LOGCOUNT_FOR_SILVER;	//p347
import static springbook.user.service.UserService.MIN_RECCOMEND_FOR_GOLD;	//p347

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="/test-applicationContext.xml")
public class UserServiceTest {

	static class TestUserService extends UserService{
		private String id;
		
		private TestUserService(String id){
			this.id = id;
		}
		
		protected void upgradeLevel(User user){
			if(user.getId().equals(this.id)){
				throw new TestUserServiceException();
			}
			super.upgradeLevel(user);
		}
	}
	
	static class TestUserServiceException extends RuntimeException{
		
	}
	
	@Autowired
	UserService userService;
	
	@Autowired
	UserDao userDao;
	
	@Autowired
	DataSource dataSource;
	
	List<User> users;
	@Before
	public void setUp(){
		users = Arrays.asList(
					new User("bumjin","�ڹ���","p1",Level.BASIC,MIN_LOGCOUNT_FOR_SILVER-1,0),
					new User("joytouch","����","p2",Level.BASIC,MIN_LOGCOUNT_FOR_SILVER,0),
					new User("erwins","�Ž���","p3",Level.SILVER,60,MIN_RECCOMEND_FOR_GOLD-1),
					new User("madnite1","�̻�ȣ","p4",Level.SILVER,60,MIN_RECCOMEND_FOR_GOLD),
					new User("green","���α�","p5",Level.GOLD,100,100)
				);
	}
	
	/*
	@Test
	public void bean(){
		assertThat(this.userService, is(notNullValue()));
	}
	*/
	
	@Test
	public void upgradeLevels()throws Exception{
		userDao.deleteAll();
		for(User user:users){
			userDao.add(user);
		}
		
		userService.upgradeLevels();
		
		/* p346 replaced to checkLevelUpgraded
		checkLevel(users.get(0),Level.BASIC);
		checkLevel(users.get(1),Level.SILVER);
		checkLevel(users.get(2),Level.SILVER);
		checkLevel(users.get(3),Level.GOLD);
		checkLevel(users.get(4),Level.GOLD);
		*/
		
		checkLevelUpgraded(users.get(0),false);
		checkLevelUpgraded(users.get(1),true);
		checkLevelUpgraded(users.get(2),false);
		checkLevelUpgraded(users.get(3),true);
		checkLevelUpgraded(users.get(4),false);
		
	}
	
	/* p346 replaced to checkLevelUpgraded
	private void checkLevel(User user, Level expectedLevel) {
		User userUpdate = userDao.get(user.getId());
		assertThat(userUpdate.getLevel(),is(expectedLevel));
	}
	*/
	
	private void checkLevelUpgraded(User user , boolean upgraded){
		User userUpdate = userDao.get(user.getId());
		if(upgraded){
			assertThat(userUpdate.getLevel(), is(user.getLevel().nextLevel()));
		}
		else{
			assertThat(userUpdate.getLevel(), is(user.getLevel()));
		}
	}
	
	@Test
	public void add(){
		userDao.deleteAll();
		
		User userWithLevel = users.get(4);
		User userWithoutLevel = users.get(0);
		userWithoutLevel.setLevel(null);
		
		userService.add(userWithLevel);
		userService.add(userWithoutLevel);
		
		User userWithLevelRead = userDao.get(userWithLevel.getId());
		User userWithoutLevelRead = userDao.get(userWithoutLevel.getId());
		
		assertThat(userWithLevelRead.getLevel(),is(userWithLevel.getLevel()));
		assertThat(userWithoutLevelRead.getLevel(),is(userWithoutLevel.getLevel()));
	}
	
	@Test
	public void upgradeAllorNothing()throws Exception{
		UserService testUserService = new TestUserService(users.get(3).getId());
		testUserService.setUserDao(this.userDao);
		testUserService.setDataSource(this.dataSource);
		userDao.deleteAll();
		for(User user : users){
			userDao.add(user);
		}
		try{
			testUserService.upgradeLevels();
			fail("TestUserServiceException expected");
		}catch(TestUserServiceException e){
			
		}
		checkLevelUpgraded(users.get(1),false);
	}
}
