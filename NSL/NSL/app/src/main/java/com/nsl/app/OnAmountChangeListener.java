package com.nsl.app;

import android.widget.EditText;

/**
 * Created by Apresh on 1/25/2017.
 */

public interface OnAmountChangeListener {

    void onAmountChanged();
    void beforeAmountChanged(EditText amountText, String quantity);


}
