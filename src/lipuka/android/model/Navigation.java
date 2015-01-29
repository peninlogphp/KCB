package lipuka.android.model;

import java.util.HashMap;

public class Navigation {
	private String activity;
	private String payload;
	private boolean remote = false;
	private HashMap<String, String> values = new HashMap<String, String>();
	
	
	public String getActivity() {
		return activity;
	}

	public void setActivity(String activity) {
this.activity = activity;		
	}
	public String getPayload() {
		return payload;
	}
	public void setPayload(String payload) {
this.payload = payload;		
	}
	public boolean isRemote() {
		return remote;
	}
	public void setRemote(boolean remote) {
this.remote = remote;		
	}
	public HashMap<String, String> getHashMap() {
		return values;
	}

}
