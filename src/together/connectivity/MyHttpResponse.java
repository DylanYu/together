/**
 * get response from the server
 * @author zhangjie
 * @version 1.0
 * @since 2012-2-13
 * Created on 2012-2-13
 *
 * TODO To get response from the server
 * Window - Preferences - Java - Code Style - Code Templates
 */
package together.connectivity;


import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.ByteArrayBuffer;

import together.utils.BitmapUtil;
import together.utils.CacheTime;
import together.utils.MD5;
import together.utils.MyConstants;
import together.utils.MyUtils;
import android.graphics.Bitmap;

public class MyHttpResponse {

	public static final String ID = "_id";
//	private static String TAG = "MyHttpResponse";

	/**
	 * get bitmap from url
	 * 
	 * @param url
	 *            get bitmap from the url
	 * @return bitmap
	 * */
	public static Bitmap saveImage(final String url) {
		Bitmap bm = null;
		try {
			String hash = MD5.getMD5(url);
			String filename = MyConstants.ROOTDIR + hash + "_png";
			File f = new File(filename);
			if (f.exists()
					&& (System.currentTimeMillis() < f.lastModified()
							+ CacheTime.CACHE_DURATION_ONE_WEEK)) {
				bm = BitmapUtil.decodeFile(f, 480);
			} else { // get from url
				URL imgURL = new URL(url);
				HttpGet httpRequest = null;
				try {
					httpRequest = new HttpGet(imgURL.toURI());
				} catch (URISyntaxException e) {
					e.printStackTrace();
				}
				HttpClient httpclient = new DefaultHttpClient();
				HttpResponse response = (HttpResponse) httpclient
						.execute(httpRequest);
				HttpEntity entity = response.getEntity();
				BufferedHttpEntity bufHttpEntity = new BufferedHttpEntity(
						entity);
				InputStream instream = bufHttpEntity.getContent();
				bm = BitmapUtil.decodeStream(instream);
				if (bm != null)
					BitmapUtil.saveBitmap(filename, bm,
							CacheTime.CACHE_DURATION_ONE_WEEK);
			}
		} catch (Exception e) {
			System.out.println("url Exception");
			e.printStackTrace();
		}
		return bm;
	}

	/**
	 * get image from url and save
	 * 
	 * @param url
	 *            get image from the url and save it
	 * */
	public static void saveShareImage(String url) {// save json
		URL myURL;
		try {
			myURL = new URL(url);
			String filename = MyConstants.ROOTDIR + "shares.png";
			File f = new File(filename);
			if (f.exists()
					&& (System.currentTimeMillis() < f.lastModified()
							+ CacheTime.CACHE_DURATION_ONE_DAY)) {
				return;
			} else {// cache is old
				ByteArrayBuffer baf = MyUtils.readByteFromURL(myURL);

				if (baf.toByteArray() != null) {
					BufferedOutputStream out = new BufferedOutputStream(
							new FileOutputStream(f), 8192);
					out.write(baf.toByteArray());
					out.flush();
					out.close();
				}
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * get response from the url
	 * 
	 * @param url
	 *            get json from the url
	 * @return response from the internet
	 * */
	public static String getResponse(String url) {// save json
		String s = null;
		URL myURL;
		try {
			myURL = new URL(MyConstants.SITE + url);
			String hash = MD5.getMD5(url);
			String filename = MyConstants.ROOTDIR + hash;
			File f = new File(filename);
			if (f.exists()
					&& (System.currentTimeMillis() < f.lastModified()
							+ CacheTime.CACHE_DURATION_ONE_DAY)) {
				s = MyUtils.readFromFile(f);
			} else {// cache is old
				s = MyUtils.readStringFromURL(myURL);
				if (s != null) {
					BufferedOutputStream out = new BufferedOutputStream(
							new FileOutputStream(f), 8192);
					out.write(s.getBytes());
					out.flush();
					out.close();
				}
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return s;
	}

	/***
	 * get response with port
	 * @param url
	 * @param cachetime
	 * @return response String
	 */
	public static String getResponsePort(String url, int cachetime) {
		String s = null;
		URL myURL;
		try {
			myURL = new URL(MyConstants.SITE_PORT + url);
			String hash = MD5.getMD5(url);
			String filename = MyConstants.ROOTDIR + hash;
			File f = new File(filename);
			if (f.exists()
					&& (System.currentTimeMillis() < f.lastModified()
							+ cachetime)) {
				//
				s = MyUtils.readFromFile(f);
			} else {// cache is old
				s = MyUtils.readStringFromURL(myURL);
				if (s != null) {
					BufferedOutputStream out = new BufferedOutputStream(
							new FileOutputStream(f), 8192);
					out.write(s.getBytes());
					out.flush();
					out.close();
				}
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return s;
	}

	/**
	 * this is different from getResponse in site
	 * 
	 * @param url
	 *            get json from the url
	 * @return response string from url
	 * */
	public static String getResponsePort(String url) {// save json
		String s = null;
		URL myURL;
		try {
			myURL = new URL(MyConstants.SITE_PORT + url);
			String hash = MD5.getMD5(url);
			String filename = MyConstants.ROOTDIR + hash;
			File f = new File(filename);
			if (f.exists()
					&& (System.currentTimeMillis() < f.lastModified()
							+ CacheTime.CACHE_DURATION_ONE_DAY)) {
				//
				s = MyUtils.readFromFile(f);
			} else {// cache is old
				s = MyUtils.readStringFromURL(myURL);
				if (s != null) {
					BufferedOutputStream out = new BufferedOutputStream(
							new FileOutputStream(f), 8192);
					out.write(s.getBytes());
					out.flush();
					out.close();
				}
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return s;
	}

	 

	 
 
	/**
	 * request to a url and get response
	 * 
	 * @param url
	 *            url of the request
	 * @param time
	 *            cachetime
	 * @return string get response from the internet, and set the cache time
	 * */
	public static String getResponse(String url, long cacheTime) {// save json
		String s = null;
		URL myURL;
		try {
			myURL = new URL(MyConstants.SITE + url);
			String hash = MD5.getMD5(url);
			String filename = MyConstants.ROOTDIR + hash;
			File f = new File(filename);
			// BufferedInputStream bis = null;
			if (f.exists()
					&& (System.currentTimeMillis() < f.lastModified()
							+ cacheTime)) {
				s = MyUtils.readFromFile(f);
				 
			} else {// cache is old
				 
				s = MyUtils.readStringFromURL(myURL);
				BufferedOutputStream out = new BufferedOutputStream(
						new FileOutputStream(f), 8192);
				out.write(s.getBytes());
				out.flush();
				out.close();
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return s;
	}

}
