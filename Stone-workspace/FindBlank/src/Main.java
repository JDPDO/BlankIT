//ps -ax | grep tomcat

import java.util.ArrayList;
import java.util.Iterator;
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
	public boolean Noun;
	public int frequency;
	
	public Word(String word, boolean Noun, int frequency)
	{
		this.word = word;
		this.Noun = Noun;
		this.frequency = frequency;
	}
	
}

public class Main 
{
	public static void main(String[] args)
	{
		
		Debug.Log("Init!");
		Debug.Seperator();
		
		//int quatschafaktor = 1;		
		String satz = "Beton kann außerdem Betonzusatzstoffe und Betonzusatzmittel enthalten , die die Eigenschaften des Baustoffs gezielt beeinflussen.";  
		
		List<Word> words = AnalyseSentence(satz);
		
		for (int i = 0; i < words.size(); i++)
			Debug.Log(words.get(i).word + "   Noun: " + words.get(i).Noun 
					+ "   Frequency: " + words.get(i).frequency);
//		String sentence = "Hallo, ich bin ein Noun!";
//		String[] parts = SplitSentence(sentence);
//		
//		List<Word> words = new ArrayList<Word>();
//		
//		for (int i = 0; i < parts.length; i++)
//		{
//			boolean Noun = IsWordNoun(parts[i]);
//			words.add(new Word(parts[i], Noun));
//		}
	}
	
	public static String[] SplitSentence(String sentence)
	{
		sentence = sentence.replace("  ", " ");
		
		String[] ret = sentence.split(" ");
		return ret;
	}
	
	public static List<Word> AnalyseSentence(String sentence)
	{
		Connection conn = GetDBConnection();
		
		String[] parts = SplitSentence(sentence);
		List<Word> words = new ArrayList<Word>();
				
		for (int i = 0; i < parts.length; i++)
		{
			String part = parts[i];
			
			boolean noun = IsWordNoun(conn, part);
			int frequency = noun ? GetWordCount(conn, part) : -1;
			
			Word word = new Word(part, noun, frequency);
			words.add(word);
		}
		return words;
	}
	
	public static void StripOut(List<Word> word)
	{
		
	}

	
	public static boolean IsWordNoun(Connection conn, String word)
	{
		try
		{
			Debug.Log("Checking... " + word);
			PreparedStatement stmt = conn.prepareStatement("SELECT COUNT(*) AS count FROM Nomen WHERE LOWER(wort)=LOWER(?)");
			stmt.setString(1, word);
			
			ResultSet rs = stmt.executeQuery();
			
			rs.next();
			return rs.getInt("count") == 1;
		} catch (SQLException e) {
			Debug.Error("Hier bin ich!!! FEHLER FEHLER!! \n\n" + 
			e.getMessage() + "\n\n" + e.getStackTrace());
		}
		return false;
	}
	
	public static int GetWordCount(Connection conn, String word)
	{
		try
		{
			PreparedStatement stmt = conn.prepareStatement(
"select sum(W.anz) AS count from dict as D join worte as W on D.ID=W.ID AND LOWER(D.Wort) = LOWER(?);");
			stmt.setString(1, word);
			ResultSet rs = stmt.executeQuery();

			rs.next();
			return rs.getInt("count");
		} catch (SQLException e) {
			Debug.Error("Hier bin ich!!! FEHLER FEHLER!! \n\n" + 
			e.getMessage() + "\n\n" + e.getStackTrace());
		}
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
