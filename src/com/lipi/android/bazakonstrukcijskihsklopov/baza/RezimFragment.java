package com.lipi.android.bazakonstrukcijskihsklopov.baza;

import com.lipi.android.bazakonstrukcijskihsklopov.R;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;

public class RezimFragment extends Activity {
	private String OgrevanjeDan;
	private String OgrevanjeNoc;
	private String ZunajDanString;
	private String ZunajNocString;
	private EditText ZunajDan;
	private EditText ZunajNoc;
	private String SteviloVgospodinjstvu;
	private EditText EditDan;
	private EditText EditNoc;
	private EditText EditStevilo;
	private MyDB dba;
	private Integer mRowIdRezim;
	private Cursor zCursor;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);	
    	
        setContentView(R.layout.rezim_layout);
        dba = new MyDB(this);
        EditDan = (EditText) findViewById(R.id.editText1);
        EditNoc = (EditText) findViewById(R.id.EditText01);
        ZunajDan = (EditText) findViewById(R.id.EditText4);
        ZunajNoc = (EditText) findViewById(R.id.EditText5);
        EditStevilo = (EditText) findViewById(R.id.EditText02);
        dba.open();
    }
    
    @Override
	public void onPause() {
        super.onPause();
        toStrings();
        shrani();
        dba.close(); 
    }
    
    @Override
	public void onResume() {
        super.onResume();
        dba.open();
    	setRowIdFromIntent();
		populateFields();
		toStrings();
    }
    
	private void setRowIdFromIntent() {
		
		zCursor = dba.getrezimi();
		Integer stev = zCursor.getCount();
    	if (stev.equals(0))
    	{
    		mRowIdRezim = 0;
    	}	
    	else
    	{
    		mRowIdRezim = 1;
    	}
	}
	
    public void populateFields()  {
    	
  	
    	
    	// Only populate the text boxes and change the calendar date
    	// if the row is not null from the database. 
        if (mRowIdRezim.equals(1)) {
            Cursor podatkiRezim = dba.getrezim(zCursor.getCount());
            EditDan.setText(podatkiRezim.getString(podatkiRezim.getColumnIndex(Constants.REZIM_DAN)));
            EditNoc.setText(podatkiRezim.getString(podatkiRezim.getColumnIndex(Constants.REZIM_NOC)));
            ZunajDan.setText(podatkiRezim.getString(podatkiRezim.getColumnIndex(Constants.REZIM_ZUNAJ_DAN)));
            ZunajNoc.setText(podatkiRezim.getString(podatkiRezim.getColumnIndex(Constants.REZIM_ZUNAJ_NOC)));
            EditStevilo.setText(podatkiRezim.getString(podatkiRezim.getColumnIndex(Constants.REZIM_LJUDI)));
    		
        }
    }
    
	private void toStrings()
	{
		OgrevanjeDan = EditDan.getText().toString();
        OgrevanjeNoc = EditNoc.getText().toString();
        ZunajDanString = ZunajDan.getText().toString();
        ZunajNocString = ZunajNoc.getText().toString();
        SteviloVgospodinjstvu = EditStevilo.getText().toString();
		
	}
    
    public void shrani()
	{	      
        if (mRowIdRezim.equals(0)) {
        	
        	dba.insertrezim(OgrevanjeDan,OgrevanjeNoc,ZunajDanString,ZunajNocString,SteviloVgospodinjstvu);

        } else {
            dba.updaterezim(mRowIdRezim,OgrevanjeDan,OgrevanjeNoc,ZunajDanString,ZunajNocString,SteviloVgospodinjstvu);
        }
		dba.close();

}	
}