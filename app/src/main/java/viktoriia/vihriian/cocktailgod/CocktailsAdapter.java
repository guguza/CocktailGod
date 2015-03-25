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

import com.nostra13.universalimageloader.core.ImageLoader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

/**
 * Created by Администратор on 23.03.2015.
 */

public class CocktailsAdapter extends ArrayAdapter<String> {
    Context myContext;
    ImageLoader imageLoader;
    ViewHolder holder;

    public CocktailsAdapter(Context context, ArrayList<String> objects) {
        super(context, R.layout.row, R.id.name, objects);
       // imageLoader = ImageLoader.getInstance();
       // imageLoader.init(ImageLoaderConfiguration.createDefault(getContext()));

    }
    @Override
    public View getView(int pos, View v, ViewGroup parent) {
        View row = super.getView(pos, v, parent);

        holder = (ViewHolder) row.getTag();
        if (holder==null) {
            holder=new ViewHolder(row);
            row.setTag(holder);
        }
        String[] ing = CocktailsListActivity.toIngredientsList(CocktailsListActivity.cocktailsArr.get(pos).ingredients);
        StringBuilder temp = new StringBuilder();
        for(int i = 0; i < ing.length; i++) {
            if(ing[i] == null) {
                temp.append("...");
                break;
            }
            if(i != 0) ing[i] = ing[i].toLowerCase();
            temp.append(ing[i].subSequence(0, ing[i].length()-1));
            temp.append((i != ing.length-1)? ", " : "...");
        }
        holder.ingredients.setText(temp);
        if(CocktailsListActivity.cocktailsArr.get(pos).imageURL == null) {
            CocktailsListActivity.cocktailsArr.get(pos).imageURL = "http://www.barbook.ru/upload/" +
                    "cocktails2/2001c5cfd1b9cc99112cf33d10dc0a77_80x110.png";
        }
        new DownloadImageTask(holder.icon)
                .execute(CocktailsListActivity.cocktailsArr.get(pos).imageURL);

        int rate = 1;
        if(CocktailsListActivity.cocktailsArr.get(pos).difficulty.equals("Средне")) {
            rate = 2;
        }
        if(CocktailsListActivity.cocktailsArr.get(pos).difficulty.equals("Сложно")) {
            rate = 3;
        }
        holder.level.setRating(rate);

        return(row);
    }

}

