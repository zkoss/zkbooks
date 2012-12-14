package org.zkoss.reference.developer.integration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Logger;

import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

public class JdbcComposer extends SelectorComposer<Window> {

	private static final long serialVersionUID = 5471706011875481102L;
	private static Logger log = Logger.getLogger(JdbcComposer.class.getName());
	@Wire
	private Textbox name;
	@Wire
	private Textbox email;

	@Listen("onClick = button")
	public void submit() {
		PreparedStatement stmt = null;
		Connection conn = null;
		try {
			//load driver and get a database connection
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(
					"jdbc:mysql://localhost/test?user=root&password=root");
			stmt = conn.prepareStatement("INSERT INTO user values(?, ?)");

			//insert what end user entered into database table
			stmt.setString(1, name.getValue());
			stmt.setString(2, email.getValue());

			//execute the statement
			stmt.executeUpdate();
		} catch(Exception e){
			log.severe(e.toString());
		}finally { //cleanup
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException ex) {
					log.severe(ex.toString()); //log and ignore
				}
			}
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException ex) {
					log.severe(ex.toString()); //log and ignore
				}
			}
		}
	}	
}
