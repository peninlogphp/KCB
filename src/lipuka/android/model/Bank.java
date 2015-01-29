package lipuka.android.model;


import java.io.Serializable;
import java.util.ArrayList;

import kcb.android.Main;







import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

public class Bank implements Parcelable, Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8816514153353941261L;
	public String name;
	public int id;

	 ArrayList<BankAccount> accounts;
		public static final Parcelable.Creator<Bank> CREATOR = new Bank.BankCreator();

	public Bank(String name, int id) {
		this.name = name;
		this.id = id;
		accounts = new ArrayList<BankAccount>();
	}
	public Bank(Parcel source){

        name = source.readString();
        id = source.readInt();
      try{  	Parcelable[] bankAccountArray = source.readParcelableArray(BankAccount.class.getClassLoader());
      accounts = new ArrayList<BankAccount>(bankAccountArray.length);
  	
      for (Parcelable account: bankAccountArray) {
      	accounts.add((BankAccount)account);
              }
      }catch(Exception e){
          Log.d(Main.TAG, "reading accounts error: ", e);

      }
		
  }
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;		
	}
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;		
	}

	public ArrayList<BankAccount> getAccounts(){
   	 return accounts;
    }

	public void addBankAccount(BankAccount account) {
		accounts.add(account);		
	}

	@Override
	public int describeContents() {
		return hashCode();
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
dest.writeString(name);	
dest.writeInt(id);	

BankAccount[] bankAccountArray = new BankAccount[accounts.size()];
accounts.toArray(bankAccountArray);
dest.writeParcelableArray(bankAccountArray, flags);

	}
	
	public static class BankCreator implements Parcelable.Creator<Bank> {
	      public Bank createFromParcel(Parcel source) {
	            return new Bank(source);
	      }
	      public Bank[] newArray(int size) {
	            return new Bank[size];
	      }
	}
}
