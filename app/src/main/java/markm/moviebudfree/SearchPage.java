package markm.moviebudfree;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class SearchPage extends Activity {
	
	private EditText search_editText;
	private String API_KEY;
	private int page_limit;
	private ListView lv;
	private Button clear_button;
	private ArrayAdapter<Movie> adapter;
	private final ArrayList<Movie> list = new ArrayList<Movie>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.search_page);
		
		 AdView adView = (AdView)this.findViewById(R.id.searchPage_adView);
		    AdRequest adRequest = new AdRequest.Builder().build();
		    adView.loadAd(adRequest);
		
		API_KEY = "6dj4mz5gzfzh6w9vz4nw999d";
		page_limit = 10;
		lv = (ListView)findViewById(R.id.searchPage_listView);
		adapter = new ArrayAdapter<Movie>(SearchPage.this, -1, list){
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				ViewHolderSearch view = null;
				if(convertView == null) {
					view = new ViewHolderSearch(SearchPage.this);
				} else {
					view = (ViewHolderSearch)convertView;
					if (!view.getTask().isCancelled()) {
						view.getTask().cancel(true);
					}
				}
				
				view.setTitle_textView(list.get(position).getTitle().toString());
				view.setYear_textView(list.get(position).getYear().toString());
				view.setSynopsis_textView(list.get(position).getSynopsis().toString());
				DownloadImageTask task = new DownloadImageTask(view);
				task.execute(list.get(position).getUrl_detailed().toString());
				view.setTask(task);
				return view;
			}
		};
		lv.setAdapter(adapter);
		
		search_editText = (EditText)findViewById(R.id.searchPage_search_editText);
		clear_button = (Button)findViewById(R.id.searchPage_searchEditText_clear_button);
		search_editText.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if (s.length() == 0) {
					clear_button.setBackgroundResource(R.drawable.abc_ic_clear_disabled);
				} else {
					clear_button.setBackgroundResource(R.drawable.abc_ic_clear);
				}
			}
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}
			@Override
			public void afterTextChanged(Editable s) {
			}
		});
		clear_button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				search_editText.setText("");
				ProgressBar bar = (ProgressBar)findViewById(R.id.searchPage_progressBar);
				if (bar.isShown()) {
					bar.setVisibility(ProgressBar.GONE);
				}
			}
		});
		search_editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				if (actionId == EditorInfo.IME_ACTION_SEARCH) {
					// hide keyboard.
					InputMethodManager inputManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
					inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
					SearchAsyncTask task = new SearchAsyncTask(SearchPage.this);
					String user_input = search_editText.getText().toString().replaceAll("\\s+", "+");
					String url = "http://api.rottentomatoes.com/api/public/v1.0/movies.json?q=" + user_input + "&page_limit=" + page_limit + "&page=1&apikey=" + API_KEY + "";
					task.execute(url);
		            return true;
		        }
				return false;
			}
		});
		
		lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Intent edit_intent = new Intent(SearchPage.this, EditPage.class);
				edit_intent.putExtra("code", 13);
				edit_intent.putExtra("title", list.get(position).getTitle().toString());
				edit_intent.putExtra("genre", list.get(position).getGenre().toString());
				edit_intent.putExtra("year", list.get(position).getYear().toString());
				edit_intent.putExtra("mpaa_rating", list.get(position).getMpaa_rating().toString());
				edit_intent.putExtra("runtime", list.get(position).getRuntime().toString());
				edit_intent.putExtra("rt_rating", list.get(position).getRt_rating().toString());
				edit_intent.putExtra("synopsis", list.get(position).getSynopsis().toString());
				edit_intent.putExtra("cast", list.get(position).getCast().toString());
				edit_intent.putExtra("url_local", list.get(position).getUrl_local().toString());
				edit_intent.putExtra("directors", list.get(position).getDirectors().toString());
				edit_intent.putExtra("url_detailed", list.get(position).getUrl_detailed().toString());
				edit_intent.putExtra("imdb_id", list.get(position).getImdb_id());
				
				startActivityForResult(edit_intent, 13);
				overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
			}
		});
		ImageButton adCloseBtn = (ImageButton)findViewById(R.id.searchPage_adCloseBtn);
		adCloseBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				LinearLayout ad_layout = (LinearLayout)findViewById(R.id.searchPage_ad_layout);
				ad_layout.setVisibility(View.GONE);
			}
		});
	}
	
	private class SearchAsyncTask extends AsyncTask<String, Void, String> {

		private Activity activity;
		
		public SearchAsyncTask(Activity act ) {
			activity = act;
		}
		
		@Override
		protected void onPreExecute() {
			ProgressBar bar = (ProgressBar)activity.findViewById(R.id.searchPage_progressBar);
			bar.setVisibility(ProgressBar.FOCUS_UP);
			list.clear();
		}
		
		@Override
		protected String doInBackground(String... params) {
			String result = getJson(params);
			return result.toString();
		}

		@Override
		protected void onPostExecute(String result) {
			String title = activity.getString(R.string.searchPage_na);
			String synopsis = activity.getString(R.string.searchPage_na);
			String url_local = activity.getString(R.string.searchPage_url_local_na);
			String genre = activity.getString(R.string.searchPage_na);
			String year = activity.getString(R.string.searchPage_na);
			String mpaa_rating = activity.getString(R.string.searchPage_na);
			String runtime = activity.getString(R.string.searchPage_na);
			String rt_rating = activity.getString(R.string.searchPage_na);
			String cast = activity.getString(R.string.searchPage_na);
			String directors = activity.getString(R.string.searchPage_na);
			String url_detailed = activity.getString(R.string.searchPage_na);
			String imdb_id = activity.getString(R.string.searchPage_na);
			
			TextView nothing_found_textview = (TextView)activity.findViewById(R.id.searchPage_no_movies_found);
			ProgressBar bar = (ProgressBar)activity.findViewById(R.id.searchPage_progressBar);
			try {
				JSONObject json = new JSONObject(result);
				JSONArray arr = json.getJSONArray("movies");
				if (arr.length() == 0) {
					nothing_found_textview.setVisibility(TextView.FOCUS_UP);
					bar.setVisibility(ProgressBar.GONE);
				} else {
					if (nothing_found_textview.isShown()) {
						nothing_found_textview.setVisibility(TextView.GONE);
					}
					for (int i = 0; i < arr.length(); i++) {
						JSONObject js = (JSONObject)arr.get(i);
						
							// if an extended-info id exists in the json, a second a-sync task is executed.
						if (js.has("id")) {
							String mID = js.getString("id").toString();
							Gtask gtask = new Gtask();
							String url_extended = "http://api.rottentomatoes.com/api/public/v1.0/movies/"+ mID +".json?apikey="+API_KEY+"";
							gtask.execute(url_extended);
							
							// if there isn't an extended-info id.
						} else {
							bar.setVisibility(ProgressBar.GONE);
							if(js.has("title")) {
								title = js.getString("title").toString();
							}
							if(js.has("synopsis")) {
								synopsis = js.getString("synopsis").toString();
								if (synopsis.length() < 2) {
									synopsis = activity.getString(R.string.searchPage_synopsis_na);
								}
							}
							if(js.has("posters")) {
								JSONObject ur_obj = (JSONObject)js.get("posters");
								if (ur_obj.has("detailed")) {
									url_detailed = ur_obj.getString("detailed").toString();
								}
							}
							if(js.has("year")) {
								year = "(" + js.getString("year").toString() + ")";
							}
							if(js.has("mpaa_rating")) {
								mpaa_rating = js.getString("mpaa_rating").toString();
							}
							if(js.has("runtime")) {
								runtime = js.getString("runtime").toString() + activity.getString(R.string.searchPage_runtime_mins);
							}
							if(js.has("ratings")) {
								JSONObject rt_rating_obj = (JSONObject)js.get("ratings");
								if (rt_rating_obj.has("critics_score")) {
									if (rt_rating_obj.getString("critics_score").toString().equals("-1")) {
										rt_rating = activity.getString(R.string.searchPage_na);
									} else {
										rt_rating = rt_rating_obj.getString("critics_score").toString() + "/100";
									}
								}
							}
							
							if(json.has("alternate_ids")) {
								JSONObject alternate_ids_obj = (JSONObject)json.get("alternate_ids");
								if (alternate_ids_obj.has("imdb")) {
									imdb_id = alternate_ids_obj.getString("imdb").toString();
								}
							}
							
							if(js.has("abridged_cast")) {
								cast = "";
								JSONArray cast_arr = js.getJSONArray("abridged_cast");
								for (int j = 0; j < cast_arr.length(); j++) {
									JSONObject cast_obj = (JSONObject)cast_arr.get(j);
									if (cast_obj.has("name")) {
										cast += cast_obj.getString("name").toString() + ", ";
									}
								}
								try {
									String cast_temp = cast;
									cast = cast_temp.substring(0, cast_temp.length()-2);
								} catch (Exception e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
							list.add(new Movie(0, title, synopsis, url_local, 0, genre, year, mpaa_rating, runtime, rt_rating, getString(R.string.searchPage_na), cast, directors, url_detailed, imdb_id));
						}
					}
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			adapter.notifyDataSetChanged();
		}	
	}
	
	private class Gtask extends AsyncTask<String, Void, String> {
		@Override
		protected String doInBackground(String... params) {
			String result = getJson(params);
			return result.toString();
		}
		@Override
		protected void onPostExecute(String result) {
			ProgressBar bar = (ProgressBar)findViewById(R.id.searchPage_progressBar);
			bar.setVisibility(ProgressBar.GONE);
			
			String title = getString(R.string.searchPage_na);
			String synopsis = getString(R.string.searchPage_na);
			String url_local = getString(R.string.searchPage_url_local_na);
			String genre = getString(R.string.searchPage_na);
			String year = getString(R.string.searchPage_na);
			String mpaa_rating = getString(R.string.searchPage_na);
			String runtime = getString(R.string.searchPage_na);
			String rt_rating = getString(R.string.searchPage_na);
			String cast = getString(R.string.searchPage_na);
			String directors = getString(R.string.searchPage_na);
			String url_detailed = getString(R.string.searchPage_na);
			String imdb_id = getString(R.string.searchPage_na);
			
			try {
				JSONObject json = new JSONObject(result);
						
					if(json.has("title")) {
						title = json.getString("title").toString();
					}
					if(json.has("synopsis")) {
						synopsis = json.getString("synopsis").toString();
						if (synopsis.length() < 2) {
							synopsis = getString(R.string.searchPage_synopsis_na);
						}
					}
					if(json.has("posters")) {
						JSONObject ur_obj = (JSONObject)json.get("posters");
						if (ur_obj.has("detailed")) {
							url_detailed = ur_obj.getString("detailed").toString();
						}
					}
					if(json.has("year")) {
						year = "(" + json.getString("year").toString() + ")";
					}
					if(json.has("genres")) {
						genre = "";
						JSONArray genresArr = json.getJSONArray("genres");
							genre += genresArr.get(0).toString() + ", ";
							try {
								genre += genresArr.get(1).toString() + ", ";
							} catch (Exception e1) {
								e1.printStackTrace();
							}
						try {
							String genre_temp = genre;
							genre = genre_temp.substring(0, genre_temp.length()-2);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					if(json.has("mpaa_rating")) {
						mpaa_rating = json.getString("mpaa_rating").toString();
					}
					if(json.has("runtime")) {
						runtime = json.getString("runtime").toString() + getString(R.string.searchPage_runtime_mins);
					}
					if(json.has("ratings")) {
						JSONObject rt_rating_obj = (JSONObject)json.get("ratings");
						if (rt_rating_obj.has("critics_score")) {
							if (rt_rating_obj.getString("critics_score").toString().equals("-1")) {
								rt_rating = getString(R.string.searchPage_na);
							} else {
								rt_rating = rt_rating_obj.getString("critics_score").toString() + "/100";
							}
						}
					}
					if(json.has("abridged_directors")) {
						JSONArray directors_arr = (JSONArray)json.getJSONArray("abridged_directors");
						directors = "";
						for (int i = 0; i < directors_arr.length(); i++) {
							JSONObject directors_obj = (JSONObject)directors_arr.get(i);
							if (directors_obj.has("name")) {
								directors += directors_obj.getString("name").toString() + ", ";
							}
						}
						try {
							String directors_temp = directors;
							directors = directors_temp.substring(0, directors_temp.length()-2);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					
					if(json.has("alternate_ids")) {
						JSONObject alternate_ids_obj = (JSONObject)json.get("alternate_ids");
						if (alternate_ids_obj.has("imdb")) {
							imdb_id = alternate_ids_obj.getString("imdb").toString();
						}
					}
					
					if(json.has("abridged_cast")) {
						cast = "";
						JSONArray cast_arr = json.getJSONArray("abridged_cast");
						for (int j = 0; j < cast_arr.length(); j++) {
							JSONObject cast_obj = (JSONObject)cast_arr.get(j);
							if (cast_obj.has("name")) {
								cast += cast_obj.getString("name").toString() + ", ";
							}
						}	
						try {
							String cast_temp = cast;
							cast = cast_temp.substring(0, cast_temp.length()-2);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					list.add(new Movie(0, title, synopsis, url_local, 0, genre, year, mpaa_rating, runtime, rt_rating, getString(R.string.searchPage_na), cast, directors, url_detailed, imdb_id));
			
			} catch (JSONException e) {
				e.printStackTrace();
			}
			adapter.notifyDataSetChanged();
		}
		
	}
	
	private String getJson(String... params){
		BufferedReader input = null;
		HttpURLConnection httpCon = null;
		InputStream input_stream =null;
		InputStreamReader input_stream_reader = null;
		StringBuilder response = new StringBuilder();
		try{
			URL url = new URL(params[0]);
			httpCon = (HttpURLConnection)url.openConnection();
			if(httpCon.getResponseCode()!=HttpURLConnection.HTTP_OK){
				return null;
			}
			
			input_stream = httpCon.getInputStream();
			input_stream_reader = new InputStreamReader(input_stream);
			input = new BufferedReader(input_stream_reader);
			String line ;
			while ((line = input.readLine())!= null){
				response.append(line +"\n");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			if(input!=null){
				try {
					input_stream_reader.close();
					input_stream.close();
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				if(httpCon != null){
					httpCon.disconnect();
				}
			}
		}
		return response.toString();
	}
	
}
