package com.example.aaron.shoppinglist;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;



public class DataHandler {

    public static final String PRIMARY_KEY_DH = "_id";
    public static final String FOOD_NAME_DH = "FoodName";
    public static final String ON_SALE_DH = "OnSaleAt";
    public static final String CHECKED_DH = "Checked";
    public static final String PRICE_DH = "Price";
    public static final String PRODUCT_TYPE_DH = "ProductType";
    public static final String QUANTITY_DH = "Quantity";
    public static final String TABLE_NAME_DH = "GroceryListTable";
    public static final String NOTES_DH = "Notes";
    public static final String ASC_DH = "ASC";
    public static final String DESC_DH = "DESC";
    public static final String DATABASE_NAME_DH ="GroceryDatabase";
    public static final int DATABASE_VERSION_DH = 1;
    public static final int USER_FIELDS = 6;
    public static final String TABLE_CREATE_DH =
            "create table "+TABLE_NAME_DH+
                    " ("+PRIMARY_KEY_DH+" text primary key not null, "+
                    FOOD_NAME_DH+" text not null, "+
                    QUANTITY_DH+" text, "+
                    ON_SALE_DH+" text, "+
                    PRICE_DH+" text, "+
                    PRODUCT_TYPE_DH+" text, "+
                    NOTES_DH+" text, "+
                    CHECKED_DH+" integer not null);";


    DataBaseHelper dbHelperInnerClass;
    Context contextDataHandler;
    SQLiteDatabase databaseDataHandler;

    public DataHandler(Context pDataHandlerContext)
    {
        this.contextDataHandler = pDataHandlerContext;
        dbHelperInnerClass = new DataBaseHelper(pDataHandlerContext);
    }


    private static class DataBaseHelper extends SQLiteOpenHelper {

        public DataBaseHelper(Context pDBHelperContext)
        {
            super(pDBHelperContext, DATABASE_NAME_DH, null, DATABASE_VERSION_DH);
        }

        @Override
        public void onCreate(SQLiteDatabase pDBOnCreate) {
            // TODO Auto-generated method stub

            try{
                pDBOnCreate.execSQL(TABLE_CREATE_DH);
            }
            catch(SQLException e)
            {
                e.printStackTrace();
            }
        }


        @Override
        public void onUpgrade(SQLiteDatabase pDBOnUpgrade, int pOldVersion, int pNewVersion) {
            // TODO Auto-generated method stub

            pDBOnUpgrade.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME_DH+";");
            onCreate(pDBOnUpgrade);
        }

    }

    public DataHandler open()
    {
        databaseDataHandler = dbHelperInnerClass.getWritableDatabase();
        return this;
    }

    public void close()
    {
        dbHelperInnerClass.close();
    }

    public long insertData(String pPrimaryKey, String pFoodName, String pQuantity, String pOnSaleAt, String pPrice, String pProductType, String pNotes)
    {
        ContentValues content = new ContentValues();
        int bool = 0;
        content.put(PRIMARY_KEY_DH, pPrimaryKey);
        content.put(FOOD_NAME_DH, pFoodName);
        content.put(QUANTITY_DH, pQuantity);
        content.put(ON_SALE_DH, pOnSaleAt);
        content.put(PRICE_DH, pPrice);
        content.put(PRODUCT_TYPE_DH, pProductType);
        content.put(NOTES_DH, pNotes);
        content.put(CHECKED_DH, bool);
        return databaseDataHandler.insertOrThrow(TABLE_NAME_DH, null, content);
    }


    public void updateFoodName(String pPrimaryKey, String pFoodName, String pQuantity, String pOnSaleAt, String pPrice, String pProductType, String pNotes)
    {
        ContentValues content = new ContentValues();
        content.put(PRIMARY_KEY_DH, pPrimaryKey);
        content.put(FOOD_NAME_DH, pFoodName);
        content.put(QUANTITY_DH, pQuantity);
        content.put(ON_SALE_DH, pOnSaleAt);
        content.put(PRICE_DH, pPrice);
        content.put(PRODUCT_TYPE_DH, pProductType);
        content.put(NOTES_DH, pNotes);
        databaseDataHandler.update(TABLE_NAME_DH, content, "_id='"+pPrimaryKey+"'", null);
    }

    public void updateChecked(String pPrimaryKey, int pCheckedBool) {
        ContentValues content = new ContentValues();
        content.put(CHECKED_DH, pCheckedBool);
        databaseDataHandler.update(TABLE_NAME_DH, content,"_id='"+pPrimaryKey+"'", null);
    }

    public Cursor returnData()
    {
        return databaseDataHandler.query(TABLE_NAME_DH, new String[] {FOOD_NAME_DH,ON_SALE_DH,CHECKED_DH,}, null, null, null, null, null);
    }


    public Cursor returnList(String pFoodStr) {
        return databaseDataHandler.query(TABLE_NAME_DH, new String[] {FOOD_NAME_DH,}	, pFoodStr, null, null, null, null);
    }

    public Cursor getListCount(String pTableName) {
        return databaseDataHandler.rawQuery("SELECT COUNT(*) FROM "+pTableName, null);
    }

    public int getRowCount(String pTableName, String pFieldName) {
        Cursor getCursor = selectRowsOnField(pTableName, pFieldName);
        int cursorCount = getCursor.getCount();
        return cursorCount;
    }

    public int getRowCount(Cursor pSelectCursor) {
        int cursorCount = pSelectCursor.getCount();
        return cursorCount;
    }

    public Cursor selectRowsOnField(String pTableName, String pFieldName) {
        return databaseDataHandler.rawQuery("SELECT "+pFieldName+" FROM "+pTableName, null);
    }

    public Cursor selectRowByKey(String pTableName, String pPrimaryKey) {
        return databaseDataHandler.rawQuery("SELECT * FROM "+pTableName+" WHERE "+PRIMARY_KEY_DH+"='"+pPrimaryKey+"'", null);
    }

    public void deleteRowByKey(String pTableName, String pPrimaryKey) {
        String whereString = PRIMARY_KEY_DH+"='"+pPrimaryKey+"'";
        databaseDataHandler.delete(TABLE_NAME_DH, whereString, null);
    }

    public Cursor selectAllRows(String pTableName) {
        return databaseDataHandler.rawQuery("SELECT * FROM "+pTableName+" ORDER BY "+PRIMARY_KEY_DH, null);
    }

    public Cursor selectAndOrder(String pTableName, String pOrderBy, String pDirection) {
        return databaseDataHandler.rawQuery("SELECT * FROM "+pTableName+" ORDER BY "+pOrderBy+" "+pDirection, null);
    }

    public Cursor selectChildRows(String pTableName, String pPrimaryKey) {
        return databaseDataHandler.rawQuery("SELECT Quantity, OnSaleAt, Price, ProductType, Notes FROM "+pTableName+" WHERE "+PRIMARY_KEY_DH+"='"+pPrimaryKey+"'", null);
    }

    public void deleteAllChecked(String pTableName) {
        String whereString = CHECKED_DH+"=1";
        databaseDataHandler.delete(pTableName, whereString, null);
    }

    public boolean checkChecked(String pTableName, String pPrimaryKey) {
        Cursor myCursor = databaseDataHandler.rawQuery("SELECT "+CHECKED_DH+" FROM "+pTableName+" WHERE "+PRIMARY_KEY_DH+"='"+pPrimaryKey+"'", null);
        if(myCursor.moveToFirst()) {
            myCursor.moveToFirst();
            Integer testInt = myCursor.getInt(myCursor.getColumnIndex(CHECKED_DH));
            if ( testInt == 0) {
                return false;
            }
            else {
                return true;
            }
        }
        else {
            return false;
        }
    }

    public void toggleCheckAll(String pTableName, Integer pCheckedBool) {
        Cursor myCursor = databaseDataHandler.rawQuery("SELECT "+CHECKED_DH+" FROM "+pTableName, null);
        ContentValues content = new ContentValues();
        if(myCursor.moveToFirst()) {
            do {
                content.put(CHECKED_DH, pCheckedBool);
                databaseDataHandler.update(TABLE_NAME_DH, content,null, null);
            } while(myCursor.moveToNext());
        }

    }

    public boolean existsTable(String pTableName) {
        Cursor myCursor = databaseDataHandler.rawQuery("SELECT * FROM "+pTableName, null);
        if(myCursor.moveToFirst()){
            return true;
        }
        else {
            return false;
        }
    }
}
