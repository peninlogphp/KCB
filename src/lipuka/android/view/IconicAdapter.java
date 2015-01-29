package lipuka.android.view;

import kcb.android.LipukaApplication;
import kcb.android.Main;
import kcb.android.R;
import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class IconicAdapter extends ArrayAdapter<String> {
	Activity context;
	String listItems[];
	LipukaApplication lipukaApplication;
	public IconicAdapter(Activity context, String listItems[]) {
	super(context, R.layout.activity_list_item, R.id.listitem_label, listItems);

	this.context=context;
	this.listItems=listItems;
    lipukaApplication = (LipukaApplication)context.getApplication();


	}

	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
			        if (row == null) {
			LayoutInflater inflater = (LayoutInflater)context.getSystemService
	      (Context.LAYOUT_INFLATER_SERVICE);
		
	 row=inflater.inflate(R.layout.activity_list_item, null);
	}
	TextView label=(TextView)row.findViewById(R.id.listitem_label);
	   ImageView imageView = (ImageView)row.findViewById(R.id.listitem_icon);

	label.setText(listItems[position]);
	     Log.d(Main.TAG, "stack size: "+lipukaApplication.getNavigationStack().size());

	if (lipukaApplication.getNavigationStack().size() == 1) {
		//imageView.setImageResource(mThumbIds[position]);
	}else{
		imageView.setImageResource(R.drawable.listitem);		
	}

	return(row);
	}
	
	/*private Integer[] mThumbIds = {
            R.drawable.balance, R.drawable.ministmt,
            R.drawable.topup, R.drawable.transfer_money,
            R.drawable.mpesa, R.drawable.pay_bills,
            R.drawable.cheque_book, R.drawable.fullstmt,
            R.drawable.foreign_exchange, R.drawable.stop_cheque,
            R.drawable.change_pin
    };*/
	}
	
