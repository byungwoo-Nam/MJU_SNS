package com.example.mju_sns;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

public class BaseFragment extends Fragment {
    public void onChange(){
    }
    public static void downKeyboard(Fragment fragment, EditText editText) {
        InputMethodManager mInputMethodManager = (InputMethodManager)(fragment.getContext()).getSystemService(Context.INPUT_METHOD_SERVICE);
        mInputMethodManager.hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }
}
