/*
 * Copyright (C) 2017 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.biplelibs.loginlibrary;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.android.biplelibs.R;

/**
 * Custom view that is an extension of EditText.
 * It provides a Clear ("x") button within the text field
 * that, when tapped, clears the text from the field.
 */

public class EditTextWithClear
        extends AppCompatEditText {

    Drawable mClearButton;
    Drawable[] mClearButtonImage;
    public EditTextWithClear(Context context) {
        super(context);
        init();
    }

    public EditTextWithClear(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public EditTextWithClear(Context context,
                             AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void scaleDrawble(){
        Bitmap bitmap = ((BitmapDrawable) mClearButton).getBitmap();
        // Scale it to 50 x 50
        mClearButtonImage = new Drawable[]{new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(bitmap, getDP(10), getDP(10), true))};
    }
    public int getDP(int size) {
        size = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, size, getResources()
                        .getDisplayMetrics());
        return size;
    }

    private void init() {
        // Initialize Drawable member variable.
        mClearButton =
                ResourcesCompat.getDrawable(getResources(),
                        R.drawable.text_del_icon, null);
        //scaleDrawble();
        // If the X (clear) button is tapped, clear the text.
        setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // Use the getCompoundDrawables()[2] expression to check
                // if the drawable is on the "end" of text [2].
                if ((getCompoundDrawablesRelative()[2] != null)) {
                    float clearButtonStart; // Used for LTR languages
                    float clearButtonEnd;  // Used for RTL languages
                    boolean isClearButtonClicked = false;
                    // Detect the touch in RTL or LTR layout direction.
                    if (getLayoutDirection() == LAYOUT_DIRECTION_RTL) {
                        // If RTL, get the end of the button on the left side.
                        clearButtonEnd = mClearButton
                                .getIntrinsicWidth() + getPaddingStart();
                        // If the touch occurred before the end of the button,
                        // set isClearButtonClicked to true.
                        if (event.getX() < clearButtonEnd) {
                            isClearButtonClicked = true;
                        }
                    } else {
                        // Layout is LTR.
                        // Get the start of the button on the right side.
                        clearButtonStart = (getWidth() - getPaddingEnd()
                                - mClearButton.getIntrinsicWidth());
                        // If the touch occurred after the start of the button,
                        // set isClearButtonClicked to true.
                        if (event.getX() > clearButtonStart) {
                            isClearButtonClicked = true;
                        }
                    }
                    // Check for actions if the button is tapped.
                    if (isClearButtonClicked) {
                            // Check for ACTION_DOWN (always occurs before ACTION_UP).
                            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                                // Switch to the black version of clear button.

                                showClearButton();
                            }
                            // Check for ACTION_UP.
                            if (event.getAction() == MotionEvent.ACTION_UP) {
                                // Switch to the opaque version of clear button.

                                getText().clear();
                                hideClearButton();
                                return true;
                            }
                    } else {
                        return false;
                    }
                }
                return false;
            }
        });

        setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b){
                    showClearButton();
                }else{
                    hideClearButton();
                }
            }
        });

        // If the text changes, show or hide the X (clear) button.
        addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s,
                                          int start, int count, int after) {
                // Do nothing.
            }

            @Override
            public void onTextChanged(CharSequence s,
                                      int start, int before, int count) {
                if (s.length()>0){
                    showClearButton();
                }else{
                    hideClearButton();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Do nothing.
            }
        });
    }

    /**
     * Shows the clear (X) button.
     */

    private void showClearButton() {
        // Sets the Drawables (if any) to appear to the left of,
        // above, to the right of, and below the text.
        setCompoundDrawablesRelativeWithIntrinsicBounds
                (null,                      // Start of text.
                        null,
                        mClearButton,  // End of text.
                        null);              // Below text.
    }

    /**
     * Hides the clear button.
     */
    private void hideClearButton() {
        setCompoundDrawablesRelativeWithIntrinsicBounds
                (null,             // Start of text.
                        null,      // Top of text.
                        null,      // End of text.
                        null);     // Below text.
    }
}
