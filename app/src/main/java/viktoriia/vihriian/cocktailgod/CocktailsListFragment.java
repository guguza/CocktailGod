package viktoriia.vihriian.cocktailgod;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.recyclerview.animators.adapters.SlideInRightAnimationAdapter;

/**
 * Created by Администратор on 11.04.2015.
 */
public class CocktailsListFragment extends Fragment {

    ArrayList<Cocktail> cocktailsArr;
    CAdapter adapter;
    RecyclerView rv;
    Context myContext;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        myContext = getActivity().getApplicationContext();
        cocktailsArr = new ArrayList<Cocktail>();
        adapter = new CAdapter(cocktailsArr);
    }
    @Override
    public View onCreateView(LayoutInflater li, ViewGroup container, Bundle bundle) {

        setHasOptionsMenu(true);
        View rootView = li.inflate(R.layout.fragment_cocktails_list, container, false);

        rv = (RecyclerView) rootView.findViewById(R.id.rv);
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
                        Intent intent = new Intent(getActivity(), DetailsActivity.class);
                        intent.putExtra("image", adapter.cocktails.get(pos).imageURL);
                        intent.putExtra("name", adapter.cocktails.get(pos).name);
                        intent.putExtra("ingredients", adapter.cocktails.get(pos).ingredients);
                        intent.putExtra("instructions", adapter.cocktails.get(pos).instructions);
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

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        if(!adapter.setNewDataset())
            Toast.makeText(myContext, "Ничего не найдено по запросу!",
                    Toast.LENGTH_SHORT).show();
        rv.swapAdapter(adapter, false);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        inflater.inflate(R.menu.cocktails_list, menu);
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
    }
}
