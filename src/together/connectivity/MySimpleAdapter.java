/**
 * used to handle listview data
 * @author zhangjie
 * @version 1.0
 * @since 2012-2-13
 * Created on 2012-2-13
 *
 * TODO To handle data of gallery and listview
 * Window - Preferences - Java - Code Style - Code Templates
 */
package together.connectivity;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import together.utils.ImageLoader;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Checkable;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class MySimpleAdapter extends SimpleAdapter {

	private int[] mTo;
	private String[] mFrom;
	private ViewBinder mViewBinder;
	private List<HashMap<String, Object>> mData;
	private int mResource;
	private LayoutInflater mInflater;
	private Context mContext;
	private boolean flag = false;
	private int maxSize = 480;

	/**
	 * get all the data the adapter holds
	 * 
	 * @return a List of HashMap<String , Object> data
	 * */
	public List<HashMap<String, Object>> GetmData() {
		return mData;
	}

	/**
	 * construction of the adapter
	 * 
	 * @param context
	 *            Context of the list view
	 * @param data
	 *            List data
	 * @param resource
	 *            id of the listview
	 * @param from
	 *            string[] name of the items
	 * @param to
	 *            int[] id of the items
	 * @param f
	 *            flag of the listview
	 * */
	public MySimpleAdapter(Context context, List<HashMap<String, Object>> data,
			int resource, String[] from, int[] to, boolean f) {
		super(context, data, resource, from, to);
		mContext = context;
		mData = data;
		mResource = resource;// 是listview的每一个子项的id
		mFrom = from;
		mTo = to;
		mInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		flag = f;

	}

	/***
	 * construtor with params
	 * @param context Context
	 * @param data List<HashMap<String,Object>>
	 * @param res resource 
	 * @param from fromArray
	 * @param to toArray
	 * @param f flag
	 * @param m maxsize
	 */
	public MySimpleAdapter(Context context, List<HashMap<String, Object>> data,
			int resource, String[] from, int[] to, boolean f, int m) {
		super(context, data, resource, from, to);
		mContext = context;
		mData = data;
		mResource = resource;// 是list view的每一个子项的id
		mFrom = from;
		mTo = to;
		mInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		flag = f;
		maxSize = m;
	}

	/***
	 * constructor with params
	 * @param context Context
	 * @param data List<HashMap<String,Object>>
	 * @param res resource 
	 * @param from fromArray
	 * @param to toArray
	 * @param m maxSize
	 */
	public MySimpleAdapter(Context context, List<HashMap<String, Object>> data,
			int resource, String[] from, int[] to, int m) {
		super(context, data, resource, from, to);
		mContext = context;
		mData = data;
		mResource = resource;// 是listview的每一个子项的id
		mFrom = from;
		mTo = to;
		mInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		maxSize = m;
	}

	/**
	 * construction of the adapter
	 * 
	 * @param context
	 *            Context of the list view
	 * @param data
	 *            List data
	 * @param resource
	 *            id of the listview
	 * @param from
	 *            string[] name of the items
	 * @param to
	 *            int[] id of the items
	 * */
	public MySimpleAdapter(Context context, List<HashMap<String, Object>> data,
			int resource, String[] from, int[] to) {
		super(context, data, resource, from, to);
		mContext = context;
		mData = data;
		mResource = resource;// 是listview的每一个子项的id
		mFrom = from;
		mTo = to;
		mInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	/**
	 * return the size of the data
	 * 
	 * @return size of the listview
	 * */
	public int getCount() {
		if (mData != null)
			return mData.size();
		else
			return 0;
	}

	/**
	 * get the object of the selected position
	 * 
	 * @param pos
	 *            position of the selected item
	 * @return object in the position
	 * */
	public Object getItem(int position) {
		if (mData != null)
			return mData.get(position);
		else
			return null;
	}

	/**
	 * return the object id of the given position
	 * 
	 * @param pos
	 *            position
	 * @return object id of the position
	 * */
	public long getItemId(int position) {
		return position;
	}

	/**
	 * add data to the List
	 * 
	 * @param map
	 *            HashMap<String,Object>
	 * @param bottom
	 *            boolean
	 * @return null
	 */
	public void addItem(HashMap<String, Object> map, boolean bottom) {
		if (mData != null) {
			if (bottom)
				mData.add(map);
			else
				mData.add(0, map);
		}
	}

	/**
	 * delete item in the location
	 * 
	 * @param location
	 *            position deleted
	 * 
	 * */
	public void deleteItem(int location) {
		if (mData != null)
			mData.remove(location);
	}

	/**
	 * return the view of the given position
	 * 
	 * @param pos
	 *            position
	 * @param view
	 *            convertView
	 * @param parent
	 *            ViewGroup
	 * */
	public View getView(int position, View convertView, ViewGroup parent) {
		return createViewFromResource(position, convertView, parent, mResource);
	}

	/***
	 * create view From Resource
	 * @param pos position
	 * @param convertView View
	 * @param parent ViewGroup
	 * @param res resource
	 * @return view View
	 */
	private View createViewFromResource(int position, View convertView,
			ViewGroup parent, int resource) {
		View v;
		if (convertView == null) {
			v = mInflater.inflate(resource, parent, false);
			final int[] to = mTo;
			final int count = to.length;
			final View[] holder = new View[count];
			for (int i = 0; i < count; i++) {
				holder[i] = v.findViewById(to[i]);
			}
			v.setTag(holder);
		} else {
			v = convertView;
		}
		bindView(position, v);
		return v;
	}

	/***
	 * bind view
	 * @param pos position
	 * @param view View
	 */
	private void bindView(int position, View view) {
		@SuppressWarnings("rawtypes")
		final Map dataSet = mData.get(position);
		if (dataSet == null) {
			return;
		}
		final ViewBinder binder = mViewBinder;
		final View[] holder = (View[]) view.getTag();
		final String[] from = mFrom;
		final int[] to = mTo;
		final int count = to.length;

		for (int i = 0; i < count; i++) {
			final View v = holder[i];
			if (v != null) {
				final Object data = dataSet.get(from[i]);
				String text = data == null ? "" : data.toString();
				if (text == null) {
					text = "";
				}
				boolean bound = false;
				if (binder != null) {
					bound = binder.setViewValue(v, data, text);
				}
				if (!bound) {
					if (v instanceof Checkable) {
						if (data instanceof Boolean) {
							((Checkable) v).setChecked((Boolean) data);
						} else {
							throw new IllegalStateException(v.getClass()
									.getName()
									+ " should be bound to a Boolean, not a "
									+ data.getClass());
						}
					} else if (v instanceof ImageView) {
						if (data instanceof Integer) {
							setViewImage((ImageView) v, (Integer) data);
						} else {
							setViewImage((ImageView) v, text);
						}

					} else if (v instanceof ProgressBar) {
						ProgressBar pb = (ProgressBar) v;
						pb.setMax(100);
						if (data instanceof Float) {
							Float t = (Float) data * 100;
							pb.setProgress((int) Math.floor(t));
						}
					} else if (v instanceof Button) {
						setViewText((TextView) v, text);
						Button button = (Button) v;
						// otherwise button will fetch the focus
						button.setFocusable(false);
						button.setFocusableInTouchMode(false);
					} else if (v instanceof TextView) {
						setViewText((TextView) v, text);
					} else {
						throw new IllegalStateException(
								v.getClass().getName()
										+ " is not a "
										+ " view that can be bounds by this SimpleAdapter");
					}
				}
			}
		}
	}

	 
	/**
	 * Called by bindView() to set the image for an ImageView but only if there
	 * is no existing ViewBinder or if the existing ViewBinder cannot handle
	 * binding to an ImageView.
	 * 
	 * This method is called instead of {@link #setViewImage(ImageView, String)}
	 * if the supplied data is an int or Integer.
	 * 
	 * @param v
	 *            ImageView to receive an image
	 * @param value
	 *            the value retrieved from the data set
	 * 
	 * @see #setViewImage(ImageView, String)
	 */
	public void setViewImage(ImageView v, int value) {
		v.setImageResource(value);
	}

	/**
	 * Called by bindView() to set the image for an ImageView but only if there
	 * is no existing ViewBinder or if the existing ViewBinder cannot handle
	 * binding to an ImageView.
	 * 
	 * By default, the value will be treated as an image resource. If the value
	 * cannot be used as an image resource, the value is used as an image Uri.
	 * 
	 * This method is called instead of {@link #setViewImage(ImageView, int)} if
	 * the supplied data is not an int or Integer.
	 * 
	 * @param v
	 *            ImageView to receive an image
	 * @param value
	 *            the value retrieved from the data set
	 * 
	 * @see #setViewImage(ImageView, int)
	 */
	public void setViewImage(ImageView v, String value) {
		ImageLoader imageLoader = new ImageLoader(mContext);
		if (flag == false)
			imageLoader.DisplayImage(value, v, maxSize);
		else
			imageLoader.DisplayImage(value, v, true, maxSize);
	}

}
