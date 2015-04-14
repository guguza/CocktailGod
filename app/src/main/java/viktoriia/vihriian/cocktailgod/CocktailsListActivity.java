package viktoriia.vihriian.cocktailgod;


import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.Toast;

import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import jp.wasabeef.recyclerview.animators.adapters.SlideInRightAnimationAdapter;


public class CocktailsListActivity extends ActionBarActivity {

    public static ArrayList<Cocktail> cocktailsArr;
    static SQLiteDatabase myDataBase;
    static CocktailsDBHelper myDbHelper;
    static final String TAG = "myLogs";
    static Context myContext;
    Toolbar toolbar;
    CAdapter adapter;
    RecyclerView rv;
    Random random;

    @Override
    public void onResume() {
        super.onResume();
        if(!adapter.setNewDataset())
            Toast.makeText(myContext, "Ничего не найдено по запросу!",
                    Toast.LENGTH_SHORT).show();
        rv.swapAdapter(adapter, false);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        myDbHelper.close();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cocktails_list);

        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);

        myContext = CocktailsListActivity.this;
        cocktailsArr = new ArrayList<Cocktail>();

        myDbHelper = new CocktailsDBHelper(this);
        myDbHelper.createDataBase();

        try {

            myDataBase = myDbHelper.openDataBase();

        } catch (SQLException sqle) {

            throw new Error("Unable to open database");

        }
        readCocktailsFromDB();



        adapter = new CAdapter(cocktailsArr);
        rv = (RecyclerView)findViewById(R.id.rv);
        rv.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(myContext);
        rv.setLayoutManager(llm);
        rv.addOnItemTouchListener(
                new RecyclerItemClickListener(myContext, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int pos) {
                        Animation fadeIn = new AlphaAnimation(0, 1);
                        fadeIn.setDuration(300);
                        view.startAnimation(fadeIn);
                        Intent intent = new Intent(CocktailsListActivity.this, DetailsActivity.class);
                        intent.putExtra("image", adapter.cocktails.get(pos).imageURL);
                        intent.putExtra("name", adapter.cocktails.get(pos).name);
                        intent.putExtra("ingredients", adapter.cocktails.get(pos).ingredients);
                        intent.putExtra("instructions", adapter.cocktails.get(pos).instructions);

                        intent.putExtra("favourite", adapter.cocktails.get(pos).favourite);
                        intent.putExtra("id", adapter.cocktails.get(pos).id);
                        startActivity(intent);

                    }
                })
        );

        int sdk = android.os.Build.VERSION.SDK_INT;
        if (sdk > 10){
            rv.setAdapter(new SlideInRightAnimationAdapter(adapter));
        } else{
            rv.setAdapter(adapter);
        }
       // rv.setItemAnimator(new DefaultItemAnimator());
        Drawer.Result result = new Drawer()
                .withActivity(this)
                .withToolbar(toolbar)
                .withHeader(R.layout.material_drawer_layout)
                .addDrawerItems(
                        new PrimaryDrawerItem().withName("Все коктейли"),
                        new DividerDrawerItem(),
                        new PrimaryDrawerItem().withName("Избранное"),
                        new DividerDrawerItem(),
                        new PrimaryDrawerItem().withName("Настройки"),
                        new DividerDrawerItem(),
                        new SecondaryDrawerItem().withName("О программе")
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id, IDrawerItem drawerItem) {
                        // do something with the clicked item :D
                        if(position == 0) {
                            Filter.favourites = 0;
                            if(!adapter.setNewDataset())
                                Toast.makeText(myContext, "Ничего не найдено по запросу!",
                                        Toast.LENGTH_SHORT).show();
                            rv.swapAdapter(adapter, false);
                        }
                        if(position == 2) {
                            Filter.favourites = 1;
                            if(!adapter.setNewDataset())
                                Toast.makeText(myContext, "Ничего не найдено по запросу!",
                                        Toast.LENGTH_SHORT).show();
                            rv.swapAdapter(adapter, false);
                        }
                        if(position == 4){
                            Intent intent = new Intent(CocktailsListActivity.this, SettingsActivity.class);
                            startActivity(intent);
                            finish();
                        } else if(position == 6) {
                            Intent intent = new Intent(CocktailsListActivity.this, AboutActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                })
                .build();
    }

    public ArrayList<String> readCocktailsFromDB() {
        if (myDataBase == null) {
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
            int favourite = cursor.getInt(cursor.getColumnIndex(myDbHelper.FAVOURITES));

            cocktailsArr.add(new Cocktail(this, id, name, ingredients, instructions, favourite));

        }
        cursor.close();

        ArrayList<String> namesArr = new ArrayList<String>();
        for (int k = 0; k < cocktailsArr.size(); k++) {
            namesArr.add(cocktailsArr.get(k).name.substring(0, cocktailsArr.get(k).name.length() - 1));

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
        for (int i = 0; i < lol.length; i++) {
            if (i % 2 != 0) {
                dest[k++] = lol[i];
                if (k == 3) {
                    break;
                }
            }
        }
        return dest;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.cocktails_list, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);

        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener(){

            @Override
            public boolean onQueryTextSubmit(String s) {
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                if(!adapter.find(s))
                    Toast.makeText(CocktailsListActivity.myContext, "Ничего не найдено по запросу!",
                            Toast.LENGTH_SHORT).show();
                rv.swapAdapter(adapter, false);
                return true;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id) {
            case R.id.action_random:
                random = new Random();
                int pos = random.nextInt(adapter.cocktails.size());
                Intent intent = new Intent(CocktailsListActivity.this, DetailsActivity.class);
                intent.putExtra("image", adapter.cocktails.get(pos).imageURL);
                intent.putExtra("name", adapter.cocktails.get(pos).name);
                intent.putExtra("ingredients", adapter.cocktails.get(pos).ingredients);
                intent.putExtra("instructions", adapter.cocktails.get(pos).instructions);
                intent.putExtra("favourite", adapter.cocktails.get(pos).favourite);
                intent.putExtra("id", adapter.cocktails.get(pos).id);
                startActivity(intent);
                return true;
            case R.id.action_search:
                return true;

        }
        return super.onOptionsItemSelected(item);
    }
}


