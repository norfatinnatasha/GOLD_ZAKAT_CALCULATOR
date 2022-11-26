package com.example.zakatpayment;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener  {

    EditText weight, value;
    Spinner spinner;
    Button button, reset;

    private final int S_WEAR = 200;
    private final int S_KEEP = 85;

    private final String WEAR = "Wear";
    private final String KEEP = "Keep";

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate (R.menu.about_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if (item.getItemId() == R.id.aboutus)
        {
            Intent intent = new Intent(MainActivity.this, AboutUsActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        String getWeight = sharedPref.getString("weight", "");
        String getValue = sharedPref.getString("value", "");

        weight = findViewById(R.id.weight);
        value = findViewById(R.id.value);

        weight.setText(getWeight);
        value.setText(getValue);

        spinner = findViewById(R.id.spinner);

        button = findViewById(R.id.button);
        reset = findViewById(R.id.reset);


        String[] items = new String[]{KEEP, WEAR};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        spinner.setAdapter(adapter);

        button.setOnClickListener(view -> {

            if (weight.getText().toString().length() == 0) {
                weight.setError("Please input gold weight");
                Toast.makeText(this, "Missing input", Toast.LENGTH_SHORT).show();
                return;
            }

            if (value.getText().toString().length() == 0) {
                value.setError("Please input gold value");
                Toast.makeText(this, "Missing input", Toast.LENGTH_SHORT).show();
                return;
            }


            double T_weight = Double.parseDouble(weight.getText().toString());
            double T_value = Double.parseDouble(value.getText().toString());

            int T_type;
            String spinValue = spinner.getSelectedItem().toString();
            if (spinValue.equals(KEEP)) {
                T_type = S_KEEP;
            } else if (spinValue.equals(WEAR)) {
                T_type = S_WEAR;
            } else {
                T_type = 0;
            }

            double totalValueOfGold = T_weight * T_value;
            double uruf = T_weight - T_type;
            double zakatPayable = uruf <= 0 ? 0 : T_value * uruf;
            double totalZakat = zakatPayable * 0.025;


            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("RESULT");
            builder.setMessage("" +
                    "\nTotal Value of Gold : RM " + totalValueOfGold +
                    "\n\nUruf : RM " + uruf +
                    "\n\nZakat Payable : RM " + zakatPayable +
                    "\n\nTotal Zakat : RM " + totalZakat);

            // add the buttons
            builder.setPositiveButton("OK", null);

            // create and show the alert dialog
            AlertDialog dialog = builder.create();
            dialog.show();

            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString("weight", String.valueOf(T_weight));
            editor.putString("value", String.valueOf(T_value));
            editor.apply();

        });

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                weight.setText(null);
                weight.dispatchDisplayHint(View.VISIBLE);
                spinner.setSelection(0);
                value.setText(null);
                value.dispatchDisplayHint(View.VISIBLE);
            }
        });

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}