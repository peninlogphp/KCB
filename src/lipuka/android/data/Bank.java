package lipuka.android.data;

import org.json.JSONArray;

public class Bank implements Comparable<Bank>{

	private int code;
	private String name;
	private JSONArray branches;

	public Bank(int code, String name, JSONArray branches) {
		this.code =  code;
		this.name = name;
		this.branches = branches;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;		
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
	this.name = name;		
	}
	public JSONArray getBranches() {
		return branches;
	}

	public void setBranches(JSONArray branches) {
	this.branches = branches;		
	}
	public String toString(){
		return name;
	}
	public int compareTo(Bank bank) {
		if( bank == this) {
		return 0;
		}
		return this.name.compareTo(bank.getName());
		}
	}
