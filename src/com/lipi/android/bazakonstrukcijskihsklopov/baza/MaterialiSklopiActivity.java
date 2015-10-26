package com.lipi.android.bazakonstrukcijskihsklopov.baza;

import com.lipi.android.bazakonstrukcijskihsklopov.R;

import android.app.ExpandableListActivity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorTreeAdapter;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.ExpandableListContextMenuInfo;
import android.widget.TextView;

public class MaterialiSklopiActivity extends ExpandableListActivity {

	private ExpandableListAdapter mAdapter;
	private MyDB dba;
	private static final int ACTIVITY_CREATE = 0;
	private static final int ACTIVITY_EDIT = 1;
	private Cursor mGroupsCursor;
	int width;
	private Button mButton;
	private Button mButtonDodajMaterial;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sklopi_list);
		mButton = (Button) findViewById(R.id.button1);
		dba = new MyDB(this);
		dba.open();
		registerForContextMenu(getExpandableListView());
		registerButtonNoviSklop();
		filldata();
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
        filldata();
    }
	
	private void registerButtonNoviSklop()
	{
		mButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				noviSklop();
			}
		});	
	}
	private void filldata() {
		
		mGroupsCursor = dba.getsklopi();
		startManagingCursor(mGroupsCursor);

		mAdapter = new ExpAdapter(
				mGroupsCursor,this);
		

		setListAdapter(mAdapter);

	}

	private void noviSklop() {
		Intent i = new Intent(this, MaterialiVnosActivity.class);
		startActivityForResult(i, ACTIVITY_CREATE);
	}

	final class ExpAdapter extends CursorTreeAdapter {
        LayoutInflater mInflator;

        public ExpAdapter(Cursor cursor, Context context) {
            super(cursor, context);
            mInflator = LayoutInflater.from(context);
            
        }
        
        
        
        @Override
        protected void bindChildView(View view, Context context, Cursor cursor,
                boolean isLastChild) {
        	TextView tvChild0 = (TextView) view.findViewById(R.id.tvSklopiNamePP);
            tvChild0.setText(cursor.getString(cursor
                    .getColumnIndex(Constants.KEY_ID_ZAPOREDNA))+".");
            TextView tvChild = (TextView) view.findViewById(R.id.tvSklopiNameP);
            tvChild.setText(cursor.getString(cursor
                    .getColumnIndex(Constants.KEY_NAME)));
            TextView tvChild2 = (TextView) view.findViewById(R.id.tvSklopiNameP2);
            tvChild2.setText(cursor.getString(cursor
                    .getColumnIndex(Constants.KEY_LAMBDA)));
            
            int debelinaCm = (Integer.parseInt(cursor.getString(cursor
                    .getColumnIndex(Constants.KEY_DEBELINA))))/10;
            
            TextView tvChild3 = (TextView) view.findViewById(R.id.tvSklopiNameP5);
            tvChild3.setText(String.valueOf(debelinaCm));
            
        }

        @Override
        protected void bindGroupView(View view, Context context, Cursor cursor,
                boolean isExpanded) {
        	
            TextView tvGrp = (TextView) view.findViewById(R.id.tvSklopName);
            tvGrp.setText(cursor.getString(cursor
                    .getColumnIndex(Constants.SKLOP_NAME)));
            TextView tvGrp2 = (TextView) view.findViewById(R.id.tvSklopName2);
            tvGrp2.setText(cursor.getString(cursor
                    .getColumnIndex(Constants.SKLOP_POVRSINA)));
            final Long groupID = cursor.getLong(cursor
                    .getColumnIndex(Constants.SKLOP_ID));
            final String groupVrsta = cursor.getString(cursor
                    .getColumnIndex(Constants.SKLOP_VRSTA));
            final String groupPovrsina = cursor.getString(cursor
                    .getColumnIndex(Constants.SKLOP_POVRSINA));
            final String groupName = cursor.getString(cursor
                    .getColumnIndex(Constants.SKLOP_NAME));
            int c = dba.sestevekDebelineSklopa2(groupName);
            TextView tvGrp3 = (TextView) view.findViewById(R.id.tvSklopName5);
            tvGrp3.setText(String.valueOf(c/10)); 
            mButtonDodajMaterial = (Button) view.findViewById(R.id.dodajMaterial);
            mButtonDodajMaterial.setOnClickListener(new View.OnClickListener() {
    			public void onClick(View view) {
    				
    				dodajMaterial(groupID,groupName,groupPovrsina,groupVrsta);
    			}
    		});
        }

        @Override
        protected Cursor getChildrenCursor(Cursor groupCursor) {
            
            return dba.getsklopiVSE(groupCursor.getInt(groupCursor
                    .getColumnIndex(Constants.SKLOP_ID)));
        }

        @Override
        protected View newChildView(Context context, Cursor cursor,
                boolean isLastChild, ViewGroup parent) {
            View mView = mInflator.inflate(R.layout.sklopi_podvrstica, null);
            TextView tvChild0 = (TextView) mView
                    .findViewById(R.id.tvSklopiNamePP);
            tvChild0.setText(cursor.getString(cursor
                    .getColumnIndex(Constants.KEY_ID_ZAPOREDNA))+".");
            TextView tvChild = (TextView) mView
                    .findViewById(R.id.tvSklopiNameP);
            tvChild.setText(cursor.getString(cursor
                    .getColumnIndex(Constants.KEY_NAME)));
            TextView tvChild2 = (TextView) mView
                    .findViewById(R.id.tvSklopiNameP2);
            tvChild2.setText(cursor.getString(cursor
                    .getColumnIndex(Constants.KEY_LAMBDA)));
            TextView tvChild3 = (TextView) mView.findViewById(R.id.tvSklopiNameP5);
            tvChild3.setText(cursor.getString(cursor
                    .getColumnIndex(Constants.KEY_DEBELINA)));
            return mView;
        }

        @Override
        protected View newGroupView(Context context, Cursor cursor,
                boolean isExpanded, ViewGroup parent) {
        	
        	
            View mView = mInflator.inflate(R.layout.sklopi_vrstica, parent,false);
            TextView tvGrp = (TextView) mView.findViewById(R.id.tvSklopName);
            tvGrp.setText(cursor.getString(cursor
                    .getColumnIndex(Constants.SKLOP_NAME)));
            TextView tvGrp2 = (TextView) mView.findViewById(R.id.tvSklopName2);
            tvGrp2.setText(cursor.getString(cursor
                    .getColumnIndex(Constants.SKLOP_POVRSINA)));
            final Long groupID = cursor.getLong(cursor
                    .getColumnIndex(Constants.SKLOP_ID));
            final String groupVrsta = cursor.getString(cursor
                    .getColumnIndex(Constants.SKLOP_VRSTA));
            final String groupPovrsina = cursor.getString(cursor
                    .getColumnIndex(Constants.SKLOP_POVRSINA));
            final String groupName = cursor.getString(cursor
                    .getColumnIndex(Constants.SKLOP_NAME));
            int c = dba.sestevekDebelineSklopa2(groupName);
            TextView tvGrp3 = (TextView) mView.findViewById(R.id.tvSklopName5);
            tvGrp3.setText(String.valueOf(c/10)); 
            mButtonDodajMaterial = (Button) mView.findViewById(R.id.dodajMaterial);
            mButtonDodajMaterial.setOnClickListener(new View.OnClickListener() {
    			public void onClick(View view) {
    				
    				dodajMaterial(groupID,groupName,groupPovrsina,groupVrsta);
    			}
    		});
			return mView;
            
        }
        

    }

	private void dodajMaterial(long groupID,String groupName,String groupPovrsina,String groupVrsta) {
		
		Intent i = new Intent(this, ExpList2.class);
		i.putExtra(Constants.SKLOP_ID, groupID);
		i.putExtra(Constants.SKLOP_NAME, groupName);
		i.putExtra(Constants.SKLOP_POVRSINA, groupPovrsina);
		i.putExtra(Constants.SKLOP_VRSTA, groupVrsta);
		startActivityForResult(i, ACTIVITY_CREATE);
		
	}
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		
		ExpandableListView.ExpandableListContextMenuInfo info =
			    (ExpandableListView.ExpandableListContextMenuInfo) menuInfo;
		MenuInflater mi = getMenuInflater();
		

		int type =
			    ExpandableListView.getPackedPositionType(info.packedPosition);
			  
			  if (type == ExpandableListView.PACKED_POSITION_TYPE_GROUP) 
			  {
					mi.inflate(R.menu.list_menu_item_longpress, menu);
			  }
			  else if (type == ExpandableListView.PACKED_POSITION_TYPE_CHILD)
			  {
				  mi.inflate(R.menu.list_menu_item_longpress_delete_only, menu);
			  }

	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		ExpandableListContextMenuInfo info = (ExpandableListContextMenuInfo) item.getMenuInfo();
		 int type = ExpandableListView.getPackedPositionType(info.packedPosition);
		  if (type == ExpandableListView.PACKED_POSITION_TYPE_CHILD) 
		  {
			  int itemId = item.getItemId();
			if (itemId == R.id.menu_delete) {
				dba.deletematerial(info.id);
				filldata();
				return true;
			}
		  }
		  else if (type == ExpandableListView.PACKED_POSITION_TYPE_GROUP)
		  {
			  int itemId = item.getItemId();
			if (itemId == R.id.menu_delete) {
				dba.deletesklop(info.id);
				filldata();
				return true;
			} else if (itemId == R.id.menu_uredi) {
				Intent i = new Intent(this, MaterialiVnosActivity.class);
				i.putExtra(Constants.SKLOP_ID, info.id);
				startActivityForResult(i, ACTIVITY_EDIT);
				return true;
			}	
		  }
		return super.onContextItemSelected(item);
	}
}
