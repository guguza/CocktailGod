package viktoriia.vihriian.cocktailgod;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;

import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;


public class AboutActivity extends ActionBarActivity {
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        toolbar.setTitle("О программе");
        setSupportActionBar(toolbar);

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
                        if(position == 0){
                            Filter.favourites = 0;
                            Intent intent = new Intent(AboutActivity.this, CocktailsListActivity.class);
                            startActivity(intent);
                            finish();
                        } else if(position == 2) {
                            Filter.favourites = 1;
                            Intent intent = new Intent(AboutActivity.this, CocktailsListActivity.class);
                            startActivity(intent);
                            finish();
                        }else if(position == 4) {
                            Intent intent = new Intent(AboutActivity.this, SettingsActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                })
                .build();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_about, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }
}
