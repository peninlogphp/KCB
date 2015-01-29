package lipuka.android.model;

import android.os.Parcel;
import android.os.Parcelable;

public class BankAccount  implements Parcelable{

	private int id;
	private String alias;
	public String type;
	public static final Parcelable.Creator<BankAccount> CREATOR = new BankAccount.BankAccountCreator();
	public BankAccount(int id, String alias, String type) {
		this.id = id;
		this.alias = alias;
		this.type = type;
	}
	public BankAccount(Parcel source){
        /*
         * Reconstruct from the Parcel
         */
        id = source.readInt();
        alias = source.readString();
        type = source.readString();
  }
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;		
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
this.alias = alias;		
	}
	public String getType() {
		return type;
	}

	public void setType(String type) {
this.type = type;		
	}
	@Override
	public int describeContents() {
		return hashCode();
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
dest.writeInt(id);	
dest.writeString(alias);	
dest.writeString(type);	
	}
	
	public static class BankAccountCreator implements Parcelable.Creator<BankAccount> {
	      public BankAccount createFromParcel(Parcel source) {
	            return new BankAccount(source);
	      }
	      public BankAccount[] newArray(int size) {
	            return new BankAccount[size];
	      }
	}
}
