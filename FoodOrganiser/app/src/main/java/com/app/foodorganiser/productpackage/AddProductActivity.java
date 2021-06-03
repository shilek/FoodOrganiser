package com.app.foodorganiser.productpackage;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.app.foodorganiser.R;
import com.app.foodorganiser.entity.ProductTable;

public class AddProductActivity extends AppCompatActivity {

    EditText nameBox;
    EditText carbosBox;
    EditText proteinsBox;
    EditText fatsBox;
    EditText codeBox;
    Button confirm;
    String name;
    int carbos = -1;
    int proteins = -1;
    int fats = -1;
    String code = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        nameBox = findViewById(R.id.name_et);
        carbosBox = findViewById(R.id.carbo_et);
        proteinsBox = findViewById(R.id.protein_et);
        fatsBox = findViewById(R.id.fat_et);
        codeBox = findViewById(R.id.code_et);
        confirm = findViewById(R.id.confirm_button);

        confirm.setOnClickListener(v -> {
            getValues();
            new Thread(() -> {
                DatabaseClass db = new  DatabaseClass();
                db.openConnection();
                ProductTable productObject = new ProductTable(-1, name, proteins, carbos, fats, code, null);
                db.sendQuery(QueryBuilder.insert(productObject));
            }).start();
            this.finish();
        });
    }

    public void getValues() {
        name = nameBox.getText().toString();
        carbos = Integer.parseInt(carbosBox.getText().toString());
        proteins = Integer.parseInt(proteinsBox.getText().toString());
        fats = Integer.parseInt(fatsBox.getText().toString());
        code = codeBox.getText().toString();
    }
}