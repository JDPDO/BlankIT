//ps -ax | grep tomcat

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

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

class TreeMapComperator implements Comparator<Integer>
{
    public int compare(Integer o1,Integer o2)
    {
    	return o1 < o2 ? 1 : 0;
    }
}


public class Main 
{
	public static void main(String[] args)
	{
		
//		for (int i = 0; i < args.length; i++)
//			Debug.Log(args[i]);
//		Debug.Log("Init!");
//		Debug.Seperator();
		
//		String satz = "Beton kann außerdem Betonzusatzstoffe und Betonzusatzmittel enthalten , die die Eigenschaften des Baustoffs gezielt beeinflussen.";  
//		String satz = "Hallo, ich bin eine Kuh."; 
		boolean run = true;
		while (run)
		{
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));        
			String satz = "#WAITING_FOR_INPUT";
			try {
				satz = br.readLine();
			} catch (IOException e) {
				Debug.Error("Invalid input stream!");
			}
			if (satz.contains("[@24]"))
				run = false;
	//		Debug.Log(satz);
			List<Word> words = AnalyseSentence(satz);
	//		for (int i = 0; i < words.size(); i++)
	//			Debug.Log(words.get(i).word + "   Noun: " + words.get(i).Noun 
	//			+ "   Frequency: " + words.get(i).frequency);
			String ret = StripOutAndConcat(words, Integer.parseInt(args[0]));
			if (ret != null)
			System.out.println(ret + "[@23]"); /* RETURN TO CLIENT */
		}
//		for (int i = 0; i < words.size(); i++)
//			Debug.Log(words.get(i).word + "   Noun: " + words.get(i).Noun 
//					+ "   Frequency: " + words.get(i).frequency);
	}
	
	public static String[] SplitSentence(String sentence)
	{
//		sentence = sentence.replace("  ", " ");
		
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
	
	public static String StripOutAndConcat(List<Word> words, int Fq)
	{
		////INDEX////FREQUENCY///////
		Map<Integer, Integer> nouns = new HashMap<Integer, Integer>();
		for (int i = 0; i < words.size(); i++)
			if (words.get(i).Noun) nouns.put(i, words.get(i).frequency);
		
		if (nouns.size() == 0) return null;
		
		nouns = sortByValue(nouns);
		
		int maxTake = nouns.size();
		int noun = (int)Math.floor(Math.random() * Math.min(maxTake, Fq));
		int index = (int)nouns.keySet().toArray()[noun];
		words.get(index).word = "[@22]" + words.get(index).word;
		
		
		//CONCAT
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < words.size(); i++)
			sb.append(words.get(i).word + (i == words.size()-1 ? "" : " "));
		return sb.toString();
	}
	
	public static Map<Integer, Integer> SortMapByKey(Map<Integer, Integer> orgMap)
	{
		TreeMap<Integer, Integer> treeMap = new TreeMap<Integer, Integer>();

		for (int i = 0; i < orgMap.size(); i++)
		{
			Integer index = (Integer) orgMap.keySet().toArray()[i];
			treeMap.put(index, orgMap.get(index));
		}
		return treeMap;
	}

	public static <K, V extends Comparable<? super V>> Map<K, V> 
    sortByValue( Map<K, V> map )
	{
	    List<Map.Entry<K, V>> list =
	        new LinkedList<Map.Entry<K, V>>( map.entrySet() );
	    Collections.sort( list, new Comparator<Map.Entry<K, V>>()
	    {
	        public int compare( Map.Entry<K, V> o1, Map.Entry<K, V> o2 )
	        {
	            return (o1.getValue()).compareTo( o2.getValue() );
	        }
	    } );
	
	    Map<K, V> result = new LinkedHashMap<K, V>();
	    for (Map.Entry<K, V> entry : list)
	    {
	        result.put( entry.getKey(), entry.getValue() );
	    }
	    return result;
	}

	
	public static boolean IsWordNoun(Connection conn, String word)
	{
		try
		{
//			Debug.Log("Checking... " + word);
			PreparedStatement stmt = conn.prepareStatement(
					"SELECT COUNT(*) AS count FROM Nomen WHERE Wort=?");
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
				"SELECT sum(anz) AS count FROM woAnz WHERE Wort=?");
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
			conn.createStatement().executeQuery("SET collation_connection = 'latin1_general_cs'");
			return conn;
			
		} catch (SQLException e) {
			Debug.Error("Hier bin ich!!! FEHLER FEHLER!! \n\n" + 
			e.getMessage() + "\n\n" + e.getStackTrace());
		}
		return null;
	}
}
