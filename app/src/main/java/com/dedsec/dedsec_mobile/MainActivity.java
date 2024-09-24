package com.dedsec.dedsec_mobile;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private Button btn_ingress;
    private EditText usr_email;
    private EditText usr_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        validateUser();
    }

    private void validateUser() {
        btn_ingress = findViewById(R.id.btn_ingress);
        btn_ingress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                usr_email = findViewById(R.id.inputUserEmail);
                usr_password = findViewById(R.id.inputUserPasswd);
                String email = usr_email.getText().toString();
                String passwd = usr_password.getText().toString();
                new PostValidateUser().execute(email, passwd);
            }
        });
    }

    private class PostValidateUser extends AsyncTask<String, Void, JSONObject> {

        @Override
        protected JSONObject doInBackground(String... params) {
            String email = params[0];
            String password = params[1];
            try {
                JSONObject postData = new JSONObject();
                postData.put("email", email);
                postData.put("passwd", password);

                URL url = new URL("https://dedsec.cl/api-mars/users/check");
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
            try {
                String value = result.getString("access");
                status = Boolean.parseBoolean(value);
            } catch (JSONException e) {
                status = false;
            }
            if (status) {
                Toast.makeText(MainActivity.this, "Bienvenido!!!", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(MainActivity.this, "Credenciales invalidas", Toast.LENGTH_LONG).show();
            }
        }
    }
}
