package springbook.user.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import springbook.user.domain.User;

public class AddStatement implements StatementStrategy{
	User user;	// #p225
	
	public AddStatement(User user) {
		this.user = user;	// #p225
	}
	
	public PreparedStatement makePreparedStatement(Connection c) throws SQLException {
		PreparedStatement ps = c.prepareStatement("insert into users(id, name, password) values(?,?,?)");	// #p225
		ps.setString(1, user.getId());	// #p225
		ps.setString(2, user.getName());	// #p225
		ps.setString(3, user.getPassword());	// #p225
		
		return ps;	// #p225
	}
	
}
