import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

//***** THIS PROTOTYPE PROJECT IS CURRENTLY NOT IN USE AND MAY NOT BE USED UNTIL IT HAS BEEN COMPLETED *****//
//***** THIS PROTOTYPE PROJECT IS CURRENTLY NOT IN USE AND MAY NOT BE USED UNTIL IT HAS BEEN COMPLETED *****//
//***** THIS PROTOTYPE PROJECT IS CURRENTLY NOT IN USE AND MAY NOT BE USED UNTIL IT HAS BEEN COMPLETED *****//
//***** THIS PROTOTYPE PROJECT IS CURRENTLY NOT IN USE AND MAY NOT BE USED UNTIL IT HAS BEEN COMPLETED *****//

public class Main 
{
	public static final String API_prefix =
		"https://en.wikipedia.org/w/api.php" +
		"?format=xml&action=query&prop=extracts&exlimit=max&explaintext&titles=";
	
	public static void main(String[] args)
	{
//		String wikiUrl = args[0];
		String wikiUrl = "Google";
		
		String xml = getWikiXML(wikiUrl);
		System.out.println(xml);
	}
	
	public static String getWikiXML(String article)
	{
		StringBuilder sb = new StringBuilder();
		sb.append(API_prefix);
		sb.append(article);
				
		return getText(sb.toString());
	}

    public static String getText(String url)
    {
        try {
	        URL website = new URL(url);
	        URLConnection connection = website.openConnection();
	        BufferedReader in = new BufferedReader(
	                                new InputStreamReader(
	                                    connection.getInputStream()));
	
	        StringBuilder response = new StringBuilder();
	        String inputLine = "";
	        while ((inputLine = in.readLine()) != null) 
	        	response.append(inputLine);

			in.close();
			
	        return response.toString();
		} catch (IOException e) {
			Debug.Error(e.getMessage());
		}
        return "";
    }
}
