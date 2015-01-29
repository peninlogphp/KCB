package lipuka.android.view.adapter;


import java.util.List;

import kcb.android.EcobankMain;
import kcb.android.Forex;
import kcb.android.PaymaxHome;






import lipuka.android.data.ForexItem;
import lipuka.android.data.HomeItem;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import kcb.android.R;

public class ForexAdapter extends BaseAdapter {
    private Forex mContext;

    private List<ForexItem> items;
    public ForexAdapter(Forex c, List<ForexItem> items) {
        mContext = c;
        this.items = items;
    }

    public int getCount() {
        return items.size();
    }

    public Object getItem(int position) {
        return items.get(position);
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new LinearLayout for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
    	   View homeListItem;
    	    
    	   if(convertView==null){
    	    homeListItem = new View(mContext);
    	    LayoutInflater inflater=(LayoutInflater)mContext.getSystemService
  	      (Context.LAYOUT_INFLATER_SERVICE);
    	    homeListItem=inflater.inflate(R.layout.forex_list_item, parent, false);
    	   }else{
    	    homeListItem = (View)convertView;
    	   }
    	   ForexItem item = items.get(position);
    	   ImageView imageView = (ImageView)homeListItem.findViewById(R.id.currency_icon);
    	   TextView currencyCode = (TextView)homeListItem.findViewById(R.id.currency_code);
    	   currencyCode.setText(item.getCode());
    	   TextView buyingRate = (TextView)homeListItem.findViewById(R.id.buying_rate);
    	   buyingRate.setText(item.getBuying());
    	   TextView sellingRate = (TextView)homeListItem.findViewById(R.id.selling_rate);
    	   sellingRate.setText(item.getSelling());
           imageView.setImageResource(mThumbIds[position]);
    	   
    	   return homeListItem;
    }

    private Integer[] mThumbIds = {
            R.drawable.dollar, R.drawable.euro,
            R.drawable.yen
    };
  
}