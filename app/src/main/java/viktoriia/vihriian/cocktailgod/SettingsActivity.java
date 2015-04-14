package viktoriia.vihriian.cocktailgod;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;


public class SettingsActivity extends ActionBarActivity {

    Toolbar toolbar;
    RecyclerView recyclerView;
    Context context = this;

    private final String[] sort = {"По имени", "По ингредиенту"};
    private final String[] category         =   {"Не выбрано", "Шоты", "Лонги", "Тини", "Горячие"};
    private final String[] mainIngredient   =   {"Не выбрано", "Водка", "Ром", "Виски", "Вино", "Пиво",
            "Портвейн", "Коньяк", "Текила"};
    private final String[] strength         =   {"Не выбрано", "Безалкогольный", "Слабоалкогольный", "Крепкий"};
    private final String[] difficulty       =   {"Не выбрано", "Легко", "Средне", "Сложно"};
    private final String[] taste          =   {"Не выбрано", "Кислосладкие", "Кислые", "Сладкие", "С горчинкой", "Пряные",
            "Острые", "Фруктовые", "Ягодные", "Сливочные"};

    String[][] settingsAll = {sort, category, mainIngredient, strength, difficulty, taste};
    Map<String, String[]> map;
    Filter filter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        toolbar = (Toolbar) findViewById(R.id.tb_settings);
        toolbar.setTitle("Фильтры");
        setSupportActionBar(toolbar);
        filter = Filter.getInstance();
        fillMap();

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
                            Intent intent = new Intent(SettingsActivity.this, CocktailsListActivity.class);
                            startActivity(intent);
                            finish();
                        }  else if(position == 2) {
                            Filter.favourites = 1;
                            Intent intent = new Intent(SettingsActivity.this, CocktailsListActivity.class);
                            startActivity(intent);
                            finish();
                        }else if(position == 6) {
                            Intent intent = new Intent(SettingsActivity.this, AboutActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                })
                .build();

        final SettingsAdapter adapter = new SettingsAdapter();
        recyclerView = (RecyclerView) findViewById(R.id.rv_settings);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(llm);
        recyclerView.setAdapter(adapter);
        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(context, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int p) {

                Animation fadeIn = new AlphaAnimation(0, 1);
                fadeIn.setDuration(300);
                view.startAnimation(fadeIn);
                final Dialog dialog = new Dialog(context);
                final int pos = p;
                dialog.setContentView(R.layout.settings_dialog_layout);
                dialog.setTitle(filter.titleArray[pos]);
                ListView list = (ListView) dialog.findViewById(R.id.lv);
                ArrayAdapter<String> dialAdapter =
                        new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1,
                                map.get(filter.titleArray[pos])) {
                            @Override
                            public View getView(int position, View convertView, ViewGroup parent) {
                                View view = super.getView(position, convertView, parent);
                                TextView text1 = (TextView) view.findViewById(android.R.id.text1);
                                text1.setTextColor(getResources().getColor(R.color.ColorPrimary));
                                return view;
                            }

                        };
                list.setAdapter(dialAdapter);
                list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                        TextView tv1 = (TextView) view.findViewById(android.R.id.text1);
                        filter.currentFilters[pos] = tv1.getText().toString();
                        dialog.dismiss();
                        adapter.notifyDataSetChanged();
                    }
                });
                dialog.show();
            }
        }));

    }


    private void fillMap() {
        map = new TreeMap<String, String[]>();
        for(int i = 0; i < filter.titleArray.length; i++) {
            map.put(filter.titleArray[i], settingsAll[i]);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();


        return super.onOptionsItemSelected(item);
    }
}
