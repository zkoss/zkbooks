package org.zkoss.tutorial;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson.JacksonFactory;
import com.google.api.services.customsearch.Customsearch;
import com.google.api.services.customsearch.CustomsearchRequest;
import com.google.api.services.customsearch.model.Result;
import com.google.api.services.customsearch.model.Search;

public class CustomSearchService {

	final private String GOOGLE_API_URL = "https://www.googleapis.com/customsearch/v1?";
	final private String API_KEY = "AIzaSyAK-f2C3JQJrC00hCEYDuhDnGvM8FyZigs";
	//custom search engine ID
	final private String CSE_ID = "014994008257058938644:2bqirpsh0ys";

	final private String CSE_URL=GOOGLE_API_URL+"key="+API_KEY+"&cx="+CSE_ID;

	public String searchByRest(String keyword){
		StringBuffer result = new StringBuffer();
		try {

			HttpURLConnection connection = (HttpURLConnection)new URL(CSE_URL+"&q="+keyword).openConnection();
			connection.setRequestMethod("GET");
			connection.setDoOutput(true);
			connection.connect();
			BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String line;
			while ((line = reader.readLine())!=null){
				result.append(line);
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		return result.toString();
	}

	public List<Result> search(String keyword){
		Customsearch customsearch = new Customsearch(new NetHttpTransport(), new JacksonFactory());
		List<Result> resultList = null;
		try {
			Customsearch.Cse.List list = customsearch.cse().list(keyword);
			list.setKey(API_KEY);
			list.setCx(CSE_ID);
			Search results = list.execute();
			resultList = results.getItems();

		}catch (Exception e) {
			e.printStackTrace();
		}
		
		return resultList;
	}

}
