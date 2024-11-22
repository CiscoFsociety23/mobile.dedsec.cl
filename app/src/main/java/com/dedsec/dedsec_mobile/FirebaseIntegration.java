package com.dedsec.dedsec_mobile;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class FirebaseIntegration extends AppCompatActivity {

    private EditText inputChipCode, inputPetName, inputNameOwner, inputAddressOwner;
    private ListView listaPets;
    private Spinner spPet;

    private FirebaseFirestore db;

    String[] TiposMascota = {"Perro", "Gato", "Otro"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firebase_integration);

        CargarLista();

        db = FirebaseFirestore.getInstance();

        inputChipCode = findViewById(R.id.inputChipCode);
        inputPetName = findViewById(R.id.inputPetName);
        inputNameOwner = findViewById(R.id.inputNameOwner);
        inputAddressOwner = findViewById(R.id.inputAddressOwner);
        spPet = findViewById(R.id.spPet);
        listaPets = findViewById(R.id.listaPets);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, TiposMascota);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spPet.setAdapter(adapter);

    }

    public void enviarDatosFirestore(View view){
        String chipCode = inputChipCode.getText().toString();
        String petName = inputPetName.getText().toString();
        String nameOwner = inputNameOwner.getText().toString();
        String addressOwner = inputAddressOwner.getText().toString();
        String petType = spPet.getSelectedItem().toString();

        Map<String, Object> pet = new HashMap<>();
        pet.put("codigo", chipCode);
        pet.put("nombreMascota", petName);
        pet.put("nombreDueño", nameOwner);
        pet.put("direccion", addressOwner);
        pet.put("tipo", petType);

        db.collection("/pets")
                .document(chipCode)
                .set(pet)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(FirebaseIntegration.this, "Datos Enviados",
                            Toast.LENGTH_LONG).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(FirebaseIntegration.this, "Datos No Enviados",
                            Toast.LENGTH_LONG).show();
                });
    }

    public void CargarLista(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("/pets")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            List<String> listPets = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()){
                                String line = "||" + document.getString("codigo") + "||" +
                                        document.getString("nombreMascota") + "||" +
                                        document.getString("nombreDueño") + "||" +
                                        document.getString("direccion") + "||" +
                                        document.getString("tipo");
                                listPets.add(line);
                            }
                            ArrayAdapter<String> adapter = new ArrayAdapter<>(
                                    FirebaseIntegration.this,
                                    android.R.layout.simple_list_item_1,
                                    listPets
                            );
                            listaPets.setAdapter(adapter);
                        } else {
                            Log.e("TAG", "Error al obtener datos", task.getException());
                        }
                    }
                });
    }

}