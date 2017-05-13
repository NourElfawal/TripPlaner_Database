package com.example.belal.tripplanner;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.app.Fragment;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

/**
 * A simple {@link Fragment} subclass.
 */
public class EditProfileFragment extends Fragment {
    EditText userNameReg, passwordReg;
    Button saveReg;
    CheckBox checkBox;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
      View v= inflater.inflate(R.layout.fragment_edit_profile, container, false);
        userNameReg = (EditText) v.findViewById(R.id.usernameEditReg);
        passwordReg = (EditText) v.findViewById(R.id.passwordEditReg);
        saveReg = (Button) v.findViewById(R.id.saveBtnReg);
        checkBox= (CheckBox) v.findViewById(R.id.checkboxOfPassword);


        //TODO Password hidden or Shown

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(!isChecked) {
                    passwordReg.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

                } else {
                    passwordReg.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);

                }
            }
        });

        RegisterScreen.sharedPreferences=getActivity().getSharedPreferences("SH2",0);
        String Name=RegisterScreen.sharedPreferences.getString("username","******");
        String password=RegisterScreen.sharedPreferences.getString("password","******");
        userNameReg.setText(Name);
        passwordReg.setText(password);
        saveReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // get String name , password
                String name = userNameReg.getText().toString();
                String password = passwordReg.getText().toString();
                //TODO Validation For Inputs Length
                if(name.length()<3 ){

                    userNameReg.setError("less than 3 chars");



                }

                if(password.length()<6){
                    passwordReg.setError("less than 6 chars");

                }
                else {

                    RegisterScreen.sharedPreferences = getActivity().getSharedPreferences("SH2", 0);
                    SharedPreferences.Editor editor = RegisterScreen.sharedPreferences.edit();
                    editor.putString("username", name);
                    editor.putString("password", password);
                    editor.commit();
                     getActivity().finish();
                    startActivity(new Intent(getActivity(), ProfileNavigationDrawer.class));

                }
            }
        });

      return  v;

    }

}
