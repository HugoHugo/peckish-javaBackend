import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.io.*;

public class ManipulateDatabase {
	public static void main(String[] args) {
		ResourceBundle bundle = ResourceBundle.getBundle("javaconfig");
		String user = bundle.getString("jdbc.user");
		String password = bundle.getString("jdbc.password");
		String url = bundle.getString("jdbc.url");
		String driver = bundle.getString("jdbc.driver");
		String dbname = bundle.getString("jdbc.dbname");
		String spath = bundle.getString("jdbc.searchpath");
		url = url + dbname;
		try {
			Connection con;
			Class.forName(driver);
			con = DriverManager.getConnection(url, user, password);
			Statement st =  con.createStatement();
			st.executeUpdate(spath);
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		} catch (ClassNotFoundException e){
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
	}
}
