package viktoriia.vihriian.cocktailgod;

import android.content.Context;
import android.util.Log;

/**
 * Created by Администратор on 06.03.2015.
 */
public class Cocktail {

    int id;
    String name;
    String ingredients;
    String instructions;
    String difficulty;
    Context context;
    String imageURL;

    public Cocktail() {

    }
    public Cocktail(Context context, int id, String name, String ingredients, String instructions) {
        this.context = context;
        this.id = id;
        this.name = name;
        this.ingredients = ingredients;
        this.instructions = instructions;
        setDifficulty();
    }

    private boolean setDifficulty() throws NullPointerException{
        try {
            int l = instructions.length();
            if (l <= 100) {
                difficulty = context.getResources().getString(R.string.level1);
                return true;
            }
            if (l <= 150) {
                difficulty = context.getResources().getString(R.string.level2);
                return true;
            }
            difficulty = context.getResources().getString(R.string.level3);
            return true;
        } catch(NullPointerException NPe) {
            Log.e(NPe.getMessage(), "Instructions are not found!");
            return false;
        }
    }
}
