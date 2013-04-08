package together.utils;


import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import together.activity.R;
import together.activity.TogetherApp;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.widget.Toast;

import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.map.ItemizedOverlay;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.OverlayItem;
import com.baidu.mapapi.map.PopupClickListener;
import com.baidu.mapapi.map.PopupOverlay;
import com.baidu.mapapi.search.MKPlanNode;
import com.baidu.platform.comapi.basestruct.GeoPoint;

public class Overlays extends ItemizedOverlay<OverlayItem>{

	private List<OverlayItem> overlaylist=new ArrayList<OverlayItem>();
	 
	private Context context;
	private MapView mapView;
	private static int selected=0;
	private BMapManager mapManager;

	public Overlays(Context context,Drawable d,List<GeoPoint> list,MapView mapView,int mainindex,ArrayList<HashMap<String, Object>> allevents) throws IOException {
		super(d);
		this.context=context;
		this.mapView=mapView;
		mapManager=((TogetherApp)context.getApplicationContext()).getMapManager();
		 

		for (int i=0;i<list.size();i++){
			HashMap<String, Object> event=allevents.get(i);
			OverlayItem item=new OverlayItem(list.get(i), "event: "+(String)event.get("eid"),(String)event.get("description"));
			overlaylist.add(item);
			 

		}

		overlaylist.get(mainindex).setMarker(context.getResources().getDrawable(R.drawable.current_event));

		populate();


	}
	

	@Override
	protected OverlayItem createItem(int i) {
 		return overlaylist.get(i);
	}

	@Override
	public int size() {
 		return overlaylist.size();
	}



	protected boolean onTap(int j) {
		System.out.println("ontap: "+j);
		final int i=j;
		selected=j;
		Toast.makeText(context, "活动:" + overlaylist.get(i).getSnippet(), Toast.LENGTH_SHORT).show();
		mapView.getController().animateTo(overlaylist.get(i).getPoint());
		//popList.get(i).change();

		PopupOverlay p=new PopupOverlay(mapView, new PopupClickListener() {


			public void onClickedPopup(int index) {
 				System.out.println(selected+" "+overlaylist.get(selected).getSnippet());
				Toast.makeText(context, "活动:" + overlaylist.get(selected).getSnippet(), Toast.LENGTH_SHORT).show();
				MySearch mySearch=new MySearch(context, mapView, mapManager);
				MKPlanNode start=new MKPlanNode();
				MKPlanNode end=new MKPlanNode();
				start.pt=TogetherApp.getMyLocation();
				end.pt=overlaylist.get(selected).getPoint();
				if(start.pt==null || end.pt==null){
					System.out.println("search point null");
					return;
				}
				mySearch.walkingSearch(null, start, null, end);
			}
		});	



		Bitmap bmp=null;
		try {
			bmp = BitmapFactory.decodeStream(context.getAssets().open("pop.png"));
		} catch (IOException e) {
 			e.printStackTrace();
		}

		 
		p.showPopup(bmp, overlaylist.get(i).getPoint(), 100);

		 
		return true;
	}



}
