package com.acktos.easylaundry;

import android.app.DialogFragment;
import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.acktos.easylaundry.models.Category;
import com.acktos.easylaundry.models.Clothing;
import com.acktos.easylaundry.providers.EasyLaundryProvider;
import com.acktos.easylaundry.util.QtySelectDialog;

import org.json.JSONArray;
import org.json.JSONException;


public class ClothesActivity extends ActionBarActivity implements
        QtySelectDialog.QuantityChangeListener,
        LoaderManager.LoaderCallbacks<Cursor> {



    public static final String SHARED_PREFERENCES ="com.acktos.SHARED_PREFERENCES";
    public static final String SHARED_CLOTHING_QTY ="com.acktos.CLOTHING_QTY";
    public static final String SHARED_CLOTHING_ITEMS ="com.acktos.CLOTHING_ITEMS";

    //ANDROID UTILS
    public static SharedPreferences mPrefs;// Handle to SharedPreferences for this APP
    public static SharedPreferences.Editor mEditor;// Handle to a SharedPreferences editor
    private ClothesCursorAdapter clothesAdapter;

    public static  ActionBar actionBar;

    TextView qtyClothes;
    ListView clothesList;

    //ATTRIBUTES
    int items=0;
    public static int price;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clothes);
        clothesList=(ListView)findViewById(R.id.list_clothes);

        actionBar=getSupportActionBar();

        //get shared preferences
        mPrefs = getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);// Open Shared Preferences
        mEditor = mPrefs.edit();// Get an editor

        clothesAdapter = new ClothesCursorAdapter(this,null,0);
        clothesList.setAdapter(clothesAdapter);

        //cursorAdapter.swap

        //set click handler to categories list
        clothesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Cursor cursor = (Cursor) parent.getItemAtPosition(position);
                String categoryId= cursor.getString(cursor.getColumnIndex(LaundryContract.CategoriesTable._ID));
                String categoryThumbnail= cursor.getString(cursor.getColumnIndex(LaundryContract.CategoriesTable.COLUMN_NAME_THUMBNAIL));

                DialogFragment qtyDialog=QtySelectDialog.newInstance(categoryId,categoryThumbnail);
                qtyDialog.show(getFragmentManager(), "qtyDialogFragment");

            }
        });

        Bundle args=getIntent().getExtras();
        if(args!=null){
            getLoaderManager().initLoader(0, args, this);
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_categories, menu);

        MenuItem menuItem=menu.findItem(R.id.action_basket);
        MenuItemCompat.setActionView(menuItem, R.layout.shoppingcar_icon);
        final View shoppingcart = menu.findItem(R.id.action_basket).getActionView();

        qtyClothes = (TextView) shoppingcart.findViewById(R.id.qty_clothes);
        qtyClothes.setText(String.valueOf(items));
        qtyClothes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ClothesActivity.this, OrderActivity.class);
                startActivity(i);
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.basket) {
            Intent i=new Intent(this,OrderActivity.class);
            startActivity(i);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onPostResume();
        showItemsNumber();
    }

    private void showItemsNumber(){

        String jsonClothings;
        JSONArray jsonArray;

        jsonClothings= mPrefs.getString(SHARED_CLOTHING_ITEMS,null);

        if(jsonClothings!=null && !TextUtils.isEmpty(jsonClothings)){
            try {

                jsonArray=new JSONArray(jsonClothings);

            } catch (JSONException e) {
                jsonArray=new JSONArray();
            }
        }else{
            jsonArray=new JSONArray();
        }

        items= jsonArray.length();
        invalidateOptionsMenu();
    }

    public static void addItemToBasket(String clothingId){

        String jsonClothings;
        JSONArray jsonArray;

        jsonClothings= mPrefs.getString(SHARED_CLOTHING_ITEMS,null);

        if(jsonClothings!=null && !TextUtils.isEmpty(jsonClothings)){
            try {

                jsonArray=new JSONArray(jsonClothings);
                jsonArray.put(clothingId);

            } catch (JSONException e) {
                jsonArray=new JSONArray();
                jsonArray.put(clothingId);
            }
        }else{
            jsonArray=new JSONArray();
            jsonArray.put(clothingId);
        }


        mEditor.putString(SHARED_CLOTHING_ITEMS, jsonArray.toString());
        Log.i("json shopping cart:",jsonArray.toString());
        mEditor.commit();

    }


    @Override
    public void onQuantityChange(String clothingId) {

        addItemToBasket(clothingId);
        showItemsNumber();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        String categoryId=args.getString(Clothing.KEY_CATEGORY_ID);

        Uri uriClothesPerCategory=Uri.parse("content://" +
                EasyLaundryProvider.AUTHORITY + "/" +
                LaundryContract.ClothesTable.TABLE_NAME+"/"+
                LaundryContract.CategoriesTable.TABLE_NAME+"/"+
                categoryId);

        return new CursorLoader(this, uriClothesPerCategory, null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

       clothesAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        clothesAdapter.swapCursor(null);
    }
}
