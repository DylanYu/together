package together.activity;import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;
import org.json.JSONObject;

import together.connectivity.JsonHandler;
import together.connectivity.ServerResponse;
import together.models.EventMsg;
import together.models.UserMsg;
import together.utils.Followers;
import together.utils.MyConstants;
import together.utils.Overlays;
import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.MapController;
import com.baidu.mapapi.map.MapView;
import com.baidu.platform.comapi.basestruct.GeoPoint;
public class FollowedMessageActivity extends Activity {	private Context context;	private String eid;	private String place;	private String uid;	private String type;	private String description;	private String longitude;	private String latitude;	private String time;	private String UID;	private TextView message_in_follow;	private TextView participate;	private MapView mapView;	private Button btn1;
	private Button btn2;//	private BMapManager mMapManager;	private MapController mMapController;	private List<GeoPoint> geolist;	private List<GeoPoint> mainList;	// the index of the current event in mAllEvent	private static int mainindex;
	private static int flag=0;	private Overlays overlays;
	private Followers foverlays;	//private List<PopUpOverlay> poplist;	private LocationClient locationClient;	// 所有events	// 存储从服务器获得的所有events信息	private ArrayList<HashMap<String, Object>> mAllEvents;
	// 存储从服务器获得的所有follower信息
	private ArrayList<HashMap<String, Object>> mFollower;	// 当前event//	private HashMap<String, Object> mCurrentEvent;
	/**
	 * 初始化activity
	 * @param instance Bundle
	 * */	@Override	public void onCreate(Bundle savedInstanceState) {		super.onCreate(savedInstanceState);		//获取UID		UID = getSharedPreferences("user", Context.MODE_PRIVATE).getString("uid", null);  //		mMapManager = ((TogetherApp) getApplication()).getMapManager();		setContentView(R.layout.follow_message);		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);		context = this;		initUI();		initMap();		//new Thread(new InitMap()).start();		locationClient=((TogetherApp)getApplication()).getLocationClient();		locationClient.registerLocationListener(new TogetherApp.MyLocationListener(mapView));		locationClient.setLocOption(getlocOption());	}
	/**
	 * 当activity被唤醒时，重新start locationClient
	 * */	protected void onResume() {		super.onResume();		//从服务器获取所有event信息		new RequestAllEvents().execute(null, null, null);
		//向服务器更新自己的位置信息		new RequestFollowers().execute(null, null, null);		locationClient.start();	}
	/**
	 * 当activity被暂停时，关闭locationClient
	 * */	protected void  onPause() {		super.onPause();		locationClient.stop();	}	
	/**
	 * 初始化UI，主要是从layout中获得布局components
	 * 
	 * */	private void initUI() {		eid = (String) getIntent().getSerializableExtra("eid");		place = (String) getIntent().getSerializableExtra("place");		uid = (String) getIntent().getSerializableExtra("uid");		type = (String) getIntent().getSerializableExtra("type");		description = (String) getIntent().getSerializableExtra("description");		longitude = (String) getIntent().getSerializableExtra("longitude");		latitude = (String) getIntent().getSerializableExtra("latitude");		time = getIntent().getSerializableExtra("time").toString().substring(0, 5);		participate = (TextView) findViewById(R.id.participate);		message_in_follow = (TextView) findViewById(R.id.message_in_follow);		String showText = "用户 " + uid + " 于 " + time + " 发起了活动 " 
						+ type + " ,  地点: " + place + "。 " 
						+ "活动描述： " + description;
		if(showText.length() > 63)
			showText = showText.substring(0, 63) + "...";		message_in_follow.setText(showText);		mapView = (MapView) findViewById(R.id.followmap);		btn1=(Button)findViewById(R.id.button1);		// 定位		btn1.setOnClickListener(new Button.OnClickListener(){			@Override			public void onClick(View arg0) {				locationClient.requestLocation();			}		});
		
		btn2=(Button)findViewById(R.id.button2);

		btn2.setOnClickListener(new Button.OnClickListener(){
			@Override
			public void onClick(View arg0) {
				//刷新地图
				mapView.getOverlays().clear();
				if(flag==1){
					try {
						showallfollowers();
					} catch (IOException e) {
						e.printStackTrace();
					}
					if(mFollower==null || mFollower.size()==0) {
						Toast.makeText(getApplicationContext(), "这个活动还没有人参与，赶快参加吧！", Toast.LENGTH_SHORT).show();
						return;
					}
					mapView.getOverlays().add(foverlays);
					mapView.refresh();
                    GeoPoint p=new GeoPoint((int)(Double.parseDouble(latitude)*1e6), (int)(Double.parseDouble(longitude)*1e6));
					mapView.getController().animateTo(p);
					flag=0;
					btn2.setText("周围活动");
				}
				else {
					try {
						showallevents();
					} catch (IOException e) {
						e.printStackTrace();
					}
					if(mAllEvents==null || mAllEvents.size()==0) {
						Toast.makeText(getApplicationContext(), "没有其他活动了", Toast.LENGTH_SHORT).show();
						return;
					}
					mapView.getOverlays().add(overlays);
					mapView.refresh();
					GeoPoint p=new GeoPoint((int)(Double.parseDouble(latitude)*1e6), (int)(Double.parseDouble(longitude)*1e6));
					mapView.getController().animateTo(p);
					flag=1;
					btn2.setText("参与用户");

				}

				
			}
		});		participate.setOnClickListener(new TextView.OnClickListener(){			@Override			public void onClick(View arg0) {				try {					/*eventID， 发起者ID， 参与者（我）ID*/					participate(eid, uid, UID);				} catch (ClientProtocolException e) {					e.printStackTrace();				} catch (JSONException e) {					e.printStackTrace();				} catch (IOException e) {					e.printStackTrace();				}			}		});	}
	
	/**
	 * 异步task，用于返回跟踪者
	 * */
	private class RequestFollowers extends AsyncTask<Void, Void, ArrayList<HashMap<String, Object>>> {
		protected ArrayList<HashMap<String, Object>> doInBackground(Void... params) {
			ArrayList<HashMap<String, Object>> result = null;
			try {
				result = requestFollwers();
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (JSONException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return result;
		}

		protected void onPostExecute(ArrayList<HashMap<String, Object>> result) {
			mFollower = result;

			
	     }
	}
	
	/**
	 * 异步task，用于返回所有事件
	 * */	private class RequestAllEvents extends AsyncTask<Void, Void, ArrayList<HashMap<String, Object>>> {		protected ArrayList<HashMap<String, Object>> doInBackground(Void... params) {			ArrayList<HashMap<String, Object>> result = null;			try {				result = requestAllEvents();			} catch (ClientProtocolException e) {				e.printStackTrace();			} catch (JSONException e) {				e.printStackTrace();			} catch (IOException e) {				e.printStackTrace();			}			return result;		}		protected void onPostExecute(ArrayList<HashMap<String, Object>> result) {			mAllEvents = result;			//刷新地图			mapView.getOverlays().clear();			try {				showallevents();			} catch (IOException e) {				e.printStackTrace();			}			if(overlays==null){				System.out.println("overlay null");			}			mapView.getOverlays().add(overlays);			mapView.refresh();			//mapView.getOverlays().add(mainoverlay);			//mapView.refresh();			mapView.getController().animateTo(geolist.get(0));
			flag=1;	     }	}
	/***
	 * 初始化地图，获取经纬度信息，并显示地图
	 * */	private void initMap() {		// event经纬度		longitude = (String) getIntent().getSerializableExtra("longitude");		latitude = (String) getIntent().getSerializableExtra("latitude");		mMapController = mapView.getController();		mMapController.enableClick(true);		mMapController.setZoom(12);		mapView.displayZoomControls(true);		// mMapView.setTraffic(true);		// mMapView.setSatellite(true);		mapView.setDoubleClickZooming(true);	}
	/**
	 * 从服务器获取followers的信息，并返回arraylist
	 * @exception JSONException,ClientProtocolException,IOException
	 * @return list ArrayList<String,Object>
	 * */
	private ArrayList<HashMap<String, Object>> requestFollwers() throws JSONException, ClientProtocolException, IOException {
		ArrayList<HashMap<String, Object>> followersArray = new ArrayList<HashMap<String, Object>>();
		/*从服务器获取follower信息*/
		JSONObject jo = new JSONObject();
		//TODO 添加真实的用户信息
		jo.put("eid", eid);
		String url = MyConstants.SITE + getString(R.string.ListFollowUser);
		String result = ServerResponse.getResponse(url, jo);
		Log.i("together", "result:" + result);
		JsonHandler jsonHandler = new JsonHandler();
		List<UserMsg> msgs = jsonHandler.getUserMessages(result, "user");
		HashMap<String, Object> map;
		for (UserMsg p : msgs) {
			map = getUserMap(p);
			followersArray.add(map);
		}
		return followersArray;
	}
	
	/***
	 * @return 从服务器获取所有的事件列表
	 * @exception JSONException,ClientProtocolException,IOException
	 * */	private ArrayList<HashMap<String, Object>> requestAllEvents() throws JSONException, ClientProtocolException, IOException {		ArrayList<HashMap<String, Object>> eventsArray = new ArrayList<HashMap<String, Object>>();		/*从服务器获取event信息*/		JSONObject jo = new JSONObject();		//TODO 添加真实的用户信息		jo.put("uid", UID);		jo.put("radius", "0");		String url = MyConstants.SITE + getString(R.string.ListEvent);		String result = ServerResponse.getResponse(url, jo);		JsonHandler jsonHandler = new JsonHandler();		List<EventMsg> msgs = jsonHandler.getEventMessages(result, "event");		HashMap<String, Object> map;		for (EventMsg p : msgs) {			map = getEventMap(p);			eventsArray.add(map);		}		return eventsArray;	}
	
	/**
	 * 从UserMsg对象中获取hash map
	 * @param p userMsg
	 * @return map<String,Object>
	 * */
	private HashMap<String, Object> getUserMap(UserMsg p) {
		HashMap<String, Object> map;
		map = new HashMap<String, Object>();
		map.put("uid", p.getUid());
		map.put("longitude", p.getLongitude());
		map.put("latitude", p.getLatitude());
		return map;
	}
	
	/**
	 * @param p EventMsg
	 * @return map HashMap<String , Object>
	 * 从EventMsg对象中返回hash map
	 * */	private HashMap<String, Object> getEventMap(EventMsg p) {		HashMap<String, Object> map;		map = new HashMap<String, Object>();		map.put("eid", p.getEid());		map.put("place", p.getPlace());		map.put("uid", p.getUid());
		map.put("type", p.getType());
		map.put("description", p.getDescription());		map.put("longitude", p.getLongitude());		map.put("latitude", p.getLatitude());		map.put("startDate", p.getStartDate());		map.put("startTime", p.getStartTime());		map.put("endDate", p.getEndDate());		map.put("endTime", p.getEndTime());		return map;	}
	/**
	 * 获得所有的坐标点
	 * @return points ArrayList<GeoPoint>
	 * */	private  ArrayList<GeoPoint> getgeolist(){		ArrayList<GeoPoint> tempList=new ArrayList<GeoPoint>();		for (int i=0;i<mAllEvents.size();i++){			HashMap<String, Object> item=mAllEvents.get(i);			if(((String)item.get("eid")).equals(eid)){				mainList=new ArrayList<GeoPoint>();				int latitude=(int)(Double.parseDouble((String)item.get("latitude"))*1e6);				int longitude=(int)(Double.parseDouble((String)item.get("longitude"))*1e6);				GeoPoint geoPoint=new GeoPoint(latitude, longitude);				mainList.add(geoPoint);				mainindex=i;				//continue;			}			int latitude=(int)(Double.parseDouble((String)item.get("latitude"))*1e6);			int longitude=(int)(Double.parseDouble((String)item.get("longitude"))*1e6);			GeoPoint geoPoint=new GeoPoint(latitude, longitude);			tempList.add(geoPoint);		}		System.out.println("mainsize: "+mainList.size()+"\n"+"othersize: "+tempList.size());		return tempList;	}	/**
	 * 刷新地图
	 * @throws IOException
	 * */
		public void showallevents() throws IOException{		geolist=getgeolist();    	Drawable d=getResources().getDrawable(R.drawable.other_event);		 		overlays = new Overlays(FollowedMessageActivity.this, d , geolist, mapView,mainindex,mAllEvents);		 	}
	
	/**
	 * 在地图中显示所有的跟踪者
	 * @throws IOException
	 * */
	public void showallfollowers() throws IOException{
		ArrayList<GeoPoint> followers=new ArrayList<GeoPoint>();
		for (int i=0;i<mFollower.size();i++){
			HashMap<String, Object> follower=mFollower.get(i);
			int latitude=(int)(Double.parseDouble((String)follower.get("latitude"))*1e6);
			int longitude=(int)(Double.parseDouble((String)follower.get("longitude"))*1e6);
			GeoPoint point=new GeoPoint(latitude, longitude);
			followers.add(point);
		}
		
		followers.add(new GeoPoint((int)(Double.parseDouble(latitude)*1e6), (int)(Double.parseDouble(longitude)*1e6)));
		
    	Drawable d=getResources().getDrawable(R.drawable.follower);
		foverlays=new Followers(context, d, followers, mapView, mFollower);


	}
	
	/**
	 * 获得当前的位置信息option
	 * @return location LocationClientOption
	 * */	public LocationClientOption getlocOption(){		LocationClientOption option = new LocationClientOption();		//option.setOpenGps(true);		option.setAddrType("all");//返回的定位结果包含地址信息		option.setCoorType("bd09ll");//返回的定位结果是百度经纬度,默认值gcj02		//option.setScanSpan(5000);//设置发起定位请求的间隔时间为5000ms		option.disableCache(true);//禁止启用缓存定位		//option.setPoiNumber(5);	//最多返回POI个数			//option.setPoiDistance(1000); //poi查询距离				//option.setPoiExtraInfo(true); //是否需要POI的电话和地址等详细信息			return option;	}
	/**
	 * 参与某个活动
	 * @param eid event_id
	 * @param sid startUID
	 * @param fid followUID
	 * 
	 * */	private void participate(String eid, String startUid, String followUid) throws JSONException, ClientProtocolException, IOException {		String url = MyConstants.SITE + getString(R.string.Follow);		JSONObject json = new JSONObject();		json.put("eid", eid);		json.put("startUid", startUid);		json.put("followUid", followUid);		//向服务器发送参与请求		String result = ServerResponse.getResponse(url, json);//		Log.i("together", result);		if(result.contains("success"))			Toast.makeText(getApplicationContext(), "参加成功", Toast.LENGTH_SHORT).show();		else			Toast.makeText(getApplicationContext(), "你已经参加了..", Toast.LENGTH_SHORT).show();	}}
