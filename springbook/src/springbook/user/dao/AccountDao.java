package springbook.user.dao;

public class AccountDao {
	private ConnectionMaker connectionMaker;
	
	public AccountDao(ConnectionMaker ConnectionMaker) {
		this.connectionMaker = connectionMaker;	// #6
	}
}
