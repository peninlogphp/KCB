package lipuka.android.model;


public class LipukaList {

	public static final byte NAVIGATION_LOCAL = 1;
	public static final byte NAVIGATION_REMOTE =21;

	private String type;
	private String source;
	public byte navigationType;

	public LipukaList() {
		// TODO Auto-generated constructor stub
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;		
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
this.source = source;		
	}
	public byte getNavigationType() {
		return navigationType;
	}

	public void setNavigationType(byte navigationType) {
this.navigationType = navigationType;		
	}
}
