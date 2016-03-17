package springbook.user.service;

import java.sql.Connection;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import springbook.user.dao.UserDao;
import springbook.user.domain.Level;
import springbook.user.domain.User;

public class UserService {
	public static final int MIN_LOGCOUNT_FOR_SILVER = 50;
	public static final int MIN_RECCOMEND_FOR_GOLD = 30;
	
	UserDao userDao;
	private DataSource dataSource;
	
	public void setUserDao(UserDao userDao){
		this.userDao = userDao;
	}
	
	public void setDataSource(DataSource dataSource){
		this.dataSource = dataSource;
	}

	// p363
//	public void upgradeLevels(){
//		/* P338 removed because of Refactoring
//		List<User> users = userDao.getAll();
//		for(User user : users){
//			Boolean changed = null;
//			if(user.getLevel()== Level.BASIC && user.getLogin() >=50){
//				user.setLevel(Level.SILVER);
//				changed = true;
//			}
//			else if(user.getLevel()==Level.SILVER && user.getRecommend()>=30){
//				user.setLevel(Level.GOLD);
//				changed = true;
//			}else if(user.getLevel()== Level.GOLD){
//				changed = false;
//			}else{
//				changed = false;
//			}
//			if(changed){
//				userDao.update(user);
//			}
//		}
//		*/
//		
//		List<User> users = userDao.getAll();
//		for(User user : users){
//			if(canUpgradeLevel(user)){
//				upgradeLevel(user);
//			}
//		}
//	}
	
	public void upgradeLevels() throws Exception{
		TransactionSynchronizationManager.initSynchronization();
		Connection c = DataSourceUtils.getConnection(dataSource);
		c.setAutoCommit(false);
		
		try{
			List<User> users = userDao.getAll();
			for(User user : users){
				if(canUpgradeLevel(user)){
					upgradeLevel(user);
				}
			}
			c.commit();
		}catch(Exception e){
			c.rollback();
			throw e;
		}finally{
			DataSourceUtils.releaseConnection(c, dataSource);
			TransactionSynchronizationManager.unbindResource(this.dataSource);
			TransactionSynchronizationManager.clearSynchronization();
		}
	}
	
	private boolean canUpgradeLevel(User user){
		Level currentLevel = user.getLevel();
		switch(currentLevel){
			//case BASIC: return (user.getLogin() >= 50);	//p347
			//case SILVER: return (user.getRecommend() >= 30);	//p347
			case BASIC: return (user.getLogin() >= MIN_LOGCOUNT_FOR_SILVER);
			case SILVER: return (user.getRecommend() >= MIN_RECCOMEND_FOR_GOLD);
			case GOLD: return false;
			default: throw new IllegalArgumentException("Unknown Level: "+currentLevel);
		}
	}
	
	//private void upgradeLevel(User user){	// #p350 
	protected void upgradeLevel(User user){
		/* p343 refactoring
		if(user.getLevel()== Level.BASIC){
			user.setLevel(Level.SILVER);
		}else if(user.getLevel()== Level.SILVER){
			user.setLevel(Level.GOLD);
		}
		*/
		user.upgradeLevel();
		userDao.update(user);
	}
	
	public void add(User user){
		if(user.getLevel() == null){
			user.setLevel(Level.BASIC);
		}
		userDao.add(user);
	}
}
