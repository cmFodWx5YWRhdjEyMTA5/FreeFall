package com.develop.loginov.freefall.profile;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.develop.loginov.freefall.R;
import com.develop.loginov.freefall.main.MainActivity;


/**
 * A simple {@link Fragment} subclass.
 */
public class SignInFragment extends Fragment {

    private View rootView;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_sign_in, container, false);
        final EditText editEmailLogin = rootView.findViewById(R.id.email_login);
        final EditText editPasswordLogin = rootView.findViewById(R.id.pass_login);

        final Button btLogin = rootView.findViewById(R.id.bt_login);
        btLogin.setOnTouchListener(MainActivity.onTouchListener);
        btLogin.setOnClickListener(view -> Toast.makeText(getContext(), R.string.sorry, Toast.LENGTH_SHORT).show());
        return rootView;
    }

}
