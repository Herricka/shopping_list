package com.example.aaron.shoppinglist;

import android.app.ExpandableListActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Aaron on 4/24/2015.
 */
public class GroceryList extends ExpandableListActivity {
    private String sortBy = DataHandler.PRIMARY_KEY_DH;
    private String sortDirection = DataHandler.ASC_DH;
    private int ParentClickStatus = -1;
    private int ChildClickStatus = -1;
    private ArrayList<Parent> parents;
    Intent intent = getIntent();
    DataHandler listHandler;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.grocery_list_ab, menu);
        setTitle("My List");
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            // Sort
            case R.id.menu_item_checked:
                sortBy = DataHandler.CHECKED_DH;
                sortDirection = DataHandler.ASC_DH;
                loadHosts(buildDummyData());
                return true;
            case R.id.menu_item_atoz:
                sortBy = DataHandler.PRIMARY_KEY_DH;
                sortDirection = DataHandler.ASC_DH;
                loadHosts(buildDummyData());
                return true;
            case R.id.menu_item_ztoa:
                sortBy = DataHandler.PRIMARY_KEY_DH;
                sortDirection = DataHandler.DESC_DH;
                loadHosts(buildDummyData());
                return true;
            case R.id.menu_item_lowtohigh:
                sortBy = DataHandler.PRICE_DH;
                sortDirection = DataHandler.ASC_DH;
                loadHosts(buildDummyData());
                return true;
            case R.id.menu_item_hightolow:
                sortBy = DataHandler.PRICE_DH;
                sortDirection = DataHandler.DESC_DH;
                loadHosts(buildDummyData());
                return true;
            case R.id.menu_item_producttype:
                sortBy = DataHandler.PRODUCT_TYPE_DH;
                sortDirection = DataHandler.ASC_DH;
                loadHosts(buildDummyData());
                return true;
            case R.id.menu_item_store:
                sortBy = DataHandler.ON_SALE_DH;
                sortDirection = DataHandler.ASC_DH;
                loadHosts(buildDummyData());
                return true;
            // Options
            case R.id.menu_item_new_item:
                Intent add = new Intent(GroceryList.this, AddItemActivity.class);
                startActivity(add);
                return true;
            case R.id.menu_item_check_all:
                listHandler.open();
                listHandler.toggleCheckAll(DataHandler.TABLE_NAME_DH, 1);
                listHandler.close();
                ((MyExpandableListAdapter) getExpandableListAdapter()).notifyDataSetChanged();
                return true;
            case R.id.menu_item_uncheck_all:
                listHandler.open();
                listHandler.toggleCheckAll(DataHandler.TABLE_NAME_DH, 0);
                listHandler.close();
                ((MyExpandableListAdapter) getExpandableListAdapter()).notifyDataSetChanged();
                return true;
            case R.id.menu_item_delete_checked:
                listHandler.open();
                listHandler.deleteAllChecked(DataHandler.TABLE_NAME_DH);
                listHandler.close();
                loadHosts(buildDummyData());
                ((MyExpandableListAdapter) getExpandableListAdapter()).notifyDataSetChanged();

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

       @Override
       protected void onCreate (Bundle savedInstanceState){
           super.onCreate(savedInstanceState);

           Resources myResource = this.getResources();
           Drawable divider = myResource.getDrawable(R.drawable.line);

           //Set ExpandableListView values
           getExpandableListView().setGroupIndicator(null);
           getExpandableListView().setDivider(divider);
           getExpandableListView().setChildDivider(divider);
           getExpandableListView().setDividerHeight(1);
           registerForContextMenu(getExpandableListView());

           //Creating static data in ArrayList
           final ArrayList<Parent> dummyList = buildDummyData();

           //Adding ArrayList data to ExpandableListView values
           loadHosts(dummyList);

           //The following lines will display a verification count of the number of items in the list.
           //int i = parents.size();
           //Toast.makeText(getApplicationContext(), "ARRAY COUNT: "+Integer.toString(i), Toast.LENGTH_SHORT).show();
       }


       //Code to read from database

       private ArrayList<Parent> buildDummyData() {
           //Creating ArrayList of type Parent class to store Parent class objects
           final ArrayList<Parent> dummy_list = new ArrayList<Parent>();

           listHandler = new DataHandler(getBaseContext());
           listHandler.open();

           //Cursor getCursor = listHandler.getListCount(DataHandler.GROCERY_LIST);
           //Cursor getCursor = listHandler.databaseDataHandler.rawQuery("SELECT "+DataHandler.FOOD_NAME_DH+" FROM "+DataHandler.GROCERY_LIST, null);
           //int listCount = getCursor.getCount();
           //Cursor getParentCursor = listHandler.selectAllRows(DataHandler.TABLE_NAME_DH);

           Cursor getParentCursor = listHandler.selectAndOrder(DataHandler.TABLE_NAME_DH, sortBy, sortDirection);

           int listCount = listHandler.getRowCount(getParentCursor);
           //The following line will display a verification message of the total # of items in the list
           //Toast.makeText(getApplicationContext(), "LIST COUNT: "+Integer.toString(listCount), Toast.LENGTH_SHORT).show();

           String foodName[];
           String foodKey[];
           String foodQuantity[];
           String childName[] = {"Quantity", "OnSaleAt", "Price", "Type", "Notes"};
           foodName = new String[listCount];
           foodKey = new String[listCount];
           foodQuantity = new String[listCount];

           if (getParentCursor.moveToFirst()) {
               int parent_index = 0;


               do {
                   final Parent parentObject = new Parent();
                   int tempColID = getParentCursor.getColumnIndex(DataHandler.FOOD_NAME_DH);
                   foodName[parent_index] = getParentCursor.getString(tempColID);
                   foodKey[parent_index] = getParentCursor.getString(getParentCursor.getColumnIndex(DataHandler.PRIMARY_KEY_DH));
                   foodQuantity[parent_index] = getParentCursor.getString(getParentCursor.getColumnIndex(DataHandler.QUANTITY_DH));
                   parentObject.setName(foodKey[parent_index]);
                   parentObject.setText1(foodName[parent_index]);
                   parentObject.setQuantity(foodQuantity[parent_index]);

                   parentObject.setChildren(new ArrayList<Child>());

                   String childLabels[] = {"Qty: ", "Store: ", "Price: $", "Type: ", "Notes: "};

                   int child_index = 0;

                   Cursor getChildCursor = listHandler.selectChildRows(DataHandler.TABLE_NAME_DH, foodKey[parent_index]);
                   int field_index = getChildCursor.getColumnIndex(DataHandler.QUANTITY_DH);
                   getChildCursor.moveToFirst();


                   do {
                       final Child childObject = new Child();
                       String tempName = childName[child_index] + Integer.toString(parent_index);
                       childObject.setName(tempName);
                       String tempText = childLabels[child_index] + getChildCursor.getString(field_index);
                       childObject.setText1(tempText);
                       parentObject.getChildren().add(childObject);
                       child_index++;
                       field_index++;
                   } while (child_index < 5);

                   dummy_list.add(parentObject);
                   parent_index++;

               } while (getParentCursor.moveToNext());

           } else {
               Toast.makeText(getApplicationContext(), "There are no items in your list, yet.", Toast.LENGTH_SHORT).show();
               //Else no items exist in list
           }

           return dummy_list;

   }

    private void loadHosts(final ArrayList<Parent> newParents) {
        if (newParents == null)
            return;

        parents = newParents;

        //Check for ExpandableListAdapter object
        if (this.getExpandableListAdapter() == null) {
            //Create ELA object
            final MyExpandableListAdapter mAdapter = new MyExpandableListAdapter();

            //Set Adapter to ELA
            this.setListAdapter(mAdapter);

        }
        else {
            //Refresh ExpandableListView data
            ((MyExpandableListAdapter)getExpandableListAdapter()).notifyDataSetChanged();
        }

    }

    /**
     * A custom adapter to create Parent view (using grouprow.xml) and Child view (using childrow.xml).
     */

    private class MyExpandableListAdapter extends BaseExpandableListAdapter {
        private LayoutInflater inflater;

        public MyExpandableListAdapter() {
            inflater = LayoutInflater.from(GroceryList.this);
        }

/*		private void toggleCheckAll() {
			for(int i = 0; i < parents.size(); i++ ) {
				MyExpandableListAdapter.CheckUpdateListener.this.onCheckedChanged(pButtonView, pIsChecked)

			}
		}*/

        //This Function is used to inflate parent rows view

        @Override
        public View getGroupView(int pGroupPosition, boolean pIsExpanded, View pConvertView, ViewGroup pParentView) {
            final Parent parent = parents.get(pGroupPosition);
            //Toast.makeText(getApplicationContext(), "Parent is :"+parent.getName(), Toast.LENGTH_SHORT).show();

            //Inflate grouprow.xml file for parent rows
            pConvertView = inflater.inflate(R.layout.grouprow, pParentView, false);

            //Get grouprow.xml file elements and set values
            ((TextView) pConvertView.findViewById(R.id.text1)).setText(parent.getText1());
            ((TextView) pConvertView.findViewById(R.id.quantity)).setText(parent.getQuantity());

            //Get grouprow.xml file checkbox values from the database, set checked status accordingly
            CheckBox checkbox = (CheckBox) pConvertView.findViewById(R.id.checkbox);

            listHandler.open();
            if(listHandler.existsTable(DataHandler.TABLE_NAME_DH)) {
                Boolean myBool = listHandler.checkChecked(DataHandler.TABLE_NAME_DH,parent.getName());
                //Toast.makeText(getApplicationContext(), "Checked is: "+myBool.toString(), Toast.LENGTH_SHORT).show();
                parent.setChecked(myBool);
            }
            listHandler.close();

            checkbox.setChecked(parent.isChecked());

            //Set CheckUpdateListener for CheckBox
            checkbox.setOnCheckedChangeListener(new CheckUpdateListener(parent));

            return pConvertView;
        }

        //This Function is used to inflate child rows view
        @Override
        public View getChildView(int pGroupPosition, int pChildPosition, boolean pIsLastChild, View pConvertView, ViewGroup pParentView) {
            final Parent parent = parents.get(pGroupPosition);
            final Child child = parent.getChildren().get(pChildPosition);

            //Inflate childrow.xml file for child rows
            pConvertView = inflater.inflate(R.layout.childrow, pParentView, false);

            //Get childrow.xml file elements and set values
            ((TextView) pConvertView.findViewById(R.id.text1)).setText(child.getText1());
            //ImageView image = (ImageView) pConvertView.findViewById(R.id.image);
            //image.setImageResource(getResources().getIdentifier("com.databasedemo.:drawable/setting"+parent.getName(), null, null));

            return pConvertView;
        }

        @Override
        public Object getChild(int pGroupPosition, int pChildPosition) {
            //Log.i("Childs", groupPosition+"= getChild =="+childPosition);
            return parents.get(pGroupPosition).getChildren().get(pChildPosition);
        }

        //Call when child row clicked
        @Override
        public long getChildId(int pGroupPosition, int pChildPosition) {
            /****** When Child row click then call this function ******/

            //Log.i("Noise", "parent == "+groupPosition+"= child : =="+childPosition);
            if(ChildClickStatus!=pChildPosition) {
                ChildClickStatus = pChildPosition;
                //Toast.makeText(getApplicationContext(), "Parent :"+pGroupPosition + " Child :"+pChildPosition, Toast.LENGTH_LONG).show();

            }
            return pChildPosition;
        }

        @Override
        public int getChildrenCount(int pGroupPosition) {
            int size=0;
            if(parents.get(pGroupPosition).getChildren()!=null)
                size = parents.get(pGroupPosition).getChildren().size();
            return size;
        }

        @Override
        public Object getGroup(int pGroupPosition) {
            Log.i("Parent", pGroupPosition + "= getGroup ");

            return parents.get(pGroupPosition);
        }

        @Override
        public int getGroupCount() {
            return parents.size();
        }

        //Call when parent row clicked
        @Override
        public long getGroupId(int pGroupPosition) {
            //Log.i("Parent", pGroupPosition+"= getGroupId "+ParentClickStatus);

            if(pGroupPosition==2 && ParentClickStatus!=pGroupPosition) {
                //Alert to user
                //Toast.makeText(getApplicationContext(), "Parent :"+pGroupPosition , Toast.LENGTH_LONG).show();
            }

            if(ParentClickStatus != pGroupPosition) {
                ParentClickStatus = pGroupPosition;
            }

            if(ParentClickStatus >= 0)
                ParentClickStatus = -1;
            return pGroupPosition;

        }

        @Override
        public void notifyDataSetChanged() {
            //Refresh List rows
            super.notifyDataSetChanged();
        }

        @Override
        public boolean isEmpty() {
            return ((parents == null) || parents.isEmpty());
        }

        @Override
        public boolean isChildSelectable(int pGroupPosition, int pChildPosition) {
            return true;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

        @Override
        public boolean areAllItemsEnabled() {
            return true;
        }


        /******************* CheckBox Checked Change Listener ********************/

        private final class CheckUpdateListener implements OnCheckedChangeListener {
            private final Parent parent;

            private CheckUpdateListener(Parent pParent)
            {
                this.parent = pParent;
            }

            public void onCheckedChanged(CompoundButton pButtonView, boolean pIsChecked)
            {
                int checkedBool;
                String parentName = parent.getName();
                //Log.i("onCheckedChanged", "isChecked: "+pIsChecked);
                parent.setChecked(pIsChecked);

                ((MyExpandableListAdapter)getExpandableListAdapter()).notifyDataSetChanged();

                final Boolean isChecked = parent.isChecked();
                if(isChecked==true) {
                    checkedBool = 1;
                }
                else {
                    checkedBool = 0;
                }

                listHandler.open();
                listHandler.updateChecked(parentName, checkedBool);
                Cursor getCursor = listHandler.selectRowByKey(DataHandler.TABLE_NAME_DH, parentName);
                getCursor.moveToFirst();


/*             	Confirmation of checkbox click:
                String bool = getCursor.getString(getCursor.getColumnIndex(DataHandler.CHECKED_DH)).toString();
                Toast.makeText(getApplicationContext(),parentName + " " + (isChecked ? STR_CHECKED : STR_UNCHECKED) + " " + bool,
                           Toast.LENGTH_SHORT).show();*/

                listHandler.close();
            }
        }
        /***********************************************************************/





    }

}