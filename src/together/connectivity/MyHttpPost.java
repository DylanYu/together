/**
 * used to post data to a url
 * @author zhangjie
 * @version 1.0
 * @since 2012-2-13
 * Created on 2012-2-13
 *
 * TODO To post some comments to the server
 * Window - Preferences - Java - Code Style - Code Templates
 */
package together.connectivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

public class MyHttpPost {
	private HttpPost httppost;
	private InputStream content;
	private ArrayList<BasicNameValuePair> pairs;
	private DefaultHttpClient httpclient;
	private String returnConnection;

	/**
	 * init post
	 * @param url String
	 * */
	public void noparameterHttp(String url) {
		this.httpclient = new DefaultHttpClient();
		this.httppost = new HttpPost(url);
		this.pairs = new ArrayList<BasicNameValuePair>();
	}

	/**
	 * init the pairs
	 * 
	 * @param url
	 *            post to the url
	 * @param var
	 *            Map<String,String>
	 * */
	public void parameterHttp(String url, Map<String, String> variables) {
		this.httpclient = new DefaultHttpClient();
		this.httppost = new HttpPost(url);
		this.pairs = new ArrayList<BasicNameValuePair>();
		if (variables != null) {
			Set<String> keys = variables.keySet();
			for (Iterator<String> i = keys.iterator(); i.hasNext();) {
				String key = (String) i.next();
				pairs.add(new BasicNameValuePair(key, variables.get(key)));
			}
		}
	}

	/**
	 * post the data
	 * @return returnConnection String
	 * */
	public String doPost() {
		try {
			if (pairs != null) {
				UrlEncodedFormEntity p_entity = new UrlEncodedFormEntity(pairs,
						HTTP.UTF_8);
				httppost.setEntity(p_entity);
			}
			HttpResponse response = httpclient.execute(httppost);
			HttpEntity entity = response.getEntity();
			content = entity.getContent();
			this.returnConnection = convertStreamToString(content);
		} catch (UnsupportedEncodingException uee) {
		} catch (IOException ioe) {
		} catch (IllegalStateException ise) {
		}
		return returnConnection;
	}

	/***
	 * convert stream to String
	 * @param is inputStream
	 * @return str String
	 */
	private String convertStreamToString(InputStream is) {
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();

		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return sb.toString();

	}
}
