package markm.moviebudfree;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ItemView extends LinearLayout {

	private ImageView pic_imageView;
	private TextView title_textView;
	private TextView genre_textView;
	private TextView year_textView;
	private TextView rating_textView;
	private TextView WatchedTextView;
	
	public ItemView(Context context) {
		super(context);
			// Inflate the xml resource
		LayoutInflater layoutInflater = ((Activity)context).getLayoutInflater();
		layoutInflater.inflate(R.layout.view_holder, this);
		
			// Capture the TextView to the member variables.
		this.pic_imageView = (ImageView)findViewById(R.id.viewHolder_pic_imageView);
		this.title_textView = (TextView)findViewById(R.id.viewHolder_title_textView);
		this.WatchedTextView = (TextView)findViewById(R.id.viewHolder_watched_textView);
		this.genre_textView = (TextView)findViewById(R.id.viewHolder_genre_textView);
		this.year_textView = (TextView)findViewById(R.id.viewHolder_year_textView);
		this.rating_textView = (TextView)findViewById(R.id.viewHolder_rating_textView);
	}
	
	
	public void setPic_imageView(Bitmap bm) {
		this.pic_imageView.setImageBitmap(bm);
	}
	
	public void setTitle_textview(String title_textView) {
		this.title_textView.setText(title_textView);
	}
	
	public void setGenre_textView(String genre_textView) {
		this.genre_textView.setText(genre_textView);
	}
	
	public void setYear_textView(String year_textView) {
		this.year_textView.setText(year_textView);
	}
	
	public void setRating_textView(String rating_textView) {
		this.rating_textView.setText(rating_textView);
	}
	
	public void setWatchedTextview(String string, int color) {
		this.WatchedTextView.setText(string);
		this.WatchedTextView.setTextColor(color);
	}
	
}
