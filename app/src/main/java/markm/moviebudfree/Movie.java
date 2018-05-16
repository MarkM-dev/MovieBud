package markm.moviebudfree;

public class Movie {

	private int id;
	private String title;
	private String synopsis;
	private String url_local;
	private int watched;
	private String genre;
	private String year;
	private String mpaa_rating;
	private String runtime;
	private String rt_rating;
	private String my_rating;
	private String cast;
	private String directors;
	private String url_detailed;
	private String imdb_id;
	

	public Movie() {
		super();
	}

	public Movie(int id, String title, String synopsis, String url_local,
			int watched, String genre, String year, String mpaa_rating,
			String runtime, String rt_rating, String my_rating, String cast, String directors, String url_detailed, String imdb_id) {
		super();
		this.id = id;
		this.title = title;
		this.synopsis = synopsis;
		this.url_local = url_local;
		this.watched = watched;
		this.genre = genre;
		this.year = year;
		this.mpaa_rating = mpaa_rating;
		this.runtime = runtime;
		this.rt_rating = rt_rating;
		this.my_rating = my_rating;
		this.cast = cast;
		this.directors = directors;
		this.url_detailed = url_detailed;
		this.imdb_id = imdb_id;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getSynopsis() {
		return synopsis;
	}
	public void setSynopsis(String synopsis) {
		this.synopsis = synopsis;
	}
	public String getUrl_local() {
		return url_local;
	}
	public void setUrl_local(String url) {
		this.url_local = url;
	}
	public int getWatched() {
		return watched;
	}
	public void setWatched(int watched) {
		this.watched = watched;
	}
	public String getGenre() {
		return genre;
	}
	public void setGenre(String genre) {
		this.genre = genre;
	}
	public String getYear() {
		return year;
	}
	public void setYear(String year) {
		this.year = year;
	}
	public String getMpaa_rating() {
		return mpaa_rating;
	}
	public void setMpaa_rating(String mpaa_rating) {
		this.mpaa_rating = mpaa_rating;
	}
	public String getRuntime() {
		return runtime;
	}
	public void setRuntime(String runtime) {
		this.runtime = runtime;
	}
	public String getRt_rating() {
		return rt_rating;
	}
	public void setRt_rating(String rt_rating) {
		this.rt_rating = rt_rating;
	}
	public String getMy_rating() {
		return my_rating;
	}
	public void setMy_rating(String my_rating) {
		this.my_rating = my_rating;
	}
	public String getCast() {
		return cast;
	}
	public void setCast(String cast) {
		this.cast = cast;
	}
	public String getDirectors() {
		return directors;
	}
	public void setDirectors(String directors) {
		this.directors = directors;
	}
	public String getUrl_detailed() {
		return url_detailed;
	}
	public void setUrl_detailed(String url_detailed) {
		this.url_detailed = url_detailed;
	}
	public String getImdb_id() {
		return imdb_id;
	}
	public void setImdb_id(String imdb_id) {
		this.imdb_id = imdb_id;
	}
}
