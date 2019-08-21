
package com.example.chatapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import com.firebase.client.Firebase;

import org.json.JSONException;
import org.json.JSONObject;

public class RegisterActivity extends AppCompatActivity {

    Button submitBtn,cancelBtn;
    EditText usernameField, nameField, passwordField, emailField;
    String username, name, password, email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        submitBtn = findViewById(R.id.submit);
        cancelBtn = findViewById(R.id.cancel);
        usernameField = findViewById(R.id.usernameField);
        nameField = findViewById(R.id.nameField);
        passwordField = findViewById(R.id.passwordField);
        emailField = findViewById(R.id.emailField);

        //Firebase library init
        Firebase.setAndroidContext(this);

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToLogin();
            }
        });

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = nameField.getText().toString();
                username = usernameField.getText().toString();
                password = passwordField.getText().toString();
                email = emailField.getText().toString();

                //First check if fields are blank
                if(username.equals("")){
                    usernameField.setError("Required");
                }
                else if(password.equals("")){
                    passwordField.setError("Required");
                }
                //Regex requirements
                else if(!username.matches("[A-Za-z0-9]+")){
                    usernameField.setError("No special symbols allowed");
                }
                //If everything is good, proceed to check
                else {
                    final ProgressDialog progressDialog = new ProgressDialog(RegisterActivity.this);
                    progressDialog.setMessage("Registering...");
                    progressDialog.show();

                    String url = "https://chat-application-55873.firebaseio.com/users.json";

                    StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>(){
                        @Override
                        public void onResponse(String s) {
                            Firebase reference = new Firebase("https://chat-application-55873.firebaseio.com/users");

                            //If empty JSON file
                            if(s.equals("null")) {
                                reference.child("user").setValue(username);
                                reference.child(username).child("name").setValue(name);
                                reference.child(username).child("password").setValue(password);
                                reference.child(username).child("lastMessage").setValue("No recent messages");
                                reference.child(username).child("email").setValue(email);
                                Toast.makeText(RegisterActivity.this, "Successfully Registered", Toast.LENGTH_LONG).show();
                            }
                            else {
                                //If there are existing users
                                try {
                                    JSONObject obj = new JSONObject(s);
                                    //Check if user being created doesnt exist
                                    if (!obj.has(username)) {
                                        reference.child(username).child("name").setValue(name);
                                        reference.child(username).child("password").setValue(password);
                                        reference.child(username).child("lastMessage").setValue("No recent messages");
                                        reference.child(username).child("email").setValue(email);
                                        Toast.makeText(RegisterActivity.this, "Successfully Registered", Toast.LENGTH_LONG).show();

                                        goToLogin();

                                    } else {
                                        usernameField.setError("Username already exists");
                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                            progressDialog.dismiss();
                        }

                    },new Response.ErrorListener(){

                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            System.out.println("" + volleyError );
                            progressDialog.dismiss();
                        }
                    });

                    RequestQueue rQueue = Volley.newRequestQueue(RegisterActivity.this);
                    rQueue.add(request);
                }
            }

        });

    }

    public void goToLogin(){
        usernameField.setText("");
        passwordField.setText("");
        nameField.setText("");
        emailField.setText("");
        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
        startActivity(intent);
    }
}
