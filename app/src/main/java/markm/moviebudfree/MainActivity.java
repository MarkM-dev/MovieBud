package markm.moviebudfree;

import java.io.File;
import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.ContextThemeWrapper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.ads.*;


public class MainActivity extends Activity {
	
	DBHandler dataBase;
	ListView lv;
	MyBaseAdapter adapter;
	String sort_order;
	Spinner sort_spinner;
	private InterstitialAd interstitial;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		
		 // Look up the AdView as a resource and load a request.
	    AdView adView = (AdView)this.findViewById(R.id.adView);
	    AdRequest adRequest = new AdRequest.Builder().build();
	    adView.loadAd(adRequest);
	    
	    interstitial = new InterstitialAd(this);
	    interstitial.setAdUnitId("ca-app-pub-7088636357536669/2973990630");
	    // Create ad request.
	    AdRequest adRequestinter = new AdRequest.Builder().build();
	    // Begin loading your interstitial.
	    interstitial.loadAd(adRequestinter);

		ImageButton adCloseBtn = (ImageButton)findViewById(R.id.Main_adCloseBtn);
		adCloseBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				LinearLayout ad_layout = (LinearLayout)findViewById(R.id.main_ad_layout);
				ad_layout.setVisibility(View.GONE);
			}
		});
	    
		ArrayList<Spinner_item> spinnerArr = new ArrayList<Spinner_item>();
		spinnerArr.add(new Spinner_item(" " + getResources().getString(R.string.main_sort_a_z)));
		spinnerArr.add(new Spinner_item(" " + getResources().getString(R.string.main_sort_genre)));
		spinnerArr.add(new Spinner_item(" " + getResources().getString(R.string.main_sort_year)));
		spinnerArr.add(new Spinner_item(" " + getResources().getString(R.string.main_sort_my_rating)));
		spinnerArr.add(new Spinner_item(" " + getResources().getString(R.string.main_sort_watched)));
		
		sort_spinner = (Spinner)MainActivity.this.findViewById(R.id.main_sort_spinner);
		ArrayAdapter<Spinner_item> sort_adapter = new ArrayAdapter<Spinner_item>(MainActivity.this, R.layout.view_holder_spinner, spinnerArr);
		
		sort_spinner.setAdapter(sort_adapter);
		
		dataBase = new DBHandler(MainActivity.this);
		lv = (ListView)findViewById(R.id.main_my_list_view);
		registerForContextMenu(lv);
		adapter = new MyBaseAdapter(this, DBHelper.DB_SORT_TITLE);
		// bind the adapter to the adapterview.
		lv.setAdapter(adapter);
		checkIfNoItems();
		
		lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Intent edit_intent = new Intent(MainActivity.this, EditPage.class);
				edit_intent.putExtra("position", position);
				edit_intent.putExtra("code", 11);
				edit_intent.putExtra("sort_order", sort_order);
				startActivityForResult(edit_intent, 11);
				overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
			}
		});
		
		final ImageButton addButton = (ImageButton)findViewById(R.id.main_add_button);
		registerForContextMenu(addButton);
		addButton.setOnClickListener(new View.OnClickListener() {	
			@Override
			public void onClick(View v) {
				openContextMenu(v);
			}
		});
		
		final ImageButton settings_button = (ImageButton)findViewById(R.id.main_settings_button);
		registerForContextMenu(settings_button);
		settings_button.setOnClickListener(new View.OnClickListener() {	
			@Override
			public void onClick(View v) {
				openOptionsMenu();
			}
		});
		
		final TextView sort_textView = (TextView)findViewById(R.id.main_sort_textView);
		sort_textView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				sort_spinner.performClick();
			}
		});
		
		sort_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				switch (position) {
				case 0:
					sortBy(DBHelper.DB_SORT_TITLE);
					sort_textView.setText(R.string.main_sort_a_z);
					break;
				case 1:
					sortBy(DBHelper.DB_SORT_GENRE);
					sort_textView.setText(R.string.main_sort_genre);
					break;
				case 2:
					sortBy(DBHelper.DB_SORT_YEAR);
					sort_textView.setText(R.string.main_sort_year);
					break;
				case 3:
					sortBy(DBHelper.DB_SORT_MY_RATING);
					sort_textView.setText(R.string.main_sort_my_rating);
					break;
				case 4:
					sortBy(DBHelper.DB_SORT_WATCHED);
					sort_textView.setText(R.string.main_sort_watched);
					break;
				
				default:
					break;
				}
			}
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});
		
		
		
	}
	// refresh list according to user's selection.
	private void sortBy (String sortBy) {
		sort_order = sortBy;
		adapter = new MyBaseAdapter(MainActivity.this, sort_order);
		lv.setAdapter(adapter);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		adapter.notifyDataSetChanged();
		checkIfNoItems();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		setTheme(R.style.Theme_Base);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		ContextThemeWrapper ctw = new ContextThemeWrapper(MainActivity.this, R.style.Theme_Base);
		switch (item.getItemId()) {
		case R.id.deleteAllItems:
			MyDialog mDialog = new MyDialog(ctw, getString(R.string.alertDialog_delete_all_title), getString(R.string.alertDialog_delete_all_message));
			mDialog.show();
			break;
		case R.id.more_apps:
			final String developerId = "MarkM";
			   try {
			       startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://developer?id=" + developerId)));
			   } catch (android.content.ActivityNotFoundException anfe) {
			       startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/developer?id=" + developerId)));
			   }
			break;
		case R.id.about:
			final AlertDialog aboutDialog = new AlertDialog(ctw){
				@Override
				public void setTitle(CharSequence title) {
					super.setTitle(title);
				}
				@Override
				public void setMessage(CharSequence message) {
					super.setMessage(message);
				}
				@Override
				public void setButton(int whichButton, CharSequence text, OnClickListener listener) {
					super.setButton(whichButton, text, listener);
				}
			};
			aboutDialog.setTitle(getString(R.string.about_title));
			aboutDialog.setMessage(getString(R.string.about_msg));
			aboutDialog.setButton(DialogInterface.BUTTON_NEUTRAL, "OK", new DialogInterface.OnClickListener() {
				   public void onClick(DialogInterface dialog, int which) {
					   aboutDialog.dismiss();
				   }
					});
			aboutDialog.show();
			break;
		case R.id.exit:
			displayInterstitial();
			finish();
			break;

		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		setTheme(R.style.Theme_Base);
		// checks which menu to inflate.
		if (v.getId() == R.id.main_add_button) {
			getMenuInflater().inflate(R.menu.addmenu, menu);
		} else {
			if (v.getId() == R.id.main_settings_button) {
				getMenuInflater().inflate(R.menu.main, menu);
			} else {
				getMenuInflater().inflate(R.menu.itemsmenu, menu);
			}
		}
	}
	
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo)item.getMenuInfo(); 
		switch (item.getItemId()) {
		case R.id.add_manually:
			Intent add_intent = new Intent(MainActivity.this, EditPage.class);
			add_intent.putExtra("code", 10);
			startActivityForResult(add_intent, 10);
			overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
			break;
		case R.id.add_from_rt:
			Intent add_rt_intent = new Intent(MainActivity.this, SearchPage.class);
			startActivityForResult(add_rt_intent, 12);
			overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
			break;
		case R.id.editMenuItem:
			Intent edit_intent = new Intent(MainActivity.this, EditPage.class);
			edit_intent.putExtra("position", (int)info.position);
			edit_intent.putExtra("code", 11);
			edit_intent.putExtra("sort_order", sort_order);
			startActivityForResult(edit_intent, 11);
			overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
			break;
		case R.id.shareMenuItem:
			Intent shareIntent = new Intent(Intent.ACTION_SEND);
			shareIntent.setType("text/plain");
			shareIntent.putExtra(Intent.EXTRA_SUBJECT, adapter.getItemTitle((int)info.position));
				// if imdb id is "n/a".
			if (adapter.getItem_imdb_id((int)info.position).equals(getResources().getString(R.string.editPage_na))) {
					// if synopsis is n/a.
				if (!adapter.getItemSynopsis((int)info.position).equals(getResources().getString(R.string.searchPage_synopsis_na))) {
					shareIntent.putExtra(Intent.EXTRA_TEXT, adapter.getItemSynopsis((int)info.position));
				}
			} else {
				shareIntent.putExtra(Intent.EXTRA_TEXT, "http://www.imdb.com/title/tt" + adapter.getItem_imdb_id((int)info.position) + "/");
			}
			startActivity(shareIntent);
			break;
		case R.id.deleteMenuItem:
			ContextThemeWrapper ctw1 = new ContextThemeWrapper(MainActivity.this, R.style.Theme_Base);
			MyDialog mDialog1 = new MyDialog(ctw1, getString(R.string.alertDialog_delete_item_title), getString(R.string.alertDialog_delete_item_message) + adapter.getItemTitle((int)info.position) + "?", info.id);
			mDialog1.show();
			break;
		default:
			break;
		}
		return super.onContextItemSelected(item);
	}
	
	private void checkIfNoItems() {
		TextView no_items = (TextView)findViewById(R.id.main_no_items_textView);
		TextView sort_textView = (TextView)findViewById(R.id.main_sort_textView);
		int size = adapter.getCount();
		if (size < 1) { // if no items exist.
			no_items.setVisibility(1);
			sort_textView.setVisibility(View.GONE);
		} else {
			no_items.setVisibility(View.GONE);
			sort_textView.setVisibility(1);
		}
	}
	
	private class MyDialog extends AlertDialog {
		// single item delete dialog.
		protected MyDialog(Context context, String title, String message, final long id) {
			super(context);
			setIcon(android.R.drawable.ic_menu_delete);
			setTitle(title);
			setMessage(message);
			setButton(context.getString(R.string.alertDialog_delete_button), new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					File f = new File(adapter.getItemUrl_local_byId(id));
					if (f.exists()) {
						f.delete();
					}
					adapter.deleteMovie(String.valueOf(id));
					Toast.makeText(MainActivity.this, R.string.toast_item_deleted, Toast.LENGTH_SHORT).show();
					adapter.notifyDataSetChanged();
					checkIfNoItems();
				}
			});
			setButton2(context.getString(R.string.alertDialog_cancel_button), new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dismiss();
				}
			});
			
		}
		// delete all items dialog.
		protected MyDialog(Context context, String title, String message) {
			super(context);
			setIcon(android.R.drawable.ic_menu_delete);
			setTitle(title);
			setMessage(message);
			setButton(context.getString(R.string.alertDialog_delete_button), new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// deletes all items from database.
					adapter.deleteAll();
					
					//deletes all images from app's photo dir.
					File dir = new File(Environment.getExternalStorageDirectory().toString() + "/moviebudfree_images");
					if (dir.isDirectory()) {
				        String[] children = dir.list();
				        for (int i = 0; i < children.length; i++) {
				            new File(dir, children[i]).delete();
				        }
				    }
					adapter.notifyDataSetChanged();
					checkIfNoItems();
					Toast.makeText(MainActivity.this, R.string.toast_all_items_deleted, Toast.LENGTH_SHORT).show();
				}
			});
			setButton2(context.getString(R.string.alertDialog_cancel_button), new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dismiss();
				}
			});
		}
	};
	
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		displayInterstitial();
	}
	public void displayInterstitial() {
	    if (interstitial.isLoaded()) {
	      interstitial.show();
	    }
	  }

}
