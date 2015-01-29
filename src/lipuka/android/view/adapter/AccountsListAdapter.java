package lipuka.android.view.adapter;


import java.util.List;

import kcb.android.EcobankHome;







import kcb.android.R;
import lipuka.android.model.BankAccount;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class AccountsListAdapter extends BaseAdapter {
    private EcobankHome mContext;

    private List<BankAccount> items;
    public AccountsListAdapter(EcobankHome c, List<BankAccount> items) {
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
    	   View accountsListItem;
    	    
    	   if(convertView==null){
    	    accountsListItem = new View(mContext);
    	    LayoutInflater inflater=(LayoutInflater)mContext.getSystemService
  	      (Context.LAYOUT_INFLATER_SERVICE);
    	    accountsListItem=inflater.inflate(R.layout.accounts_list_item, parent, false);
    	   }else{
    	    accountsListItem = (View)convertView;
    	   }
    	   BankAccount item = items.get(position);
    	   //ImageView iconView = (ImageView)menuListItem.findViewById(R.id.icon_part);
    	   //TextView titleView = (TextView)menuListItem.findViewById(R.id.title_part);
    	   TextView textView = (TextView)accountsListItem.findViewById(R.id.listitem_label);
    	   ImageView listItemIcon = (ImageView)accountsListItem.findViewById(R.id.listitem_icon);
    	   //iconView.setImageResource(item.getIconId());
    	   //titleView.setText(item.getTitle());
    	   textView.setText(item.getAlias());
    	   
    	   /*if(item.isSubscribed()){
    		   subscribeView.setImageResource(R.drawable.star); 
    		   subscribeView.setOnClickListener(null);

    	   }
    	   else{
    		   subscribeView.setImageResource(R.drawable.subscribe);
    		   subscribeView.setOnClickListener(mContext);
    		   InfoItemTag tag = new InfoItemTag(item.getId(), position);
    		   subscribeView.setTag(tag);
    		   }*/
    	   return accountsListItem;
    }
  
}