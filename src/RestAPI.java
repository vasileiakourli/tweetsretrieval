import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.sql.*;

import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

import java.util.ArrayList;
import java.util.List;

public class RestAPI{
	
	//JDBC driver name and database URL
	static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";  
	static final String DB_URL = "jdbc:mysql://localhost:3306/";

	//  Database credentials
	static final String USER = "root";
	static final String PASS = "mysql";
	static Connection conn = null;
	static Statement stmt = null;
	public static void main(String[] args) {
		ConfigurationBuilder cb = new ConfigurationBuilder();
		cb.setDebugEnabled(true)
		.setOAuthConsumerKey("MqNiZUf5iGA2To9WuCT8fz7UF")
		.setOAuthConsumerSecret("OuzZTKLAU3tZu5MPXANLdRFMjEMMM4r2KYfygk0CQrmQRdDYuF")
		.setOAuthAccessToken("792329876607012865-rC4D2nTMoEFJf1C5VF2dZ8g0NSvAk41")
		.setOAuthAccessTokenSecret("p3ag9qGcYyzop6Tsyrn0KrCcNEUj9XyH54uTYvSEvIWQ2")
		.setJSONStoreEnabled(true);

		Twitter twitter = new TwitterFactory(cb.build()).getInstance();
		//Creating database -store the tweets
	   
		try{
				
		   
			//STEP 2: Register JDBC driver
			Class.forName("com.mysql.jdbc.Driver");
		
			//STEP 3: Open a connection
			System.out.println("Connecting to database...");
			conn = DriverManager.getConnection(DB_URL, USER, PASS);
			
			//STEP 4: Execute a query
			stmt = conn.createStatement();
			String sql="SET NAMES utf8mb4";
			stmt.executeUpdate(sql);
			sql = "CREATE DATABASE IF NOT EXISTS Tweets;";
			stmt.executeUpdate(sql);
	      
			sql="ALTER DATABASE Tweets CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci;";
	      
			sql="use tweets;";
			stmt.executeUpdate(sql);
	      
			//emoticon:  if positive 1 else if 0 else -1
	      
			sql="CREATE TABLE IF NOT EXISTS list(postId bigint primary key , userId bigint, name varchar(20), screenName varchar(50), text varchar(140), emoticon int, komma varchar(20));";
			stmt.executeUpdate(sql);
	      
			sql="ALTER TABLE list CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;";
			stmt.executeUpdate(sql);
	      
			sql="ALTER TABLE list CHANGE text text VARCHAR(250)CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;";
			stmt.executeUpdate(sql);
	      
			//klisi tis methodou kai eisagwgi twn tweets se listes
			List<Status> list1 = getFromQuery("#SYRIZA",twitter);
			List<Status> list2 = getFromQuery("#syrizanel",twitter);
			List<Status> list3 = getFromQuery("#NeaDemokratia",twitter);
			List<Status> list4 = getFromQuery("@atsipras",twitter);
			List<Status> list5 = getFromQuery("#NewDemocracy",twitter);
			List<Status> list6 = getFromQuery("#tsipras", twitter);
			List<Status> list7 = getFromQuery("#mitsotakis", twitter);
          
			//topothetisi twn tweets sti vasi dedomenwn 
			insertInDataBase(list1);
			insertInDataBase(list2);
			insertInDataBase(list3);
			insertInDataBase(list4);
			insertInDataBase(list5);
			insertInDataBase(list6);
			insertInDataBase(list7);
	      
			System.out.println("Tweets are stored. ");
			
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
		}//end try   
	}
   
	public static void insertInDataBase(List<Status> l) throws SQLException{
	   
		//gia oli ti lista pairnoume kapoia stoixeia twn tweets
		for(Status st : l){
			long userId = st.getUser().getId();
			long postId = st.getId();
     	  
			String name = st.getUser().getName();
			String screenName = st.getUser().getScreenName();
			String text = st.getText();
 
			int emoticon;
			if(st.getText().contains("\uD83D\uDE20") || st.getText().contains("\uD83D\uDE1E") 
					|| st.getText().contains("\uD83D\uDE42") || st.getText().contains("\uD83D\uDE00")){
				emoticon=1;
			}else if(st.getText().contains("\uD83D\uDE03") || st.getText().contains("\uD83D\uDE0B") 
					|| st.getText().contains("\uD83D\uDE15") || st.getText().contains("\uD83D\uDE1B")
					|| st.getText().contains("\uD83D\uDE02") || st.getText().contains("\uD83D\uDE14")
					|| st.getText().contains("\uD83D\uDE1E") || st.getText().contains("\uD83D\uDE20")
					|| st.getText().contains("\uD83D\uDE24") || st.getText().contains("\uD83D\uDE2E")
					|| st.getText().contains("\uD83D\uDE41") || st.getText().contains("\uD83D\uDE02")){
				emoticon=0;
			}else{
				emoticon=-1;
			}
			
			//mia epipleon stili wste na kseroume poio komma antistoixei se kathe post
			String komma="-";
			if((st.getText().contains("#mitsotakis")) || (st.getText().contains("#Mitsotakis")) 
				  || st.getText().contains("#MITSOTAKIS")
     			  || st.getText().contains("#NeaDemokratia") || st.getText().contains("#neademokratia") 
     			  || st.getText().contains("#NewDemocracy") || st.getText().contains("#newdemocracy")
     			  || st.getText().contains("#Neademokratia") || st.getText().contains("#newDemocracy")
     			  || st.getText().contains("#Newdemocracy")
				  || st.getText().contains("#NEWDEMOCRACY") || st.getText().contains("#NEADEMOKRATIA")){
				komma="ND";
			}else if((st.getText().contains("#tsipras")) || (st.getText().contains("#Tsipras"))
				|| (st.getText().contains("#TSIPRAS"))
				|| (st.getText().contains("#syrizanel")) || (st.getText().contains("#Syriza") 
				|| (st.getText().contains("#SYRIZA")) || (st.getText().contains("#syriza")) 
     			|| (st.getText().contains("#Syrizanel")) || (st.getText().contains("#SYRIZANEL")))){
				komma="Syriza";
			}
			PreparedStatement query;
			conn.setAutoCommit(true);
     	  
			//topothetisi mesa sti vasi
			String str = "INSERT IGNORE INTO list(postId ,userId ,name,screenName,text,emoticon, komma) VALUES( ? , ? , ? , ? , ? , ? , ? ) ;";
			query = conn.prepareStatement(str);
			query.setLong(1, postId);
			query.setLong(2, userId);
			query.setString(3, name);
			query.setString(4, screenName);
			query.setString(5, text);
			query.setInt(6, emoticon);
			query.setString(7, komma);
			query.executeUpdate();
     	 
		}	
   
   	}	
   
   	public static List<Status> getFromQuery(String word,Twitter twitter) throws UnsupportedEncodingException, FileNotFoundException{
 
   		//Query query = new Query(word); // auto einai me ta retweets mazi
	    
   		//dinoume ti leksi pou theloume na yparxei sto post kai vgazoume ta retweets 
   		Query query = new Query(word+ "-filter:retweets");

		QueryResult result;
        List<Status> tweets=new ArrayList<Status>();
        
        try {
        	//vazw sto result to apotelesma tis anazitisis twn tweets
			result = twitter.search(query);
			
			//gia osa tweets mazepsa gia ti sygkekrimeni leksi kanw tin analusi
			for (Status tweet : result.getTweets()){
				
				//an siniparxoun ta 2 kommata mesa se ena tweet na min to vazw stin teliki mou lista
				if(((tweet.getText().contains("#syrizanel")) || (tweet.getText().contains("#Syriza") || (tweet.getText().contains("#SYRIZA"))) 
						|| (tweet.getText().contains("#syriza")) || (tweet.getText().contains("#Syrizanel")) 
						|| (tweet.getText().contains("#tsipras")) || (tweet.getText().contains("#Tsipras"))
						|| (tweet.getText().contains("#SYRIZANEL") || (tweet.getText().contains("#TSIPRAS")))) 
						&& (tweet.getText().contains("#NeaDemokratia") || (tweet.getText().contains("#neademokratia")) 
						|| (tweet.getText().contains("#NewDemocracy")) || (tweet.getText().contains("#newdemocracy"))
						|| (tweet.getText().contains("#Newdemocracy")) || (tweet.getText().contains("#newDemocracy"))
						|| (tweet.getText().contains("#Mitsotakis")) || (tweet.getText().contains("#MITSOTAKIS"))
						|| (tweet.getText().contains("#mitsotakis")) || (tweet.getText().contains("#Neademokratia")) 
						|| (tweet.getText().contains("#NEWDEMOCRACY")) || (tweet.getText().contains("#NEADEMOKRATIA"))
						)){
						continue;
				}
				tweets.add(tweet);
				
			}
			
			return tweets;
		
        } catch (TwitterException e) {
			e.printStackTrace();
		}    
	    		
		return null;
	
    } 
		
}   