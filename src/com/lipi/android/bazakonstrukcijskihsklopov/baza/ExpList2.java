package com.lipi.android.bazakonstrukcijskihsklopov.baza;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.lipi.android.bazakonstrukcijskihsklopov.Locator;
import com.lipi.android.bazakonstrukcijskihsklopov.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ExpandableListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.PorterDuff.Mode;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.SimpleAdapter;
import android.widget.SimpleExpandableListAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupCollapseListener;
import android.widget.ExpandableListView.OnGroupExpandListener;

public class ExpList2 extends ExpandableListActivity {

	static final String URL = "http://lipnick.byethost4.com/materiali.xml";
	// XML node keys
	static final String KEY_ITEM = "item"; // parent node
	static final String KEY_SKUPINA = "skupina";
	static final String KEY_MATERIAL = "material";
	static final String KEY_ID = "id";
	static final String KEY_NAME = "name";
	static final String KEY_OPIS = "opis";
	static final String KEY_GOSTOTA = "gostota";
	static final String KEY_C = "c";
	static final String KEY_LAMBDA = "lambda";
	static final String KEY_U = "u";
	private ExpandableListAdapter mAdapter;
	private Element e;
	private Element f;
	private Element g;
	private Element eShrani;
	private Element fShrani;
	private Context ExpContext;
	private MyDB dba;
	private String groupName;
	private String groupVrsta;
	private String groupPovrsina;
	private Long IDsklopa;
	private Integer IDsklopaMateriali;
	private ExpandableListView expList;
	private NodeList nl;
	private XMLParser parser;
	private String xml;
	private Document doc;
	private Integer debelina;
	private String pozicijaS;
	private Cursor podatkiStavbe;
	private Cursor podatkiRezim;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		dba = new MyDB(this);
		groupName = savedInstanceState != null ? savedInstanceState.getString(Constants.SKLOP_NAME) 
				: null;
		IDsklopa = savedInstanceState != null ? savedInstanceState.getLong(Constants.SKLOP_ID) 
				: null;
		parser = new XMLParser();
		xml = parser.getXmlFromUrl(URL); // getting XML
		doc = parser.getDomElement(xml); // getting DOM element
		nl = doc.getElementsByTagName(KEY_ITEM);
		ExpContext = this;
		fillExpandableList();
		getMateriali();
		

		
	}

	@Override
    protected void onPause() {
        super.onPause();
        dba.close(); 
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        dba.open(); 
        fillExpandableList();
        getMateriali();
    }
    
    private void fillExpandableList()
    {
    	Bundle extras = getIntent().getExtras();
    	groupPovrsina = extras != null ? extras.getString(Constants.SKLOP_POVRSINA)
				: null;
    	groupVrsta = extras != null ? extras.getString(Constants.SKLOP_VRSTA)
				: null;
    	groupName = extras != null ? extras.getString(Constants.SKLOP_NAME)
				: null;
		IDsklopa = extras != null ? extras.getLong(Constants.SKLOP_ID)
				: null;
		IDsklopaMateriali = Integer.parseInt(IDsklopa.toString());
		// groupName = savedInstanceState != null ?
		// savedInstanceState.getString(Constants.SKLOP_NAME) : null;
		//Log.e(groupName, "bla");
		
		
		// looping through all item nodes <item>

		List<Map<String, String>> groupData = new ArrayList<Map<String, String>>();
		List<List<Map<String, String>>> childData = new ArrayList<List<Map<String, String>>>();
		List<List<Map<String, String>>> podatkiData = new ArrayList<List<Map<String, String>>>();
		for (int i = 0; i < nl.getLength(); i++) {
			Map<String, String> map = new HashMap<String, String>();
			e = (Element) nl.item(i);
			map.put(KEY_SKUPINA, parser.getValue(0, e, KEY_SKUPINA));
			groupData.add(map);

			List<Map<String, String>> children = new ArrayList<Map<String, String>>();
			for (int k = 0; k < e.getElementsByTagName(KEY_MATERIAL)
					.getLength(); k++) {
				Map<String, String> podmap = new HashMap<String, String>();
				f = (Element) e.getElementsByTagName(KEY_MATERIAL).item(k);
				podmap.put(KEY_ID, parser.getValue(k, e, KEY_ID));
				podmap.put(KEY_NAME, parser.getValue(k, e, KEY_NAME));
				podmap.put(KEY_OPIS, parser.getValue(k, e, KEY_OPIS));
				podmap.put(KEY_GOSTOTA, parser.getValue(k, e, KEY_GOSTOTA));
				podmap.put(KEY_LAMBDA, parser.getValue(k, e, KEY_LAMBDA));
				podmap.put(KEY_U, parser.getValue(k, e, KEY_U));
				children.add(podmap);

				// List<Map<String, String>> podatki = new ArrayList<Map<String,
				// String>>();
				// for (int z = 0; z <
				// f.getElementsByTagName(KEY_NAME).getLength(); z++){
				// Map<String, String> podmapPodatki = new HashMap<String,
				// String>();
				// podmapPodatki.put(KEY_NAME, parser.getValue(z, f, KEY_NAME));
				// podatki.add(podmapPodatki);
				// String hf = String.valueOf(z);
				// Log.e("vrednost z",String.valueOf((Element)
				// e.getElementsByTagName(KEY_MATERIAL).item(k)));
				//
				// }
				//
				// podatkiData.add(podatki);

			}
			childData.add(children);

		}	
		
		mAdapter = new SimpleExpandableListAdapter(this, groupData,
				R.layout.materiali_vrstica, new String[] { KEY_SKUPINA },
				new int[] { R.id.tvGroupName }, childData,
				R.layout.materiali_podvrstica, new String[] { KEY_NAME,
						KEY_OPIS }, new int[] { R.id.tvPlayerName,
						R.id.tvPlayerName2 });

		setListAdapter(mAdapter);
		
    }
    
    private void getMateriali()
	{
	expList = getExpandableListView();

	expList.setOnChildClickListener(new OnChildClickListener() {
		public boolean onChildClick(ExpandableListView parent, View v,
				int groupPosition, int childPosition, long id) {

			eShrani = (Element) nl.item(groupPosition);
			fShrani = (Element) eShrani.getElementsByTagName(KEY_MATERIAL).item(
					childPosition);
			SeekbarInPozicija();
			
			return false;
		}
	});
	}

	public void SeekbarInPozicija() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		LayoutInflater inflater = this.getLayoutInflater();
		View v = inflater.inflate(R.layout.materiali_izbira_debeline, null);
		final EditText ev = (EditText) v.findViewById(R.id.editSize);
		final EditText pozicija = (EditText) v.findViewById(R.id.editSize2);
		builder.setView(v).setTitle("Debelina sloja:");
		builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {

				Toast.makeText(ExpContext,
						"Material vnesen v konstrukcijski sklop "+groupName,
						Toast.LENGTH_SHORT).show();
				debelina = Integer.parseInt(ev.getText().toString());
				pozicijaS = pozicija.getText().toString();
				shrani();
				
			}
		});
		SeekBar sb = (SeekBar) v.findViewById(R.id.seekBar1);
		ev.setText("50");
		sb.setMax(0);
		sb.setMax(300);
		sb.setProgress(0);
		sb.setProgress(50);
		sb.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {

				progress = ((int) Math.round(progress / 5)) * 5;
				seekBar.setProgress(progress);
				ev.setText(String.valueOf(progress));

			}

			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub

			}

			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub

			}
		});
		
		Integer m = dba.pozicijaMateriala(IDsklopaMateriali)+1;
		pozicija.setText(m.toString());
		builder.create();
		builder.show();
	}
	
	
	@Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong(Constants.SKLOP_ID, IDsklopa);
    }
	
	private void shrani()
	{
		podatkiStavbe = dba.getstavba(1);
		podatkiRezim = dba.getrezim(1);
		try {
			
			dba.insertmaterial(podatkiStavbe.getString(podatkiStavbe.getColumnIndex(Constants.STAVBA_OBCINA)),podatkiStavbe.getString(podatkiStavbe.getColumnIndex(Constants.STAVBA_POSTNA)),podatkiStavbe.getString(podatkiStavbe.getColumnIndex(Constants.STAVBA_NASELJE)),
        			podatkiStavbe.getString(podatkiStavbe.getColumnIndex(Constants.STAVBA_ULICA)),podatkiStavbe.getString(podatkiStavbe.getColumnIndex(Constants.STAVBA_HISNA)),podatkiStavbe.getString(podatkiStavbe.getColumnIndex(Constants.STAVBA_DODATEK_HISNI)),podatkiStavbe.getString(podatkiStavbe.getColumnIndex(Constants.STAVBA_STEVILKA_STANOVANJA)),
        			podatkiStavbe.getString(podatkiStavbe.getColumnIndex(Constants.STAVBA_TIP_STAVBE)),podatkiStavbe.getString(podatkiStavbe.getColumnIndex(Constants.STAVBA_LETO_IZGRADNJE)),podatkiStavbe.getString(podatkiStavbe.getColumnIndex(Constants.STAVBA_VISINA)),podatkiStavbe.getString(podatkiStavbe.getColumnIndex(Constants.STAVBA_UPORABNA_POVRSINA)),
        			podatkiStavbe.getString(podatkiStavbe.getColumnIndex(Constants.STAVBA_NETO_TLORIS)),podatkiStavbe.getString(podatkiStavbe.getColumnIndex(Constants.STAVBA_POVRSINA_POD_STAVBO)),podatkiRezim.getString(podatkiRezim.getColumnIndex(Constants.REZIM_DAN)),podatkiRezim.getString(podatkiRezim.getColumnIndex(Constants.REZIM_NOC)),podatkiRezim.getString(podatkiRezim.getColumnIndex(Constants.REZIM_ZUNAJ_DAN)),podatkiRezim.getString(podatkiRezim.getColumnIndex(Constants.REZIM_ZUNAJ_NOC)),podatkiRezim.getString(podatkiRezim.getColumnIndex(Constants.REZIM_LJUDI)), IDsklopaMateriali, groupName,
					groupVrsta, groupPovrsina, eShrani.getElementsByTagName(KEY_SKUPINA)
							.item(0).getTextContent(), pozicijaS, debelina, fShrani
							.getElementsByTagName(KEY_NAME).item(0)
							.getTextContent(),
							fShrani.getElementsByTagName(KEY_OPIS).item(0)
							.getTextContent(),
							fShrani.getElementsByTagName(KEY_GOSTOTA).item(0)
							.getTextContent(),
							fShrani.getElementsByTagName(KEY_C).item(0)
							.getTextContent(),
							fShrani.getElementsByTagName(KEY_LAMBDA).item(0)
							.getTextContent(),
							fShrani.getElementsByTagName(KEY_U).item(0)
							.getTextContent());

			

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
