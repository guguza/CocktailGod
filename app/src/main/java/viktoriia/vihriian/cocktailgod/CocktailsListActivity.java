package viktoriia.vihriian.cocktailgod;

import android.app.Activity;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.io.IOException;
import java.util.ArrayList;


public class CocktailsListActivity extends Activity {

    Spinner spin;
    ArrayList<Cocktail> cocktailsArr;
    SQLiteDatabase myDataBase;
    CocktailDBHelper myDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cocktails_list);
        spin = (Spinner) findViewById(R.id.spinner);

        myDbHelper = new CocktailDBHelper(this);

        try {

            myDbHelper.createDataBase();

        } catch (IOException ioe) {

            throw new Error("Unable to create database");

        }

        try {

            myDataBase = myDbHelper.openDataBase();

        }catch(SQLException sqle){

            throw new Error("Unable to open database");

        }

        readCocktailsFromDB();
    }

    private boolean readCocktailsFromDB() {
        Cursor cursor = myDataBase.query(myDbHelper.TABLE_NAME_1, new String[]{
                        myDbHelper.ID, myDbHelper.COCKTAIL_NAME, myDbHelper.INGREDIENTS, myDbHelper.INSTRUCTIONS},
                null,
                null,
                null,
                null,
                null
        );

        cocktailsArr = new ArrayList<Cocktail>();

        for (int i = 0; cursor.moveToNext(); i++) {
            int id = cursor.getInt(cursor.getColumnIndex(myDbHelper.ID));
            String name = cursor.getString(cursor
                    .getColumnIndex(myDbHelper.COCKTAIL_NAME));
            String ingredients = cursor.getString(cursor
                    .getColumnIndex(myDbHelper.INGREDIENTS));
            String instructions = cursor.getString(cursor
                    .getColumnIndex(myDbHelper.INSTRUCTIONS));

            cocktailsArr.add(new Cocktail(this, id, name, ingredients, instructions));
            // Создаем адаптер
            ArrayAdapter<Cocktail> dataAdapter = new ArrayAdapter<Cocktail>(this,
                    android.R.layout.simple_spinner_item, cocktailsArr);

            dataAdapter
                    .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            dataAdapter.notifyDataSetChanged();
            spin.setAdapter(dataAdapter);
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
