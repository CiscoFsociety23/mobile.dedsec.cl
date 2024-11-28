package com.dedsec.dedsec_mobile;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class MqttIntegration extends AppCompatActivity {

    private static String mqttHost = "tcp://skitterkitten341.cloud.shiftr.io:1883";
    private static String idUsuario = "AppAndroid";

    private static String topico = "Mensaje";
    private static String user = "skitterkitten341";
    private static String pass = "iEbez08rQzz5NDQA";

    private TextView textView;
    private EditText editText;
    private Button button;

    private MqttClient mqttClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mqtt_integration);

        textView = findViewById(R.id.vista_texto);
        editText = findViewById(R.id.txtMessage);
        button = findViewById(R.id.btn_mqttSendData);

        try {
            mqttClient = new MqttClient(mqttHost, idUsuario, null);
            MqttConnectOptions options = new MqttConnectOptions();
            options.setUserName(user);
            options.setPassword(pass.toCharArray());
            mqttClient.connect(options);

            Toast.makeText(MqttIntegration.this, "Aplicacion Conectada", Toast.LENGTH_SHORT).show();

            mqttClient.setCallback(new MqttCallback() {
                @Override
                public void connectionLost(Throwable cause) {
                    Log.d("MQTT", "Conexion Perdida");
                }

                @Override
                public void messageArrived(String topic, MqttMessage message) throws Exception {
                    String payload = new String(message.getPayload());
                    runOnUiThread(() -> textView.setText(payload));
                }

                @Override
                public void deliveryComplete(IMqttDeliveryToken token) {
                    Log.d("MQTT", "Entrega completa");
                }
            });

        } catch (MqttException e) {
            e.printStackTrace();
        }

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mesagge = editText.getText().toString();
                try {
                    if (mqttClient != null && mqttClient.isConnected()){
                        mqttClient.publish(topico, mesagge.getBytes(), 0, false);
                        textView.append("\n - " + mesagge);
                        Toast.makeText(MqttIntegration.this, "Mesaje Enviado", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(MqttIntegration.this, "Conexion no disponible", Toast.LENGTH_SHORT).show();
                    }
                } catch (MqttException e) {
                    e.printStackTrace();
                }
            }
        });

    }
}