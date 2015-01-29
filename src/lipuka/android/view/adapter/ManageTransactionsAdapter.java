package lipuka.android.view.adapter;

import java.util.List;

import kcb.android.Main;
import kcb.android.ManageTransactionsList;

import org.json.JSONObject;

import kcb.android.R;


import greendroid.image.ChainImageProcessor;
import greendroid.image.ImageProcessor;
import greendroid.image.MaskImageProcessor;
import greendroid.image.ScaleImageProcessor;
import greendroid.widget.AsyncImageView;
import lipuka.android.data.HomeItem;
import lipuka.android.data.SongTag;
import lipuka.android.data.TransactionViewHolder;
import lipuka.android.data.ViewHolder;
import lipuka.android.util.Formatters;
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

public  class ManageTransactionsAdapter extends BaseAdapter {

    private static final String BASE_URL_PREFIX = "http://www.cyrilmottier.com/files/greendroid/images/image";
    private static final String BASE_URL_SUFFIX = ".png";
    private final StringBuilder BUILDER = new StringBuilder();

    private LayoutInflater mInflater;
    private ImageProcessor mImageProcessor;
    List<JSONObject> itemList;
    ManageTransactionsList manageTransactionsList;
public ManageTransactionsAdapter(ManageTransactionsList manageTransactionsList, List<JSONObject> itemList) {
    this.manageTransactionsList = manageTransactionsList;
    mInflater = LayoutInflater.from(manageTransactionsList);
this.itemList = itemList;
    prepareImageProcessor(manageTransactionsList);
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

	TransactionViewHolder holder;

    if (convertView == null) {
        convertView = mInflater.inflate(R.layout.wu_manage_transactions_list_item, parent, false);
        holder = new TransactionViewHolder();
        holder.dateNtimeView = (TextView) convertView.findViewById(R.id.date_n_time);
        holder.mtcnView = (TextView) convertView.findViewById(R.id.mtcn);
        holder.statusView = (TextView) convertView.findViewById(R.id.status);
        holder.amountView = (TextView) convertView.findViewById(R.id.amount);
        holder.editView = (Button) convertView.findViewById(R.id.edit);
        holder.cancelView = (Button) convertView.findViewById(R.id.cancel);
        holder.moreView = (Button) convertView.findViewById(R.id.more);
        
        convertView.setTag(holder);
    } else {
        holder = (TransactionViewHolder) convertView.getTag();
    }

    
    JSONObject item = itemList.get(position);
    try {
   
    holder.dateNtimeView.setText(item.getString("date_n_time"));
    holder.mtcnView.setText(item.getString("mtcn"));
    holder.statusView.setText(item.getString("status"));
    holder.amountView.setText(Formatters.formatAmount(item.getDouble("amount")));
    if(item.getInt("status_code") == 0){
    	holder.editView.setVisibility(View.VISIBLE); 	
    	holder.cancelView.setVisibility(View.VISIBLE); 	
    }else{
    	holder.editView.setVisibility(View.GONE); 	
    	holder.cancelView.setVisibility(View.GONE);     	
    }
    holder.editView.setOnClickListener(manageTransactionsList);
    holder.cancelView.setOnClickListener(manageTransactionsList);
    holder.moreView.setOnClickListener(manageTransactionsList);
    
    holder.editView.setTag(item);
    holder.cancelView.setTag(item);
    holder.moreView.setTag(item);
    }
    catch (org.json.JSONException jsonError) {
	     Log.d(Main.TAG, "jsonError: ", jsonError);
      }
  

    return convertView;
}
}