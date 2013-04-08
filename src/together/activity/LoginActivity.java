/**
 * the login page
 * @author zhangjie
 * @version 1.0
 * @since 2012-2-13
 * 
 * */
package together.activity;

import java.io.File;

import together.utils.MyConstants;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends Activity {
	private Context context;
	private TextView login;
	private TextView register;
	private EditText uid;
	private EditText pwd;
	private ProgressDialog progressDialog;

	// private SharedPreferences preferences;
	// private SharedPreferences.Editor editor;

	/**
	 * called when the activity is created
	 * 
	 * @param bundle
	 *            saved instance state
	 */
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		context = this;

		// /*如果已经登录，无需再输入登录信息*/
		// String uid = getSharedPreferences("user",
		// Context.MODE_PRIVATE).getString("user", "NOT_LOGGED");
		// Intent intent = null;
		// Log.i("together", uid);
		// if(!uid.equals("NOT_LOGGED")) {
		// intent = new Intent(LoginActivity.this,TabsActivity.class);
		// startActivity(intent);
		// }
		initUI();

		setClickListener();
	}

	// @Override
	// public void onResume() {
	//
	// }

	/**
	 * 初始化UI界面
	 * */
	private void initUI() {
		login = (TextView) findViewById(R.id.login_login);
		register = (TextView) findViewById(R.id.login_register);
		uid = (EditText) findViewById(R.id.login_account);
		pwd = (EditText) findViewById(R.id.login_password);
		progressDialog = new ProgressDialog(context);
		progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progressDialog.setIcon(R.drawable.loading);
		progressDialog.setMessage(getString(R.string._loading));
	}

	/**
	 * 设置点击事件
	 * */
	private void setClickListener() {

		register.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(LoginActivity.this,
						RegisterActivity.class);
				startActivity(intent);
				finish();
			}
		});

		login.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				clickLogin();
			}

		});
	}

	/**
	 * 检查login是否合法
	 * */
	private void clickLogin() {
		Animation shakeAnim = AnimationUtils.loadAnimation(context,
				R.anim.shake_x);
		String name_str = uid.getText().toString();
		String pwd_str = pwd.getText().toString();
		if (name_str == null || name_str.trim().equals("")) {
			uid.startAnimation(shakeAnim);
			progressDialog.cancel();
			Toast.makeText(LoginActivity.this, R.string.login_miss_username,
					Toast.LENGTH_SHORT).show();
			return;
		}
		if (!(name_str.equals("朱荣飞") || name_str.equals("余东亮") 
				|| name_str.equals("张婕") || name_str.equals("曹老师")
				|| name_str.equals("余老师") || name_str.equals("马老师")
				|| name_str.equals("胡老师") || name_str.equals("tester")
				|| name_str.equals("某同学"))) {
			uid.startAnimation(shakeAnim);
			progressDialog.cancel();
			Toast.makeText(LoginActivity.this, "请输入正确的测试账号",
					Toast.LENGTH_SHORT).show();
			return;
		}
		if (pwd_str == null || pwd_str.trim().equals("")) {
			pwd.startAnimation(shakeAnim);
			progressDialog.cancel();
			Toast.makeText(LoginActivity.this, R.string.login_miss_password,
					Toast.LENGTH_SHORT).show();
			return;
		}
		final String name1 = name_str.trim();
		final String pwd1 = pwd_str.trim();
		progressDialog.show();

		// Editor sharedata = getSharedPreferences("user",
		// Context.MODE_PRIVATE).edit().putString("uid",name1).commit();
		// sharedata.putString("uid",name1);
		// sharedata.commit();
		loginVarify(name1, pwd1);
	}

	/**
	 * 验证登录
	 * @param uid userid
	 * @param pwd user_pwd
	 * */
	private void loginVarify(final String uid, final String pwd1) {
		
		handler.obtainMessage(MyConstants.MSG_SUCCESS1).sendToTarget();
		getSharedPreferences("user", Context.MODE_PRIVATE).edit()
				.putString("uid", uid).commit();

	}

	/**
	 * 显示登录信息
	 * */
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MyConstants.MSG_SUCCESS1:
				progressDialog.cancel();

				Intent intent = new Intent(LoginActivity.this,
						TabsActivity.class);

				startActivity(intent);
				finish();

				break;
			case MyConstants.MSG_FAILURE:
				progressDialog.cancel();
				String str = (String) msg.obj;
				Toast.makeText(LoginActivity.this,
						getString(R.string.login_fail) + " " + str,
						Toast.LENGTH_SHORT).show();
				break;
			}
		}
	};

}
