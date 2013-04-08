package together.connectivity;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

public class ServerResponse { 
	/***
	 * get response from server
	 * @param url
	 * @param json
	 * @return result String
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public static String getResponse(String url, JSONObject json) throws ClientProtocolException, IOException {
		String result = null;
		//请求数据
		HttpClient hc = new DefaultHttpClient();
		HttpPost hp = new HttpPost(url);
		hp.setEntity(new StringEntity(json.toString(), HTTP.UTF_8));
		HttpResponse hr = hc.execute(hp);
		//获取返回json报文
		if(hr.getStatusLine().getStatusCode() == 200){
			result = EntityUtils.toString(hr.getEntity(), "utf-8");
		}
		//关闭连接
		if (hc != null) {
			hc.getConnectionManager().shutdown();
		}
		return result;
	}
}
