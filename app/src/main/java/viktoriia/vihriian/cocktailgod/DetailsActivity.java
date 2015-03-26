package viktoriia.vihriian.cocktailgod;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

/**
 * Created by Администратор on 23.03.2015.
 */
public class DetailsActivity extends Activity{
    ImageView image;
    TextView name, ingredients, instructions;
    private ImageLoader imgLoader;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details);

        image = (ImageView) findViewById(R.id.cocktail);
        String imageUrl = getIntent().getStringExtra("image");

        /*new DownloadImageTask(image)
                .execute(imageUrl);*/
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
}
