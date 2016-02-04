package springbook.user.dao;

public class DaoFactory {
	public UserDao userDao(){
		ConnectionMaker connectionMaker = new DConnectionMaker();	//#5
		UserDao userDao = new UserDao(connectionMaker);
		return userDao;
	}
}
