package com.dedsec.dedsec_mobile;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class UserRegistry extends AppCompatActivity {

    private Button button;
    private EditText usr_name;
    private EditText usr_lastname;
    private EditText usr_email;
    private EditText usr_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_registry);

        btnCreateUser();
    }

    private void btnCreateUser(){
        button = findViewById(R.id.btn_registry);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                usr_name = findViewById(R.id.inputUserName);
                usr_lastname = findViewById(R.id.inputUserLastName);
                usr_email = findViewById(R.id.inputUserEmail);
                usr_password = findViewById(R.id.inputUserPasswd);
                String name = usr_name.getText().toString();
                String lastname = usr_lastname.getText().toString();
                String correo = usr_email.getText().toString();
                String passwd = usr_password.getText().toString();
                new PostCreateUser().execute(name, lastname, correo, passwd);
            }
        });
    }

    private class PostCreateUser extends AsyncTask<String, Void, JSONObject> {

        @Override
        protected JSONObject doInBackground(String... params) {
            String name = params[0];
            String lastname = params[1];
            String email = params[2];
            String password = params[3];
            try {
                JSONObject postData = new JSONObject();
                postData.put("name", name);
                postData.put("lastname", lastname);
                postData.put("email", email);
                postData.put("passwd", password);
                postData.put("profile", 2);

                URL url = new URL("https://dedsec.cl/api-mars/users/create");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setDoOutput(true);

                OutputStream os = new BufferedOutputStream(conn.getOutputStream());
                os.write(postData.toString().getBytes());
                os.flush();
                os.close();

                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();
                return new JSONObject(response.toString());
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(JSONObject result) {
            Boolean status = null;
            String confirmation = "Usuario creado";
            try {
                String value = result.getString("Message");
                if(confirmation.equals(value)) {
                    status = true;
                } else {
                    status = false;
                }
            } catch (JSONException e) {
                status = false;
            }
            if (status) {
                Toast.makeText(UserRegistry.this, "Cuenta creada, revise su correo para validar la cuenta", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(UserRegistry.this, "No se pudo crear la cuenta", Toast.LENGTH_LONG).show();
            }
        }
    }

}