package viktoriia.vihriian.cocktailgod;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.ArrayList;


public class CocktailsAdapter extends ArrayAdapter<String> {
    Context myContext;
    ViewHolder holder;
    private ImageLoader imgLoader;

    public CocktailsAdapter(Context context, ArrayList<String> objects) {
        super(context, R.layout.row, R.id.name, objects);
        imgLoader = new ImageLoader(context);
       // imageLoader = ImageLoader.getInstance();
       // imageLoader.init(ImageLoaderConfiguration.createDefault(getContext()));

    }
    @Override
    public View getView(int pos, View v, ViewGroup parent) {
        View row = super.getView(pos, v, parent);

        holder = (ViewHolder) row.getTag();
        if (holder==null) {
            holder=new ViewHolder(row);
            row.setTag(holder);
        }
        String[] ing = CocktailsListActivity.toIngredientsList(CocktailsListActivity.cocktailsArr.get(pos).ingredients);
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
        if(CocktailsListActivity.cocktailsArr.get(pos).imageURL == null) {
            CocktailsListActivity.cocktailsArr.get(pos).imageURL = "http://www.barbook.ru/upload/" +
                    "cocktails2/2001c5cfd1b9cc99112cf33d10dc0a77_80x110.png";
        }
        /*new DownloadImageTask(holder.icon)
                .execute(CocktailsListActivity.cocktailsArr.get(pos).imageURL);*/
        imgLoader.DisplayImage(CocktailsListActivity.cocktailsArr.get(pos).imageURL, holder.icon);

        int rate = 1;
        if(CocktailsListActivity.cocktailsArr.get(pos).difficulty.equals("Средне")) {
            rate = 2;
        }
        if(CocktailsListActivity.cocktailsArr.get(pos).difficulty.equals("Сложно")) {
            rate = 3;
        }
        holder.level.setRating(rate);

        return(row);
    }

}

