/**
 * handle the gallery 
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

import together.utils.ReflectionImageLoader;
import together.widgets.*;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Checkable;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class GalleryAdapter extends SimpleAdapter {

	private int[] mTo;
	private String[] mFrom;
	private ViewBinder mViewBinder;
	private List<HashMap<String, Object>> mData;
	private int mResource;
	private LayoutInflater mInflater;
	private Context mContext;
	private int screenWidth;
	// private int screenHeight;
	private float screenDensity;
	private int imageWidth = 0;
	private int imageHeight = 0;

	/***
	 * get image width
	 * @return width imageWidth
	 */
	public int getImageWidth() {
		return imageWidth;
	}

	/***
	 * get image height
	 * @return height imageHeight
	 */
	public int getImageHeight() {
		return imageHeight;
	}

	/**
	 * return all the data the adapter holds
	 * 
	 * @return List< HashMap<String , Object>>
	 * */
	public List<HashMap<String, Object>> GetmData() {
		return mData;
	}

	/**
	 * construction of the adapter
	 * 
	 * @param context
	 *            the context of the gallery
	 * @param data
	 *            List data
	 * @param resource
	 *            id of the layout resource
	 * @param from
	 *            string[] of resource name
	 * @param to
	 *            int[] of resource id
	 * 
	 * */
	ReflectionImageLoader imageLoader;

	/***
	 * constructor
	 * @param context Context
	 * @param data List<HashMap<String,Object>>
	 * @param res resource 
	 * @param from fromArray
	 * @param to ToArray
	 * @param w width
	 * @param h height
	 * @param d density
	 */
	public GalleryAdapter(Context context, List<HashMap<String, Object>> data,
			int resource, String[] from, int[] to, int w, int h, float d) {
		super(context, data, resource, from, to);
		mContext = context;
		mData = data;
		mResource = resource;
		mFrom = from;
		mTo = to;
		mInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		screenWidth = w;
		// screenHeight = h;
		screenDensity = d;
		imageLoader = new ReflectionImageLoader(mContext);
	}

	/**
	 * return the size of the data
	 * 
	 * @return size
	 * 
	 * */
	public int getCount() {
		return Integer.MAX_VALUE;// /mData.size();
	}

	/**
	 * return the object of the selected position
	 * 
	 * @param pos
	 *            position selected
	 * @return data in the selected position
	 * 
	 * */
	public Object getItem(int position) {
		if (mData.size() > 0)
			position = position % mData.size();
		return mData.get(position);
	}

	/**
	 * return the object id of the given position
	 * 
	 * @param pos
	 *            position selected
	 * @return data in the selected position
	 * 
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
	 *            boolean value tells bottom or not
	 * 
	 */
	public void addItem(HashMap<String, Object> map, boolean bottom) {
		if (bottom)
			mData.add(map);
		else
			mData.add(0, map);
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
	 * 
	 * */
	public View getView(int position, View convertView, ViewGroup parent) {
		return createViewFromResource(position, convertView, parent, mResource);
	}

	/***
	 * create view from resource
	 * @param position imagePosition
	 * @param convertView View 
	 * @param parent ViewGroup
	 * @param res resource 
	 * @return
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
		final Map dataSet = mData.get(position % mData.size());
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
					} else if (v instanceof Button) {
						setViewText((TextView) v, text);
						Button button = (Button) v;
						// otherwise button will fetch the focus
						button.setFocusable(false);
						button.setFocusableInTouchMode(false);
					} else if (v instanceof TextView) {
						// Note: keep the instanceof TextView check at the
						// bottom of these
						// ifs since a lot of views are TextViews (e.g.
						// CheckBoxes).
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
		ReflectionImageLoader imageLoader = new ReflectionImageLoader(mContext);
		View v1 = (View) v.getParent(); // v1 = gallery
		int galleryWidth = screenWidth * 2 / 3;
		if (screenWidth == 240)
			galleryWidth = 240;
		imageWidth = imageLoader.getImageWidth();
		imageHeight = imageLoader.getImageHeight();
 		int galleryHeight;
		if (screenWidth > 720)
			galleryHeight = (int) (196 * 2 * screenDensity * 5 / 4);
		else
			galleryHeight = (int) (196 * screenDensity * 5 / 4);
		v1.setLayoutParams(new DetailGallery.LayoutParams(galleryWidth,
				galleryHeight));//
		imageLoader.DisplayImage(value, v);
	}
}
