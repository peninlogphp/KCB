package lipuka.android.view.adapter;


import java.util.List;

import kcb.android.EcobankMain;
import kcb.android.PaymaxHome;






import lipuka.android.data.HomeItem;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import kcb.android.R;

public class EcobankMainAdapter extends BaseAdapter {
    private PaymaxHome mContext;

    private List<HomeItem> items;
    public EcobankMainAdapter(PaymaxHome c, List<HomeItem> items) {
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
    	    homeListItem=inflater.inflate(R.layout.music_home_grid_item, parent, false);
    	   }else{
    	    homeListItem = (View)convertView;
    	   }
    	   HomeItem item = items.get(position);
    	   ImageView imageView = (ImageView)homeListItem.findViewById(R.id.image_part);
    	   TextView textView = (TextView)homeListItem.findViewById(R.id.text_part);
           imageView.setImageResource(mThumbIds[position]);
    	   textView.setText(item.getText());
    	   
    	   return homeListItem;
    }

    private Integer[] mThumbIds = {
            R.drawable.funds_transfer, R.drawable.my_account,
            R.drawable.locator, R.drawable.locator
    };
  
}