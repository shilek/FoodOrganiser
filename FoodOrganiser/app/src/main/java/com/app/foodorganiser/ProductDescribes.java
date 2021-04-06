package com.app.foodorganiser;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatDialogFragment;

public class ProductDescribes extends AppCompatDialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
        Bundle bundle = getArguments();
        String [] values = new String[3];

        if(bundle!=null) {
            values[0] = "Proteins: " + bundle.getInt("protein");
            values[1] = "Carbohydrates: " + bundle.getInt("carbohydrates");
            values[2] = "Fats: " + bundle.getInt("fats");

            @SuppressLint("InflateParams")
            View view =  LayoutInflater.from(getActivity()).inflate(R.layout.activity_popup_product,null);
            dialog.setView(view);

            TextView name = view.findViewById(R.id.productName);
            TextView protein = view.findViewById(R.id.productProtein);
            TextView fats = view.findViewById(R.id.productFats);
            TextView carbon = view.findViewById(R.id.productCarbon);
            ImageView image = view.findViewById(R.id.productImage);

            name.setText(bundle.getString("name"));
            protein.setText(values[0]);
            carbon.setText(values[1]);
            fats.setText(values[2]);
            //TODO: setImage
            //placeholder
            image.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.placeholder));

        }
        else {
            dialog.setTitle("Error");
            dialog.setMessage("Something is not quite right");
            dialog.setPositiveButton("OK", (dialog1, which) -> {
            });
        }
        return dialog.create();
    }
}
