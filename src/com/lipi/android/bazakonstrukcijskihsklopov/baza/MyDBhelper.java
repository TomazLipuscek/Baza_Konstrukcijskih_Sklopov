package com.lipi.android.bazakonstrukcijskihsklopov.baza;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.util.Log;

public class MyDBhelper extends SQLiteOpenHelper {

	private static final String CREATE_TABLE_STAVBA = "create table "
			+ Constants.TABLE_NAME_STAVBA + " (" 
			+ Constants.STAVBA_ID + " integer primary key autoincrement, "
			+ Constants.STAVBA_OBCINA + ", "
			+ Constants.STAVBA_POSTNA + ", "
			+ Constants.STAVBA_NASELJE + ", "
			+ Constants.STAVBA_ULICA + ", "
			+ Constants.STAVBA_HISNA + ", " 
			+ Constants.STAVBA_DODATEK_HISNI + ", " 
			+ Constants.STAVBA_STEVILKA_STANOVANJA + ", "
			+ Constants.STAVBA_TIP_STAVBE + ", "
			+ Constants.STAVBA_LETO_IZGRADNJE + ", " 
			+ Constants.STAVBA_VISINA + ", " 
			+ Constants.STAVBA_UPORABNA_POVRSINA + ", "
			+ Constants.STAVBA_NETO_TLORIS + ", "
			+ Constants.STAVBA_POVRSINA_POD_STAVBO + ");";
	
	private static final String CREATE_TABLE_REZIM = "create table "
			+ Constants.TABLE_NAME_REZIM + " (" 
			+ Constants.REZIM_ID + " integer primary key autoincrement, "
			+ Constants.REZIM_DAN + ", "
			+ Constants.REZIM_NOC + ", "
			+ Constants.REZIM_ZUNAJ_DAN + ", "
			+ Constants.REZIM_ZUNAJ_NOC + ", "
			+ Constants.REZIM_LJUDI + ");";
	
	
	private static final String CREATE_TABLE_SKLOPI = "create table "
			+ Constants.TABLE_NAME_SKLOPI + " (" 
			+ Constants.STAVBA_OBCINA + ", "
			+ Constants.STAVBA_POSTNA + ", "
			+ Constants.STAVBA_NASELJE + ", "
			+ Constants.STAVBA_ULICA + ", "
			+ Constants.STAVBA_HISNA + ", " 
			+ Constants.STAVBA_DODATEK_HISNI + ", " 
			+ Constants.STAVBA_STEVILKA_STANOVANJA + ", "
			+ Constants.STAVBA_TIP_STAVBE + ", "
			+ Constants.STAVBA_LETO_IZGRADNJE + ", " 
			+ Constants.STAVBA_VISINA + ", " 
			+ Constants.STAVBA_UPORABNA_POVRSINA + ", "
			+ Constants.STAVBA_NETO_TLORIS + ", "
			+ Constants.STAVBA_POVRSINA_POD_STAVBO + ", "
			+ Constants.REZIM_DAN + ", "
			+ Constants.REZIM_NOC + ", "
			+ Constants.REZIM_ZUNAJ_DAN + ", "
			+ Constants.REZIM_ZUNAJ_NOC + ", "
			+ Constants.REZIM_LJUDI + ", "
			+ Constants.SKLOP_ID + " integer primary key autoincrement, " 
			+ Constants.SKLOP_NAME + ", "
			+ Constants.SKLOP_VRSTA + ", " 
			+ Constants.SKLOP_POVRSINA + ");";

	private static final String CREATE_TABLE_MATERIALI = "create table "
			+ Constants.TABLE_NAME + " (" 
			+ Constants.STAVBA_OBCINA + ", "
			+ Constants.STAVBA_POSTNA + ", "
			+ Constants.STAVBA_NASELJE + ", "
			+ Constants.STAVBA_ULICA + ", "
			+ Constants.STAVBA_HISNA + ", " 
			+ Constants.STAVBA_DODATEK_HISNI + ", " 
			+ Constants.STAVBA_STEVILKA_STANOVANJA + ", "
			+ Constants.STAVBA_TIP_STAVBE + ", "
			+ Constants.STAVBA_LETO_IZGRADNJE + ", " 
			+ Constants.STAVBA_VISINA + ", " 
			+ Constants.STAVBA_UPORABNA_POVRSINA + ", "
			+ Constants.STAVBA_NETO_TLORIS + ", "
			+ Constants.STAVBA_POVRSINA_POD_STAVBO + ", "
			+ Constants.REZIM_DAN + ", "
			+ Constants.REZIM_NOC + ", "
			+ Constants.REZIM_ZUNAJ_DAN + ", "
			+ Constants.REZIM_ZUNAJ_NOC + ", "
			+ Constants.REZIM_LJUDI + ", "
			+ Constants.SKLOP_ID_MATERIALI + ", " 
			+ Constants.SKLOP_NAME + ", " 
			+ Constants.SKLOP_VRSTA + ", "
			+ Constants.SKLOP_POVRSINA + ", " 
			+ Constants.KEY_ID + " integer primary key autoincrement, "  
			+ Constants.KEY_MATERIAL + ", " 
			+ Constants.KEY_ID_ZAPOREDNA + ", "
			+ Constants.KEY_DEBELINA + ", "
			+ Constants.KEY_NAME + ", " 
			+ Constants.KEY_OPIS + ", " 
			+ Constants.KEY_GOSTOTA + ", "
			+ Constants.KEY_C + ", " 
			+ Constants.KEY_LAMBDA + ", "
			+ Constants.KEY_U + ");";
	 

	public MyDBhelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		Log.v("MyDBhelper onCreate", "Creating all the tables");
		try {
			db.execSQL(CREATE_TABLE_STAVBA);
			db.execSQL(CREATE_TABLE_REZIM);
			db.execSQL(CREATE_TABLE_MATERIALI);
			db.execSQL(CREATE_TABLE_SKLOPI);
		} catch (SQLiteException ex) {
			Log.v("Create table exception", ex.getMessage());
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w("TaskDBAdapter", "Upgrading from version " + oldVersion + " to "
				+ newVersion + ", which will destroy all old data");
		db.execSQL("drop table if exists " + Constants.TABLE_NAME_REZIM);
		db.execSQL("drop table if exists " + Constants.TABLE_NAME_STAVBA);
		db.execSQL("drop table if exists " + Constants.TABLE_NAME);
		db.execSQL("drop table if exists " + Constants.TABLE_NAME_SKLOPI);
		onCreate(db);
	}
	
}
