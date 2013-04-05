package together.activity;import java.io.IOException;import java.util.ArrayList;import java.util.HashMap;import java.util.List;import org.apache.http.client.ClientProtocolException;import org.json.JSONException;import org.json.JSONObject;import together.connectivity.JsonHandler;import together.connectivity.MySimpleAdapter;import together.connectivity.ServerResponse;import together.models.EventMsg;import together.utils.AssetsUtil;import together.utils.MyConstants;import together.widgets.PullToRefreshList;import android.annotation.SuppressLint;import android.app.ListActivity;import android.app.ProgressDialog;import android.content.Context;import android.content.Intent;import android.content.pm.ActivityInfo;import android.os.AsyncTask;import android.os.Bundle;import android.os.Handler;import android.os.Message;import android.util.Log;import android.view.View;import android.view.View.OnClickListener;import android.widget.AdapterView;import android.widget.AdapterView.OnItemClickListener;import android.widget.TextView;@SuppressLint("ParserError")public class HomeActivity extends ListActivity {	private MySimpleAdapter listAdapter;	private ProgressDialog progressDialog;	private Context context;	private View loadMoreView;	private TextView loadMoreButton;	private TextView sendText;	private PullToRefreshList listView;	private ArrayList<HashMap<String, Object>> listArray = new ArrayList<HashMap<String, Object>>();	private HashMap<String, Object> map_use;	private boolean bottomFlag = false;	// private int last_id = 0;	public void onDestroy() {		if (progressDialog != null)			progressDialog.dismiss();		super.onDestroy();	}	@Override	public void onCreate(Bundle savedInstanceState) {		super.onCreate(savedInstanceState);		setContentView(R.layout.home);		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);		context = this;		initUI();		try {			progressDialog.show();			buildlist();		} catch (Exception e) {			e.printStackTrace();		}	}	private void initUI() {		listView = ((PullToRefreshList) getListView());		listView.setCacheColorHint(0);		progressDialog = new ProgressDialog(context);		progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);		progressDialog.setIcon(R.drawable.loading);		progressDialog.setMessage(getString(R.string._loading));		loadMoreView = getLayoutInflater().inflate(R.layout.loadmore, null);		loadMoreButton = (TextView) loadMoreView				.findViewById(R.id.loadMoreButton);		loadMoreButton.setText(R.string._more);		sendText = (TextView) findViewById(R.id.sendText);		sendText.setOnClickListener(new OnClickListener() {			@Override			public void onClick(View v) {				Intent itent = new Intent(HomeActivity.this,						SendMessageActivity.class);				startActivity(itent);			}		});		loadMoreButton.setOnClickListener(new View.OnClickListener() {			@Override			public void onClick(View v) {				loadMoreButton.setText(R.string._loading);				progressDialog.show();				new ClickGetDataTask().execute();			}		});		listView.addFooterView(loadMoreView);		listView.setOnRefreshListener(new together.widgets.PullToRefreshList.OnRefreshListener() {			@Override			public void onRefresh() {				new PullGetDataTask().execute();			}		});	}	private class PullGetDataTask extends AsyncTask<Void, Void, String[]> {		@Override		protected String[] doInBackground(Void... params) {			try {				bottomFlag = false;				buildlist();			} catch (JSONException e) {				e.printStackTrace();			} catch (IOException e) {				e.printStackTrace();			}			return null;		}		@Override		protected void onPostExecute(String[] result) {			// Call onRefreshComplete when the list has been refreshed.			((PullToRefreshList) getListView()).onRefreshComplete();			super.onPostExecute(result);		}	}	/**	 * async task , used when click load more button of list view to refresh data	 * */	private class ClickGetDataTask extends AsyncTask<Void, Void, String[]> {		@Override		protected String[] doInBackground(Void... params) {			// Simulates a background job.			bottomFlag = true;			loadMoreData(true);// add to bottom			return null;		}		@Override		protected void onPostExecute(String[] result) {			// Call onRefreshComplete when the list has been refreshed.			progressDialog.cancel();			super.onPostExecute(result);		}	}	private String msgGet = "";	private Handler listHandler = new Handler() {// this is used to generate the													// groups listview		public void handleMessage(Message msg) {			switch (msg.what) {			case MyConstants.MSG_SUCCESS1:				@SuppressWarnings("unchecked")				final ArrayList<HashMap<String, Object>> array = (ArrayList<HashMap<String, Object>>) msg.obj;				loadMoreButton.setText(R.string._more);				listAdapter = new MySimpleAdapter(context, array,						R.layout.list_item, new String[] { "place", "time",								"type" }, new int[] { R.id.sItemTitle,								R.id.sItemTime, R.id.sItemInfo }, 120);				listView.setAdapter(listAdapter);				listArray = array;				listView.setVisibility(View.GONE);				listAdapter.notifyDataSetChanged();				listView.setVisibility(View.VISIBLE);				if (bottomFlag)					listView.setSelectionFromTop(							listAdapter.GetmData().size() - 1, 150);				progressDialog.cancel();				listView.setOnItemClickListener(new OnItemClickListener() {					@Override					public void onItemClick(AdapterView<?> arg0, View arg1,							int arg2, long arg3) {						itemClick(arg2);					}					private void itemClick(int arg2) {						if (arg2 > listArray.size()) {							return;						}						Intent intent = new Intent();						if (arg2 > 0)							arg2--;						intent.putExtra("name", (String) listArray.get(arg2)								.get("name"));						intent.putExtra("time", (String) listArray.get(arg2)								.get("time"));						intent.putExtra("event", (String) listArray.get(arg2)								.get("event"));						intent.setClass(HomeActivity.this,								FollowedMessageActivity.class);						startActivity(intent);					}				});				break;			case MyConstants.MSG_FAILURE:				progressDialog.cancel();				if (msgGet == null || msgGet.equals("[]")) {					loadMoreButton.setText(R.string.no_more);					if (bottomFlag)						listView.setSelectionFromTop(listAdapter.GetmData()								.size() - 1, 150);				} else					loadMoreButton.setText(R.string._more);				break;			}		}	};	private void loadMoreData(boolean bottom) {		try {			add_list(bottom);		} catch (JSONException e) {			e.printStackTrace();		} catch (IOException e) {			e.printStackTrace();		}	}	private void buildlist() throws JSONException, IOException {		final ArrayList<HashMap<String, Object>> Array = new ArrayList<HashMap<String, Object>>();		new Thread() {			public void run() {				String s = null;				try {					JSONObject jo = new JSONObject();					//TODO 添加真实的用户信息					jo.put("uid", "0");					jo.put("radius", "0");					/*从服务器获取event信息*/					String url = MyConstants.SITE + getString(R.string.ListEvent);					s = ServerResponse.getResponse(url, jo);					if (s == null) {						msgGet = s;						listHandler.obtainMessage(MyConstants.MSG_FAILURE)								.sendToTarget();						return;					}					if (s.equals("[]")) {						// 说明没有更多新闻了，此时msgGet="[]"						msgGet = s;						listHandler.obtainMessage(MyConstants.MSG_FAILURE)								.sendToTarget();						return;					}					JsonHandler jsonHandler = new JsonHandler();					List<EventMsg> msgs = jsonHandler.getMessages(s, "event");					HashMap<String, Object> map;					for (EventMsg p : msgs) {						map = getMap(p);						Array.add(map);					}				} catch (JSONException e) {					listHandler.obtainMessage(MyConstants.MSG_FAILURE)							.sendToTarget();					e.printStackTrace();					Log.i("together", e.toString());				} catch (ClientProtocolException e) {					e.printStackTrace();					Log.i("together", e.toString());				} catch (IOException e) {					e.printStackTrace();					Log.i("together", e.toString());				}				if (Array.size() == 0) {					listHandler.obtainMessage(MyConstants.MSG_FAILURE)							.sendToTarget();				} else {					listHandler.obtainMessage(MyConstants.MSG_SUCCESS1, Array)							.sendToTarget();				}			}		}.start();	}	private HashMap<String, Object> getMap(EventMsg p) {		//TODO 获取正确信息		HashMap<String, Object> map;		map = new HashMap<String, Object>();		map.put("eid", p.getEid());		map.put("time", p.getStartTime());		map.put("place", p.getPlace());		map.put("type",  p.getType());		return map;	}	private void add_list(final boolean bottom) throws JSONException,			IOException {		String s = AssetsUtil.getFromAssets(context, "star.json");		// String http_url = "/stars.json?start=" + last_star_id + "&count="		// + MyConstants.STAR;		// s = MyHttpResponse.getResponse(http_url);		if (s == null) {			Log.d("StarActivity", "null response");			msgGet = s;			listHandler.obtainMessage(MyConstants.MSG_FAILURE).sendToTarget();			return;		}		if (s.equals("[]")) {			// 说明没有更多新闻了，此时msgGet="[]"			msgGet = s;			listHandler.obtainMessage(MyConstants.MSG_FAILURE).sendToTarget();			return;		}		JsonHandler jsonHandler = new JsonHandler();		List<EventMsg> msgs = jsonHandler.getMessages(s);		ArrayList<HashMap<String, Object>> array = new ArrayList<HashMap<String, Object>>();		for (int i = 0; i < listAdapter.GetmData().size(); i++)			array.add(listAdapter.GetmData().get(i));		for (EventMsg p : msgs) {			map_use = getMap(p);			array.add(map_use);		}		// last_id += msgs.size();		if (msgs.size() == 0) {			listHandler.obtainMessage(MyConstants.MSG_FAILURE).sendToTarget();		} else {			listHandler.obtainMessage(MyConstants.MSG_SUCCESS1, array)					.sendToTarget();		}	}	 }