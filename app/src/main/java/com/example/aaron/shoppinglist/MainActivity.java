package com.example.aaron.shoppinglist;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       // final Toast toast = Toast.makeText(getApplication(), "Main", Toast.LENGTH_SHORT);

        ImageButton btnGroceryList = (ImageButton) findViewById(R.id.grocery_list_button);
        ImageButton btnPantryList = (ImageButton) findViewById(R.id.pantry_button);
        ImageButton btnRecipeBook = (ImageButton) findViewById(R.id.recipe_book_button);
        ImageButton btnCalendar = (ImageButton) findViewById(R.id.calendar_button);

        btnGroceryList.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                Intent nextScreen = new Intent(getApplicationContext(), GroceryList.class);
                startActivity(nextScreen);
            }
        });

        btnPantryList.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                Intent nextScreen = new Intent(getApplicationContext(), Pantry.class);
                startActivity(nextScreen);
            }
        });

        btnRecipeBook.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                Intent nextScreen = new Intent(getApplicationContext(), RecipeBook.class);
                startActivity(nextScreen);
            }
        });

        btnCalendar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                Intent nextScreen = new Intent(getApplicationContext(), Calendar.class);
                startActivity(nextScreen);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        Intent nextScreen;

        switch (item.getItemId()) {
            case R.id.budget:
                nextScreen = new Intent(getApplicationContext(), Budget.class);
                startActivity(nextScreen);
                return true;
            case R.id.add_item:
                nextScreen = new Intent (getApplicationContext(), AddItemActivity.class);
                startActivity(nextScreen);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
