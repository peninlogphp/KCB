package lipuka.android.view.adapter;


import java.util.List;

import kcb.android.EcobankHome;
import kcb.android.Forex;







import kcb.android.R;
import lipuka.android.data.HomeItem;
import lipuka.android.data.InfoItem;
import lipuka.android.data.InfoItemTag;
import lipuka.android.view.LipukaListItem;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class LanguageListAdapter extends BaseAdapter {
    private EcobankHome mContext;

    private List<LipukaListItem> items;
    public LanguageListAdapter(EcobankHome c, List<LipukaListItem> items) {
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
    	    homeListItem=inflater.inflate(R.layout.language_list_item, parent, false);
    	   }else{
    	    homeListItem = (View)convertView;
    	   }
    	   LipukaListItem item = items.get(position);
    	   TextView textView = (TextView)homeListItem.findViewById(R.id.listitem_label);
    	   textView.setText(item.getText());
    	   
    	   return homeListItem;
    }
  
}