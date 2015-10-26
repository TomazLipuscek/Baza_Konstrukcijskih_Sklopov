package com.lipi.android.bazakonstrukcijskihsklopov.baza;


import com.lipi.android.bazakonstrukcijskihsklopov.R;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MaterialiVnosTlaFragment extends Fragment {
	
	private EditText imeTla;
	private EditText povrsinaTal;
	private Button shrani;
    private MyDB dba;
    private Long mRowIdSklop;
    private Cursor podatkiStavbe;
    private Cursor podatkiStavb;
    private Cursor podatkiRezim;
    private Cursor podatkiRezimi;
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	
        View view = inflater.inflate(R.layout.materiali_tla, container, false);
        
        imeTla=(EditText)view.findViewById(R.id.title_tla);
        povrsinaTal=(EditText)view.findViewById(R.id.edit_povrsina_tla);
        shrani=(Button)view.findViewById(R.id.button15);
        dba = new MyDB(MaterialiVnosActivity.mContext6);
        
	    shrani.setOnClickListener(
	            new View.OnClickListener()
	            {
	                public void onClick(View view)
	                {
	                	try {
	                	shrani();
	                } 
	                	catch (Exception e) {
	                e.printStackTrace();
	                }
	                	
	                }
	            });
	   return view;
    }
    

    public void shrani()
	{	      
    	podatkiStavb = dba.getstavbe();
    	podatkiStavbe = dba.getstavba(podatkiStavb.getCount());
    	podatkiRezimi = dba.getrezimi();
    	podatkiRezim = dba.getrezim(podatkiRezimi.getCount());
        if (mRowIdSklop == null) {
        	
        	long id = dba.insertsklop(imeTla.getText().toString(),"Tla",
        			povrsinaTal.getText().toString(),podatkiStavbe.getString(podatkiStavbe.getColumnIndex(Constants.STAVBA_OBCINA)),podatkiStavbe.getString(podatkiStavbe.getColumnIndex(Constants.STAVBA_POSTNA)),podatkiStavbe.getString(podatkiStavbe.getColumnIndex(Constants.STAVBA_NASELJE)),
        			podatkiStavbe.getString(podatkiStavbe.getColumnIndex(Constants.STAVBA_ULICA)),podatkiStavbe.getString(podatkiStavbe.getColumnIndex(Constants.STAVBA_HISNA)),podatkiStavbe.getString(podatkiStavbe.getColumnIndex(Constants.STAVBA_DODATEK_HISNI)),podatkiStavbe.getString(podatkiStavbe.getColumnIndex(Constants.STAVBA_STEVILKA_STANOVANJA)),
        			podatkiStavbe.getString(podatkiStavbe.getColumnIndex(Constants.STAVBA_TIP_STAVBE)),podatkiStavbe.getString(podatkiStavbe.getColumnIndex(Constants.STAVBA_LETO_IZGRADNJE)),podatkiStavbe.getString(podatkiStavbe.getColumnIndex(Constants.STAVBA_VISINA)),podatkiStavbe.getString(podatkiStavbe.getColumnIndex(Constants.STAVBA_UPORABNA_POVRSINA)),
        			podatkiStavbe.getString(podatkiStavbe.getColumnIndex(Constants.STAVBA_NETO_TLORIS)),podatkiStavbe.getString(podatkiStavbe.getColumnIndex(Constants.STAVBA_POVRSINA_POD_STAVBO)),podatkiRezim.getString(podatkiRezim.getColumnIndex(Constants.REZIM_DAN)),podatkiRezim.getString(podatkiRezim.getColumnIndex(Constants.REZIM_NOC)),podatkiRezim.getString(podatkiRezim.getColumnIndex(Constants.REZIM_ZUNAJ_DAN)),podatkiRezim.getString(podatkiRezim.getColumnIndex(Constants.REZIM_ZUNAJ_NOC)),podatkiRezim.getString(podatkiRezim.getColumnIndex(Constants.REZIM_LJUDI)));
    		Toast.makeText(MaterialiVnosActivity.mContext6, "Konstrukcijski sklop uspešno vnesen",
					Toast.LENGTH_SHORT).show();
            if (id > 0) {
            	mRowIdSklop = id;
            }
        } else {
            dba.updatesklop(mRowIdSklop,imeTla.getText().toString(),"Tla",
            		povrsinaTal.getText().toString(),podatkiStavbe.getString(podatkiStavbe.getColumnIndex(Constants.STAVBA_OBCINA)),podatkiStavbe.getString(podatkiStavbe.getColumnIndex(Constants.STAVBA_POSTNA)),podatkiStavbe.getString(podatkiStavbe.getColumnIndex(Constants.STAVBA_NASELJE)),
        			podatkiStavbe.getString(podatkiStavbe.getColumnIndex(Constants.STAVBA_ULICA)),podatkiStavbe.getString(podatkiStavbe.getColumnIndex(Constants.STAVBA_HISNA)),podatkiStavbe.getString(podatkiStavbe.getColumnIndex(Constants.STAVBA_DODATEK_HISNI)),podatkiStavbe.getString(podatkiStavbe.getColumnIndex(Constants.STAVBA_STEVILKA_STANOVANJA)),
        			podatkiStavbe.getString(podatkiStavbe.getColumnIndex(Constants.STAVBA_TIP_STAVBE)),podatkiStavbe.getString(podatkiStavbe.getColumnIndex(Constants.STAVBA_LETO_IZGRADNJE)),podatkiStavbe.getString(podatkiStavbe.getColumnIndex(Constants.STAVBA_VISINA)),podatkiStavbe.getString(podatkiStavbe.getColumnIndex(Constants.STAVBA_UPORABNA_POVRSINA)),
        			podatkiStavbe.getString(podatkiStavbe.getColumnIndex(Constants.STAVBA_NETO_TLORIS)),podatkiStavbe.getString(podatkiStavbe.getColumnIndex(Constants.STAVBA_POVRSINA_POD_STAVBO)),podatkiRezim.getString(podatkiRezim.getColumnIndex(Constants.REZIM_DAN)),podatkiRezim.getString(podatkiRezim.getColumnIndex(Constants.REZIM_NOC)),podatkiRezim.getString(podatkiRezim.getColumnIndex(Constants.REZIM_ZUNAJ_DAN)),podatkiRezim.getString(podatkiRezim.getColumnIndex(Constants.REZIM_ZUNAJ_NOC)),podatkiRezim.getString(podatkiRezim.getColumnIndex(Constants.REZIM_LJUDI)));
    		Toast.makeText(MaterialiVnosActivity.mContext6, "Konstrukcijski sklop uspešno posodobljen",
					Toast.LENGTH_SHORT).show();
        }
		dba.close();

		getActivity().finish();

}	
    @Override
	public void onPause() {
        super.onPause();
        dba.close(); 
    }
    
    @Override
	public void onResume() {
        super.onResume();
        dba.open();
    	setRowIdFromIntent();
		populateFields();
    }
    
	private void setRowIdFromIntent() {
		
		imeTla.setText("Tla "+String.valueOf(dba.getTlaCount()+1));

			Bundle extras = getActivity().getIntent().getExtras();            
			mRowIdSklop = extras != null ? extras.getLong(Constants.SKLOP_ID) 
									: null;
			
		
	}
	
    public void populateFields()  {
    	
  	
    	
    	// Only populate the text boxes and change the calendar date
    	// if the row is not null from the database. 
        if (mRowIdSklop != null) {
        	
            Cursor podatkiSklopa = dba.getsklop(mRowIdSklop);
            getActivity().startManagingCursor(podatkiSklopa);
            imeTla.setText(podatkiSklopa.getString(podatkiSklopa.getColumnIndexOrThrow(Constants.SKLOP_NAME)));
            povrsinaTal.setText(podatkiSklopa.getString(podatkiSklopa.getColumnIndexOrThrow(Constants.SKLOP_POVRSINA)));

            
       podatkiSklopa.close();
        }
    }
    
    
}
