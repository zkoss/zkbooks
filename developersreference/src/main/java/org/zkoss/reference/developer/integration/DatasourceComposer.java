package org.zkoss.reference.developer.integration;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.naming.InitialContext;
import javax.sql.DataSource;

import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

public class DatasourceComposer extends SelectorComposer<Window> {

	private static final long serialVersionUID = 5471706011875481102L;
	@Wire
	private Textbox name;
	@Wire
	private Textbox email;

	@Listen("onClick = button")
	public void submit() {

		Connection conn = null;
		PreparedStatement stmt = null;
		try {
			DataSource ds = (DataSource) new InitialContext().lookup("java:comp/env/jdbc/MyDB");
			conn = ds.getConnection();
			//remember that we specify autocommit as false in the context.xml 
			conn.setAutoCommit(true);
			stmt = conn.prepareStatement("INSERT INTO user values(?, ?)");
			stmt.setString(1, name.getValue());
			stmt.setString(2, email.getValue());
			stmt.executeUpdate();

			stmt.close();
			stmt = null;
		} catch (SQLException e) {
			try{
				conn.rollback();
			}catch(SQLException ex){
				//log
			}
			//(optional log and) ignore
		} catch (Exception e) {
			//log
		} finally { //cleanup
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException ex) {
					//(optional log and) ignore
				}
			}
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException ex) {
					//(optional log and) ignore
				}
			}
		}
	}	
}
