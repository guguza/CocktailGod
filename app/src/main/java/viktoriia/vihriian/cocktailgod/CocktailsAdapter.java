package viktoriia.vihriian.cocktailgod;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

/**
 * Created by Администратор on 23.03.2015.
 */

public class CocktailsAdapter extends ArrayAdapter<String> {

    public CocktailsAdapter(Context context, ArrayList<String> objects) {
        super(context, R.layout.row, R.id.name, objects);
    }
    @Override
    public View getView(int pos, View v, ViewGroup parent) {
        View row = super.getView(pos, v, parent);

        ViewHolder holder = (ViewHolder) row.getTag();
        if (holder==null) {
            holder=new ViewHolder(row);
            row.setTag(holder);
        }

        holder.ingredients.setText(CocktailsListActivity.cocktailsArr.get(pos).ingredients);
        if(CocktailsListActivity.cocktailsArr.get(pos).imageURL == null) {
            CocktailsListActivity.cocktailsArr.get(pos).imageURL = "http://www.barbook.ru/upload/" +
                    "cocktails/2001c5cfd1b9cc99112cf33d10dc0a77_80x110.png";
        }
        new DownloadImageTask(holder.icon)
                .execute(CocktailsListActivity.cocktailsArr.get(pos).imageURL);
        return(row);
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon = null;
            try {
                URL aURL = new URL(urldisplay);
                URLConnection conn = aURL.openConnection();
                conn.connect();
                InputStream is = conn.getInputStream();
                BufferedInputStream bis = new BufferedInputStream(is);
                mIcon = BitmapFactory.decodeStream(bis);
                bis.close();
                is.close();
            } catch (IOException e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }
}

