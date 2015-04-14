package viktoriia.vihriian.cocktailgod;

import android.content.ContentValues;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Администратор on 23.03.2015.
 */
public class DetailsActivity extends ActionBarActivity{
    ImageView image;
    TextView name, ingredients, instructions;
    private ImageLoader imgLoader;
    Toolbar toolbar;
    int favourite;
    MenuItem favouritesItem;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details);

        favourite = getIntent().getIntExtra("favourite", 0);

        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);

        image = (ImageView) findViewById(R.id.cocktail);
        String imageUrl = getIntent().getStringExtra("image");

        imgLoader = new ImageLoader(this);
        imgLoader.DisplayImage(imageUrl, image);
        name = (TextView) findViewById(R.id.name);
        name.setText(getIntent().getStringExtra("name"));

        ingredients = (TextView) findViewById(R.id.ingredients);
        String ing = getIntent().getStringExtra("ingredients");
        ingredients.setText(ing.substring(0, ing.length()-2));

        instructions = (TextView) findViewById(R.id.instructions);
        instructions.setText(getIntent().getStringExtra("instructions").replace(":", ":\n"));


    }

    public void goBack(View v) {
    finish();
}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.details_menu, menu);

        favouritesItem = menu.findItem(R.id.action_favourites);
        if(favourite == 1) {
            favouritesItem.setIcon(R.drawable.ic_favourite_active);
            return true;
        }
        favouritesItem.setIcon(R.drawable.ic_favourite_unactive);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_favourites) {
            if(favourite == 1){
                favourite = 0;
                favouritesItem.setIcon(R.drawable.ic_favourite_unactive);
            } else {
                favourite = 1;
                favouritesItem.setIcon(R.drawable.ic_favourite_active);
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause() {
        super.onPause();
        int id = getIntent().getExtras().getInt("id");
        ContentValues cv = new ContentValues();
        cv.put("favourite", favourite);
        CocktailsListActivity.myDataBase.update("cocktails", cv, "_id"+ "="+ id, null);
        for(Cocktail item: CocktailsListActivity.cocktailsArr) {
            if(item.id == id) {
                item.favourite = favourite;
                break;
            }
        }
    }
}
