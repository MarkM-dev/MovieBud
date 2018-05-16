package markm.moviebudfree;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ViewHolderSearch extends LinearLayout{
	
	private ImageView pic_imageView;
	private TextView title_textView;
	private TextView year_textView;
	private TextView synopsis_textView;
	private DownloadImageTask task;

	public ViewHolderSearch(Context context) {
		super(context);
		
			// Inflate the xml resource
		LayoutInflater layoutInflater = ((Activity)context).getLayoutInflater();
		layoutInflater.inflate(R.layout.view_holder_search, this);
		
			// Capture the TextView to the member variables.
		this.pic_imageView = (ImageView)findViewById(R.id.viewHolderSearch_pic_imageView);
		this.title_textView = (TextView)findViewById(R.id.viewHolderSearch_title_textView);
		this.year_textView = (TextView)findViewById(R.id.viewHolderSearch_year_textView);
		this.synopsis_textView = (TextView)findViewById(R.id.viewHolderSearch_synopsis_textView);
	}
	
	public void setPic_imageView(Bitmap image) {
		this.pic_imageView.setImageBitmap(image);
	}
	public void setTitle_textView(String title_textView) {
		this.title_textView.setText(title_textView);
	}
	public void setYear_textView(String year_textView) {
		this.year_textView.setText(year_textView);
	}
	public void setSynopsis_textView(String synopsis_textView) {
		this.synopsis_textView.setText(synopsis_textView);
	}
	public DownloadImageTask getTask() {
		return task;
	}
	public void setTask(DownloadImageTask task) {
		this.task = task;
	}
	
}
