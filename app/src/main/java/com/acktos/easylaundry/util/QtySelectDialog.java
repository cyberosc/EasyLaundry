package com.acktos.easylaundry.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.acktos.easylaundry.R;

/**
 * Created by OSCAR ACKTOS on 14/04/2015.
 */
public class QtySelectDialog extends DialogFragment {

    private View rootView;
    private TextView qtyView;
    private ImageButton arrowUp;
    private ImageButton arrowDown;
    private ImageView clothingImage;

    private String clothingId;
    private String clothingResource;

    private QuantityChangeListener qtyChangelistener;
    public static final String SHARED_PREFERENCES ="com.acktos.SHARED_PREFERENCES";
    public static final String SHARED_CLOTHING_QTY ="com.acktos.CLOTHING_QTY";
    public static final String SHARED_CLOTHING_PRICE ="com.acktos.CLOTHING_PRICE";

    //ANDROID UTILS
    public static SharedPreferences mPrefs;// Handle to SharedPreferences for this APP
    public static SharedPreferences.Editor mEditor;// Handle to a SharedPreferences editor

    public static QtySelectDialog newInstance(String clothingId,int clothingResource) {

        QtySelectDialog qtySelectDialog = new QtySelectDialog();

        // Supply index input as an argument.
        Bundle args = new Bundle();
        args.putString("clothing_id", clothingId);
        args.putInt("clothing_resource", clothingResource);

        qtySelectDialog.setArguments(args);

        return qtySelectDialog;
    }

    @Override
    public void onAttach(Activity activity){
        super.onAttach(activity);
        if(activity instanceof QuantityChangeListener){
            qtyChangelistener=(QuantityChangeListener) activity;
        }else{
            throw new ClassCastException(activity.toString()+" must implement OnDataChangeListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle SaveInstanceState){

        clothingId=getArguments().getString("clothing_id");
        clothingResource=getArguments().getString("clothing_resource");


        LayoutInflater inflater=getActivity().getLayoutInflater();
        rootView=inflater.inflate(R.layout.qty_selector_dialog, null);

        qtyView=(TextView)rootView.findViewById(R.id.qty_select_items);
        arrowDown=(ImageButton)rootView.findViewById(R.id.btn_down_qty);
        arrowUp=(ImageButton)rootView.findViewById(R.id.btn_up_qty);
        clothingImage=(ImageView)rootView.findViewById(R.id.qty_select_image);

        arrowUp.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                int currentQty=Integer.parseInt(qtyView.getText().toString());

                int newQty=currentQty+1;

                qtyView.setText(Integer.toString(newQty));

            }
        });

        arrowDown.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                int currentQty=Integer.parseInt(qtyView.getText().toString());
                if(currentQty>1){
                    int newQty=currentQty-1;
                    qtyView.setText(Integer.toString(newQty));
                }

            }
        });



        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        builder.setTitle("Select your quantity");
        builder.setView(rootView);
        builder.setPositiveButton("Add to basket", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                qtyChangelistener.onQuantityChange(Integer.parseInt(qtyView.getText().toString()));
            }
        });

        builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                QtySelectDialog.this.getDialog().cancel();
            }
        });

        final AlertDialog alertDialog=builder.create();

        return alertDialog;
    }



    // interface to communicate with host Activity
    public interface QuantityChangeListener{
        void onQuantityChange(int qtyItems);
    }
}
