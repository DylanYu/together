
package together.activity; import java.io.IOException;import org.apache.http.ParseException;import org.json.JSONException;import org.json.JSONObject;import together.connectivity.ServerResponse;import together.utils.MyConstants;import together.utils.MyLocation;import android.app.Activity;import android.content.Context;import android.content.pm.ActivityInfo;import android.location.Location;import android.location.LocationManager;import android.os.Bundle;import android.util.Log;import android.view.View;import android.view.View.OnClickListener;import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;import android.widget.AdapterView.OnItemSelectedListener;import android.widget.ArrayAdapter;import android.widget.EditText;import android.widget.Spinner;import android.widget.TextView;import android.widget.TimePicker;
import android.widget.Toast;public class SendMessageActivity extends Activity{		private Context context;	private TextView sendMessageText;	private EditText placeEditText;//	private EditText timeEditText;	private EditText descriptionEditText;	private TimePicker timePicker;	private String UID;	private LocationManager locationManager;	private String eventType;	private String[] eventsArray = new String[6];		private Spinner mySpinner;
	
	/**
	 * 初始化activity
	 * @param intance Bundle
	 * 
	 * */	@Override	public void onCreate(Bundle savedInstanceState) {		super.onCreate(savedInstanceState);		setContentView(R.layout.send_message);		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);		context = this;		//获取UID		UID = getSharedPreferences("user", Context.MODE_PRIVATE).getString("uid", null);  		initUI();		//Location Service		locationManager = (LocationManager)this.getSystemService(Context.LOCATION_SERVICE);	}	
	/**
	 * 初始化UI
	 * */	private void initUI() {		eventsArray[0] = getString(R.string.event_film);		eventsArray[1] = getString(R.string.event_drama);		eventsArray[2] = getString(R.string.event_concert);		eventsArray[3] = getString(R.string.event_lecture);		eventsArray[4] = getString(R.string.event_ktv);		eventsArray[5] = getString(R.string.event_other); 		sendMessageText = (TextView) findViewById(R.id.sendMessageText);		placeEditText = (EditText) findViewById(R.id.place_edit);//		timeEditText  = (EditText) findViewById(R.id.time_edit);		descriptionEditText = (EditText) findViewById(R.id.my_event_edit);		timePicker = (TimePicker) findViewById(R.id.timePicker);		mySpinner = (Spinner) findViewById(R.id.event_spinner);		ArrayAdapter<String> adapter = new ArrayAdapter<String>( this, android.R.layout.simple_spinner_item,eventsArray); 		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); 		mySpinner.setAdapter(adapter);		mySpinner.setOnItemSelectedListener(new SpinnerSelectedListener());  				sendMessageText.setOnClickListener(new ClickListener());	}	
	/**
	 * 发送按钮的点击事件
	 * */	class ClickListener implements OnClickListener{		@Override		public void onClick(View v) {			String place = placeEditText.getText().toString();			String time = timePicker.getCurrentHour()+""+timePicker.getCurrentMinute()+"00";
			String type = eventType;			String description = descriptionEditText.getText().toString();
			Animation shakeAnim = AnimationUtils.loadAnimation(context,
					R.anim.shake_x);
			if (place == null || place.trim().equals("")) {
				placeEditText.startAnimation(shakeAnim);
				Toast.makeText(SendMessageActivity.this, R.string.fill_event_place,
						Toast.LENGTH_SHORT).show();
				return;
			}
			if (description == null || description.trim().equals("")) {
				descriptionEditText.startAnimation(shakeAnim);
				Toast.makeText(SendMessageActivity.this, R.string.fill_event_description,
						Toast.LENGTH_SHORT).show();
				return;
			}			try {				post(place,time,type,description);			} catch (Exception e) {				e.printStackTrace();				Log.i("together", e.toString());			}		}		private void post(String place, String time, String type, String description) throws ParseException, JSONException, IOException {			Location location = new MyLocation(locationManager).getLoaction();			String longitude = Double.toString(location.getLongitude());			String latitude = Double.toString(location.getLatitude());			//TODO 获取uid			String uid = UID;			String url = MyConstants.SITE + getString(R.string.NewEvent);			JSONObject json = new JSONObject();			json.put("uid", uid);			json.put("place", place);			json.put("type", type);			json.put("description", description);			json.put("longitude", longitude);			json.put("latitude", latitude);			//现阶段只设置开始时间			json.put("startTime", time);			//向服务器发送活动信息			String result = ServerResponse.getResponse(url, json);			if(result.contains("success"))			{
				Toast.makeText(getApplicationContext(), "发布成功", Toast.LENGTH_SHORT).show();
				finish();
			}			else				Toast.makeText(getApplicationContext(), "发布失败", Toast.LENGTH_SHORT).show();		}	}	
	/***
	 * spinner的选择事件
	 * */	class SpinnerSelectedListener implements OnItemSelectedListener{  		          public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,                  long arg3) {        	eventType = eventsArray[arg2];         }            public void onNothingSelected(AdapterView<?> arg0) {          }      }  }
