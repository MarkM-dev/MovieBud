package markm.moviebudfree;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper{
	
	public static final String DB_ID = "_id";
	public static final String DB_TABLE_NAME = "moviesTable";
	public static final String DB_TITLE = "title";
	public static final String DB_SYNOPSIS = "synopsis";
	public static final String DB_URL_LOCAL = "url_local";
	public static final String DB_WATCHED = "watched";
	public static final String DB_GENRE = "genre";
	public static final String DB_YEAR = "year";
	public static final String DB_MPAA_RATING = "mpaa_rating";
	public static final String DB_RUNTIME = "runtime";
	public static final String DB_RT_RATING = "rt_rating";
	public static final String DB_MY_RATING = "my_rating";
	public static final String DB_CAST = "cast";
	public static final String DB_DIRECTORS = "directors";
	public static final String DB_URL_DETAILED = "url_detailed";
	public static final String DB_IMDB_ID = "imdb_id";
	public static final String DB_SORT_TITLE = DB_TITLE + " ASC";
	public static final String DB_SORT_GENRE = DB_GENRE + " ASC";
	public static final String DB_SORT_YEAR = DB_YEAR + " DESC";
	public static final String DB_SORT_MY_RATING = DB_MY_RATING + " DESC";
	public static final String DB_SORT_WATCHED = DB_WATCHED + " DESC";

	public DBHelper(Context context, String name, CursorFactory factory, int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String cmd = "CREATE TABLE "+ DB_TABLE_NAME +" ("+DB_ID+" INTEGER PRIMARY KEY , "+DB_TITLE+" TEXT , "+DB_SYNOPSIS+" TEXT , "+DB_URL_LOCAL+" TEXT , "+DB_WATCHED+" INTEGER , "+DB_GENRE+" TEXT , "+DB_YEAR+" TEXT , "+DB_MPAA_RATING+" TEXT , "+DB_RUNTIME+" TEXT , "+DB_RT_RATING+" TEXT , "+DB_MY_RATING+" TEXT , "+DB_CAST+" TEXT , "+DB_DIRECTORS+" TEXT , "+DB_URL_DETAILED+" TEXT , "+DB_IMDB_ID+" TEXT );";
		
		try {
			db.execSQL(cmd);
		} catch (SQLiteException e) {
			e.getCause();
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		
	}

}
