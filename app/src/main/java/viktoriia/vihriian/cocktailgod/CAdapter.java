package viktoriia.vihriian.cocktailgod;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Locale;

//import dreamers.graphics.RippleDrawable;

public class CAdapter extends RecyclerView.Adapter<CAdapter.CViewHolder> {

    ArrayList<Cocktail> cocktails;
    ArrayList<Cocktail> cocktailsDemo;
    private ImageLoader imgLoader;
    Filter filter;
   // ColorStateList stateList;

    public CAdapter(ArrayList<Cocktail> cocktails) {
        cocktailsDemo = new ArrayList<Cocktail>();
        this.cocktails = new ArrayList<Cocktail>();
     //   this.cocktails.addAll(cocktails);
        imgLoader = new ImageLoader(CocktailsListActivity.myContext);
        filter = Filter.getInstance();
        //stateList = CocktailsListActivity.myContext.getResources().getColorStateList(R.color.example_colors_state);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public boolean find(String query) {
        query = query.toLowerCase(Locale.getDefault());
        cocktails.clear();

        if(filter.currentFilters[0].equals(Filter.FILTER_BY_NAME))
            return findByName(query);
        if(filter.currentFilters[0].equals(Filter.FILTER_BY_INGREDIENTS))
            return findByIngredients(query);
        return false;
    }

    private boolean findByName(String query) {
        int counter = 0;
        if(query.length() == 0) {
            cocktails.addAll(cocktailsDemo);
        } else {
            for(Cocktail cocktail: cocktailsDemo) {
                if(cocktail.name.toLowerCase(Locale.getDefault()).contains(query)) {
                    cocktails.add(cocktail);
                    counter++;
                }
            }
            if(counter == 0) {
                return false;
            }
        }
        return true;
    }

    private boolean findByIngredients(String query) {
        int counter = 0;
        if(query.length() == 0) {
            cocktails.addAll(cocktailsDemo);
        } else {
            for(Cocktail cocktail: cocktailsDemo) {
                if(cocktail.ingredients.toLowerCase(Locale.getDefault()).contains(query)) {
                    cocktails.add(cocktail);
                    counter++;
                }
            }
            if(counter == 0) {
                return false;
            }
        }
        return true;
    }

    public void setNewDataset() {
        cocktailsDemo.clear();
        cocktails.clear();
        for(int i = 0; i < CocktailsListActivity.cocktailsArr.size(); i++) {
            if(filterBy(filter.currentFilters[4], CocktailsListActivity.cocktailsArr.get(i).difficulty)) {
                cocktailsDemo.add(CocktailsListActivity.cocktailsArr.get(i));
            }
        }
        cocktails.addAll(cocktailsDemo);
    }

    private boolean filterBy(String query, String currAttribute){
            if(query.equals("Не выбрано") || currAttribute.equals(query)) {
                return true;
                }
        return false;

    }

    @Override
    public CViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row, parent, false);
        CViewHolder holder = new CViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(CViewHolder holder, int pos) {
        String[] ing = CocktailsListActivity.toIngredientsList(cocktails.get(pos).ingredients);
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
        if(cocktails.get(pos).imageURL == null) {
            cocktails.get(pos).imageURL = "http://www.barbook.ru/upload/" +
                    "cocktails2/2001c5cfd1b9cc99112cf33d10dc0a77_80x110.png";
        }

        imgLoader.DisplayImage(cocktails.get(pos).imageURL, holder.icon);

        holder.name.setText(cocktails.get(pos).name.substring(0, cocktails.get(pos).name.length() - 1));

        int rate = 1;
        if(cocktails.get(pos).difficulty.equals("Средне")) {
            rate = 2;
        }
        if(cocktails.get(pos).difficulty.equals("Сложно")) {
            rate = 3;
        }
        holder.level.setRating(rate);
        //holder.bindBackground();
    }

    @Override
    public int getItemCount() {
        return cocktails.size();
    }

    public class CViewHolder extends RecyclerView.ViewHolder{
        CardView cv;
        ImageView icon;
        TextView name;
        TextView ingredients;
        RatingBar level;

        public CViewHolder(View v) {
            super(v);
            cv = (CardView) v.findViewById(R.id.cv);
            icon = (ImageView) v.findViewById(R.id.icon);
            name = (TextView) v.findViewById(R.id.name);
            ingredients = (TextView) v.findViewById(R.id.ingredients);
            level = (RatingBar) v.findViewById(R.id.ratingBar);
        }

        public void bindBackground() {
            cv.setClickable(true);
           // RippleDrawable.makeFor(cv, stateList, true);
        }
    }
}
