package markm.moviebudfree;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;

public class MyBaseAdapter extends BaseAdapter {

	private DBHandler dataBase;
	private ArrayList<Movie> list;
	private Context mContext;
	private int mNumRows;
	private String sortBy;
	
	public MyBaseAdapter(Context context, String sortBy) {
		this.mContext = context;
		dataBase = new DBHandler(this.mContext);
		list = dataBase.getAllMovies(sortBy);
		this.mNumRows = list.size();
		this.sortBy = sortBy;
	}

	@Override
	public int getCount() {
		list = dataBase.getAllMovies(sortBy);
		this.mNumRows = list.size();
		return this.mNumRows;
	}
	
	public void addMovie(String title, String synopsis, String url_local, int watched, 
			String genre, String year, String mpaa_rating, String runtime, 
			String rt_rating , String my_rating, String cast, String directors, String url_detailed, String imdb_id) {
		
		dataBase.addMovie(title, synopsis, url_local, watched, genre, year, mpaa_rating, runtime, rt_rating, my_rating, cast, directors, url_detailed, imdb_id);
		mNumRows += 1;
	}
	
	public void updateMovie(String id, String title, String synopsis, String url_local, int watched, 
			String genre, String year, String mpaa_rating, String runtime, 
			String rt_rating , String my_rating, String cast, String directors, String url_detailed, String imdb_id) {
		
		dataBase.updateMovie(id, title, synopsis, url_local, watched, genre, year, mpaa_rating, runtime, rt_rating, my_rating, cast, directors, url_detailed, imdb_id);
	}
	
	public void deleteMovie(String id) {
		dataBase.deleteMovie(id);
		mNumRows -= 1;
	}
	
	public void deleteAll() {
		dataBase.deleteAll();
		mNumRows = 0;
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}
	public String getItemTitle(int position) {
		return list.get(position).getTitle().toString();
	}
	public String getItemSynopsis(int position) {
		return list.get(position).getSynopsis().toString();
	}
	public int getItemWatchedStatus(int position) {
		return list.get(position).getWatched();
	}
	public String getItemGenre(int position) {
		return list.get(position).getGenre().toString();
	}
	public String getItemYear(int position) {
		return list.get(position).getYear().toString();
	}
	public String getItemMpaa_rating(int position) {
		return list.get(position).getMpaa_rating().toString();
	}
	public String getItemRuntime(int position) {
		return list.get(position).getRuntime().toString();
	}
	public String getItemRt_rating(int position) {
		return list.get(position).getRt_rating().toString();
	}
	public String getItemMy_rating(int position) {
		return list.get(position).getMy_rating().toString();
	}
	public String getItemCast(int position) {
		return list.get(position).getCast().toString();
	}
	public String getItemUrl_local(int position) {
		return list.get(position).getUrl_local().toString();
	}
	public String getItemUrl_local_byId(long id) {
		String movie_url = dataBase.getMovieUrl_byId(id).toString();
			return movie_url;
	}
	@Override
	public long getItemId(int position) {
		return (int)list.get(position).getId();
	}
	public String getItemDirectors(int position) {
		return list.get(position).getDirectors().toString();
	}
	public String getItemUrl_Detailed(int position){
		return list.get(position).getUrl_detailed().toString();
	}
	public String getItem_imdb_id (int position) {
		return list.get(position).getImdb_id().toString();
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		ItemView view = null;
		
		if (convertView == null) {
			// Create a new view.
			//   This area is for properties that are all the same.
			view = new ItemView(this.mContext);
		} else {
			// Use the convertview.
			view = (ItemView)convertView;
		}
		
		// This area is for properties that are different for each view.
		//view.setTextColor(Color.rgb(position*10, 255-position*10, 200));
		
		try {
			Bitmap bm = BitmapFactory.decodeFile(list.get(position).getUrl_local().toString());
			view.setPic_imageView(bm);
		} catch (Exception e) {
			e.printStackTrace();
		}
		view.setTitle_textview(list.get(position).getTitle().toString());
		if (list.get(position).getWatched() == 1) {
			view.setWatchedTextview(mContext.getString(R.string.viewHolder_watched), view.getResources().getColor(R.color.MyGreen));
		} else {
			view.setWatchedTextview(mContext.getString(R.string.viewHolder_unwatched), view.getResources().getColor(R.color.MyRed));
		}
		view.setGenre_textView(list.get(position).getGenre().toString());
		view.setYear_textView(list.get(position).getYear().toString());
		view.setRating_textView(list.get(position).getMy_rating().toString());
		Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.fadein);
		view.startAnimation(animation);
		
		return view;
	}

	
}
