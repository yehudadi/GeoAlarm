package com.example.myapplication;

import static com.example.myapplication.MainActivity.DB;
import static com.example.myapplication.MainActivity.extractAddressAndCoordinates;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class CostumeDialog extends Dialog {

    private Context context;
    private EditText editAdress;
    private EditText editName;
    private Button enterButton;
    private Spinner distanceSpinner;
    private Boolean isEdit;
    private String selectedDistance;
    private Item item;
    private int pos = -1;


    public CostumeDialog(Context context, Item item, int pos) {
        super(context);
        this.context = context;
        if (item == null) {
            isEdit = false;
        } else {
            isEdit = true;
            this.pos = pos;
            this.item = item;
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_dialog);

        editAdress = findViewById(R.id.edit_location);
        editName = findViewById(R.id.edit_username);
        enterButton = findViewById(R.id.enterButton);
        distanceSpinner = findViewById(R.id.distanceSpinner);

        if (isEdit) {
            editName.setText(this.item.getName());
        }

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(context, R.array.distance_options, android.R.layout.simple_spinner_item);

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        distanceSpinner.setAdapter(adapter);

        // Set a listener to handle item selections
        distanceSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                selectedDistance = parentView.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Do nothing here
            }
        });


        enterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String[] qord = extractAddressAndCoordinates(editAdress);
                Item item1 = new Item(editName.getText().toString(), selectedDistance, 1L, qord[0], qord[1]);
                if (isEdit) {
                    //update adaptor without edited item and about new item
                    item1.setId(item.getId());
                    DB.updateItem(item1);
                    ((MainActivity) context).recreate();
                } else {
                    //update adaptor with new item
                    DB.insertItem(item1);
                    ((MainActivity) context).recreate();
                }
                dismiss();
            }
        });

    }
}
