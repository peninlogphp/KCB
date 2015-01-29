package lipuka.android.view.adapter;

import java.util.List;


import org.json.JSONObject;


import kcb.android.R;


import greendroid.image.ChainImageProcessor;
import greendroid.image.ImageProcessor;
import greendroid.image.MaskImageProcessor;
import greendroid.image.ScaleImageProcessor;
import greendroid.widget.AsyncImageView;
import kcb.android.Main;
import kcb.android.ManageBeneficiaries;
import kcb.android.ManageSavedCards;
import kcb.android.ManageTransactionsList;
import lipuka.android.data.BeneficiaryViewHolder;
import lipuka.android.data.HomeItem;
import lipuka.android.data.SongTag;
import lipuka.android.data.TransactionViewHolder;
import lipuka.android.data.ViewHolder;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Bitmap.Config;
import android.graphics.Paint.Style;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ImageView.ScaleType;

public  class ManageSavedCardsAdapter extends BaseAdapter {

    private static final String BASE_URL_PREFIX = "http://www.cyrilmottier.com/files/greendroid/images/image";
    private static final String BASE_URL_SUFFIX = ".png";
    private final StringBuilder BUILDER = new StringBuilder();

    private LayoutInflater mInflater;
    private ImageProcessor mImageProcessor;
    List<JSONObject> itemList;
    ManageSavedCards manageSavedCards;
public ManageSavedCardsAdapter(ManageSavedCards manageSavedCards, List<JSONObject> itemList) {
    this.manageSavedCards = manageSavedCards;
    mInflater = LayoutInflater.from(manageSavedCards);
this.itemList = itemList;
    prepareImageProcessor(manageSavedCards);
        }

private void prepareImageProcessor(Context context) {
    
    final int thumbnailSize = context.getResources().getDimensionPixelSize(R.dimen.thumbnail_size);
    final int thumbnailRadius = context.getResources().getDimensionPixelSize(R.dimen.thumbnail_radius);

    if (Math.random() >= 0.5f) {
        //@formatter:off
        mImageProcessor = new ChainImageProcessor(
                new ScaleImageProcessor(thumbnailSize, thumbnailSize, ScaleType.FIT_XY),
                new MaskImageProcessor(thumbnailRadius));
        //@formatter:on
    } else {
        
        Path path = new Path();
        path.moveTo(thumbnailRadius, 0);
        
        path.lineTo(thumbnailSize - thumbnailRadius, 0);
        path.lineTo(thumbnailSize, thumbnailRadius);
        path.lineTo(thumbnailSize, thumbnailSize - thumbnailRadius);
        path.lineTo(thumbnailSize - thumbnailRadius, thumbnailSize);
        path.lineTo(thumbnailRadius, thumbnailSize);
        path.lineTo(0, thumbnailSize - thumbnailRadius);
        path.lineTo(0, thumbnailRadius);
        
        path.close();
        
        Bitmap mask = Bitmap.createBitmap(thumbnailSize, thumbnailSize, Config.ARGB_8888);
        Canvas canvas = new Canvas(mask);
        
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Style.FILL_AND_STROKE);
        paint.setColor(Color.RED);
        
        canvas.drawPath(path, paint);
        
        //@formatter:off
        mImageProcessor = new ChainImageProcessor(
                new ScaleImageProcessor(thumbnailSize, thumbnailSize, ScaleType.FIT_XY),
                new MaskImageProcessor(mask));
        //@formatter:on
    }
}

public int getCount() {
	return itemList.size();    
}

public Object getItem(int position) {
    return null;
}

public long getItemId(int position) {
    return position;
}

public View getView(int position, View convertView, ViewGroup parent) {

	BeneficiaryViewHolder holder;

    if (convertView == null) {
        convertView = mInflater.inflate(R.layout.manage_beneficiaries_list_item, parent, false);
        holder = new BeneficiaryViewHolder();
        holder.aliasView = (TextView) convertView.findViewById(R.id.alias);
        holder.countryView = (TextView) convertView.findViewById(R.id.country);
        holder.editView = (Button) convertView.findViewById(R.id.edit);
        holder.cancelView = (Button) convertView.findViewById(R.id.cancel);
        
        convertView.setTag(holder);
    } else {
        holder = (BeneficiaryViewHolder) convertView.getTag();
    }

    
    JSONObject item = itemList.get(position);
    try {
   
    holder.aliasView.setText(item.getString("alias"));
    holder.countryView.setText(item.getString("phone_no"));
    
    holder.editView.setOnClickListener(manageSavedCards);
    holder.cancelView.setOnClickListener(manageSavedCards);
    
    holder.editView.setTag(item);
    holder.cancelView.setTag(item);
    }
    catch (org.json.JSONException jsonError) {
	     Log.d(Main.TAG, "jsonError: ", jsonError);
      }
  

    return convertView;
}
}