package com.cscodetech.urbantaxi.utility;


import android.content.Context;
import android.widget.EditText;

import androidx.annotation.NonNull;

public class Inputbox extends androidx.appcompat.widget.AppCompatEditText {
    Context context;
    public Inputbox(@NonNull Context context) {
        super(context);
        this.context=context;
    }
    EditText myEditText = new EditText(context);

}
