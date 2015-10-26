package com.lipi.android.bazakonstrukcijskihsklopov.baza;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.util.ArrayList;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;
import org.xmlpull.v1.XmlSerializer;

import com.lipi.android.bazakonstrukcijskihsklopov.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.util.Xml;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class RezultatiFragment extends Activity {
	private MyDB dba;
	private final String NAMESPACE = "http://ws.apache.org/axis2";
    private final String URL = "http://192.168.1.101:8080/InsertToUsers/services/Users?wsdl";
    private final String SOAP_ACTION = "http://ws.login.com/axis2/insertData";
    private final String METHOD_NAME = "insertData";
    private String imeOpenDatoteke;
    private File dir;
    private String STAVBA_OBCINA;
    private String STAVBA_POSTNA;
    private String STAVBA_NASELJE;
    private String STAVBA_ULICA;
    private String STAVBA_HISNA;
    private String STAVBA_DODATEK_HISNI;
    private String STAVBA_STEVILKA_STANOVANJA;
    private String STAVBA_TIP_STAVBE;
    private String STAVBA_LETO_IZGRADNJE;
    private String STAVBA_VISINA;
    private String STAVBA_UPORABNA_POVRSINA;
    private String STAVBA_NETO_TLORIS;
    private String STAVBA_POVRSINA_POD_STAVBO;
    private String REZIM_DAN;
    private String REZIM_NOC;
    private String REZIM_ZUNAJ_DAN;
    private String REZIM_ZUNAJ_NOC;
    private String REZIM_LJUDI;
    private String SKLOP_ID_MATERIALI;
    private String SKLOP_NAME;
    private String SKLOP_VRSTA;
    private String SKLOP_POVRSINA;
    private String KEY_MATERIAL;
    private String KEY_DEBELINA;
    private String KEY_ID_ZAPOREDNA;
    private String KEY_NAME;
    private String KEY_OPIS;
    private String KEY_GOSTOTA;
    private String KEY_C;
    private String KEY_LAMBDA;
    private String KEY_U;
    private Cursor podatkiRezim;
    private Cursor podatkiStavbe;
    private Cursor RezimCursori;
    private Cursor RezimCursor;
    private Cursor podatkiStavb;
    private Cursor podatkiRezimi;
    private String poslano;
    private String imeObjekta; 
    private String poslano1;
    private String poslano2;
    private String poslano3;
    private String poslano4;
    private Button btnposlji;
    private Button btnshrani;
    private Button btnodpri;
    private Button btnizracunaj;
    private Cursor SklopiCursor;
    private TextView result;
    private TextView result1;
    private TextView result2;
    private TextView result3;
    private TextView result4;
    private Cursor mSklopiCursor;
    private Cursor mSklopiCursorMateriali;
    private Context mContextOdpri;
    private String rezultat;
    private String rezultat1;
    private String rezultat2;
    private String rezultat3;
    private AlertDialog.Builder coco;
    private AlertDialog alert;
    private DecimalFormat df = new DecimalFormat("#");
    private Double Upovprecni;
    private Double SSkupajZanaprej;
    private Double T1;
    private Double T2;
    private Double T1zunaj;
    private Double T2zunaj;
    private Double TspremembaDan;
    private Double TspremembaNoc;
    private Double Tsprememba;
    private Double P;
    private Double LdLs;
    private Double Ppodnevi;
    private Double Pponoci;
    
    
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);	
    	
        setContentView(R.layout.rezultati_layout); 
        dba = new MyDB(this);
        result = (TextView) findViewById(R.id.textView00);
        result1 = (TextView) findViewById(R.id.textView25);
        result2 = (TextView) findViewById(R.id.textView55);
        result3 = (TextView) findViewById(R.id.textView65);
        result4 = (TextView) findViewById(R.id.textView75);
        btnposlji = (Button)findViewById(R.id.button1);
        btnshrani = (Button)findViewById(R.id.button2);
        btnizracunaj = (Button)findViewById(R.id.button3);
        btnodpri = (Button)findViewById(R.id.button0);
        dba.open();
        mContextOdpri = this;
        
        btnposlji.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            insertValues();
            SklopiCursor.close();
            }
        });
        btnshrani.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            shrani();
            }
        });
        btnodpri.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	dodatnoOkno();
            }
        });
        btnizracunaj.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	izracunaj();
            }
        });
    }
	
	@Override
    protected void onPause() {
        super.onPause();
        dba.close(); 
    }
    
    @Override
    protected void onResume() {
        super.onResume();      
        result.setText(poslano);  
        dba.open();
        try {
        izracunaj();
        }
        catch(Exception e){
         	 result.setText("Izraèun ni mogoè!");
          }
    }
     
    public void insertValues(){
     
     SklopiCursor = dba.getmateriali();
     SklopiCursor.moveToPrevious();
     while (SklopiCursor.moveToNext()) {
     	SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
     	
		try {
			//Pass value for userName variable of the web service
			
	    PropertyInfo _idProp =new PropertyInfo();
	    _idProp.setName("_id");
	    _idProp.setValue(SklopiCursor.getString(SklopiCursor.getColumnIndex("_id")));
	    _idProp.setType(String.class);
	    request.addProperty(_idProp);
			
		PropertyInfo obcinaProp =new PropertyInfo();
		obcinaProp.setName("obcina");//Define the variable name in the web service method
		obcinaProp.setValue(SklopiCursor.getString(SklopiCursor.getColumnIndex("obcina")));//Define value for fname variable
		obcinaProp.setType(String.class);//Define the type of the variable
        request.addProperty(obcinaProp);//Pass properties to the variable
		     
      //Pass value for userPassword variable of the web service
        PropertyInfo postnaProp =new PropertyInfo();
        postnaProp.setName("postna");
        postnaProp.setValue(SklopiCursor.getString(SklopiCursor.getColumnIndex("postna")));
        postnaProp.setType(String.class);
        request.addProperty(postnaProp);
        
        PropertyInfo naseljeProp =new PropertyInfo();
        naseljeProp.setName("naselje");
        naseljeProp.setValue(SklopiCursor.getString(SklopiCursor.getColumnIndex("naselje")));
        naseljeProp.setType(String.class);
        request.addProperty(naseljeProp);
        
        PropertyInfo ulicaProp =new PropertyInfo();
        ulicaProp.setName("ulica");
        ulicaProp.setValue(SklopiCursor.getString(SklopiCursor.getColumnIndex("ulica")));
        ulicaProp.setType(String.class);
        request.addProperty(ulicaProp);
        
        PropertyInfo hisnaProp =new PropertyInfo();
        hisnaProp.setName("hisna");
        hisnaProp.setValue(SklopiCursor.getString(SklopiCursor.getColumnIndex("hisna")));
        hisnaProp.setType(String.class);
        request.addProperty(hisnaProp);
        
        PropertyInfo dodatekProp =new PropertyInfo();
        dodatekProp.setName("dodatek");
        dodatekProp.setValue(SklopiCursor.getString(SklopiCursor.getColumnIndex("dodatek")));
        dodatekProp.setType(String.class);
        request.addProperty(dodatekProp);
        
        PropertyInfo stevilka_stanovanjaProp =new PropertyInfo();
        stevilka_stanovanjaProp.setName("stevilka_stanovanja");
        stevilka_stanovanjaProp.setValue(SklopiCursor.getString(SklopiCursor.getColumnIndex("stevilka_stanovanja")));
        stevilka_stanovanjaProp.setType(String.class);
        request.addProperty(stevilka_stanovanjaProp);
        
        PropertyInfo tipProp =new PropertyInfo();
        tipProp.setName("tip");
        tipProp.setValue(SklopiCursor.getString(SklopiCursor.getColumnIndex("tip")));
        tipProp.setType(String.class);
        request.addProperty(tipProp);
        
        PropertyInfo letoProp =new PropertyInfo();
        letoProp.setName("leto");
        letoProp.setValue(SklopiCursor.getString(SklopiCursor.getColumnIndex("leto")));
        letoProp.setType(String.class);
        request.addProperty(letoProp);
        
        PropertyInfo visinaProp =new PropertyInfo();
        visinaProp.setName("visina");
        visinaProp.setValue(SklopiCursor.getString(SklopiCursor.getColumnIndex("visina")));
        visinaProp.setType(String.class);
        request.addProperty(visinaProp);
        
        PropertyInfo uporabna_povrsinaProp =new PropertyInfo();
        uporabna_povrsinaProp.setName("uporabna_povrsina");
        uporabna_povrsinaProp.setValue(SklopiCursor.getString(SklopiCursor.getColumnIndex("uporabna_povrsina")));
        uporabna_povrsinaProp.setType(String.class);
        request.addProperty(uporabna_povrsinaProp);
        
        PropertyInfo neto_tlorisProp =new PropertyInfo();
        neto_tlorisProp.setName("neto_tloris");
        neto_tlorisProp.setValue(SklopiCursor.getString(SklopiCursor.getColumnIndex("neto_tloris")));
        neto_tlorisProp.setType(String.class);
        request.addProperty(neto_tlorisProp);
        
        PropertyInfo povrsina_pod_stavboProp =new PropertyInfo();
        povrsina_pod_stavboProp.setName("povrsina_pod_stavbo");
        povrsina_pod_stavboProp.setValue(SklopiCursor.getString(SklopiCursor.getColumnIndex("povrsina_pod_stavbo")));
        povrsina_pod_stavboProp.setType(String.class);
        request.addProperty(povrsina_pod_stavboProp);
        
        PropertyInfo danProp =new PropertyInfo();
        danProp.setName("dan");
        danProp.setValue(SklopiCursor.getString(SklopiCursor.getColumnIndex("dan")));
        danProp.setType(String.class);
        request.addProperty(danProp);
        
        PropertyInfo nocProp =new PropertyInfo();
        nocProp.setName("noc");
        nocProp.setValue(SklopiCursor.getString(SklopiCursor.getColumnIndex("noc")));
        nocProp.setType(String.class);
        request.addProperty(nocProp);
        
        PropertyInfo ljudiProp =new PropertyInfo();
        ljudiProp.setName("ljudi");
        ljudiProp.setValue(SklopiCursor.getString(SklopiCursor.getColumnIndex("ljudi")));
        ljudiProp.setType(String.class);
        request.addProperty(ljudiProp);
        
        PropertyInfo IDsklopaProp =new PropertyInfo();
        IDsklopaProp.setName("IDsklopa");
        IDsklopaProp.setValue(String.valueOf(SklopiCursor.getInt(SklopiCursor.getColumnIndex("IDsklopa"))));
        IDsklopaProp.setType(String.class);
        request.addProperty(IDsklopaProp);
        
        PropertyInfo titleProp =new PropertyInfo();
        titleProp.setName("title");
        titleProp.setValue(SklopiCursor.getString(SklopiCursor.getColumnIndex("title")));
        titleProp.setType(String.class);
        request.addProperty(titleProp);
        
        PropertyInfo vrstaProp =new PropertyInfo();
        vrstaProp.setName("vrsta");
        vrstaProp.setValue(SklopiCursor.getString(SklopiCursor.getColumnIndex("vrsta")));
        vrstaProp.setType(String.class);
        request.addProperty(vrstaProp);
        
        PropertyInfo povrsinaProp =new PropertyInfo();
        povrsinaProp.setName("povrsina");
        povrsinaProp.setValue(SklopiCursor.getString(SklopiCursor.getColumnIndex("povrsina")));
        povrsinaProp.setType(String.class);
        request.addProperty(povrsinaProp);
        
        PropertyInfo materialProp =new PropertyInfo();
        materialProp.setName("material");
        materialProp.setValue(SklopiCursor.getString(SklopiCursor.getColumnIndex("material")));
        materialProp.setType(String.class);
        request.addProperty(materialProp);
        
        PropertyInfo zaporednaidProp =new PropertyInfo();
        zaporednaidProp.setName("zaporednaid");
        zaporednaidProp.setValue(SklopiCursor.getString(SklopiCursor.getColumnIndex("zaporednaid")));
        zaporednaidProp.setType(String.class);
        request.addProperty(zaporednaidProp);
        
        PropertyInfo debelinaProp =new PropertyInfo();
        debelinaProp.setName("debelina");
        debelinaProp.setValue(SklopiCursor.getString(SklopiCursor.getColumnIndex("debelina")));
        debelinaProp.setType(String.class);
        request.addProperty(debelinaProp);

        
        PropertyInfo nameProp =new PropertyInfo();
        nameProp.setName("name");
        nameProp.setValue(SklopiCursor.getString(SklopiCursor.getColumnIndex("name")));
        nameProp.setType(String.class);
        request.addProperty(nameProp);
        
        PropertyInfo opisProp =new PropertyInfo();
        opisProp.setName("opis");
        opisProp.setValue(SklopiCursor.getString(SklopiCursor.getColumnIndex("opis")));
        opisProp.setType(String.class);
        request.addProperty(opisProp);
        
        PropertyInfo gostotaProp =new PropertyInfo();
        gostotaProp.setName("gostota");
        gostotaProp.setValue(SklopiCursor.getString(SklopiCursor.getColumnIndex("gostota")));
        gostotaProp.setType(String.class);
        request.addProperty(gostotaProp);
        
        PropertyInfo cProp =new PropertyInfo();
        cProp.setName("c");
        cProp.setValue(SklopiCursor.getString(SklopiCursor.getColumnIndex("c")));
        cProp.setType(String.class);
        request.addProperty(cProp);
        
        PropertyInfo lambdaProp =new PropertyInfo();
        lambdaProp.setName("lambda");
        lambdaProp.setValue(SklopiCursor.getString(SklopiCursor.getColumnIndex("lambda")));
        lambdaProp.setType(String.class);
        request.addProperty(lambdaProp);
        
        PropertyInfo uProp =new PropertyInfo();
        uProp.setName("u");
        uProp.setValue(SklopiCursor.getString(SklopiCursor.getColumnIndex("u")));
        uProp.setType(String.class);
        request.addProperty(uProp);
        
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.setOutputSoapObject(request);
        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
      
        androidHttpTransport.call(SOAP_ACTION, envelope);
        SoapPrimitive response = (SoapPrimitive)envelope.getResponse();
             
        result.setText(response.toString());
        poslano = response.toString();
        }
     catch(Exception e){
    	 result.setText("Pošiljanje neuspešno");
     } 
     	}
     	
     	
     }
    private void shrani() {
    	
    	//naslov stavbe iz SQLite in definiranje imena, kot ga želimo shraniti
    	podatkiStavb = dba.getstavbe();
    	podatkiStavbe = dba.getstavba(podatkiStavb.getCount());    	
    	podatkiRezimi = dba.getrezimi();
    	podatkiRezim = dba.getrezim(podatkiRezimi.getCount());
    	imeObjekta = podatkiStavbe.getString(podatkiStavbe.getColumnIndex(Constants.STAVBA_POSTNA)) + "," + podatkiStavbe.getString(podatkiStavbe.getColumnIndex(Constants.STAVBA_NASELJE)) + "," + podatkiStavbe.getString(podatkiStavbe.getColumnIndex(Constants.STAVBA_ULICA)) + "," + podatkiStavbe.getString(podatkiStavbe.getColumnIndex(Constants.STAVBA_HISNA));
    	File newxmlfile = new File(Environment.getExternalStorageDirectory()+"/Baza Konstrukcijskih Sklopov/"+imeObjekta+".xml");

        try{
                newxmlfile.createNewFile();
                
        }catch(IOException e){
                Log.e("IOException", "exception in createNewFile() method");
        }
        //we have to bind the new file with a FileOutputStream
        FileOutputStream fileos = null;         
        try{
                fileos = new FileOutputStream(newxmlfile);
        }catch(FileNotFoundException e){
                Log.e("FileNotFoundException", "can't create FileOutputStream");
        }
        //we create a XmlSerializer in order to write xml data
        XmlSerializer serializer = Xml.newSerializer();
        try {
                //we set the FileOutputStream as output for the serializer, using UTF-8 encoding
                        serializer.setOutput(fileos, "UTF-8");
                        //Write <?xml declaration with encoding (if encoding not null) and standalone flag (if standalone not null) 
                        serializer.startDocument(null, Boolean.valueOf(true)); 
                        //set indentation option
                        serializer.setFeature("http://xmlpull.org/v1/doc/features.html#indent-output", true);            

                        serializer.startTag(null, "root");
                        serializer.startTag(null, "stavba-objekt");
                        serializer.startTag(null, Constants.STAVBA_OBCINA);
                        serializer.text(String.valueOf(podatkiStavbe.getString(podatkiStavbe.getColumnIndex("obcina"))));
                        serializer.endTag(null, Constants.STAVBA_OBCINA);
                        serializer.startTag(null, Constants.STAVBA_POSTNA);
                        serializer.text(String.valueOf(podatkiStavbe.getString(podatkiStavbe.getColumnIndex("postna"))));
                        serializer.endTag(null, Constants.STAVBA_POSTNA);
                        serializer.startTag(null, Constants.STAVBA_NASELJE);
                        serializer.text(String.valueOf(podatkiStavbe.getString(podatkiStavbe.getColumnIndex("naselje"))));
                        serializer.endTag(null, Constants.STAVBA_NASELJE);
                        serializer.startTag(null, Constants.STAVBA_ULICA);
                        serializer.text(String.valueOf(podatkiStavbe.getString(podatkiStavbe.getColumnIndex("ulica"))));
                        serializer.endTag(null, Constants.STAVBA_ULICA);
                        serializer.startTag(null, Constants.STAVBA_HISNA);
                        serializer.text(String.valueOf(podatkiStavbe.getString(podatkiStavbe.getColumnIndex("hisna"))));
                        serializer.endTag(null, Constants.STAVBA_HISNA);
                        serializer.startTag(null, Constants.STAVBA_DODATEK_HISNI);
                        serializer.text(String.valueOf(podatkiStavbe.getString(podatkiStavbe.getColumnIndex("dodatek"))));
                        serializer.endTag(null, Constants.STAVBA_DODATEK_HISNI);
                        serializer.startTag(null, Constants.STAVBA_STEVILKA_STANOVANJA);
                        serializer.text(String.valueOf(podatkiStavbe.getString(podatkiStavbe.getColumnIndex("stevilka_stanovanja"))));
                        serializer.endTag(null, Constants.STAVBA_STEVILKA_STANOVANJA);
                        serializer.startTag(null, Constants.STAVBA_TIP_STAVBE);
                        serializer.text(String.valueOf(podatkiStavbe.getString(podatkiStavbe.getColumnIndex("tip"))));
                        serializer.endTag(null, Constants.STAVBA_TIP_STAVBE);
                        serializer.startTag(null, Constants.STAVBA_LETO_IZGRADNJE);
                        serializer.text(String.valueOf(podatkiStavbe.getString(podatkiStavbe.getColumnIndex("leto"))));
                        serializer.endTag(null, Constants.STAVBA_LETO_IZGRADNJE);
                        serializer.startTag(null, Constants.STAVBA_VISINA);
                        serializer.text(String.valueOf(podatkiStavbe.getString(podatkiStavbe.getColumnIndex("visina"))));
                        serializer.endTag(null, Constants.STAVBA_VISINA);
                        serializer.startTag(null, Constants.STAVBA_UPORABNA_POVRSINA);
                        serializer.text(String.valueOf(podatkiStavbe.getString(podatkiStavbe.getColumnIndex("uporabna_povrsina"))));
                        serializer.endTag(null, Constants.STAVBA_UPORABNA_POVRSINA);
                        serializer.startTag(null, Constants.STAVBA_NETO_TLORIS);
                        serializer.text(String.valueOf(podatkiStavbe.getString(podatkiStavbe.getColumnIndex("neto_tloris"))));
                        serializer.endTag(null, Constants.STAVBA_NETO_TLORIS);
                        serializer.startTag(null, Constants.STAVBA_POVRSINA_POD_STAVBO);
                        serializer.text(String.valueOf(podatkiStavbe.getString(podatkiStavbe.getColumnIndex("povrsina_pod_stavbo"))));
                        serializer.endTag(null, Constants.STAVBA_POVRSINA_POD_STAVBO);
                        serializer.startTag(null, Constants.REZIM_DAN);
                        serializer.text(String.valueOf(podatkiRezim.getString(podatkiRezim.getColumnIndex("dan"))));
                        serializer.endTag(null, Constants.REZIM_DAN);
                        serializer.startTag(null, Constants.REZIM_NOC);
                        serializer.text(String.valueOf(podatkiRezim.getString(podatkiRezim.getColumnIndex("noc"))));
                        serializer.endTag(null, Constants.REZIM_NOC);
                        serializer.startTag(null, Constants.REZIM_ZUNAJ_DAN);
                        serializer.text(String.valueOf(podatkiRezim.getString(podatkiRezim.getColumnIndex("zunajdan"))));
                        serializer.endTag(null, Constants.REZIM_ZUNAJ_DAN);
                        serializer.startTag(null, Constants.REZIM_ZUNAJ_NOC);
                        serializer.text(String.valueOf(podatkiRezim.getString(podatkiRezim.getColumnIndex("zunajnoc"))));
                        serializer.endTag(null, Constants.REZIM_ZUNAJ_NOC);
                        serializer.startTag(null, Constants.REZIM_LJUDI);
                        serializer.text(String.valueOf(podatkiRezim.getString(podatkiRezim.getColumnIndex("ljudi"))));
                        serializer.endTag(null, Constants.REZIM_LJUDI);
                        serializer.endTag(null, "stavba-objekt");
                        mSklopiCursor = dba.getsklopi();
                        mSklopiCursor.moveToPrevious();
                        while (mSklopiCursor.moveToNext()) {
                        	
                     	try{
                        serializer.startTag(null, "sklop"); 
                        Integer steviloSklopa = mSklopiCursor.getInt(mSklopiCursor.getColumnIndex("_id"));
                        mSklopiCursorMateriali = dba.getsklopiVSE(steviloSklopa);
                        mSklopiCursorMateriali.moveToPosition(-1);
                        while (mSklopiCursorMateriali.moveToNext()) {
                        	try{
                        		serializer.startTag(null, "vnosmaterial");
                        		
                        		serializer.startTag(null, Constants.KEY_ID);
                                serializer.text(String.valueOf(mSklopiCursorMateriali.getString(mSklopiCursorMateriali.getColumnIndex("_id"))));
                                serializer.endTag(null, Constants.KEY_ID);
                                serializer.startTag(null, Constants.SKLOP_ID_MATERIALI);
                                serializer.text(String.valueOf(mSklopiCursorMateriali.getString(mSklopiCursorMateriali.getColumnIndex("IDsklopa"))));
                                serializer.endTag(null, Constants.SKLOP_ID_MATERIALI);
                                serializer.startTag(null, Constants.SKLOP_NAME);
                                serializer.text(String.valueOf(mSklopiCursorMateriali.getString(mSklopiCursorMateriali.getColumnIndex("title"))));
                                serializer.endTag(null, Constants.SKLOP_NAME);
                                serializer.startTag(null, Constants.SKLOP_VRSTA);
                                serializer.text(String.valueOf(mSklopiCursorMateriali.getString(mSklopiCursorMateriali.getColumnIndex("vrsta"))));
                                serializer.endTag(null, Constants.SKLOP_VRSTA);
                                serializer.startTag(null, Constants.SKLOP_POVRSINA);
                                serializer.text(String.valueOf(mSklopiCursorMateriali.getString(mSklopiCursorMateriali.getColumnIndex("povrsina"))));
                                serializer.endTag(null, Constants.SKLOP_POVRSINA);
                                serializer.startTag(null, Constants.KEY_MATERIAL);
                                serializer.text(String.valueOf(mSklopiCursorMateriali.getString(mSklopiCursorMateriali.getColumnIndex("material"))));
                                serializer.endTag(null, Constants.KEY_MATERIAL);
                                serializer.startTag(null, Constants.KEY_ID_ZAPOREDNA);
                                serializer.text(String.valueOf(mSklopiCursorMateriali.getString(mSklopiCursorMateriali.getColumnIndex("zaporednaid"))));
                                serializer.endTag(null, Constants.KEY_ID_ZAPOREDNA);
                                serializer.startTag(null, Constants.KEY_DEBELINA);
                                serializer.text(String.valueOf(mSklopiCursorMateriali.getString(mSklopiCursorMateriali.getColumnIndex("debelina"))));
                                serializer.endTag(null, Constants.KEY_DEBELINA);
                                serializer.startTag(null, Constants.KEY_NAME);
                                serializer.text(String.valueOf(mSklopiCursorMateriali.getString(mSklopiCursorMateriali.getColumnIndex("name"))));
                                serializer.endTag(null, Constants.KEY_NAME);
                                serializer.startTag(null, Constants.KEY_OPIS);
                                serializer.text(String.valueOf(mSklopiCursorMateriali.getString(mSklopiCursorMateriali.getColumnIndex("opis"))));
                                serializer.endTag(null, Constants.KEY_OPIS);
                                serializer.startTag(null, Constants.KEY_GOSTOTA);
                                serializer.text(String.valueOf(mSklopiCursorMateriali.getString(mSklopiCursorMateriali.getColumnIndex("gostota"))));
                                serializer.endTag(null, Constants.KEY_GOSTOTA);
                                serializer.startTag(null, Constants.KEY_C);
                                serializer.text(String.valueOf(mSklopiCursorMateriali.getString(mSklopiCursorMateriali.getColumnIndex("c"))));
                                serializer.endTag(null, Constants.KEY_C);
                                serializer.startTag(null, Constants.KEY_LAMBDA);
                                serializer.text(String.valueOf(mSklopiCursorMateriali.getString(mSklopiCursorMateriali.getColumnIndex("lambda"))));
                                serializer.endTag(null, Constants.KEY_LAMBDA);
                                serializer.startTag(null, Constants.KEY_U);
                                serializer.text(String.valueOf(mSklopiCursorMateriali.getString(mSklopiCursorMateriali.getColumnIndex("u"))));
                                serializer.endTag(null, Constants.KEY_U);
                                
                                serializer.endTag(null, "vnosmaterial");
                        	}
                        	catch(Exception e){
                              	 result.setText(String.valueOf(e));
                               }
                        }
                     	serializer.endTag(null, "sklop");
                     	}
                     	catch(Exception e){
                       	 result.setText(String.valueOf(e));
                        }
                     	                 	
                     	}
                        serializer.endTag(null, "root");
                        serializer.endDocument();
                     	
                        //write xml data into the FileOutputStream
                        serializer.flush();
                        //finally we close the file stream
                        fileos.close();
                        result.setText("Podatki iz " + imeObjekta + " so bili shranjeni na SD kartico!");
                } catch (Exception e) {
                        Log.e("Exception","error occurred while creating xml file");
                }

    }
    
    private void dodatnoOkno(){
    	try{
    	dir = new File(Environment.getExternalStorageDirectory()
				+ "/Baza Konstrukcijskih Sklopov");
    	}
    	catch (Exception e)
        {
            Log.e("Exception", e.getMessage());
             
        } 

		File[] files = dir.listFiles(new XmlFileFilter());
		ArrayList<String> fileList = new ArrayList<String>();
	    for (File f : files)
	    {
	      String fileIME = f.getName();
	      fileList.add(fileIME);
	    }
	
	coco = new AlertDialog.Builder(mContextOdpri);
	LayoutInflater inflater = this.getLayoutInflater();
	final View v = inflater.inflate(R.layout.open_file, null);
	coco.setView(v).setTitle("Izberite podatke iz željene lokacije");
	ListView mainListView = (ListView) v.findViewById(R.id.listView1);
	ArrayAdapter<String> openAdapter = new ArrayAdapter<String>(this, R.layout.materiali_vrstica, R.id.tvGroupName, fileList); 		    
	mainListView.setAdapter(openAdapter);
	alert = coco.create();
	mainListView.setOnItemClickListener(new OnItemClickListener() {         
		
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			dba.close();
        	mContextOdpri.deleteDatabase("datastorage");
        	dba.open();
            imeOpenDatoteke = (String) arg0.getItemAtPosition(arg2);
            try {
        		parseXML();
        	} catch (XmlPullParserException e) {
        		// TODO Auto-generated catch block
        		e.printStackTrace();
        	} catch (IOException e) {
        		// TODO Auto-generated catch block
        		e.printStackTrace();
        	}
            alert.dismiss();
		}       
	    });
	//coco.create();
	
	alert.show();
    }
    
    private void parseXML() throws XmlPullParserException, IOException {
		    
		try{
		File file = new File(Environment.getExternalStorageDirectory()
				+ "/Baza Konstrukcijskih Sklopov/" + imeOpenDatoteke);
		XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
		factory.setNamespaceAware(true);
		XmlPullParser xpp = factory.newPullParser();
		// create an input stream to be read by the stream reader.
		FileInputStream fis = new FileInputStream(file);
		// set the input for the parser using an InputStreamReader
		xpp.setInput(new InputStreamReader(fis));

		int eventType = xpp.getEventType();
		
		while (eventType != XmlPullParser.END_DOCUMENT) {
            
			if ((eventType == XmlPullParser.START_TAG) &&(xpp.getName().equals("obcina"))){
				STAVBA_OBCINA = xpp.nextText();
                xpp.nextTag();
                STAVBA_POSTNA = xpp.nextText();
                xpp.nextTag();
                STAVBA_NASELJE = xpp.nextText();
                xpp.nextTag();
                STAVBA_ULICA = xpp.nextText();
                xpp.nextTag();
                STAVBA_HISNA = xpp.nextText();
                xpp.nextTag();
                STAVBA_DODATEK_HISNI = xpp.nextText();
                xpp.nextTag();
                STAVBA_STEVILKA_STANOVANJA = xpp.nextText();
                xpp.nextTag();
                STAVBA_TIP_STAVBE = xpp.nextText();
                xpp.nextTag();
                STAVBA_LETO_IZGRADNJE = xpp.nextText();
                xpp.nextTag();
                STAVBA_VISINA = xpp.nextText();
                xpp.nextTag();
                STAVBA_UPORABNA_POVRSINA = xpp.nextText();
                xpp.nextTag();
                STAVBA_NETO_TLORIS = xpp.nextText();
                xpp.nextTag();
                STAVBA_POVRSINA_POD_STAVBO = xpp.nextText();
                xpp.nextTag();
                REZIM_DAN = xpp.nextText();
                xpp.nextTag();
                REZIM_NOC = xpp.nextText();
                xpp.nextTag();
                REZIM_ZUNAJ_DAN = xpp.nextText();
                xpp.nextTag();
                REZIM_ZUNAJ_NOC = xpp.nextText();
                xpp.nextTag();
                REZIM_LJUDI = xpp.nextText();            
			}
			
            if ((eventType == XmlPullParser.START_TAG) &&(xpp.getName().equals("_id"))){
                xpp.nextText();
                xpp.nextTag();
                SKLOP_ID_MATERIALI = xpp.nextText();
                xpp.nextTag();
                SKLOP_NAME = xpp.nextText();
                xpp.nextTag();
                SKLOP_VRSTA = xpp.nextText();
                xpp.nextTag();
                SKLOP_POVRSINA = xpp.nextText();
                xpp.nextTag();
                KEY_MATERIAL = xpp.nextText();
                xpp.nextTag();
                KEY_ID_ZAPOREDNA = xpp.nextText();
                xpp.nextTag();
                KEY_DEBELINA = xpp.nextText();
                xpp.nextTag();
                KEY_NAME = xpp.nextText();
                xpp.nextTag();
                KEY_OPIS = xpp.nextText();
                xpp.nextTag();
                KEY_GOSTOTA = xpp.nextText();
                xpp.nextTag();
                KEY_C = xpp.nextText();
                xpp.nextTag();
                KEY_LAMBDA = xpp.nextText();
                xpp.nextTag();
                KEY_U = xpp.nextText();
                
                dobiVENsklope();
                
                dba.insertstavba(STAVBA_OBCINA,STAVBA_POSTNA,STAVBA_NASELJE,STAVBA_ULICA,STAVBA_HISNA,STAVBA_DODATEK_HISNI,STAVBA_STEVILKA_STANOVANJA,STAVBA_TIP_STAVBE,
                		STAVBA_LETO_IZGRADNJE,STAVBA_VISINA,STAVBA_UPORABNA_POVRSINA,STAVBA_NETO_TLORIS, STAVBA_POVRSINA_POD_STAVBO);
            			
            	dba.insertrezim(REZIM_DAN,REZIM_NOC,REZIM_ZUNAJ_DAN,REZIM_ZUNAJ_NOC,REZIM_LJUDI);
                
                dba.insertmaterial(STAVBA_OBCINA,STAVBA_POSTNA,STAVBA_NASELJE,STAVBA_ULICA,STAVBA_HISNA,STAVBA_DODATEK_HISNI,STAVBA_STEVILKA_STANOVANJA,STAVBA_TIP_STAVBE,
            			STAVBA_LETO_IZGRADNJE,STAVBA_VISINA,STAVBA_UPORABNA_POVRSINA,STAVBA_NETO_TLORIS,STAVBA_POVRSINA_POD_STAVBO,REZIM_DAN,REZIM_NOC,REZIM_ZUNAJ_DAN,REZIM_ZUNAJ_NOC,REZIM_LJUDI,Integer.parseInt(SKLOP_ID_MATERIALI),SKLOP_NAME,SKLOP_VRSTA,
            			SKLOP_POVRSINA,KEY_MATERIAL,KEY_ID_ZAPOREDNA,Integer.parseInt(KEY_DEBELINA),KEY_NAME,KEY_OPIS,KEY_GOSTOTA,
            			KEY_C,KEY_LAMBDA,KEY_U);

            }
            eventType = xpp.next();
        }
		result.setText("Podatki so bili odprti!");
		 try {
		        izracunaj();
		        }
		        catch(Exception e){
		         	 result.setText("Izraèun ni mogoè!");
		          }
    }
    //Catch errors
    catch (XmlPullParserException e)
    {       
        Log.e("Exception", e.getMessage());
    }
    catch (IOException e)
    {
        Log.e("Exception", e.getMessage());
         
    }            
}

    private void dobiVENsklope(){
    	Cursor mCursor = dba.getsklopiVSE(Integer.valueOf(SKLOP_ID_MATERIALI));
    	Integer stev = mCursor.getCount();
    	if (stev.equals(0))
		{
    		Log.d(String.valueOf(STAVBA_OBCINA), "obcina");
    		Log.d(String.valueOf(Integer.valueOf(SKLOP_ID_MATERIALI)), "id");
    		dba.insertsklop(SKLOP_NAME, SKLOP_VRSTA, SKLOP_POVRSINA, STAVBA_OBCINA, STAVBA_POSTNA, STAVBA_NASELJE, STAVBA_ULICA, STAVBA_HISNA,
		STAVBA_DODATEK_HISNI, STAVBA_STEVILKA_STANOVANJA, STAVBA_TIP_STAVBE, STAVBA_LETO_IZGRADNJE, STAVBA_VISINA, STAVBA_UPORABNA_POVRSINA,
		STAVBA_NETO_TLORIS, STAVBA_POVRSINA_POD_STAVBO, REZIM_DAN, REZIM_NOC, REZIM_ZUNAJ_DAN,REZIM_ZUNAJ_NOC,REZIM_LJUDI);
		}
    	
    	
    }
    
    private class XmlFileFilter implements FileFilter
    {
      private final String[] okFileExtensions = 
        new String[] {"xml"};

      public boolean accept(File file)
      {
        for (String extension : okFileExtensions)
        {
          if (file.getName().toLowerCase().endsWith(extension))
          {
            return true;
          }
        }
        return false;
      }
    }
    private void izracunaj(){
    	
    	double U = 0;
    	double USSklop = 0;
    	double USZaSklop = 0;
    	double SSkupaj = 0;

    	mSklopiCursor = dba.getsklopi();
        mSklopiCursor.moveToPrevious();
        while (mSklopiCursor.moveToNext()) {
        	
        	
        	
        	Integer steviloSklopa = mSklopiCursor.getInt(mSklopiCursor.getColumnIndex("_id"));
        	mSklopiCursorMateriali = dba.getsklopiVSE(steviloSklopa);
        	Integer steviloCursorjev = mSklopiCursorMateriali.getCount();

        	double zn = 0;	
        	
    			mSklopiCursorMateriali.moveToPrevious();
        		while (mSklopiCursorMateriali.moveToNext()) {

        			Log.d(mSklopiCursorMateriali.getString(mSklopiCursorMateriali.getColumnIndex("name")), "materjal");

        			double debelina = Double.parseDouble(mSklopiCursorMateriali.getString(mSklopiCursorMateriali.getColumnIndex("debelina")));
        			double lambda = Double.parseDouble(mSklopiCursorMateriali.getString(mSklopiCursorMateriali.getColumnIndex("lambda")));

        			zn = zn + (debelina/1000/lambda);
        			
        			
        	}
        	Log.d(String.valueOf(zn), "R");
        	U = 1/(0.04+zn+0.13);
        	Log.d(String.valueOf(U), "U");
        	
        	double S = Double.parseDouble(mSklopiCursor.getString(mSklopiCursor.getColumnIndex("povrsina")));
    		
    		if (mSklopiCursor.getString(mSklopiCursor.getColumnIndex("vrsta")).equals("Tla")){
    		USSklop = 0.5 * U * S;
    			}
    		if (mSklopiCursor.getString(mSklopiCursor.getColumnIndex("vrsta")).equals("Streha")){
    		USSklop = 0.8 * U * S;
        		}
    		if (mSklopiCursor.getString(mSklopiCursor.getColumnIndex("vrsta")).equals("Stena")){
    		USSklop = U * S;
        		}
    		USZaSklop = USZaSklop + USSklop;
    		SSkupaj = SSkupaj + S;
    		SSkupajZanaprej = SSkupaj;
    		
    		Log.d(String.valueOf(USZaSklop), "USZaSklop");
    		Log.d(String.valueOf(SSkupaj), "SSkupaj");
    		Log.d(String.valueOf(SSkupajZanaprej), "SSkupajZanaprej");
    		
    }
        Upovprecni = USZaSklop/SSkupajZanaprej;
        RezimCursori = dba.getrezimi();
        RezimCursor = dba.getrezim(RezimCursori.getCount());
        T1 = Double.parseDouble(RezimCursor.getString(RezimCursor.getColumnIndex("dan")));
        T2 = Double.parseDouble(RezimCursor.getString(RezimCursor.getColumnIndex("noc")));
        T1zunaj = Double.parseDouble(RezimCursor.getString(RezimCursor.getColumnIndex("zunajdan")));
        T2zunaj = Double.parseDouble(RezimCursor.getString(RezimCursor.getColumnIndex("zunajnoc")));
        TspremembaDan = (T1-T1zunaj);
        TspremembaNoc = (T2-T2zunaj);
        Tsprememba = (TspremembaDan + TspremembaNoc)/2;
        Log.d(String.valueOf(SSkupajZanaprej), "SSkupajZanaprej");
        Log.d(String.valueOf(Upovprecni), "Upovprecni");
        Log.d(String.valueOf(Tsprememba), "Tsprememba");
		P = Upovprecni*SSkupajZanaprej*Tsprememba;
		LdLs = Upovprecni*SSkupajZanaprej;
		Ppodnevi = Upovprecni*SSkupajZanaprej*TspremembaDan;
		Pponoci = Upovprecni*SSkupajZanaprej*TspremembaNoc;
		rezultat = df.format(P);
		rezultat1 = df.format(LdLs);
		rezultat2 = df.format(Ppodnevi);
		rezultat3 = df.format(Pponoci);
		poslano1 = " " + rezultat1 + " W/K";
		poslano2 = " " + rezultat2 + " W";
		poslano3 = " " + rezultat3 + " W";
		poslano4 = " " + rezultat + " W";
		result1.setText(poslano1);
		result2.setText(poslano2);
		result3.setText(poslano3);
		result4.setText(poslano4);
    }
    
}