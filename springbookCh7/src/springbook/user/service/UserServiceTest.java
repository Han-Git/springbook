package springbook.user.service;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static springbook.user.service.UserServiceImpl.MIN_LOGCOUNT_FOR_SILVER;	//p347
import static springbook.user.service.UserServiceImpl.MIN_RECCOMEND_FOR_GOLD;	//p347

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.PlatformTransactionManager;

import springbook.user.dao.UserDao;
import springbook.user.domain.Level;
import springbook.user.domain.User;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="/test-applicationContext.xml")
public class UserServiceTest {
	
	static class MockUserDao implements UserDao{
		private List<User> userss;
		private List<User> updated = new ArrayList();
		
		private MockUserDao(List<User> users){
			this.userss = users;
		}
		
		public List<User> getUpdated(){
			return this.updated;
		}

		public List<User> getAll(){
			return this.userss;
		}
		
		public void update(User user){
			updated.add(user);
		}
		
		public void add(User user){throw new UnsupportedOperationException();}
		public void deleteAll(){throw new UnsupportedOperationException();}
		public User get(String id){throw new UnsupportedOperationException();}
		public int getCount(){throw new UnsupportedOperationException();}
	}
	
	static class TestUserService extends UserServiceImpl{
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
	
	static class MockMailSender implements MailSender{
		private List<String> requests = new ArrayList<String>();
		
		public List<String> getRequests(){
			return requests;
		}
		
		public void send(SimpleMailMessage mailMessage) throws MailException {
			requests.add(mailMessage.getTo()[0]);
		}

		public void send(SimpleMailMessage[] mailMessage) throws MailException {
			
		}
	}
	
	@Autowired
	UserService userService;
	
	@Autowired
	UserServiceImpl userServiceImpl;
	
	@Autowired
	UserDao userDao;
	
	@Autowired
	MailSender mailSender;
	
	List<User> users;
	
	@Autowired
	PlatformTransactionManager transactionManager;
	
	@Before
	public void setUp(){
		users = Arrays.asList(
					new User("bumjin","박범진","p1", Level.BASIC, MIN_LOGCOUNT_FOR_SILVER-1,0,"test1@gmail.com"),
					new User("joytouch","강명성","p2", Level.BASIC, MIN_LOGCOUNT_FOR_SILVER,0,"test2gmail.com"),
					new User("erwins","신승한","p3", Level.SILVER,60, MIN_RECCOMEND_FOR_GOLD-1,"test3gmail.com"),
					new User("madnite1","이상호","p4", Level.SILVER,60, MIN_RECCOMEND_FOR_GOLD,"test4gmail.com"),
					new User("jmh","정명한","p5", Level.GOLD, 100, 100,"topofstar0@gmail.com")
				);
	}
	
	@Test
	@DirtiesContext	// 컨택스트의 DI 설정을 변경하는 테스트라는 것을 알려준다.
	public void upgradeLevels()throws Exception{
		UserServiceImpl userServiceImpl = new UserServiceImpl();	//P421 고립된 테스트에서는 테스트 대상 오브젝트를 직접생성하면된다.
		
		MockUserDao mockUserDao = new MockUserDao(this.users);
		userServiceImpl.setUserDao(mockUserDao);
		
		MockMailSender mockMailSender = new MockMailSender();	// P397 매일 발송 결과를 테스트할수 있도록 목오브젝트를 만들어 userService의 의존 오브젝트로 주입해준다.
		userServiceImpl.setMailSender(mockMailSender);	// P397
		
		/*
		userDao.deleteAll();
		for(User user:users){
			userDao.add(user);
		}
		*/
		
		userServiceImpl.upgradeLevels();
		
		List<User> updated = mockUserDao.getUpdated();	// P421 MockUserDao로부터 업데이트 결과를 가져온다.
		System.out.println("updated.size()==="+updated.size());
		System.out.println("0==="+updated.get(0).getId());
		System.out.println("1==="+updated.get(1).getId());
		
		assertThat(updated.size(), is(2));
		checkUserAndLevel(updated.get(0),"joytouch", Level.SILVER);
		checkUserAndLevel(updated.get(1),"madnite1", Level.GOLD);
		
		
		/*
		checkLevelUpgraded(users.get(0),false);
		checkLevelUpgraded(users.get(1),true);
		checkLevelUpgraded(users.get(2),false);
		checkLevelUpgraded(users.get(3),true);
		checkLevelUpgraded(users.get(4),false);
		*/
		
		// P397 목오브젝트에 저장된 메일 수신자 목록을 가져와 업그레이드 대상과 일치하는지 확인한다.
		List<String> request = mockMailSender.getRequests();
		assertThat(request.size(), is(2));	
		assertThat(request.get(0), is(users.get(1).getEmail()));
		assertThat(request.get(1), is(users.get(3).getEmail()));
		
	}
	
	private void checkUserAndLevel(User updated, String expectedId, Level expectedLevel){
		assertThat(updated.getId(), is(expectedId));
		assertThat(updated.getLevel(), is(expectedLevel));
	}
	
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
		TestUserService testUserService = new TestUserService(users.get(3).getId());
		testUserService.setUserDao(this.userDao);
		testUserService.setMailSender(mailSender);
		
		UserServiceTx txUserService = new UserServiceTx();
		txUserService.setTransactionManager(transactionManager);
		txUserService.setUserService(testUserService);
		
		userDao.deleteAll();
		for(User user : users){
			userDao.add(user);
		}
		try{
			txUserService.upgradeLevels();
			fail("TestUserServiceException expected");
		}catch(TestUserServiceException e){
			
		}
		checkLevelUpgraded(users.get(1),false);
	}
}
