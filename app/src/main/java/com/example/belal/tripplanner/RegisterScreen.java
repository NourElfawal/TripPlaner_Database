package com.example.belal.tripplanner;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import de.hdodenhof.circleimageview.CircleImageView;

public class RegisterScreen extends AppCompatActivity {
    static SharedPreferences sharedPreferences;
    EditText userNameReg, passwordReg;
    Button saveReg;
   CheckBox checkBox;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_screen);

        userNameReg = (EditText) findViewById(R.id.usernameEditReg);
        passwordReg = (EditText) findViewById(R.id.passwordEditReg);
        saveReg = (Button) findViewById(R.id.saveBtnReg);
        checkBox= (CheckBox) findViewById(R.id.checkboxOfPassword);


        //TODO Password hidden or Shown

       checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
           @Override
           public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
               if(!isChecked) {
                 passwordReg.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                  // passwordReg.setSelection(passwordReg.length());
               } else {
                passwordReg.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                   //passwordReg.setSelection(passwordReg.length());

               }
           }
       });

          //TODO Check SharedPreference
        sharedPreferences = getSharedPreferences("SH2", 0);
        String shared_UserName=sharedPreferences.getString("username","******");
        String shared_Password=sharedPreferences.getString("password","******");
        if(shared_UserName.equals("******") && shared_Password.equals("******")){
            onStart();
            onResume();

        }else {
          finish();
            startActivity(new Intent(RegisterScreen.this,ProfileNavigationDrawer.class));
        }



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

                    sharedPreferences = getSharedPreferences("SH2", 0);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("username", name);
                    editor.putString("password", password);
                    editor.commit();
                     finish();
                    startActivity(new Intent(RegisterScreen.this, ProfileNavigationDrawer.class));

                }
            }
        });

    }



}