package lipuka.android.data;

import greendroid.widget.AsyncImageView;
import android.widget.Button;
import android.widget.TextView;

public class TransactionViewHolder {
    public TextView dateNtimeView;
    public TextView mtcnView;
    public TextView statusView;
    public TextView amountView;
    public Button editView;
    public Button cancelView;
    public Button moreView;

    public StringBuilder textBuilder = new StringBuilder();
}
