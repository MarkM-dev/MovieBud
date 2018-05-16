package markm.moviebudfree;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

public class DBHandler {
	
	private DBHelper helper;
	
	public DBHandler(Context con) {
		helper = new DBHelper(con, "movies.db", null, 1);
	}
	
	
	public void addMovie(String title, String synopsis, String url_local, int watched, 
						String genre, String year, String mpaa_rating, String runtime, 
						String rt_rating , String my_rating, String cast, String directors, String url_detailed, String imdb_id) {
		SQLiteDatabase db = helper.getWritableDatabase();
		
		try {
			ContentValues cv = new ContentValues();
			cv.put(DBHelper.DB_TITLE, title);
			cv.put(DBHelper.DB_SYNOPSIS, synopsis);
			cv.put(DBHelper.DB_URL_LOCAL, url_local);
			cv.put(DBHelper.DB_WATCHED, watched);
			cv.put(DBHelper.DB_GENRE, genre);
			cv.put(DBHelper.DB_YEAR, year);
			cv.put(DBHelper.DB_MPAA_RATING, mpaa_rating);
			cv.put(DBHelper.DB_RUNTIME, runtime);
			cv.put(DBHelper.DB_RT_RATING, rt_rating);
			cv.put(DBHelper.DB_MY_RATING, my_rating);
			cv.put(DBHelper.DB_CAST, cast);
			cv.put(DBHelper.DB_DIRECTORS, directors);
			cv.put(DBHelper.DB_URL_DETAILED, url_detailed);
			cv.put(DBHelper.DB_IMDB_ID, imdb_id);
			
			db.insertOrThrow(DBHelper.DB_TABLE_NAME, null, cv);
		} catch (SQLiteException e) {
			e.getCause();
		}finally {
			if (db.isOpen()) {
				db.close();
			}
		}
	}
	
	public void updateMovie (String id, String title, String synopsis, String url_local, int watched, 
			String genre, String year, String mpaa_rating, String runtime, 
			String rt_rating , String my_rating, String cast, String directors, String url_detailed, String imdb_id) {
		SQLiteDatabase db = helper.getWritableDatabase();
		
		try {
			ContentValues cv = new ContentValues();
			cv.put(DBHelper.DB_TITLE, title);
			cv.put(DBHelper.DB_SYNOPSIS, synopsis);
			cv.put(DBHelper.DB_URL_LOCAL, url_local);
			cv.put(DBHelper.DB_WATCHED, watched);
			cv.put(DBHelper.DB_GENRE, genre);
			cv.put(DBHelper.DB_YEAR, year);
			cv.put(DBHelper.DB_MPAA_RATING, mpaa_rating);
			cv.put(DBHelper.DB_RUNTIME, runtime);
			cv.put(DBHelper.DB_RT_RATING, rt_rating);
			cv.put(DBHelper.DB_MY_RATING, my_rating);
			cv.put(DBHelper.DB_CAST, cast);
			cv.put(DBHelper.DB_DIRECTORS, directors);
			cv.put(DBHelper.DB_URL_DETAILED, url_detailed);
			cv.put(DBHelper.DB_IMDB_ID, imdb_id);
			
			db.update(DBHelper.DB_TABLE_NAME, cv, "_id=?", new String [] {id});
		} catch (SQLiteException e) {
			e.getCause();
		}finally {
			if (db.isOpen()) {
				db.close();
			}
		}
	}
	
	public void deleteMovie (String id) {
		SQLiteDatabase db = helper.getWritableDatabase();
		
		try {
			db.delete(DBHelper.DB_TABLE_NAME, "_id=?", new String [] {id});
			
		} catch (SQLiteException e) {
			e.getCause();
		}finally {
			if (db.isOpen()) {
				db.close();
			}
		}
	}
	
	public void deleteAll () {
		SQLiteDatabase db = helper.getWritableDatabase();
		
		try {
			db.delete(DBHelper.DB_TABLE_NAME, null, null);
		} catch (SQLiteException e) {
			
		}finally {
			if (db.isOpen()) {
				db.close();
			}
		}
	}
	
	public String getMovieUrl_byId(long id){
		String id1 = String.valueOf(id);
		Cursor cursor = null;
		SQLiteDatabase db = helper.getReadableDatabase();
		String url_local;
		try {
			cursor = db.query(DBHelper.DB_TABLE_NAME, new String[] {DBHelper.DB_URL_LOCAL}, "_id="+id1, null, null, null, null);
		} catch (SQLiteException e) {
			e.getCause();
		}
		if (cursor.moveToFirst()) {
			url_local = cursor.getString(0);
			return url_local;
		} else {
			return null;
		}
		
		
		
	}
	
	public ArrayList<Movie> getAllMovies(String sortBy){
		Cursor cursor = null;
		
		SQLiteDatabase db = helper.getReadableDatabase();
		
		try {
			cursor = db.query(DBHelper.DB_TABLE_NAME, null, null, null, null, null, sortBy);
		} catch (SQLiteException e) {
			e.getCause();
		}
		
		ArrayList<Movie> list = new ArrayList<Movie>();
		while (cursor.moveToNext()) {
			int id = cursor.getInt(0);
			String title = cursor.getString(1);
			String synopsis = cursor.getString(2);
			String url_local = cursor.getString(3);
			int watched = cursor.getInt(4);
			String genre = cursor.getString(5);
			String year = cursor.getString(6);
			String mpaa_rating = cursor.getString(7);
			String runtime = cursor.getString(8);
			String rt_rating = cursor.getString(9);
			String my_rating = cursor.getString(10);
			String cast = cursor.getString(11);
			String directors = cursor.getString(12);
			String url_detailed = cursor.getString(13);
			String imdb_id = cursor.getString(14);
			
			list.add(new Movie(id, title, synopsis, url_local, watched, genre, year, mpaa_rating, runtime, rt_rating, my_rating, cast, directors, url_detailed, imdb_id));
		}
		return list;
	}
}
