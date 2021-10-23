package com.empact.android.pufin.ui.form;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.empact.android.pufin.R;
import com.empact.android.pufin.model.UserItem;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static android.text.format.DateUtils.YEAR_IN_MILLIS;
import static com.empact.android.pufin.ui.form.DatePickerFragment.DATE_PICKER_BUNDLE_KEY;
import static com.empact.android.pufin.ui.form.DatePickerFragment.DATE_PICKER_REQUEST_KEY;

public class FormFragment extends Fragment {


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_form, container, false);
        return root;
    }



}

