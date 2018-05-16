package markm.moviebudfree;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Random;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.view.ContextThemeWrapper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

public class EditPage extends Activity{
	
	private MyBaseAdapter adapter;
	private EditText title_editText, genre_editText, year_editText, mpaa_rating_editText, url_detailed_editText;
	private EditText runtime_editText, synopsis_editText, directors_editText, cast_editText, url_local_EditText;
	private TextView rt_rating_textView, my_rating_textView, my_rating_layout_textview;
	private int position, code;
	private ToggleButton watched_toggle_button;
	private ImageView url_image;
	private LinearLayout rt_rating_layout, my_rating_layout;
	private SeekBar my_rating_seekbar;
	private Intent intent;
	private Bitmap bitmap_image;
	private ProgressBar progressBar;
	private static final int CAMERA_REQUEST = 1888;
	private ImageView imageDialog_imgview;
	private Bitmap photo_from_cam;
	private String imdb_id;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.edit_page);
		// prevent keyboard from popping up on page init.
		this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		// go full-screen.
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
	    
		intent = getIntent();
		
		adapter = new MyBaseAdapter(EditPage.this, intent.getStringExtra("sort_order"));
		
		title_editText = (EditText)findViewById(R.id.editPage_title_editText);
		watched_toggle_button = (ToggleButton)findViewById(R.id.editPage_watched_toggleButton);
		url_image = (ImageView)findViewById(R.id.editPage_urlImage_imageView);
		genre_editText = (EditText)findViewById(R.id.editPage_genre_editText);
		year_editText = (EditText)findViewById(R.id.editPage_year_editText);
		mpaa_rating_editText = (EditText)findViewById(R.id.editPage_mpaa_rating_editText);
		runtime_editText = (EditText)findViewById(R.id.editPage_runtime_editText);
		rt_rating_layout = (LinearLayout)findViewById(R.id.editPage_rt_rating_layout);
		rt_rating_textView = (TextView)findViewById(R.id.editPage_rt_rating_textView);
		my_rating_textView = (TextView)findViewById(R.id.editPage_my_rating_textView);
		Button imdb_button = (Button)findViewById(R.id.editPage_imdb_button);
		Button share_button = (Button)findViewById(R.id.editPage_share_button);
		Button rate_button = (Button)findViewById(R.id.editPage_rate_button);
		synopsis_editText = (EditText)findViewById(R.id.editPage_synopsis_editText);
		directors_editText = (EditText)findViewById(R.id.editPage_directors_editText);
		cast_editText = (EditText)findViewById(R.id.editPage_cast_editText);
		url_local_EditText = (EditText)findViewById(R.id.editPage_url_local_EditText);
		url_detailed_editText = (EditText)findViewById(R.id.editPage_url_detailed_editText);
		my_rating_layout = (LinearLayout)findViewById(R.id.editPage_my_rating_layout);
		Button cancel_button = (Button)findViewById(R.id.editPage_cancel_button);
		Button save_button = (Button)findViewById(R.id.editPage_save_button);
		ImageButton delete_button = (ImageButton)findViewById(R.id.editPage_delete_imageButton);
		my_rating_seekbar = (SeekBar)findViewById(R.id.editPage_my_rating_seekBar);
		my_rating_layout_textview = (TextView)findViewById(R.id.editPage_my_rating_layout_textview);
		progressBar = (ProgressBar)findViewById(R.id.editPage_progressBar);
		bitmap_image = null;
		position = intent.getIntExtra("position", -1);
		code = intent.getIntExtra("code", -1);
		imdb_id = getString(R.string.editPage_na);
		
		switch (code) {
		case 10: // add new.
			rt_rating_layout.setVisibility(LinearLayout.GONE);
			delete_button.setVisibility(View.GONE);
			imdb_button.setVisibility(View.GONE);
			break;
		case 11: // edit.
			title_editText.setText(adapter.getItemTitle(position));
			if (adapter.getItemWatchedStatus(position) == 1) {
				watched_toggle_button.setChecked(true);
			} else {
				watched_toggle_button.setChecked(false);
			}
			genre_editText.setText(adapter.getItemGenre(position));
			year_editText.setText(adapter.getItemYear(position));
			mpaa_rating_editText.setText(adapter.getItemMpaa_rating(position));
			runtime_editText.setText(adapter.getItemRuntime(position));
			rt_rating_textView.setText(adapter.getItemRt_rating(position));
			if (adapter.getItemMy_rating(position).equals(R.string.editPage_na)) {
				my_rating_textView.setText("");
			} else {
				my_rating_textView.setText(adapter.getItemMy_rating(position));
			}
			synopsis_editText.setText(adapter.getItemSynopsis(position));
			directors_editText.setText(adapter.getItemDirectors(position));
			cast_editText.setText(adapter.getItemCast(position));
			url_local_EditText.setText(adapter.getItemUrl_local(position));
			url_detailed_editText.setText(adapter.getItemUrl_Detailed(position));
			my_rating_layout_textview.setText(adapter.getItemMy_rating(position));
			if (adapter.getItem_imdb_id(position).equals(getResources().getString(R.string.editPage_na))) {
				imdb_button.setVisibility(View.GONE);
			} else {
				imdb_id = adapter.getItem_imdb_id(position);
			}
			break;
		case 13: // came from search page.
			title_editText.setText(intent.getStringExtra("title"));
			genre_editText.setText(intent.getStringExtra("genre"));
			year_editText.setText(intent.getStringExtra("year"));
			mpaa_rating_editText.setText(intent.getStringExtra("mpaa_rating"));
			runtime_editText.setText(intent.getStringExtra("runtime"));
			rt_rating_textView.setText(intent.getStringExtra("rt_rating"));
			synopsis_editText.setText(intent.getStringExtra("synopsis"));
			directors_editText.setText(intent.getStringExtra("directors"));
			cast_editText.setText(intent.getStringExtra("cast"));
			url_local_EditText.setText(intent.getStringExtra("url_local"));
			url_detailed_editText.setText(intent.getStringExtra("url_detailed"));
			imdb_id = intent.getStringExtra("imdb_id");
			delete_button.setVisibility(View.GONE);
			break;
		default:
			break;
		}
		// load poster.
		showImage();
		my_rating_seekbar.setMax(10);
		my_rating_seekbar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {	
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				my_rating_textView.setText(progress + "/10");
				my_rating_layout_textview.setText(progress + "/10");
			}
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
			}
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
			}
		});
		
		imdb_button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String url = "http://www.imdb.com/title/tt"+imdb_id+"/";
				Intent imdb_intent = new Intent(Intent.ACTION_VIEW);
				imdb_intent.setData(Uri.parse(url));
				startActivity(imdb_intent);
			}
		});
		
		delete_button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				ContextThemeWrapper ctw1 = new ContextThemeWrapper(EditPage.this, R.style.Theme_Base);
				DelDialog delDialog = new DelDialog(ctw1, getString(R.string.alertDialog_delete_item_title), getString(R.string.alertDialog_delete_item_message) + adapter.getItemTitle(position) + "?", adapter.getItemId(position));
				delDialog.show();
			}
		});
		
			
		
			// image click listener (opens image edit dialog).
		url_image.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				ContextThemeWrapper ctw = new ContextThemeWrapper(EditPage.this, R.style.Theme_Base);
				ImageDialog imgDialog = new ImageDialog(ctw);
				imgDialog.show();
			}
		});
			// opens and closes the url texts layout.
		share_button.setOnClickListener(new View.OnClickListener() {	
			@Override
			public void onClick(View v) {
				Intent shareIntent = new Intent(Intent.ACTION_SEND);
				shareIntent.setType("text/plain");
				shareIntent.putExtra(Intent.EXTRA_SUBJECT, title_editText.getText().toString());
					// if imdb id is "n/a".
				if (imdb_id.equals(getResources().getString(R.string.editPage_na))) {
						// if synopsis is n/a.
					if (!synopsis_editText.equals(getResources().getString(R.string.searchPage_synopsis_na))) {
						shareIntent.putExtra(Intent.EXTRA_TEXT, synopsis_editText.getText().toString());
					}
				} else {
					shareIntent.putExtra(Intent.EXTRA_TEXT, "http://www.imdb.com/title/tt"+imdb_id+"/");
				}
				startActivity(shareIntent);
			}
		});
			// opens and closes the user rating layout.
		rate_button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (my_rating_layout.getVisibility() == View.VISIBLE) {
					my_rating_layout.setVisibility(View.GONE);
				} else {
					my_rating_layout.setVisibility(View.VISIBLE);					
				}
			}
		});
			// opens and closes the user rating layout.
		my_rating_textView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (my_rating_layout.getVisibility() == View.VISIBLE) {
					my_rating_layout.setVisibility(View.GONE);
				} else {
					my_rating_layout.setVisibility(View.VISIBLE);					
				}
			}
		});
			// opens confirmation dialog.
		cancel_button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				ContextThemeWrapper ctx = new ContextThemeWrapper(EditPage.this, R.style.Theme_Base);
				MyDialog myDialog = new MyDialog(ctx);
				myDialog.show();
			}
		});
		
		save_button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				save();
			}
		});
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.editoptionsmenu, menu);
		setTheme(R.style.Theme_Base);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.editPage_saveMenuItem:
			save();
			break;
		case R.id.editPage_cancelMenuItem:
			ContextThemeWrapper ctw = new ContextThemeWrapper(EditPage.this, R.style.Theme_Base);
			MyDialog myDialog = new MyDialog(ctw);
			myDialog.show();
			break;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}
	
	private void save() {
		String title = title_editText.getText().toString();
		String synopsis = synopsis_editText.getText().toString();
		String url_detailed = url_detailed_editText.getText().toString();
		int watched;
		if (watched_toggle_button.isChecked()) {
			watched = 1;
		} else {
			watched = 0;
		}
		String genre;
		if (genre_editText.getText().toString().equals("")) {
			genre = getString(R.string.editPage_genre_na);
		} else {
			genre = genre_editText.getText().toString();
		}
		String year;
		if (year_editText.getText().toString().equals("")) {
			year = getString(R.string.editPage_year_na);
		} else {
			year = year_editText.getText().toString();
		}
		String mpaa_rating = mpaa_rating_editText.getText().toString();
		String runtime = runtime_editText.getText().toString();
		String rt_rating = rt_rating_textView.getText().toString();
		String my_rating;
		if (my_rating_textView.getText().toString().equals("")) {
			my_rating = getString(R.string.editPage_na);
		} else {
			my_rating = my_rating_textView.getText().toString();
		}
		String cast = cast_editText.getText().toString();
		String directors = directors_editText.getText().toString();
		
		switch (code) {
		case 11: // code 11 = editing a movie - update the corresponding movie details in the data-base.
			if (title.isEmpty()) {
				Toast.makeText(EditPage.this, R.string.toast_editPage_empty_title, Toast.LENGTH_LONG).show();
				title_editText.requestFocus();
			} else {
				if (bitmap_image != null) {
					SaveImage(bitmap_image);
				}
				String id = String.valueOf(adapter.getItemId(position));
				String imdb_id =  this.imdb_id.toString();
				adapter.updateMovie(id, title, synopsis, url_local_EditText.getText().toString(), watched, genre, year, mpaa_rating, runtime, rt_rating, my_rating, cast, directors, url_detailed, imdb_id);
				Toast.makeText(EditPage.this, R.string.toast_movie_updated, Toast.LENGTH_SHORT).show();
				finish();
				overridePendingTransition(R.anim.push_right_out, R.anim.push_right_out);
			}
			break;

		default: // add a movie.
			if (title.isEmpty()) {
				Toast.makeText(EditPage.this, R.string.toast_editPage_empty_title, Toast.LENGTH_LONG).show();
				title_editText.requestFocus();
			} else {
				if (bitmap_image != null) {
					SaveImage(bitmap_image);
				}
				String imdb_id = this.imdb_id.toString();
				adapter.addMovie(title, synopsis, url_local_EditText.getText().toString(), watched, genre, year, mpaa_rating, runtime, rt_rating, my_rating, cast, directors, url_detailed, imdb_id);
				Toast.makeText(EditPage.this, R.string.toast_movie_added, Toast.LENGTH_SHORT).show();
				finish();
				overridePendingTransition(R.anim.push_right_out, R.anim.push_right_out);
			}
			break;
		}
	}
	
	@Override
	public void onBackPressed() { // opens confirmation dialog.
		ContextThemeWrapper ctw = new ContextThemeWrapper(EditPage.this, R.style.Theme_Base);
		MyDialog myDialog = new MyDialog(ctw);
		myDialog.show();
	}
		// confirmation dialog.
	private class MyDialog extends AlertDialog {
		protected MyDialog(Context context) {
			super(context);
			setTitle(R.string.EditPage_cancel_alertDialog_title);
			setMessage(context.getString(R.string.EditPage_cancel_alertDialog_message));
			setButton(context.getString(R.string.alertDialog_save_button), new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					save();
				}
			});
			setButton2(context.getString(R.string.alertDialog_cancel_button), new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dismiss();
				}
			});
			setButton3(context.getString(R.string.alertDialog_goBack_button), new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					finish();
					overridePendingTransition(R.anim.push_right_out, R.anim.push_right_out);
				}
			});
		}
	}
	
	private class DownloadImageTask extends AsyncTask<String, Integer,Bitmap> {
		private ImageView view;
		
		DownloadImageTask(ImageView view) {
			this.view = view;
		}
		
		protected void onPreExecute() {
			TextView errorMsg = (TextView)findViewById(R.id.editPage_errorMsg);
			errorMsg.setVisibility(View.INVISIBLE);
		}
		
		protected Bitmap doInBackground(String... urls) {
			Bitmap image = downloadImage(urls[0]);
			return image;
		}
		
		protected void onPostExecute(Bitmap result) {
			if (result != null) {
				view.setImageBitmap(result);
				bitmap_image = result;
			}
			else {
				if (bitmap_image != null) { // if image exists.
					TextView errorMsg = (TextView)findViewById(R.id.editPage_errorMsg);
					errorMsg.setVisibility(View.GONE);
				} else {
					TextView errorMsg = (TextView)findViewById(R.id.editPage_errorMsg);
					errorMsg.setVisibility(View.VISIBLE);
					if (code == 10) { // if page is in "add new movie" mode.
						errorMsg.setText(R.string.editPage_image_na_add);
						errorMsg.setTextColor(getResources().getColor(R.color.WhiteSmoke));
					} else {
						if (code == 11) {
							errorMsg.setText(R.string.editPage_image_na_add);
							errorMsg.setTextColor(getResources().getColor(R.color.WhiteSmoke));
						} else {
							errorMsg.setText(R.string.editPage_image_na);
						}
					}
				}
			}
				// hide progress bar.
				progressBar.setVisibility(ProgressBar.GONE);
			}
		
		private Bitmap downloadImage(String urlString) {
			URL url;
			try {
				url = new URL(urlString);
				HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
				InputStream is = httpCon.getInputStream();
				ByteArrayOutputStream buffer = new ByteArrayOutputStream();
				int nRead, totalBytesRead = 0;
				byte[] data = new byte[2048];
				// Read the image bytes in chunks of 2048 bytes
				while ((nRead = is.read(data, 0, data.length)) != -1) {
					buffer.write(data, 0, nRead);
					totalBytesRead += nRead;
					publishProgress(totalBytesRead);
				}
				buffer.flush();
				byte[] image = buffer.toByteArray();
				Bitmap bitmap = BitmapFactory.decodeByteArray(image, 0, image.length);
				return bitmap;
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}
	}
		// saves image to app's image dir.
	private void SaveImage(Bitmap finalBitmap) {
	    String root = Environment.getExternalStorageDirectory().toString();
	    File myDir = new File(root + "/moviebudfree_images");    
	    myDir.mkdirs();
	    
	    String fname = "";
	    String fname1 = url_detailed_editText.getText().toString();
	    if (fname1.equals("")) { // if it's a local photo - add a random name.
	    	Random r = new Random();
	    	int i1 = r.nextInt();
	    	int i2 = r.nextInt();
			fname = i1 +"_"+ i2+ ".jpg";
		} else {
			fname = fname1.substring(fname1.lastIndexOf("/") + 1);
		}
	    
	    url_local_EditText.setText(myDir + "/" + fname); 
	    File file = new File (myDir, fname);
	    if (file.exists ()) file.delete (); 
	    try {
	           FileOutputStream out = new FileOutputStream(file);
	           finalBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
	           out.flush();
	           out.close();

	    } catch (Exception e) {
	           e.printStackTrace();
	    }
	}
		// edit image dialog.
	public class ImageDialog extends Dialog {
		public ImageDialog(Context context) {
			super(context);
			requestWindowFeature(Window.FEATURE_NO_TITLE);
			setContentView(R.layout.image_dialog);
			setCanceledOnTouchOutside(true);
			ImageButton imageDialog_edit_button = (ImageButton)findViewById(R.id.imageDialog_edit_button);
			ImageButton imageDialog_cancel_button = (ImageButton)findViewById(R.id.imageDialog_cancel_button);
			ImageButton imageDialog_save_button = (ImageButton)findViewById(R.id.imageDialog_save_button);
			imageDialog_imgview = (ImageView)findViewById(R.id.imageDialog_imageView);
			if (bitmap_image != null) {
				imageDialog_imgview.setImageBitmap(bitmap_image);
			} else {
				if (new File(url_local_EditText.getText().toString()).exists()) {
					Bitmap bm = BitmapFactory.decodeFile(url_local_EditText.getText().toString());
					imageDialog_imgview.setImageBitmap(bm);
				} else {
					findViewById(R.id.imageDialog_imageNA_textView).setVisibility(View.FOCUS_UP);
				}
			}
			
			imageDialog_edit_button.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
	                startActivityForResult(cameraIntent, CAMERA_REQUEST);
	                findViewById(R.id.imageDialog_imageNA_textView).setVisibility(View.GONE);
				}
			});
			imageDialog_cancel_button.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					dismiss();
				}
			});
			imageDialog_save_button.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					bitmap_image = photo_from_cam;
					url_image.setImageBitmap(bitmap_image);
					dismiss();
					if (bitmap_image != null) {
						TextView errorMsg = (TextView)EditPage.this.findViewById(R.id.editPage_errorMsg);
						errorMsg.setVisibility(View.GONE);	
					}
				}
			});
		}
	}
	
	private void showImage() {
		progressBar.setVisibility(1);
		// if image available in app's image dir.
		if (new File(url_local_EditText.getText().toString()).exists()) {
			Bitmap bm = BitmapFactory.decodeFile(url_local_EditText.getText().toString());
			url_image.setImageBitmap(bm);
			progressBar.setVisibility(ProgressBar.GONE);
		} else { // download image if it doesn't exist.
			DownloadImageTask task = new DownloadImageTask(url_image);
			task.execute(url_detailed_editText.getText().toString());
		}
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) { 
            photo_from_cam = (Bitmap) data.getExtras().get("data"); 
            imageDialog_imgview.setImageBitmap(photo_from_cam);
        }
    }

	private class DelDialog extends AlertDialog {

		// single item delete dialog.
		protected DelDialog(Context context, String title, String message, final long id) {
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
					Toast.makeText(EditPage.this, R.string.toast_item_deleted, Toast.LENGTH_SHORT).show();
					finish();
				}
			});
			setButton2(context.getString(R.string.alertDialog_cancel_button), new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dismiss();
				}
			});
		}
	}
}
