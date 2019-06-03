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


public class SignUpFragment extends Fragment {

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_sign_up, container, false);
        final EditText editEmailSignUp = rootView.findViewById(R.id.email_sign_up);
        final EditText editLoginSignUp = rootView.findViewById(R.id.login_sign_up);
        final EditText editPasswordSignUp = rootView.findViewById(R.id.pass_sign_up);

        Button btSignUp = rootView.findViewById(R.id.bt_sign_up);
        btSignUp.setOnTouchListener(MainActivity.onTouchListener);
        btSignUp.setOnClickListener(view -> Toast.makeText(getContext(),
                                                           R.string.sorry,
                                                           Toast.LENGTH_SHORT).show());

        return rootView;
    }
}
