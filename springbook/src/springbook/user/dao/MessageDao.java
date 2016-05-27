package springbook.user.dao;

public class MessageDao {

	private ConnectionMaker connectionMaker;
	
	public MessageDao(ConnectionMaker ConnectionMaker) {
		this.connectionMaker = connectionMaker;	// #6
	}

}
