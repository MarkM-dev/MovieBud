package markm.moviebudfree;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

public class DownloadImageTask extends AsyncTask<String, Integer,Bitmap> {
	
	private ViewHolderSearch view;
	
	DownloadImageTask(ViewHolderSearch view) {
		this.view = view;
	}
	
	protected void onPreExecute() {
	}
	
	protected Bitmap doInBackground(String... urls) {
		Bitmap image = downloadImage(urls[0]);
		return image;
	}
	protected void onPostExecute(Bitmap result) {
		view.setPic_imageView(result);
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