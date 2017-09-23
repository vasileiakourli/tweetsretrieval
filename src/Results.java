import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Results {
	
	//JDBC driver name and database URL
	static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";  
	static final String DB_URL = "jdbc:mysql://localhost:3306/";

	//  Database credentials
	static final String USER = "root";
	static final String PASS = "mysql";
	static Connection conn = null;
	static Statement stmt = null;
	
	public static void main(String[] args){
	
		try{
		//STEP 2: Register JDBC driver
		Class.forName("com.mysql.jdbc.Driver");
	
		//STEP 3: Open a connection
		System.out.println("Connecting to database...");
		conn = DriverManager.getConnection(DB_URL, USER, PASS);
		
		//STEP 4: Execute a query
		stmt = conn.createStatement();
		
		String sql="use tweets;";
		stmt.executeUpdate(sql);
		
		//choosing positive posts for Syriza
		
		String sql1;
		ResultSet rs1;
		sql1="Select count(postId) from list where (emoticon = 1) and (komma = 'Syriza' ) ;";
		rs1=stmt.executeQuery(sql1);
		while(rs1.next()){
			System.out.println("Syriza-Positive tweets: "+rs1.getString("count(postId)")+ ".");
		}
		//negative tweets for Syriza
		sql1="Select count(postId) from list where (emoticon = 0) and (komma = 'Syriza' ) ;";
		rs1=stmt.executeQuery(sql1);
		while(rs1.next()){
			System.out.println("Syriza-Negative tweets: "+rs1.getString("count(postId)")+ ".");
		}
		
		//choosing positive posts for nea demokratia
		ResultSet rs2;
		sql1="Select count(postId) from list where (emoticon = 1 and komma = 'ND');";
		rs2=stmt.executeQuery(sql1);
		while(rs2.next()){
			System.out.println("Nea Demokratia-Positive tweets: " + rs2.getString("count(postId)")+ ".");
		}
		//negative posts for nea demokratia
		sql1="Select count(postId) from list where (emoticon = 0) and komma = 'ND';";
		rs2=stmt.executeQuery(sql1);
		while(rs2.next()){
			System.out.println("Nea Demokratia-Negative tweets: " + rs2.getString("count(postId)")+ ".");
		}
		}catch(SQLException se){
			//Handle errors for JDBC
			se.printStackTrace();
		}catch(Exception e){
			//Handle errors for Class.forName
			e.printStackTrace();
		}finally{
			//finally block used to close resources
			try{
				if(stmt!=null)
					stmt.close();
			}catch(SQLException se2){
			
			}// nothing we can do
			try{
				if(conn!=null)
					conn.close();
			}catch(SQLException se){
				se.printStackTrace();
			}//end finally try
		}
	}
	
}
