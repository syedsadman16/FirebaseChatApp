package com.example.chatapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    EditText username,password;
    Button loginBtn, goToRegister;
    String user,pw;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Define the views
        username = findViewById(R.id.emailField);
        password = findViewById(R.id.usernameField);
        loginBtn = findViewById(R.id.loginBtn);
        goToRegister = findViewById(R.id.registerBtn);


        goToRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        //Functionality for login
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user = username.getText().toString();
                pw = password.getText().toString();

                //Blank fields
                if(user.equals("")){
                    username.setError("Field is empty");
                }
                else if(password.equals("")){
                    password.setError("Field is empty");
                }
                //login authentication using volley
                else{
                    final ProgressDialog pd = new ProgressDialog(LoginActivity.this);
                    pd.setMessage("Loading...");
                    pd.show();

                    //Link to firebase realtime database json file
                    String url = "https://chat-application-55873.firebaseio.com/users.json";
                    //Requests string response from url
                    StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>(){
                        @Override
                        public void onResponse(String s) {

                            //if JSON is empty
                            if(s.equals("null")){
                                Toast.makeText(LoginActivity.this, "Unknown User", Toast.LENGTH_LONG).show();
                            }
                            else{
                                try {
                                    //Setup as JSONObject
                                    JSONObject obj = new JSONObject(s);

                                    //If JSON doesn't have a mapping for the username
                                    if(!obj.has(user)){
                                        Toast.makeText(LoginActivity.this, "user not found", Toast.LENGTH_SHORT).show();
                                    }
                                    //Assuming the user was correct, check the password from the user object
                                    else if(obj.getJSONObject(user).getString("password").equals(pw)){
                                        //Set static variables for the user and pw
                                        User.user = user;
                                        User.pass = pw;
                                        String name = obj.getJSONObject(user).getString("name");
                                        User.name = name;
                                        Log.i("Name", name);
                                        //Authentication passed, go to the next activity
                                        startActivity(new Intent(LoginActivity.this, UserListActivity.class));
                                    }
                                    else {
                                        //If user associated password is incorrect
                                        Toast.makeText(LoginActivity.this, "Incorrect password", Toast.LENGTH_SHORT).show();
                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                            //Once processed, exit ProgressDialog
                            pd.dismiss();
                        }
                    },new Response.ErrorListener(){
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            Log.i("VolleyError", ""+volleyError);
                            pd.dismiss();
                        }
                    });

                    //instantiate and add request to request queue
                    //RequestQueue manages worker threads for running the network operations,
                    //reading from and writing to the cache, and parsing responses
                    RequestQueue rQueue = Volley.newRequestQueue(LoginActivity.this);
                    rQueue.add(stringRequest);
                }

            }
        });
    }
}
