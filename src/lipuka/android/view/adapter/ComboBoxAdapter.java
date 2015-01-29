package lipuka.android.view.adapter;

import java.util.List;

import kcb.android.R;

import lipuka.android.view.LipukaListItem;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ComboBoxAdapter extends ArrayAdapter<LipukaListItem>{

    // Your sent context
    private Context context;
    // Your custom values for the spinner (User)
    private LipukaListItem[] values;

    public ComboBoxAdapter(Context context, int textViewResourceId,
    		LipukaListItem[] values) {
        super(context, textViewResourceId, values);
        this.context = context;
        this.values = values;
    }

    public int getCount(){
       return values.length;
    }

    public LipukaListItem getItem(int position){
       return values[position];
    }

    public long getItemId(int position){
       return position;
    }


    // And the "magic" goes here
    // This is for the "passive" state of the spinner
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // I created a dynamic TextView here, but you can reference your own  custom layout for each spinner item
        TextView label = new TextView(context);
        label.setTextColor(Color.BLACK);
        // Then you can get the current item using the values array (Users array) and the current position
        // You can NOW reference each method you has created in your bean object (User class)
        label.setText(values[position].getText());

        // And finally return your dynamic (or custom) view for each spinner item
        return label;
    }

    // And here is when the "chooser" is popped up
    // Normally is the same view, but you can customize it if you want
    @Override
    public View getDropDownView(int position, View convertView,
            ViewGroup parent) {
    	 View homeListItem;
 	    
  	   if(convertView==null){
  	    homeListItem = new View(context);
  	    LayoutInflater inflater=(LayoutInflater)context.getSystemService
	      (Context.LAYOUT_INFLATER_SERVICE);
  	    homeListItem=inflater.inflate(R.layout.combo_item, parent, false);
  	   }else{
  	    homeListItem = (View)convertView;
  	   }
  	   LipukaListItem item = values[position];
  	   TextView textView = (TextView)homeListItem.findViewById(R.id.listitem_label);
  	   textView.setText(item.getText());
  	   
  	   return homeListItem;
    }
}