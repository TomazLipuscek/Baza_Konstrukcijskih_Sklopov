package com.lipi.android.bazakonstrukcijskihsklopov.baza;

import com.lipi.android.bazakonstrukcijskihsklopov.R;

import android.os.Bundle;
import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

public class MainActivity extends TabActivity {
	public static String Postna;	
	public static String Naselje;
	public static String besedilo;
	public static String stevilka;
	public static Context Stavba;
	public static TabHost tabHost;
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        Stavba = this;
        Bundle extras = getIntent().getExtras();
        Postna = extras != null ? extras.getString(Constants.STAVBA_POSTNA)
				: null;
        Naselje = extras != null ? extras.getString(Constants.STAVBA_NASELJE)
				: null;
    	besedilo = extras != null ? extras.getString(Constants.STAVBA_ULICA)
				: null;
    	stevilka = extras != null ? extras.getString(Constants.STAVBA_HISNA)
				: null;
        tabHost = getTabHost();
        
        TabSpec stavbaspec = tabHost.newTabSpec("Stavba");
        stavbaspec.setIndicator("Stavba");
        Intent stavbaIntent = new Intent(this, StavbaFragment.class);
        stavbaspec.setContent(stavbaIntent);
        
        TabSpec pregledspec = tabHost.newTabSpec("Režim");
        pregledspec.setIndicator("Režim");
        Intent pregledIntent = new Intent(this, RezimFragment.class);
        pregledspec.setContent(pregledIntent);
        
        TabSpec materialispec = tabHost.newTabSpec("Materiali")
        		.setIndicator("Materiali")
                .setContent(new Intent(this, MaterialiSklopiActivity.class)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        
        TabSpec rezultatispec = tabHost.newTabSpec("Rezultati");
        rezultatispec.setIndicator("Rezultati");
        Intent rezultatiIntent = new Intent(this, RezultatiFragment.class);
        rezultatispec.setContent(rezultatiIntent);
        
        tabHost.addTab(stavbaspec);
        tabHost.addTab(pregledspec);
        tabHost.addTab(materialispec);
        tabHost.addTab(rezultatispec);
        
        
    	
    }
    public void switchTab(int tab){
        tabHost.setCurrentTab(tab);
    }

    }

