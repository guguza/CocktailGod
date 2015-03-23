package viktoriia.vihriian.cocktailgod;

import android.app.ListActivity;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

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

        try {

            myDataBase = myDbHelper.openDataBase();

        }catch(SQLException sqle){

            throw new Error("Unable to open database");

        }

        setListAdapter(new CocktailsAdapter(myContext, readCocktailsFromDB()));

        ListView lv = getListView();
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
            namesArr.add(cocktailsArr.get(k).name);
        }
        readImagesFromDB();
/*
        // Создаем адаптер
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, namesArr);
        dataAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dataAdapter.notifyDataSetChanged();
        spin.setAdapter(dataAdapter);*/
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
