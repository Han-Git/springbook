package springbook.user.service;

import java.util.List;

import springbook.user.dao.UserDao;
import springbook.user.domain.Level;
import springbook.user.domain.User;

public class UserService {
	UserDao userDao;
	
	public void setUserDao(UserDao userDao){
		this.userDao = userDao;
	}
	
	public void upgradeLevels(){
		/* P338 removed because of Refactoring
		List<User> users = userDao.getAll();
		for(User user : users){
			Boolean changed = null;
			if(user.getLevel()== Level.BASIC && user.getLogin() >=50){
				user.setLevel(Level.SILVER);
				changed = true;
			}
			else if(user.getLevel()==Level.SILVER && user.getRecommend()>=30){
				user.setLevel(Level.GOLD);
				changed = true;
			}else if(user.getLevel()== Level.GOLD){
				changed = false;
			}else{
				changed = false;
			}
			if(changed){
				userDao.update(user);
			}
		}
		*/
		
		List<User> users = userDao.getAll();
		for(User user : users){
			if(canUpgradeLevel(user)){
				upgradeLevel(user);
			}
		}
	}
	
	private boolean canUpgradeLevel(User user){
		Level currentLevel = user.getLevel();
		switch(currentLevel){
			case BASIC: return (user.getLogin() >= 50);
			case SILVER: return (user.getRecommend() >= 30);
			case GOLD: return false;
			default: throw new IllegalArgumentException("Unknown Level: "+currentLevel);
		}
	}
	
	private void upgradeLevel(User user){
		if(user.getLevel()== Level.BASIC){
			user.setLevel(Level.SILVER);
		}else if(user.getLevel()== Level.SILVER){
			user.setLevel(Level.GOLD);
		}
		userDao.update(user);
	}
	
	public void add(User user){
		if(user.getLevel() == null){
			user.setLevel(Level.BASIC);
		}
		userDao.add(user);
	}
}
