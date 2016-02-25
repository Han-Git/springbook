package springbook.user.dao;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.sql.SQLException;

import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;

import springbook.user.domain.User;

public class UserDaoTest {
	@Test
	public void addAndGet() throws SQLException, ClassNotFoundException{
		ApplicationContext context = new GenericXmlApplicationContext("applicationContext.xml");	// #p158
		UserDao dao = context.getBean("userDao", UserDao.class);	// #p158
		
		User user = new User();	// #p158
		user.setId("gyumee");	// #p158
		user.setName("박성철");	// #p158
		user.setPassword("springno1");	// #p158
		
		dao.add(user);	// #p158
		
		System.out.println(user.getId() + "등록 성공!");	// #p158
		
		User user2 = dao.get(user.getId());	// #p158
		
		assertThat(user2.getName(), is(user.getName()));	// #p158
		assertThat(user2.getPassword(), is(user.getPassword()));	// #p158
		
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
