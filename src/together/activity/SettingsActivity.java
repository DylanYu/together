
package together.activity;import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.TextView;
public class SettingsActivity extends Activity{	Context context;	private TextView save;	
	private SharedPreferences preferences;
	private SharedPreferences.Editor editor;
	private int distance_values[] = {0,50,100,200,1000,2000,10000};	private String UID;	private CheckBox checkBox_film;
	private CheckBox checkBox_concert;
	private CheckBox checkBox_eatting;
	private CheckBox checkBox_ktv;	private Spinner mySpinner;
	/**
	 * 创建activity
	 * @param instance Bundle
	 * 
	 * */	public void onCreate(Bundle savedInstanceState) {		super.onCreate(savedInstanceState);		setContentView(R.layout.settings);		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);		context = this;		//获取UID		UID = getSharedPreferences("user", Context.MODE_PRIVATE).getString("uid", null);  		initUI();	}
	
	/**
	 * 初始化UI
	 * */	private void initUI() {		String[] distances = { getString(R.string.distance_less), getString(R.string.distance_50), 				getString(R.string.diestace_100),getString(R.string.distance_200),getString(R.string.distance_1000),						getString(R.string.distance_2000),getString(R.string.distance_more)}; 		mySpinner = (Spinner) findViewById(R.id.event_spinner); 		ArrayAdapter<String> adapter = new ArrayAdapter<String>( this, android.R.layout.simple_spinner_item,distances); 		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); 		mySpinner.setAdapter(adapter);		mySpinner.setOnItemSelectedListener(new SpinnerSelectedListener());  
		checkBox_concert = (CheckBox)findViewById(R.id.checkBox_concert);
		checkBox_eatting = (CheckBox) findViewById(R.id.checkBox_eatting);
		checkBox_film = (CheckBox) findViewById(R.id.checkBox_film);
		checkBox_ktv = (CheckBox) findViewById(R.id.checkBox_ktv);
		preferences = getSharedPreferences("settings", Context.MODE_PRIVATE);
		checkBox_concert.setChecked(preferences.getBoolean("concert", false));
		checkBox_eatting.setChecked(preferences.getBoolean("eatting", false));
		checkBox_film.setChecked(preferences.getBoolean("film", false));
		checkBox_ktv.setChecked(preferences.getBoolean("ktv", false));
		
		save = (TextView) findViewById(R.id.settings_save);
		save.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {				
				  editor = preferences.edit();
				  editor.putInt("distance", distance_values[mySpinner.getSelectedItemPosition()]);
				  editor.putBoolean("film",checkBox_film.isChecked());
				  editor.putBoolean("concert", checkBox_concert.isChecked());
				  editor.putBoolean("eatting", checkBox_eatting.isChecked());
				  editor.putBoolean("ktv", checkBox_ktv.isChecked());
				  editor.commit();
			}
		});	}
	
		/**
	 * Spinner选择事件
	 * */	@SuppressLint("ParserError")
	class SpinnerSelectedListener implements OnItemSelectedListener{  		          public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,                  long arg3) {           }            public void onNothingSelected(AdapterView<?> arg0) {          }      }  }
