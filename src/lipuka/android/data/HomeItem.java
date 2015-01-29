package lipuka.android.data;

public class HomeItem 	{

	private int id;
	private String title;
	private String text;
	private int iconId;
	public boolean subscribed;

	public HomeItem(int id, String title, String text, int iconId, boolean subscribed) {
		this.id =  id;
		this.title = title;
		this.text = text;
		this.iconId = iconId;
		this.subscribed = subscribed;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;		
	}
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;		
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
	this.text = text;		
	}
	public int getIconId() {
		return iconId;
	}

	public void setIconId(int iconId) {
	this.iconId = iconId;		
	}
	public boolean isSubscribed() {
		return subscribed;
	}

	public void setSubscribed(boolean subscribed) {
	this.subscribed = subscribed;		
	}
	}
