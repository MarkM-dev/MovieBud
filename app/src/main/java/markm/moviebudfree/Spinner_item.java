package markm.moviebudfree;


public class Spinner_item{
	
	private String spinner_item_textView;

	public Spinner_item(String item_text) {
		
		this.spinner_item_textView = item_text;
	}
	
	@Override
	public String toString() {
		return spinner_item_textView+"";
	}
	
	
}
