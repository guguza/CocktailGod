package viktoriia.vihriian.cocktailgod;

import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

/**
 * Created by Администратор on 23.03.2015.
 */
public class ViewHolder {
    ImageView icon;
    TextView ingredients;
    RatingBar level;

    ViewHolder(View row) {
        ingredients = (TextView) row.findViewById(R.id.ingredients);
        icon = (ImageView) row.findViewById(R.id.icon);
        level = (RatingBar) row.findViewById(R.id.ratingBar);
    }
}
