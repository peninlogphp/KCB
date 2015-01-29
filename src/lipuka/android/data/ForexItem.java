package lipuka.android.data;

public class ForexItem 	{

	private int id;
	private String code;
	private String buying;
	private int iconId;
	public String selling;

	public ForexItem(int id, String code, String buying,  String selling, int iconId) {
		this.id =  id;
		this.code = code;
		this.buying = buying;
		this.iconId = iconId;
		this.selling = selling;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;		
	}
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;		
	}

	public String getBuying() {
		return buying;
	}

	public void setBuying(String buying) {
	this.buying = buying;		
	}
	public int getIconId() {
		return iconId;
	}

	public void setIconId(int iconId) {
	this.iconId = iconId;		
	}
	public  String getSelling() {
		return selling;
	}

	public void setSelling(String selling) {
	this.selling = selling;		
	}
	}
