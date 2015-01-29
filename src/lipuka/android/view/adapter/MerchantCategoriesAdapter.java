package lipuka.android.view.adapter;

import java.util.List;

import kcb.android.FundsTransfer;
import kcb.android.Merchants;
import kcb.android.R;

import greendroid.image.ChainImageProcessor;
import greendroid.image.ImageProcessor;
import greendroid.image.MaskImageProcessor;
import greendroid.image.ScaleImageProcessor;
import greendroid.widget.AsyncImageView;
import lipuka.android.data.HomeItem;
import lipuka.android.data.SongTag;
import lipuka.android.data.ViewHolder;
import lipuka.android.view.SignInDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Bitmap.Config;
import android.graphics.Paint.Style;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ImageView.ScaleType;

public  class MerchantCategoriesAdapter extends BaseAdapter{

    private static final String BASE_URL_PREFIX = "http://www.cyrilmottier.com/files/greendroid/images/image";
    private static final String BASE_URL_SUFFIX = ".png";
    private final StringBuilder BUILDER = new StringBuilder();

    private LayoutInflater mInflater;
    private ImageProcessor mImageProcessor;
    List<HomeItem> homeItemList;
    Merchants merchants;
public MerchantCategoriesAdapter(Merchants merchants, List<HomeItem> homeItemList) {
    this.merchants = merchants;
    mInflater = LayoutInflater.from(merchants);
this.homeItemList = homeItemList;
    prepareImageProcessor(merchants);
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
	return homeItemList.size();    
}

public Object getItem(int position) {
    return null;
}

public long getItemId(int position) {
    return position;
}

public View getView(int position, View convertView, ViewGroup parent) {

    ViewHolder holder;

    if (convertView == null) {
        convertView = mInflater.inflate(R.layout.merchant_categories_item, parent, false);
        holder = new ViewHolder();
        holder.imageView = (AsyncImageView) convertView.findViewById(R.id.icon_part);
        holder.imageView.setImageProcessor(mImageProcessor);
        holder.artistView = (TextView) convertView.findViewById(R.id.label_part);
        
        convertView.setTag(holder);
    } else {
        holder = (ViewHolder) convertView.getTag();
    }

    BUILDER.setLength(0);
    BUILDER.append(BASE_URL_PREFIX);
    BUILDER.append(position+1);
    BUILDER.append(BASE_URL_SUFFIX);
    //holder.imageView.setUrl(BUILDER.toString());
    HomeItem item = homeItemList.get(position);
    int iconID = 0;
    switch(position){
	case 0:
		if(position == merchants.getSelectedCategory()){
			iconID = R.drawable.utility_bills_selected;
			    }else{
					iconID = R.drawable.utility_bills;
			    }
		break;
	case 1:
		if(position == merchants.getSelectedCategory()){
			iconID = R.drawable.supermarkets_selected;
			    }else{
					iconID = R.drawable.supermarkets;
			    }
		break;
	case 2:
		if(position == merchants.getSelectedCategory()){
			iconID = R.drawable.restaurants_selected;
			    }else{
					iconID = R.drawable.restaurants;
			    }
		break;
	case 3:
		if(position == merchants.getSelectedCategory()){
			iconID = R.drawable.gas_station_selected;
			    }else{
					iconID = R.drawable.gas_station;
			    }
		break;
	default:
	}
    holder.imageView.setDefaultImageResource(iconID);

    final StringBuilder textBuilder = holder.textBuilder;
    textBuilder.setLength(0);
   
    holder.artistView.setText(item.getText());
    SongTag songTag = new SongTag();
    songTag.artist = item.getText();
   // songTag.contentId = topsongsIds[position];
    
    holder.imageView.setTag(songTag);
    
    if(position == merchants.getSelectedCategory()){
 convertView.setBackgroundDrawable(merchants.getResources().getDrawable(R.drawable.horizontal_listview_selected_item_bg));
    }else{
    	 convertView.setBackgroundDrawable(merchants.getResources().getDrawable(R.drawable.horizontal_listview_item_bg));
    }

    return convertView;
}

}