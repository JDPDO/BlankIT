//ps -ax | grep tomcat

import java.util.ArrayList;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

class Word
{
	public String word;
	public boolean nomen;
	public int frequency;
	
	public Word(String word, boolean nomen)
	{
		this.word = word;
		this.nomen = nomen;
	}
}

public class Main 
{
	
	
	public static void main(String[] args)
	{
		Connection conn = GetDBConnection();
		
		Debug.Log("Init!");
		Debug.Seperator();
		
		boolean r = IsWordNomen(conn, "haus");
		Debug.Log("" + r);
		int b = GetWordCount(conn, "haus");
		Debug.Log("" + b);
//		String sentence = "Hallo, ich bin ein Nomen!";
//		String[] parts = SplitSentence(sentence);
//		
//		List<Word> words = new ArrayList<Word>();
//		
//		for (int i = 0; i < parts.length; i++)
//		{
//			boolean nomen = IsWordNomen(parts[i]);
//			words.add(new Word(parts[i], nomen));
//		}
	}
	
	public static String[] SplitSentence(String sentence)
	{
		sentence = sentence.replace("  ", " ");
		
		String[] ret = sentence.split(" ");
		return ret;
	}

	
	public static boolean IsWordNomen(Connection conn, String word)
	{
		try
		{
			PreparedStatement stmt = conn.prepareStatement("SELECT COUNT(*) AS count FROM Nomen WHERE LOWER(wort)=LOWER(?)");
			stmt.setString(1, word);
			ResultSet rs = stmt.executeQuery();
			rs.next();
			return rs.getInt("count") == 1;
		}catch (Exception ex) {Debug.Error("Exception !!!");}
		return false;
	}
	
	public static int GetWordCount(Connection conn, String word)
	{
		try
		{
			PreparedStatement stmt = conn.prepareStatement("SELECT COUNT(*) AS count FROM Nomen WHERE LOWER(wort)=LOWER(?)");
			stmt.setString(1, word);
			ResultSet rs = stmt.executeQuery();
			rs.next();
			return rs.getInt("count") == 1;
		}catch (Exception ex) {Debug.Error("Exception !!!");}
		return -1;
	}
	
	public static Connection GetDBConnection()
	{
		MysqlDataSource dataSource = new MysqlDataSource();
		dataSource.setUser("jh");
		dataSource.setPassword("");
		dataSource.setDatabaseName("WC");
		//dataSource.setPort("3306");
		dataSource.setServerName("192.168.173.28");
		Connection conn;
		try {
			
			conn = dataSource.getConnection();
			return conn;
			
		} catch (SQLException e) {
			Debug.Error("Hier bin ich!!! FEHLER FEHLER!! \n\n" + 
			e.getMessage() + "\n\n" + e.getStackTrace());
		}
		return null;
	}
}
