package com.lipi.android.bazakonstrukcijskihsklopov.baza;


import com.lipi.android.bazakonstrukcijskihsklopov.R;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class StavbaFragment extends Activity {
	private String Obcina;
	private String Postna;
	private String Naselje;
	private String besedilo;
	private String stevilka;
	private String Dodatek;
	private String StevilkaStanovanja;
	private String Tip;
	private String Leto;
	private String Visina;
	private String UporabnaPovrsina;
	private String NetoTloris;
	private String PovrsinaPodStavbo;
	private Integer mRowIdStavba;
	
	
	private EditText editText1;
	private EditText editText001;
	private EditText editText01;
	private EditText editText02;
	private EditText editText03;
	private EditText editText04;
	private EditText editText05;
	private Spinner spinner1;
	private EditText editText08;
	private EditText editText09;
	private EditText editText10;
	private EditText editText11;
	private EditText editText12;
	private MyDB dba;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);	
    	
        setContentView(R.layout.stavba_layout);
        dba = new MyDB(this);
        editText1 = (EditText) findViewById(R.id.EditText1);
    	editText001 = (EditText) findViewById(R.id.EditText001);
        editText01 = (EditText) findViewById(R.id.EditText01);
        editText02 = (EditText) findViewById(R.id.EditText02);
        editText03 = (EditText) findViewById(R.id.EditText03);
        editText04 = (EditText) findViewById(R.id.EditText04);
        editText05 = (EditText) findViewById(R.id.EditText05);
        spinner1 = (Spinner) findViewById(R.id.spinner1);
        editText08 = (EditText) findViewById(R.id.EditText08);
        editText09 = (EditText) findViewById(R.id.EditText09);
        editText10 = (EditText) findViewById(R.id.EditText10);
        editText11 = (EditText) findViewById(R.id.EditText11);
        editText12 = (EditText) findViewById(R.id.EditText12);
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
		
		Cursor zCursor = dba.getstavbe();
		Integer stev = zCursor.getCount();
    	if (stev.equals(0))
    	{
    		mRowIdStavba = 0;
    	}	
    	else
    	{
    		mRowIdStavba = 1;
    	}
	}
	
    public void populateFields()  {
    	
  	
    	
    	// Only populate the text boxes and change the calendar date
    	// if the row is not null from the database. 
    	Log.d(String.valueOf(mRowIdStavba), "sklop stevilo");
        if (mRowIdStavba.equals(1)) {
            Cursor podatkiStavbe = dba.getstavba(mRowIdStavba);
            editText1.setText(podatkiStavbe.getString(podatkiStavbe.getColumnIndex(Constants.STAVBA_OBCINA)));
    		editText001.setText(podatkiStavbe.getString(podatkiStavbe.getColumnIndex(Constants.STAVBA_POSTNA)));
    		editText01.setText(podatkiStavbe.getString(podatkiStavbe.getColumnIndex(Constants.STAVBA_NASELJE)));
    		editText02.setText(podatkiStavbe.getString(podatkiStavbe.getColumnIndex(Constants.STAVBA_ULICA)));
    		editText03.setText(podatkiStavbe.getString(podatkiStavbe.getColumnIndex(Constants.STAVBA_HISNA)));
    		editText04.setText(podatkiStavbe.getString(podatkiStavbe.getColumnIndex(Constants.STAVBA_DODATEK_HISNI)));
    		editText05.setText(podatkiStavbe.getString(podatkiStavbe.getColumnIndex(Constants.STAVBA_STEVILKA_STANOVANJA)));
    		spinner1.setSelection(0);
    		editText08.setText(podatkiStavbe.getString(podatkiStavbe.getColumnIndex(Constants.STAVBA_LETO_IZGRADNJE)));
    		editText09.setText(podatkiStavbe.getString(podatkiStavbe.getColumnIndex(Constants.STAVBA_VISINA)));
    		editText10.setText(podatkiStavbe.getString(podatkiStavbe.getColumnIndex(Constants.STAVBA_UPORABNA_POVRSINA)));
    		editText11.setText(podatkiStavbe.getString(podatkiStavbe.getColumnIndex(Constants.STAVBA_NETO_TLORIS)));
    		editText12.setText(podatkiStavbe.getString(podatkiStavbe.getColumnIndex(Constants.STAVBA_POVRSINA_POD_STAVBO)));
    		
        }
        else
        {
        	editText001.setText(MainActivity.Postna);
    		editText01.setText(MainActivity.Naselje);
    		editText02.setText(MainActivity.besedilo);
    		editText03.setText(MainActivity.stevilka);
        	
        }
    }
    
	private void toStrings()
	{
		Obcina = editText1.getText().toString();
		Postna = editText001.getText().toString();
		Naselje = editText01.getText().toString();
		besedilo = editText02.getText().toString();
		stevilka = editText03.getText().toString();
		Dodatek = editText04.getText().toString();
		StevilkaStanovanja = editText05.getText().toString();
		Tip = spinner1.getSelectedItem().toString();
		Leto = editText08.getText().toString();
		Visina = editText09.getText().toString();
		UporabnaPovrsina = editText10.getText().toString();
		NetoTloris = editText11.getText().toString();
		PovrsinaPodStavbo = editText12.getText().toString();
		
	}
    
    public void shrani()
	{	      
        if (mRowIdStavba.equals(0)) {
        	
        	dba.insertstavba(Obcina,Postna,Naselje,besedilo,stevilka,Dodatek,StevilkaStanovanja,Tip,
        			Leto,Visina,UporabnaPovrsina,NetoTloris,PovrsinaPodStavbo);

        } else {
            dba.updatestavba(mRowIdStavba,Obcina,Postna,Naselje,besedilo,stevilka,Dodatek,StevilkaStanovanja,Tip,
        			Leto,Visina,UporabnaPovrsina,NetoTloris,PovrsinaPodStavbo);
        }
		dba.close();

}	
}
