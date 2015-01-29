package lipuka.android.view.adapter;

import java.util.ArrayList;

import org.json.JSONObject;

import kcb.android.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

public class BranchesAdapter extends ArrayAdapter<String> implements Filterable{
    private ArrayList<String> items;
    private ArrayList<String> itemsAll;
    private ArrayList<String> suggestions;

    public BranchesAdapter(Context context, ArrayList<String> items) {
        super(context, R.layout.branch_item, items);
        this.items = items;
        this.itemsAll = (ArrayList<String>) items.clone();
        this.suggestions = new ArrayList<String>();
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            LayoutInflater vi = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.branch_item, null);
        }
        String branchName = items.get(position);
        if (branchName != null) {
            TextView branchNameLabel = (TextView) v.findViewById(R.id.listitem_label);
            if (branchNameLabel != null) {
                branchNameLabel.setText(branchName);
            }
        }
        return v;
    }

    @Override
    public Filter getFilter() {
        return nameFilter;
    }

    Filter nameFilter = new Filter() {
        public String convertResultToString(Object resultValue) {
            String str = (String)resultValue; 
            return str;
        }
    
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            if(constraint != null) {
                suggestions.clear();
                for (String branchName : itemsAll) {
                    if(branchName.toLowerCase().contains(constraint.toString().toLowerCase())){
                        suggestions.add(branchName);
                    }
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = suggestions;
                filterResults.count = suggestions.size();
                return filterResults;
            } else {
                return new FilterResults();
            }
        }
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            ArrayList<String> filteredList = (ArrayList<String>) results.values;
            if(results != null && results.count > 0) {
                clear();
                for (String branchName : filteredList) {
                    add(branchName);
                }
                notifyDataSetChanged();
            }
        }
    };

}