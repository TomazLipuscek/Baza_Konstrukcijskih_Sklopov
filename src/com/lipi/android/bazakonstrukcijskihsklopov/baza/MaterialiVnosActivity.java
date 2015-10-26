package com.lipi.android.bazakonstrukcijskihsklopov.baza;

import com.lipi.android.bazakonstrukcijskihsklopov.R;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;


public class MaterialiVnosActivity extends FragmentActivity {
	
	public static Spinner mSpinner;
	private FragmentManager fm = getSupportFragmentManager();
	public static Context mContext6;
	FragmentTransaction ft;	
    private Long mRowId;
    private long pos;
    MyDB dba;
    private ArrayAdapter<MaterialiSpinnerVrstaSklopa> spinnerArrayAdapter;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.materiali_glavna);

		mSpinner = (Spinner)findViewById(R.id.spinner1);
		mContext6 = this;
		Bundle extras = getIntent().getExtras();
		mRowId = extras != null ? extras.getLong(Constants.SKLOP_ID) 
								: null;
		
	    spinnerArrayAdapter = new ArrayAdapter<MaterialiSpinnerVrstaSklopa>(this, R.layout.spinner_entry_with_icon, new MaterialiSpinnerVrstaSklopa[] {	
	  			new MaterialiSpinnerVrstaSklopa( 1, "Streha"), 
	    		new MaterialiSpinnerVrstaSklopa( 2, "Stena"), 
	    		new MaterialiSpinnerVrstaSklopa( 3, "Tla"),
	      			});
	    mSpinner.setAdapter(spinnerArrayAdapter);
		
		if (mRowId == null) {
			
			vrsticaIzbiraSklopa();
		}
		
		else{
			popravaSklopa();	

		}
	}
    
    private void popravaSklopa(){
	    dba = new MyDB(this);
		dba.open();
	    Cursor podatkiSklopa = dba.getsklop(mRowId);
	    startManagingCursor(podatkiSklopa);
	    String sklop = podatkiSklopa.getString(podatkiSklopa.getColumnIndexOrThrow(Constants.SKLOP_VRSTA));
		if (sklop.equals("Streha")){
	        pos = 0;
	        mSpinner.setSelection(0);
		}
		else if (sklop.equals("Stena")){
	        pos = 1;
	        mSpinner.setSelection(1);
		}
		else if (sklop.equals("Tla")){
	        pos = 2;
	        mSpinner.setSelection(2);
		}
		
		podatkiSklopa.close();
		dba.close();
		vrsticaIzbiraSklopa();
	    }
    
    private void vrsticaIzbiraSklopa(){

		mSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

				
				
				pos = spinnerArrayAdapter.getItemId(position);
				Ifstavki();

		        
		        }

			public void onNothingSelected(AdapterView<?> parent) {	
	}
		});
    }
	private void Ifstavki(){
	ft = fm.beginTransaction();	
	if (pos == 0){
        ft.replace(R.id.fragment_spodaj, new MaterialiVnosStrehaFragment());
	}
	if (pos == 1){
        ft.replace(R.id.fragment_spodaj, new MaterialiVnosStenaFragment());
	}
	if (pos == 2){
        ft.replace(R.id.fragment_spodaj, new MaterialiVnosTlaFragment());       
	}
	ft.commit();
	}
}

