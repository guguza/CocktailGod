package viktoriia.vihriian.cocktailgod;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.UsingFreqLimitedMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.utils.StorageUtils;

import java.io.File;
import java.util.ArrayList;


public class CocktailsListActivity extends ListActivity {

    public static ArrayList<Cocktail> cocktailsArr;
    SQLiteDatabase myDataBase;
    CocktailsDBHelper myDbHelper;
    static final String TAG = "myLogs";
    static Context myContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myContext = CocktailsListActivity.this;
        cocktailsArr = new ArrayList<Cocktail>();
        myDbHelper = new CocktailsDBHelper(this);

        myDbHelper.createDataBase();


        File cacheDir = StorageUtils.getCacheDirectory(myContext, true);
        ImageLoaderConfiguration config;
        config = new ImageLoaderConfiguration.Builder(getApplicationContext())
                .memoryCacheExtraOptions(480, 800) // width, height
                .threadPoolSize(5)
                .threadPriority(Thread.MIN_PRIORITY + 2)
                .denyCacheImageMultipleSizesInMemory()
                .memoryCache(new UsingFreqLimitedMemoryCache(2 * 1024 * 1024)) // 2 Mb
                .discCache(new UnlimitedDiscCache(cacheDir))
                .discCacheFileNameGenerator(new HashCodeFileNameGenerator())
                .imageDownloader(new BaseImageDownloader(myContext, 5 * 1000, 30 * 1000)) // connectTimeout (5 s), readTimeout (30 s)
                .defaultDisplayImageOptions(DisplayImageOptions.createSimple())
                .build();
        try {

            myDataBase = myDbHelper.openDataBase();

        }catch(SQLException sqle){

            throw new Error("Unable to open database");

        }

        setListAdapter(new CocktailsAdapter(myContext, readCocktailsFromDB()));

        ListView lv = getListView();
        lv.setLongClickable(true);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> arg0, View arg1,
                                           int pos, long id) {
               Intent intent = new Intent(CocktailsListActivity.this, DetailsActivity.class);
               intent.putExtra("image", cocktailsArr.get(pos).imageURL);
               intent.putExtra("name",cocktailsArr.get(pos).name);
               intent.putExtra("ingredients",cocktailsArr.get(pos).ingredients);
               intent.putExtra("instructions", cocktailsArr.get(pos).instructions);
               startActivity(intent);
            }
        });
    }

    private ArrayList<String> readCocktailsFromDB() {
        if(myDataBase == null) {
            Log.w(TAG, "Database doesn't exist");
        }
        Cursor cursor = myDataBase.query(myDbHelper.TABLE_NAME_1, null,
                null,
                null,
                null,
                null,
                null
        );

        for (int i = 0; cursor.moveToNext(); i++) {
            int id = cursor.getInt(cursor.getColumnIndex(myDbHelper.ID));
            String name = cursor.getString(cursor
                    .getColumnIndex(myDbHelper.COCKTAIL_NAME));
            String ingredients = cursor.getString(cursor
                    .getColumnIndex(myDbHelper.INGREDIENTS));
            String instructions = cursor.getString(cursor
                    .getColumnIndex(myDbHelper.INSTRUCTIONS));

            cocktailsArr.add(new Cocktail(this, id, name, ingredients, instructions));

         }
        cursor.close();

        ArrayList<String> namesArr = new ArrayList<String>();
        for (int k = 0; k < cocktailsArr.size(); k++) {
             namesArr.add(cocktailsArr.get(k).name.substring(0, cocktailsArr.get(k).name.length()-1));

        }
        readImagesFromDB();

        return namesArr;
    }

    private boolean readImagesFromDB() {
        if (myDataBase == null) {
            Log.w(TAG, "Database doesn't exist");
            return false;
        }
        Cursor cursor = myDataBase.query(myDbHelper.TABLE_NAME_2, null,
                null,
                null,
                null,
                null,
                null
        );
        String image;
        String name;
        for (int i = 0; cursor.moveToNext(); i++) {
            image = cursor.getString(cursor
                    .getColumnIndex(myDbHelper.IMAGE));
            name = cursor.getString(cursor
                    .getColumnIndex(myDbHelper.COCKTAIL_NAME));
            for (Cocktail cocktail : cocktailsArr) {
                if (cocktail.name.equals(name)) {
                    cocktail.imageURL = "http://www.barbook.ru" + image;
                    continue;
                }
            }
        }
        cursor.close();
        return true;
    }

    public static String[] toIngredientsList(String resource) {
        String[] lol = resource.split("[\\r\\n\\—]+");
        String[] dest = new String[3];
        int k = 0;
        for(int i = 0; i < lol.length; i++){
            if(i % 2 != 0) {
                dest[k++] = lol[i];
                if(k == 3) {
                    break;
                }
            }
        }
        return dest;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.cocktails_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
