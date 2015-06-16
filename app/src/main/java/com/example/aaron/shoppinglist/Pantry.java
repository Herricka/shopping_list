package com.example.aaron.shoppinglist;

import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

/**
 * Created by Aaron on 5/2/2015.
 */
public class Pantry extends ActionBarActivity {

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.pantry_ab, menu);
        setTitle("My Pantry");
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        final Toast toast = Toast.makeText(getApplication(), "Main", Toast.LENGTH_SHORT);
        switch (item.getItemId()) {
            // Sort
            case R.id.menu_item_checked:
                toast.setText("Sort By Checked");
                toast.show();
                return true;
            case R.id.menu_item_atoz:
                toast.setText("Sorted A to Z");
                toast.show();
                return true;
            case R.id.menu_item_ztoa:
                toast.setText("Sorted Z to A");
                toast.show();
                return true;
            case R.id.exp_date_sooner:
                toast.setText("Sorted Expiration date First to Last");
                toast.show();
                return true;
            case R.id.exp_date_later:
                toast.setText("Sorted Expiration date Last to First");
                toast.show();
                return true;
            case R.id.menu_item_producttype:
                toast.setText("Sorted by Product Type");
                toast.show();
                return true;
            case R.id.menu_item_store:
                toast.setText("Sorted by Store");
                toast.show();
                return true;
            // Options
            case R.id.menu_item_new_item:
                toast.setText("Add New Item");
                toast.show();
                return true;
            case R.id.menu_item_check_all:
                toast.setText("Check All Items");
                toast.show();
                return true;
            case R.id.menu_item_uncheck_all:
                toast.setText("Uncheck All Items");
                toast.show();
                return true;
            case R.id.menu_item_delete_checked:
                toast.setText("Delete Checked Items");
                toast.show();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}

