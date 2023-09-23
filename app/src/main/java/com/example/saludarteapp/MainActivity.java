package com.example.saludarteapp;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ArrayList<Sound> soundList;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Inicializar la referencia de la base de datos de Firebase
        mDatabase = FirebaseDatabase.getInstance().getReference();

        // Inicializar el ArrayList
        soundList = new ArrayList<>();

        // Enlazar el ListView con el ArrayList usando un ArrayAdapter
        final ListView soundListView = findViewById(R.id.lv_soundList);
        final ArrayAdapter<Sound> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, soundList);
        soundListView.setAdapter(arrayAdapter);

        // Leer los sonidos desde Firebase
        // Leer los sonidos desde Firebase
        mDatabase.child("sounds").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                soundList.clear();  // Vaciar la lista antes de agregar nuevos elementos
                for (DataSnapshot soundSnapshot : dataSnapshot.getChildren()) {
                    Sound sound = soundSnapshot.getValue(Sound.class);
                    soundList.add(sound);
                }
                arrayAdapter.notifyDataSetChanged();  // Notificar al adaptador que los datos han cambiado
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Código para manejar errores
            }
        });


        // Agregar un OnClickListener al ListView
        // Agregar un OnClickListener al ListView
        soundListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Obtener el sonido clickeado
                Sound clickedSound = soundList.get(position);

                if (clickedSound.getName() != null) {
                    // Leer el estado actual del campo "status"
                    DatabaseReference statusRef = mDatabase.child("sounds").child(clickedSound.getName()).child("status");
                    statusRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Boolean currentStatus = dataSnapshot.getValue(Boolean.class);
                            if (currentStatus != null) {
                                // Cambiar el estado al valor opuesto
                                statusRef.setValue(!currentStatus);

                                // Mostrar un Toast
                                Toast.makeText(MainActivity.this, "Has cambiado el estado de: " + clickedSound.getName(), Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            // Código para manejar errores
                            Toast.makeText(MainActivity.this, "Error al cambiar el estado.", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    // Mostrar un mensaje de error
                    Toast.makeText(MainActivity.this, "El sonido clickeado tiene un nombre nulo.", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }
}
