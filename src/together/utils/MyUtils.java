package together.utils;
 
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import org.apache.http.util.ByteArrayBuffer;
import org.apache.http.util.EncodingUtils;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class MyUtils {

	/**
	 * change the height and width of the video
	 * 
	 * @param video
	 *            myVideoView
	 * @param layout
	 *            video layout style
	 * @param top
	 *            margin top
	 * @return new_videoview
	 * */
	 

	 
	public static void Fav(SQLiteDatabase download, String m3u8, String title,
			String url, String image, String id) {
		insert(download, m3u8, title, url, id);
	}

	/***
	 * 
	 * tell if a video is exist
	 * 
	 * @param download
	 *            SQL database
	 * @param m3u8
	 *            m3u8 url
	 * @return true if the m3u8 exists otherwise return false
	 */
	public static boolean exsitFav(SQLiteDatabase download, String m3u8) {
		String sql = "SELECT title FROM " + MyConstants.FAV_TB_NAME
				+ " where m3u8 = '" + m3u8 + "'";
		Cursor c = download.rawQuery(sql, null);
		int count = c.getCount();
		c.close();
		if (count != 0)
			return true;
		else
			return false;
	}

	/**
	 * @param download
	 *            SQLiteDatabase
	 * @param m3u8
	 *            video source
	 * */
	public static boolean exsitDownloads(SQLiteDatabase download, String m3u8) {
		String sql = "SELECT title FROM " + MyConstants.DOWN_TB_NAME
				+ " where m3u8 = '" + m3u8 + "'";
		Cursor c = download.rawQuery(sql, null);
		int count = c.getCount();
		c.close();
		if (count != 0)
			return true;
		else
			return false;
	}

	/****
	 * 
	 * insert into database
	 * 
	 * @param download
	 *            SQL database
	 * @param m3u8
	 *            m3u8 url
	 * @param title
	 *            title of m3u8
	 * @param url
	 *            image url of m3u8
	 * @param music_id
	 *            id of m3u8
	 */
	public static void insert(SQLiteDatabase download, String m3u8,
			String title, String url, String music_id) {
		ContentValues values = new ContentValues();
		values.put("m3u8", m3u8);
		values.put("title", title);
		values.put("image_url", url);
		values.put("music_id", music_id);
		download.insert(MyConstants.FAV_TB_NAME, MyConstants.FAV_ID, values);
	}

	/**
	 * @param is
	 *            InputStream
	 * @return buffer ByteArrayBuffer
	 * */
	public static ByteArrayBuffer readByteFromURL(InputStream subis)
			throws IOException {
		BufferedInputStream sub_buffer_is = new BufferedInputStream(subis, 8192);
		ByteArrayBuffer sub_buffer = new ByteArrayBuffer(50);
		int subcurrent = 0;
		while ((subcurrent = sub_buffer_is.read()) != -1) {
			sub_buffer.append((byte) subcurrent);
		}
		return sub_buffer;
	}

	/**
	 * @param is
	 *            InputStream
	 * @return str String
	 * */
	public static String readFromURL(InputStream is) throws IOException {
		String s;
		ByteArrayBuffer baf = readByteFromURL(is);
		s = EncodingUtils.getString(baf.toByteArray(), "UTF-8");
		return s;
	}

	/**
	 * @param f
	 *            File
	 * @return str String
	 * */
	public static String readFromFile(File f) throws FileNotFoundException,
			IOException {
		String s;
		BufferedInputStream bis;
		FileInputStream fis = new FileInputStream(f);
		bis = new BufferedInputStream(fis, 8192);
		ByteArrayBuffer buffer = new ByteArrayBuffer(50);
		int current = 0;
		while ((current = bis.read()) != -1) {
			buffer.append((byte) current);
		}
		s = EncodingUtils.getString(buffer.toByteArray(), "UTF-8");
		return s;
	}

	/**
	 * @param m3u8
	 *            video source
	 * @param title
	 *            String
	 * @param image
	 *            image url
	 * @param id
	 *            String
	 * */
	 
	/**
	 * @param myURL
	 *            URL
	 * @return str String
	 * */
	public static String readStringFromURL(URL myURL) throws IOException {
		String s;
		URLConnection ucon = myURL.openConnection();
		// ucon.setConnectTimeout(5 * 1000);
		InputStream is = ucon.getInputStream();
		BufferedInputStream biss = new BufferedInputStream(is, 8192);
		ByteArrayBuffer baf = new ByteArrayBuffer(50);
		int current = 0;
		while ((current = biss.read()) != -1) {
			baf.append((byte) current);
		}
		s = EncodingUtils.getString(baf.toByteArray(), "UTF-8");
		return s;
	}

	/**
	 * @param myURL
	 *            URL
	 * @return buffer ByteArrayBuffer
	 * */
	public static ByteArrayBuffer readByteFromURL(URL myURL) throws IOException {
		URLConnection ucon = myURL.openConnection();
		// ucon.setConnectTimeout(5 * 1000);
		InputStream is = ucon.getInputStream();
		BufferedInputStream biss = new BufferedInputStream(is, 8192);
		ByteArrayBuffer baf = new ByteArrayBuffer(50);
		int current = 0;
		while ((current = biss.read()) != -1) {
			baf.append((byte) current);
		}
		return baf;
	}
}
