package com.codyvbs.malwhere;

import android.app.Activity;
import android.graphics.Typeface;
import android.widget.EditText;
import android.widget.TextView;

import androidx.core.content.res.ResourcesCompat;

import com.jaredrummler.materialspinner.MaterialSpinner;

public class CustomUI {
    public void setTextViewFontFamily(Activity activity, TextView[] tvs, EditText[] editxts){
        Typeface customFont = ResourcesCompat.getFont(activity,R.font.robotobold);

        for(int x = 0; x<tvs.length ;x++){
            tvs[x].setTypeface(customFont);
        }

        for (int y =0 ; y<editxts.length;y++){
            editxts[y].setTypeface(customFont);
        }

    }

    public void setSpinnerFont(Activity activity, MaterialSpinner materialSpinner[]){
        Typeface customFont = ResourcesCompat.getFont(activity,R.font.robotobold);

        for(int x = 0; x<materialSpinner.length ;x++){
            materialSpinner[x].setTypeface(customFont);
        }
    }
}
