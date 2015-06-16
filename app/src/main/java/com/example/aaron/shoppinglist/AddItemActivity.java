package com.example.aaron.shoppinglist;

/**
 * Created by Aaron on 6/2/2015.
 */

import android.content.Intent;
import android.database.SQLException;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.Locale;


public class AddItemActivity extends ActionBarActivity {
    public static String nameText;

    DataHandler listHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);
        //Create the object to handle using the database
        listHandler = new DataHandler(getBaseContext());

        EditText nameET = (EditText) findViewById(R.id.food_name);

        if (nameText != null) {
            nameET.setText(nameText);
            nameText = null;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.add_item_ab, menu);
        setTitle("Add Item(s)");
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent nextScreen;
        switch (item.getItemId()) {
            case R.id.menu_item_addupdate:
                addItem();
                return true;
            case R.id.menu_item_gotolist:
                nextScreen = new Intent(getApplicationContext(), GroceryList.class);
                startActivity(nextScreen);
                return true;

            case R.id.menu_item_clearall:

                EditText nameET = (EditText) findViewById(R.id.food_name);
                EditText qtyET = (EditText) findViewById(R.id.quantity);
                EditText onSaleAtET = (EditText) findViewById(R.id.on_sale_at);
                EditText priceET = (EditText) findViewById(R.id.price);
                Spinner typeET = (Spinner) findViewById(R.id.product_type);
                EditText notesET = (EditText) findViewById(R.id.notes);
                nameET.setText(null);
                qtyET.setText(null);
                onSaleAtET.setText(null);
                priceET.setText(null);
                typeET.setSelection(0);
                notesET.setText(null);

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


    public void addItem() {
        //Called from the action bar
        //Get the string values from the relevant fields and insert or update the db table with the info
        EditText nameET = (EditText) findViewById(R.id.food_name);
        EditText qtyET = (EditText) findViewById(R.id.quantity);
        EditText onSaleAtET = (EditText) findViewById(R.id.on_sale_at);
        EditText priceET = (EditText) findViewById(R.id.price);
        Spinner typeET = (Spinner) findViewById(R.id.product_type);
        EditText notesET = (EditText) findViewById(R.id.notes);

        String userStrings[];
        userStrings = new String[7];
        userStrings[0] = nameET.getText().toString().toUpperCase(Locale.US).trim();
        userStrings[1] = nameET.getText().toString().trim();
        userStrings[2] = qtyET.getText().toString().trim();
        userStrings[3] = onSaleAtET.getText().toString().trim();
        userStrings[4] = priceET.getText().toString().trim();
        userStrings[5] = typeET.getSelectedItem().toString().trim();
        userStrings[6] = notesET.getText().toString().trim();

        //Can't have nulls in some DB fields. Change nulls to zero-length empty strings.
        for(int i = 0; i <= 6; i++) {
            if(userStrings[i] == null) {
                userStrings[i] = "";
            }
        }

        listHandler.open();
        //Check to make sure the Food Name field isn't empty before trying to add it to the table
        if(!userStrings[0].isEmpty()) {
            try {
                listHandler.insertData(userStrings[0], userStrings[1], userStrings[2], userStrings[3], userStrings[4], userStrings[5], userStrings[6]);
                Toast.makeText(AddItemActivity.this, R.string.add_item_toast, Toast.LENGTH_SHORT).show();
            }
            catch(SQLException e)
            {
                //If insertion fails, the item already exists and should be updated instead
                listHandler.updateFoodName(userStrings[0], userStrings[1], userStrings[2], userStrings[3], userStrings[4], userStrings[5], userStrings[6]);
                Toast.makeText(AddItemActivity.this, R.string.update_item_toast, Toast.LENGTH_SHORT).show();
            }
        }
        listHandler.close();
    }

    public void openList(View pView) {
        Intent listIntent = new Intent(this, GroceryList.class);
        startActivity(listIntent);
    }


/*	DO NOT DELETE THESE COMMENTS, THEY MUST BE ALTERED FOR NEW LAYOUT:

	public void searchByKey(View v) {
		EditText text = (EditText) findViewById(R.id.myTextField);
		String displayString = "";

		String myString = text.getText().toString();
		if(myString == null) {
			myString = "blank";
		}
		String myStringKey = myString.toUpperCase(Locale.US);

		listHandler.open();
		Cursor myCursor = listHandler.selectRowByKey(DataHandler.TABLE_NAME_DH, myStringKey);
		if(myCursor.moveToFirst()) {
			int limit = myCursor.getColumnCount();
			int i = 0;
			do {
				displayString += myCursor.getString(i)+", ";
				i++;
				limit--;
			} while(limit>0);
		}
//		int colID = myCursor.getColumnIndex(DataHandler.PRICE_DH);
//		Toast.makeText(getBaseContext(), Integer.toString(colID), Toast.LENGTH_SHORT).show();
		Toast.makeText(getBaseContext(), displayString, Toast.LENGTH_SHORT).show();
		listHandler.close();



	}

	public void deleteByKey(View v) {
		EditText text = (EditText) findViewById(R.id.myTextField);
		String displayString = "";

		String myString = text.getText().toString();
		if(myString == null) {
			myString = "blank";
		}
		String myStringKey = myString.toUpperCase(Locale.US);

		listHandler.open();
		listHandler.deleteRowByKey(DataHandler.TABLE_NAME_DH, myStringKey);
		listHandler.close();
	}
*/

}