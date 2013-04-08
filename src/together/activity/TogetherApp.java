package together.activity;


import android.app.Application;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.MKGeneralListener;
import com.baidu.mapapi.map.LocationData;
import com.baidu.mapapi.map.MKEvent;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationOverlay;
import com.baidu.platform.comapi.basestruct.GeoPoint;

public class TogetherApp extends Application{

	private static TogetherApp mApp=null;
	private BMapManager mMapManager=null;
	private final String key="1F2D99F56D2766A3C32DD06ED9EBDB603595634C";
	private static LocationClient mLocationClient=null;
	private static GeoPoint mylocation; 


	public void onCreate(){
		super.onCreate();
		mApp=this;
		initMapManager(this);
		mLocationClient=new LocationClient(this);

	}

	public void onTerminate(){
		if (mMapManager!=null)
			mMapManager.destroy();
		mMapManager=null;
		super.onTerminate();
	}

	public void initMapManager(Context context){

		Log.v("app", "init mapmanager");
		if(mMapManager==null)
			mMapManager=new BMapManager(context);
		if(!mMapManager.init(key, new MyGeneralListener())){
			Toast.makeText(context, "mMapManager init error!", Toast.LENGTH_LONG).show();
		}

	}

	public static Application getInstance(){
		return mApp;
	}

	public BMapManager getMapManager(){
		return mMapManager;
	}

	public static LocationClient getLocationClient(){
		return mLocationClient;
	}

	public static GeoPoint getMyLocation(){
		return mylocation;
	}

	// 常用事件监听，用来处理通常的网络错误，授权验证错误等
    static class MyGeneralListener implements MKGeneralListener {
        
        @Override
        public void onGetNetworkState(int iError) {
            if (iError == MKEvent.ERROR_NETWORK_CONNECT) {
                Toast.makeText(TogetherApp.getInstance().getApplicationContext(), "您的网络出错啦！",
                    Toast.LENGTH_LONG).show();
            }
            else if (iError == MKEvent.ERROR_NETWORK_DATA) {
                Toast.makeText(TogetherApp.getInstance().getApplicationContext(), "输入正确的检索条件！",
                        Toast.LENGTH_LONG).show();
            }

        }

        @Override
        public void onGetPermissionState(int iError) {
            if (iError ==  MKEvent.ERROR_PERMISSION_DENIED) {
                //授权Key错误：
                Toast.makeText(TogetherApp.getInstance().getApplicationContext(), 
                        "请在 DemoApplication.java文件输入正确的授权Key！", Toast.LENGTH_LONG).show();
            }
        }
    }
    
    static class MyLocationListener implements BDLocationListener{
    	
    	private MapView mapView;
    	
    	public MyLocationListener(MapView mapView){
    		this.mapView=mapView;
    	}

		@Override
		public void onReceiveLocation(BDLocation arg0) {
			// TODO Auto-generated method stub
			if(arg0==null){
				System.out.println("location null");
				return;
			}
			//System.out.println("lala"+arg0.getLongitude());

			//Toast.makeText(getInstance(), arg0.getLongitude()+", "+arg0.getLatitude(),Toast.LENGTH_SHORT  );
			GeoPoint newlocation=new GeoPoint((int)(arg0.getLatitude()*1e6),(int) (arg0.getLongitude()*1e6));
			mylocation=newlocation;
			MyLocationOverlay mylocOverlay=new MyLocationOverlay(mapView);
			LocationData locData=new LocationData();
			locData.latitude=arg0.getLatitude();
			locData.longitude=arg0.getLongitude();
			mylocOverlay.setData(locData);
			mylocOverlay.enableCompass();
			mapView.getOverlays().add(mylocOverlay);
			mapView.refresh();
			mapView.getController().animateTo(mylocation);
		}

		@Override
		public void onReceivePoi(BDLocation arg0) {
			// TODO Auto-generated method stub

		}
    	
    }

}