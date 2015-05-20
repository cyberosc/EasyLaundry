package com.acktos.easylaundry;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.app.DialogFragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.acktos.easylaundry.util.QtySelectDialog;

import java.util.ArrayList;


public class SubCategoryActivity extends ActionBarActivity implements QtySelectDialog.QuantityChangeListener {


    private ListView subCategoriesList;
    public static final String SHARED_PREFERENCES ="com.acktos.SHARED_PREFERENCES";
    public static final String SHARED_CLOTHING_QTY ="com.acktos.CLOTHING_QTY";
    public static final String SHARED_CLOTHING_PRICE ="com.acktos.CLOTHING_PRICE";

    //ANDROID UTILS
    public static SharedPreferences mPrefs;// Handle to SharedPreferences for this APP
    public static SharedPreferences.Editor mEditor;// Handle to a SharedPreferences editor

    public static  ActionBar actionBar;
    public static int price;
    TextView qtyClothes;
    int items=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_category);
        subCategoriesList=(ListView)findViewById(R.id.list_sub_categories);

        actionBar=getSupportActionBar();

        //get shared preferences
        mPrefs = getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);// Open Shared Preferences
        mEditor = mPrefs.edit();// Get an editor

        ArrayList<Clothing> clothings=new ArrayList<Clothing>();

        clothings.add(new Clothing(R.drawable.circle_suit_2_pieces,"2 PIECE SUIT","$ 11"));
        clothings.add(new Clothing(R.drawable.circle_suit_3_pieces,"3 PIECE SUIT","$ 14"));
        clothings.add(new Clothing(R.drawable.circle_dinner_suite,"DINNER SUIT","$ 13"));
        clothings.add(new Clothing(R.drawable.circle_waistcoat,"WAISTCOAT","$ 10"));



        CategoryAdapter categoryAdapter = new CategoryAdapter(this, clothings);
        subCategoriesList.setAdapter(categoryAdapter);

        //set click handler to categories list
        subCategoriesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,int position, long id) {

                Object item=parent.getItemAtPosition(position);
                Clothing clothingItem=(Clothing)item;

                //Toast.makeText(CardListActivity.this, getString(R.string.set_payment_success),Toast.LENGTH_SHORT).show();

               // Intent i=new Intent(SubCategoryActivity.this,SubCategoryActivity.class);
               // startActivity(i);

                price=Integer.parseInt(clothingItem.price.substring(2,clothingItem.price.length()));

                Log.i("price", clothingItem.price.substring(2, clothingItem.price.length()));
                DialogFragment qtyDialog=QtySelectDialog.newInstance("1",1);
                qtyDialog.show(getFragmentManager(), "newCarDialog");


            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_categories, menu);

        final View shoppingcart = menu.findItem(R.id.action_basket).getActionView();
        qtyClothes = (TextView) shoppingcart.findViewById(R.id.qty_clothes);
        qtyClothes.setText(String.valueOf(items));
        qtyClothes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(SubCategoryActivity.this,OrderActivity.class);
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

        items= mPrefs.getInt(SHARED_CLOTHING_QTY, 0);
        invalidateOptionsMenu();
    }

    public static void addItemToBasket(int qty){

        int totalQty=0;
        int currentQty=0;

        currentQty= mPrefs.getInt(SHARED_CLOTHING_QTY, 0);
        totalQty=currentQty+qty;

        mEditor.putInt(SHARED_CLOTHING_QTY, totalQty);

        mEditor.commit();

    }


    @Override
    public void onQuantityChange(int qtyItems) {

        addItemToBasket(qtyItems);
        showItemsNumber();
    }
}
