package com.example.aaron.shoppinglist;

import android.support.v7.app.ActionBarActivity;
import android.view.Menu;

/**
 * Created by Aaron on 5/2/2015.
 */
public class RecipeBook extends ActionBarActivity {

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.recipe_book_ab, menu);
        setTitle("My Recipe Book");
        return super.onCreateOptionsMenu(menu);
    }
}
