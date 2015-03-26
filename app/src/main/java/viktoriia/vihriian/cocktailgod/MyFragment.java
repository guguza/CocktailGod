package viktoriia.vihriian.cocktailgod;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;

public class MyFragment extends ListFragment {
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setListAdapter(new CocktailsAdapter(CocktailsListActivity.myContext, readCocktailsFromDB()));

        ListView lv = getListView();
    }

    public void onListItemClick(ListView listView, View view, int pos, long id) {

        Intent intent = new Intent(getActivity(), DetailsActivity.class);
        intent.putExtra("image", CocktailsListActivity.cocktailsArr.get(pos).imageURL);
        intent.putExtra("name", CocktailsListActivity.cocktailsArr.get(pos).name);
        intent.putExtra("ingredients", CocktailsListActivity.cocktailsArr.get(pos).ingredients);
        intent.putExtra("instructions", CocktailsListActivity.cocktailsArr.get(pos).instructions);
        startActivity(intent);
    }

    public ArrayList<String> readCocktailsFromDB() {
        if (CocktailsListActivity.myDataBase == null) {
            Log.w(CocktailsListActivity.TAG, "Database doesn't exist");
        }
        Cursor cursor = CocktailsListActivity.myDataBase.query(CocktailsListActivity.myDbHelper.TABLE_NAME_1, null,
                null,
                null,
                null,
                null,
                null
        );

        for (int i = 0; cursor.moveToNext(); i++) {
            int id = cursor.getInt(cursor.getColumnIndex(CocktailsListActivity.myDbHelper.ID));
            String name = cursor.getString(cursor
                    .getColumnIndex(CocktailsListActivity.myDbHelper.COCKTAIL_NAME));
            String ingredients = cursor.getString(cursor
                    .getColumnIndex(CocktailsListActivity.myDbHelper.INGREDIENTS));
            String instructions = cursor.getString(cursor
                    .getColumnIndex(CocktailsListActivity.myDbHelper.INSTRUCTIONS));

            CocktailsListActivity.cocktailsArr.add(new Cocktail(getActivity().getBaseContext(), id, name, ingredients, instructions));

        }
        cursor.close();

        ArrayList<String> namesArr = new ArrayList<String>();
        for (int k = 0; k < CocktailsListActivity.cocktailsArr.size(); k++) {
            namesArr.add(CocktailsListActivity.cocktailsArr.get(k).name.substring(0, CocktailsListActivity.cocktailsArr.get(k).name.length() - 1));

        }
        readImagesFromDB();

        return namesArr;
    }

    private boolean readImagesFromDB() {
        if (CocktailsListActivity.myDataBase == null) {
            Log.w(CocktailsListActivity.TAG, "Database doesn't exist");
            return false;
        }
        Cursor cursor = CocktailsListActivity.myDataBase.query(CocktailsListActivity.myDbHelper.TABLE_NAME_2, null,
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
                    .getColumnIndex(CocktailsListActivity.myDbHelper.IMAGE));
            name = cursor.getString(cursor
                    .getColumnIndex(CocktailsListActivity.myDbHelper.COCKTAIL_NAME));
            for (Cocktail cocktail : CocktailsListActivity.cocktailsArr) {
                if (cocktail.name.equals(name)) {
                    cocktail.imageURL = "http://www.barbook.ru" + image;
                    continue;
                }
            }
        }
        cursor.close();
        return true;
    }
}