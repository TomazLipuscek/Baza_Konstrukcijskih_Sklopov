package com.lipi.android.bazakonstrukcijskihsklopov.baza;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

public class MyDB {

	private SQLiteDatabase db;
	private final Context context;
	private final MyDBhelper dbhelper;
	
	public MyDB(Context c){
		context = c;
		dbhelper = new MyDBhelper(context, Constants.DATABASE_NAME, null,
				Constants.DATABASE_VERSION);
	}
	
	public void close()
	{
	db.close();
	}
	
	public void open() throws SQLiteException{
	try {
	db = dbhelper.getWritableDatabase();
	} 
	catch(SQLiteException ex) {
	Log.v("Open database exception caught", ex.getMessage());
	db = dbhelper.getReadableDatabase();
	}
	}
	
	public long insertrezim(String dan,String noc,String zunajdan,String zunajnoc,String ljudi)
	{
	try{
	ContentValues newTaskValue = new ContentValues();
	newTaskValue.put(Constants.REZIM_DAN,dan);
	newTaskValue.put(Constants.REZIM_NOC,noc);
	newTaskValue.put(Constants.REZIM_ZUNAJ_DAN,zunajdan);
	newTaskValue.put(Constants.REZIM_ZUNAJ_NOC,zunajnoc);
	newTaskValue.put(Constants.REZIM_LJUDI,ljudi);
	return db.insert(Constants.TABLE_NAME_REZIM, null, newTaskValue);
	} 
	catch(SQLiteException ex) {
		Log.v("Insert into database exception caught",
			ex.getMessage());
			return -1;
			}
	}
	
	public Cursor getrezimi(){

		Cursor c = db.query(Constants.TABLE_NAME_REZIM, new String[] {
		Constants.REZIM_DAN,
		Constants.REZIM_NOC,
		Constants.REZIM_ZUNAJ_DAN,
		Constants.REZIM_ZUNAJ_NOC,
		Constants.REZIM_LJUDI,
		Constants.REZIM_ID,
		}, null, null, null, null, null);		
		return c;
	}
	

    
    public Cursor getrezim(Integer mRowId) throws SQLException {

        Cursor mCursor = db.query(true, Constants.TABLE_NAME_REZIM, new String[] {
        		Constants.REZIM_DAN,
        		Constants.REZIM_NOC,
        		Constants.REZIM_ZUNAJ_DAN,
        		Constants.REZIM_ZUNAJ_NOC,
        		Constants.REZIM_LJUDI,
        		Constants.REZIM_ID,
        		}, Constants.REZIM_ID + "=" + mRowId, null,
                        null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }
	public boolean updaterezim(long rowIdstavba, String dan,String noc,String zunajdan,String zunajnoc,String ljudi) 
	{
        ContentValues args = new ContentValues();
        args.put(Constants.REZIM_DAN,dan);
        args.put(Constants.REZIM_NOC,noc);
        args.put(Constants.REZIM_ZUNAJ_DAN,zunajdan);
        args.put(Constants.REZIM_ZUNAJ_NOC,zunajnoc);
        args.put(Constants.REZIM_LJUDI,ljudi);
        return db.update(Constants.TABLE_NAME_REZIM, args, Constants.REZIM_ID + "=" + rowIdstavba, null) > 0;
    }
    
	public boolean deleterezim(long rowIdstavba) {
		
	    return db.delete(Constants.TABLE_NAME_REZIM, Constants.REZIM_ID + "=" + rowIdstavba, null) > 0;
	    
	}
	
	public long insertstavba(String obcina,String postna,String naselje,String ulica,String hisna,String dodatek,String stevilka_stanovanja,String tip,
			String leto,String visina,String uporabna_povrsina,String neto_tloris, String povrsina_pod_stavbo)
	{
	try{
	ContentValues newTaskValue = new ContentValues();
	newTaskValue.put(Constants.STAVBA_OBCINA,obcina);
	newTaskValue.put(Constants.STAVBA_POSTNA,postna);
	newTaskValue.put(Constants.STAVBA_NASELJE,naselje);
	newTaskValue.put(Constants.STAVBA_ULICA,ulica);
	newTaskValue.put(Constants.STAVBA_HISNA,hisna);
	newTaskValue.put(Constants.STAVBA_DODATEK_HISNI,dodatek);
	newTaskValue.put(Constants.STAVBA_STEVILKA_STANOVANJA,stevilka_stanovanja);
	newTaskValue.put(Constants.STAVBA_TIP_STAVBE,tip);
	newTaskValue.put(Constants.STAVBA_LETO_IZGRADNJE,leto);
	newTaskValue.put(Constants.STAVBA_VISINA,visina);
	newTaskValue.put(Constants.STAVBA_UPORABNA_POVRSINA,uporabna_povrsina);
	newTaskValue.put(Constants.STAVBA_NETO_TLORIS,neto_tloris);
	newTaskValue.put(Constants.STAVBA_POVRSINA_POD_STAVBO,povrsina_pod_stavbo);
	return db.insert(Constants.TABLE_NAME_STAVBA, null, newTaskValue);
	} 
	catch(SQLiteException ex) {
		Log.v("Insert into database exception caught",
			ex.getMessage());
			return -1;
			}
	}
	
	public Cursor getstavbe(){

		Cursor c = db.query(Constants.TABLE_NAME_STAVBA, new String[] {
		Constants.STAVBA_OBCINA,
		Constants.STAVBA_POSTNA,
		Constants.STAVBA_NASELJE,
		Constants.STAVBA_ULICA,
		Constants.STAVBA_HISNA,
		Constants.STAVBA_DODATEK_HISNI,
		Constants.STAVBA_STEVILKA_STANOVANJA,
		Constants.STAVBA_TIP_STAVBE,
		Constants.STAVBA_LETO_IZGRADNJE,
		Constants.STAVBA_VISINA,
		Constants.STAVBA_UPORABNA_POVRSINA,
		Constants.STAVBA_NETO_TLORIS,
		Constants.STAVBA_POVRSINA_POD_STAVBO,
		Constants.STAVBA_ID,
		}, null, null, null, null, null);		
		return c;
	}
	

    
    public Cursor getstavba(Integer mRowId) throws SQLException {

        Cursor mCursor = db.query(true, Constants.TABLE_NAME_STAVBA, new String[] {
        		Constants.STAVBA_OBCINA,
        		Constants.STAVBA_POSTNA,
        		Constants.STAVBA_NASELJE,
        		Constants.STAVBA_ULICA,
        		Constants.STAVBA_HISNA,
        		Constants.STAVBA_DODATEK_HISNI,
        		Constants.STAVBA_STEVILKA_STANOVANJA,
        		Constants.STAVBA_TIP_STAVBE,
        		Constants.STAVBA_LETO_IZGRADNJE,
        		Constants.STAVBA_VISINA,
        		Constants.STAVBA_UPORABNA_POVRSINA,
        		Constants.STAVBA_NETO_TLORIS,
        		Constants.STAVBA_POVRSINA_POD_STAVBO,
        		Constants.STAVBA_ID,
        		}, Constants.SKLOP_ID + "=" + mRowId, null,
                        null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }
	public boolean updatestavba(long rowIdstavba, String obcina,String postna,String naselje,String ulica,String hisna,String dodatek,String stevilka_stanovanja,String tip,
			String leto,String visina,String uporabna_povrsina,String neto_tloris, String povrsina_pod_stavbo) 
	{
        ContentValues args = new ContentValues();
        args.put(Constants.STAVBA_OBCINA,obcina);
        args.put(Constants.STAVBA_POSTNA,postna);
        args.put(Constants.STAVBA_NASELJE,naselje);
        args.put(Constants.STAVBA_ULICA,ulica);
        args.put(Constants.STAVBA_HISNA,hisna);
        args.put(Constants.STAVBA_DODATEK_HISNI,dodatek);
        args.put(Constants.STAVBA_STEVILKA_STANOVANJA,stevilka_stanovanja);
        args.put(Constants.STAVBA_TIP_STAVBE,tip);
        args.put(Constants.STAVBA_LETO_IZGRADNJE,leto);
        args.put(Constants.STAVBA_VISINA,visina);
        args.put(Constants.STAVBA_UPORABNA_POVRSINA,uporabna_povrsina);
        args.put(Constants.STAVBA_NETO_TLORIS,neto_tloris);
        args.put(Constants.STAVBA_POVRSINA_POD_STAVBO,povrsina_pod_stavbo);
        return db.update(Constants.TABLE_NAME_STAVBA, args, Constants.STAVBA_ID + "=" + rowIdstavba, null) > 0;
    }
    
	public boolean deletestavba(long rowIdstavba) {
		
	    return db.delete(Constants.TABLE_NAME_STAVBA, Constants.STAVBA_ID + "=" + rowIdstavba, null) > 0;
	    
	}
	
	public long insertsklop(String title,String vrsta,String povrsina,String obcina,String postna,String naselje,String ulica,String hisna,String dodatek,String stevilka_stanovanja,String tip,
			String leto,String visina,String uporabna_povrsina,String neto_tloris, String povrsina_pod_stavbo,String dan,String noc,String zunajdan,String zunajnoc,String ljudi)
	{
	try{
	ContentValues newTaskValue = new ContentValues();
	newTaskValue.put(Constants.STAVBA_OBCINA,obcina);
	newTaskValue.put(Constants.STAVBA_POSTNA,postna);
	newTaskValue.put(Constants.STAVBA_NASELJE,naselje);
	newTaskValue.put(Constants.STAVBA_ULICA,ulica);
	newTaskValue.put(Constants.STAVBA_HISNA,hisna);
	newTaskValue.put(Constants.STAVBA_DODATEK_HISNI,dodatek);
	newTaskValue.put(Constants.STAVBA_STEVILKA_STANOVANJA,stevilka_stanovanja);
	newTaskValue.put(Constants.STAVBA_TIP_STAVBE,tip);
	newTaskValue.put(Constants.STAVBA_LETO_IZGRADNJE,leto);
	newTaskValue.put(Constants.STAVBA_VISINA,visina);
	newTaskValue.put(Constants.STAVBA_UPORABNA_POVRSINA,uporabna_povrsina);
	newTaskValue.put(Constants.STAVBA_NETO_TLORIS,neto_tloris);
	newTaskValue.put(Constants.STAVBA_POVRSINA_POD_STAVBO,povrsina_pod_stavbo);
	newTaskValue.put(Constants.REZIM_DAN,dan);
	newTaskValue.put(Constants.REZIM_NOC,noc);
	newTaskValue.put(Constants.REZIM_ZUNAJ_DAN,zunajdan);
	newTaskValue.put(Constants.REZIM_ZUNAJ_NOC,zunajnoc);
	newTaskValue.put(Constants.REZIM_LJUDI,ljudi);
	newTaskValue.put(Constants.SKLOP_NAME,title);
	newTaskValue.put(Constants.SKLOP_VRSTA,vrsta);
	newTaskValue.put(Constants.SKLOP_POVRSINA,povrsina);
	return db.insert(Constants.TABLE_NAME_SKLOPI, null, newTaskValue);
	} 
	catch(SQLiteException ex) {
		Log.v("Insert into database exception caught",
			ex.getMessage());
			return -1;
			}
	}
	
	public Cursor getsklopi(){

		Cursor c = db.query(Constants.TABLE_NAME_SKLOPI, new String[] {
		
		Constants.SKLOP_NAME,
		Constants.SKLOP_VRSTA,
		Constants.SKLOP_POVRSINA,
		Constants.SKLOP_ID,
		}, null, null, null, null, Constants.SKLOP_VRSTA);		
		return c;
	}
	

    
    public Cursor getsklop(Long mRowId) throws SQLException {

        Cursor mCursor = db.query(true, Constants.TABLE_NAME_SKLOPI, new String[] {
        		Constants.SKLOP_NAME,
        		Constants.SKLOP_VRSTA,
        		Constants.SKLOP_POVRSINA,
        		Constants.SKLOP_ID,
        		}, Constants.SKLOP_ID + "=" + mRowId, null,
                        null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }
	public boolean updatesklop(long rowIdsklop, String title,String vrsta, String povrsina,String obcina,String postna,String naselje,String ulica,String hisna,String dodatek,String stevilka_stanovanja,String tip,
			String leto,String visina,String uporabna_povrsina,String neto_tloris, String povrsina_pod_stavbo,String dan,String noc,String zunajdan,String zunajnoc,String ljudi) 
	{
        ContentValues args = new ContentValues();
        args.put(Constants.STAVBA_OBCINA,obcina);
        args.put(Constants.STAVBA_POSTNA,postna);
        args.put(Constants.STAVBA_NASELJE,naselje);
        args.put(Constants.STAVBA_ULICA,ulica);
        args.put(Constants.STAVBA_HISNA,hisna);
        args.put(Constants.STAVBA_DODATEK_HISNI,dodatek);
        args.put(Constants.STAVBA_STEVILKA_STANOVANJA,stevilka_stanovanja);
        args.put(Constants.STAVBA_TIP_STAVBE,tip);
        args.put(Constants.STAVBA_LETO_IZGRADNJE,leto);
        args.put(Constants.STAVBA_VISINA,visina);
        args.put(Constants.STAVBA_UPORABNA_POVRSINA,uporabna_povrsina);
        args.put(Constants.STAVBA_NETO_TLORIS,neto_tloris);
        args.put(Constants.STAVBA_POVRSINA_POD_STAVBO,povrsina_pod_stavbo);
        args.put(Constants.REZIM_DAN,dan);
        args.put(Constants.REZIM_NOC,noc);
        args.put(Constants.REZIM_ZUNAJ_DAN,zunajdan);
        args.put(Constants.REZIM_ZUNAJ_NOC,zunajnoc);
        args.put(Constants.REZIM_LJUDI,ljudi);
        args.put(Constants.SKLOP_NAME,title);
        args.put(Constants.SKLOP_VRSTA,vrsta);
        args.put(Constants.SKLOP_POVRSINA,povrsina);
        return db.update(Constants.TABLE_NAME_SKLOPI, args, Constants.SKLOP_ID + "=" + rowIdsklop, null) > 0;
    }
    
	public boolean deletesklop(long rowIdsklop) {
		
		deletematerialivsklopu(rowIdsklop);
	    return db.delete(Constants.TABLE_NAME_SKLOPI, Constants.SKLOP_ID + "=" + rowIdsklop, null) > 0;
	    
	}
	
	public boolean deletematerialivsklopu(long rowIdsklop)
	{
		Integer rowIdsklopInt = Integer.parseInt(String.valueOf(rowIdsklop));
		return db.delete(Constants.TABLE_NAME, Constants.SKLOP_ID_MATERIALI + "=" + rowIdsklopInt, null) > 0;
		
	}
	
	public long insertmaterial(String obcina,String postna,String naselje,String ulica,String hisna,String dodatek,String stevilka_stanovanja,String tip,
			String leto,String visina,String uporabna_povrsina,String neto_tloris, String povrsina_pod_stavbo,String dan,String noc,String zunajdan,String zunajnoc,String ljudi,Integer IDsklopa,String title,String vrsta,
			String povrsina,String material,String zaporednaid,Integer debelina,String name,String opis,String gostota,
			String c,String lambda,String u)
	{
	try{
	ContentValues newTaskValue = new ContentValues();
	newTaskValue.put(Constants.STAVBA_OBCINA,obcina);
	newTaskValue.put(Constants.STAVBA_POSTNA,postna);
	newTaskValue.put(Constants.STAVBA_NASELJE,naselje);
	newTaskValue.put(Constants.STAVBA_ULICA,ulica);
	newTaskValue.put(Constants.STAVBA_HISNA,hisna);
	newTaskValue.put(Constants.STAVBA_DODATEK_HISNI,dodatek);
	newTaskValue.put(Constants.STAVBA_STEVILKA_STANOVANJA,stevilka_stanovanja);
	newTaskValue.put(Constants.STAVBA_TIP_STAVBE,tip);
	newTaskValue.put(Constants.STAVBA_LETO_IZGRADNJE,leto);
	newTaskValue.put(Constants.STAVBA_VISINA,visina);
	newTaskValue.put(Constants.STAVBA_UPORABNA_POVRSINA,uporabna_povrsina);
	newTaskValue.put(Constants.STAVBA_NETO_TLORIS,neto_tloris);
	newTaskValue.put(Constants.STAVBA_POVRSINA_POD_STAVBO,povrsina_pod_stavbo);
	newTaskValue.put(Constants.REZIM_DAN,dan);
	newTaskValue.put(Constants.REZIM_NOC,noc);
	newTaskValue.put(Constants.REZIM_ZUNAJ_DAN,zunajdan);
	newTaskValue.put(Constants.REZIM_ZUNAJ_NOC,zunajnoc);
	newTaskValue.put(Constants.REZIM_LJUDI,ljudi);
	newTaskValue.put(Constants.SKLOP_ID_MATERIALI,IDsklopa);    //ID sklopa
	newTaskValue.put(Constants.SKLOP_NAME,title);
	newTaskValue.put(Constants.SKLOP_VRSTA,vrsta);
	newTaskValue.put(Constants.SKLOP_POVRSINA,povrsina);
	newTaskValue.put(Constants.KEY_MATERIAL,material);
	newTaskValue.put(Constants.KEY_ID_ZAPOREDNA,zaporednaid);
	newTaskValue.put(Constants.KEY_DEBELINA,debelina);
	newTaskValue.put(Constants.KEY_NAME,name);
	newTaskValue.put(Constants.KEY_OPIS,opis);
	newTaskValue.put(Constants.KEY_GOSTOTA,gostota);
	newTaskValue.put(Constants.KEY_C,c);
	newTaskValue.put(Constants.KEY_LAMBDA,lambda);
	newTaskValue.put(Constants.KEY_U,u);
	return db.insert(Constants.TABLE_NAME, null, newTaskValue);

	
	} 
	catch(SQLiteException ex) {
		Log.v("Insert into database exception caught",
			ex.getMessage());
			return -1;
			}
			}
			

	
	public Cursor getmateriali(){

		Cursor c = db.query(Constants.TABLE_NAME, new String[] {
		Constants.STAVBA_OBCINA,
		Constants.STAVBA_POSTNA,
		Constants.STAVBA_NASELJE,
		Constants.STAVBA_ULICA,
		Constants.STAVBA_HISNA,
		Constants.STAVBA_DODATEK_HISNI,
		Constants.STAVBA_STEVILKA_STANOVANJA,
		Constants.STAVBA_TIP_STAVBE,
		Constants.STAVBA_LETO_IZGRADNJE,
		Constants.STAVBA_VISINA,
		Constants.STAVBA_UPORABNA_POVRSINA,
		Constants.STAVBA_NETO_TLORIS,
		Constants.STAVBA_POVRSINA_POD_STAVBO,
		Constants.REZIM_DAN,
		Constants.REZIM_NOC,
		Constants.REZIM_ZUNAJ_DAN,
		Constants.REZIM_ZUNAJ_NOC,
		Constants.REZIM_LJUDI,
		Constants.SKLOP_ID_MATERIALI,
		Constants.SKLOP_NAME,
		Constants.SKLOP_VRSTA,
		Constants.SKLOP_POVRSINA,
		Constants.KEY_ID,
		Constants.KEY_MATERIAL,
		Constants.KEY_ID_ZAPOREDNA,
		Constants.KEY_DEBELINA,
		Constants.KEY_NAME,
		Constants.KEY_OPIS,
		Constants.KEY_GOSTOTA,
		Constants.KEY_C,
		Constants.KEY_LAMBDA,
		Constants.KEY_U}, null, null, null, null, Constants.SKLOP_VRSTA);		
		return c;
	}
	
    public Cursor getmaterial(long rowId) throws SQLException {

        Cursor mCursor = db.query(true, Constants.TABLE_NAME, new String[] {
        		Constants.STAVBA_OBCINA,
        		Constants.STAVBA_POSTNA,
        		Constants.STAVBA_NASELJE,
        		Constants.STAVBA_ULICA,
        		Constants.STAVBA_HISNA,
        		Constants.STAVBA_DODATEK_HISNI,
        		Constants.STAVBA_STEVILKA_STANOVANJA,
        		Constants.STAVBA_TIP_STAVBE,
        		Constants.STAVBA_LETO_IZGRADNJE,
        		Constants.STAVBA_VISINA,
        		Constants.STAVBA_UPORABNA_POVRSINA,
        		Constants.STAVBA_NETO_TLORIS,
        		Constants.STAVBA_POVRSINA_POD_STAVBO,
        		Constants.REZIM_DAN,
        		Constants.REZIM_NOC,
        		Constants.REZIM_ZUNAJ_DAN,
        		Constants.REZIM_ZUNAJ_NOC,
        		Constants.REZIM_LJUDI,
        		Constants.SKLOP_ID_MATERIALI,
        		Constants.SKLOP_NAME,
        		Constants.SKLOP_VRSTA,
        		Constants.SKLOP_POVRSINA,
        		Constants.KEY_ID,
        		Constants.KEY_MATERIAL,
        		Constants.KEY_ID_ZAPOREDNA,
        		Constants.KEY_DEBELINA,
        		Constants.KEY_NAME,
        		Constants.KEY_OPIS,
        		Constants.KEY_GOSTOTA,
        		Constants.KEY_C,
        		Constants.KEY_LAMBDA,
        		Constants.KEY_U}, Constants.KEY_ID + "=" + rowId, null,
                        null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;

    }
    
    public boolean updatematerial(long rowId, String obcina,String postna,String naselje,String ulica,String hisna,String dodatek,String stevilka_stanovanja,String tip,
			String leto,String visina,String uporabna_povrsina,String neto_tloris, String povrsina_pod_stavbo,String dan,String noc,String zunajdan,String zunajnoc,String ljudi,Integer IDsklopa,String title,String vrsta,
			String povrsina,String material,String zaporednaid,Integer debelina,String name,String opis,String gostota,
			String c,String lambda,String u) {
        ContentValues args = new ContentValues();
        args.put(Constants.STAVBA_OBCINA,obcina);
        args.put(Constants.STAVBA_POSTNA,postna);
        args.put(Constants.STAVBA_NASELJE,naselje);
        args.put(Constants.STAVBA_ULICA,ulica);
        args.put(Constants.STAVBA_HISNA,hisna);
        args.put(Constants.STAVBA_DODATEK_HISNI,dodatek);
        args.put(Constants.STAVBA_STEVILKA_STANOVANJA,stevilka_stanovanja);
        args.put(Constants.STAVBA_TIP_STAVBE,tip);
        args.put(Constants.STAVBA_LETO_IZGRADNJE,leto);
        args.put(Constants.STAVBA_VISINA,visina);
        args.put(Constants.STAVBA_UPORABNA_POVRSINA,uporabna_povrsina);
        args.put(Constants.STAVBA_NETO_TLORIS,neto_tloris);
        args.put(Constants.STAVBA_POVRSINA_POD_STAVBO,povrsina_pod_stavbo);
        args.put(Constants.REZIM_DAN,dan);
        args.put(Constants.REZIM_NOC,noc);
        args.put(Constants.REZIM_ZUNAJ_DAN,zunajdan);
        args.put(Constants.REZIM_ZUNAJ_NOC,zunajnoc);
        args.put(Constants.REZIM_LJUDI,ljudi);
        args.put(Constants.SKLOP_ID_MATERIALI,IDsklopa);
        args.put(Constants.SKLOP_NAME,title);
        args.put(Constants.SKLOP_VRSTA,vrsta);
        args.put(Constants.SKLOP_POVRSINA,povrsina);
        args.put(Constants.KEY_MATERIAL,material);
        args.put(Constants.KEY_ID_ZAPOREDNA,zaporednaid);
        args.put(Constants.KEY_DEBELINA,name);
        args.put(Constants.KEY_NAME,name);
        args.put(Constants.KEY_OPIS,opis);
        args.put(Constants.KEY_GOSTOTA,gostota);
        args.put(Constants.KEY_C,c);
        args.put(Constants.KEY_LAMBDA,lambda);
        args.put(Constants.KEY_U,u);
        return db.update(Constants.TABLE_NAME, args, Constants.KEY_ID + "=" + rowId, null) > 0;
    }
    
	public boolean deletematerial(long rowId) {
		
	    return db.delete(Constants.TABLE_NAME, Constants.KEY_ID + "=" + rowId, null) > 0;
	}
	

    
    int getStrehaCount()
	 {
    	Cursor cur= db.query(true, Constants.TABLE_NAME_SKLOPI, new String[] {
        		Constants.SKLOP_NAME,
        		Constants.SKLOP_VRSTA,
        		Constants.SKLOP_POVRSINA,
        		Constants.SKLOP_ID
        		}, Constants.SKLOP_VRSTA + "=?", new String[] {"Streha"},
                        null, null, null, null);
		int x= cur.getCount();
		cur.close();
		return x;
	 }
    
    int getStenaCount()
	 {
   	Cursor cur= db.query(true, Constants.TABLE_NAME_SKLOPI, new String[] {
       		Constants.SKLOP_NAME,
       		Constants.SKLOP_VRSTA,
       		Constants.SKLOP_POVRSINA,
       		Constants.SKLOP_ID
       		}, Constants.SKLOP_VRSTA + "=?", new String[] {"Stena"},
                       null, null, null, null);
		int x= cur.getCount();
		cur.close();
		return x;
	 }
    
    int getTlaCount()
	 {
   	Cursor cur= db.query(true, Constants.TABLE_NAME_SKLOPI, new String[] {
       		Constants.SKLOP_NAME,
       		Constants.SKLOP_VRSTA,
       		Constants.SKLOP_POVRSINA,
       		Constants.SKLOP_ID,
       		}, Constants.SKLOP_VRSTA + "=?", new String[] {"Tla"},
                       null, null, null, null);
		int x= cur.getCount();
		cur.close();
		return x;
	 }
    
    int pozicijaMateriala(Integer iDsklopa)
	 {
    	Log.d("IDsklopa", String.valueOf(iDsklopa));
  	Cursor cur= db.query(true, Constants.TABLE_NAME, new String[] {
  			Constants.SKLOP_ID_MATERIALI,
  			Constants.KEY_ID,
      		}, Constants.SKLOP_ID_MATERIALI + "=" + iDsklopa, null,
                      null, null, null, null);
		int x= cur.getCount();
		cur.close();
		return x;
	 }

    int sestevekDebelineSklopa2(String imeSklopa)
    {
    	Cursor cursor = db.rawQuery("SELECT sum("+Constants.KEY_DEBELINA+") FROM "+Constants.TABLE_NAME +" WHERE "+Constants.SKLOP_NAME+"=?", new String[] {imeSklopa});
    	cursor.moveToFirst();
    	 int cnt =  cursor.getInt(0);
    	 cursor.close();
    	 cursor.deactivate();
		return cnt;
    }
    public Cursor getsklopiVSE(Integer mu) throws SQLException {

        Cursor mCursor = db.query(true, Constants.TABLE_NAME, new String[] {
        		Constants.STAVBA_OBCINA,
        		Constants.STAVBA_POSTNA,
        		Constants.STAVBA_NASELJE,
        		Constants.STAVBA_ULICA,
        		Constants.STAVBA_HISNA,
        		Constants.STAVBA_DODATEK_HISNI,
        		Constants.STAVBA_STEVILKA_STANOVANJA,
        		Constants.STAVBA_TIP_STAVBE,
        		Constants.STAVBA_LETO_IZGRADNJE,
        		Constants.STAVBA_VISINA,
        		Constants.STAVBA_UPORABNA_POVRSINA,
        		Constants.STAVBA_NETO_TLORIS,
        		Constants.STAVBA_POVRSINA_POD_STAVBO,
        		Constants.REZIM_DAN,
        		Constants.REZIM_NOC,
        		Constants.REZIM_ZUNAJ_DAN,
        		Constants.REZIM_ZUNAJ_NOC,
        		Constants.REZIM_LJUDI,
        		Constants.SKLOP_ID_MATERIALI,    //ID sklopa
        		Constants.SKLOP_NAME,
        		Constants.SKLOP_VRSTA,
        		Constants.SKLOP_POVRSINA,
        		Constants.KEY_ID,
        		Constants.KEY_MATERIAL,
        		Constants.KEY_ID_ZAPOREDNA,
        		Constants.KEY_DEBELINA,
        		Constants.KEY_NAME,
        		Constants.KEY_OPIS,
        		Constants.KEY_GOSTOTA,
        		Constants.KEY_C,
        		Constants.KEY_LAMBDA,
        		Constants.KEY_U}, Constants.SKLOP_ID_MATERIALI + "=" + mu, null, null,
                        null, Constants.KEY_ID_ZAPOREDNA, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }
    
    public Cursor getsklopiPoVrstiSklopa(String[] vrsta) throws SQLException {

        Cursor mCursor = db.query(true, Constants.TABLE_NAME, new String[] {
        		Constants.STAVBA_OBCINA,
        		Constants.STAVBA_POSTNA,
        		Constants.STAVBA_NASELJE,
        		Constants.STAVBA_ULICA,
        		Constants.STAVBA_HISNA,
        		Constants.STAVBA_DODATEK_HISNI,
        		Constants.STAVBA_STEVILKA_STANOVANJA,
        		Constants.STAVBA_TIP_STAVBE,
        		Constants.STAVBA_LETO_IZGRADNJE,
        		Constants.STAVBA_VISINA,
        		Constants.STAVBA_UPORABNA_POVRSINA,
        		Constants.STAVBA_NETO_TLORIS,
        		Constants.STAVBA_POVRSINA_POD_STAVBO,
        		Constants.REZIM_DAN,
        		Constants.REZIM_NOC,
        		Constants.REZIM_ZUNAJ_DAN,
        		Constants.REZIM_ZUNAJ_NOC,
        		Constants.REZIM_LJUDI,
        		Constants.SKLOP_ID_MATERIALI,    //ID sklopa
        		Constants.SKLOP_NAME,
        		Constants.SKLOP_VRSTA,
        		Constants.SKLOP_POVRSINA,
        		Constants.KEY_ID,
        		Constants.KEY_MATERIAL,
        		Constants.KEY_ID_ZAPOREDNA,
        		Constants.KEY_DEBELINA,
        		Constants.KEY_NAME,
        		Constants.KEY_OPIS,
        		Constants.KEY_GOSTOTA,
        		Constants.KEY_C,
        		Constants.KEY_LAMBDA,
        		Constants.KEY_U}, Constants.SKLOP_VRSTA + "=?", vrsta, null,
                        null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }
    
    public boolean deleteDatabase1() {
		
	    return db.delete(Constants.TABLE_NAME, null, null)>0;

	}
    public boolean deleteDatabase2() {
		
	    return db.delete(Constants.TABLE_NAME_SKLOPI, null, null)>0;
	    
	}
	
}
	
