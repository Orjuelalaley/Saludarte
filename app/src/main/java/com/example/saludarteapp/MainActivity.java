package com.example.saludarteapp;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.saludarteapp.R;
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

        mDatabase = FirebaseDatabase.getInstance().getReference();
        soundList = new ArrayList<>();

        final ListView soundListView = findViewById(R.id.lv_soundList);
        final ArrayAdapter<Sound> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, soundList);
        soundListView.setAdapter(arrayAdapter);

        mDatabase.child("sounds").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                soundList.clear();
                for (DataSnapshot soundSnapshot : dataSnapshot.getChildren()) {
                    Sound sound = soundSnapshot.getValue(Sound.class);
                    assert sound != null;
                    sound.setId(soundSnapshot.getKey()); // Establece el identificador único del sonido
                    soundList.add(sound);
                }
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("DEBUG", "Error al leer sonidos: " + databaseError.getMessage());
            }
        });

        soundListView.setOnItemClickListener((parent, view, position, id) -> {
            Sound clickedSound = soundList.get(position);
            Log.d("DEBUG", "Sonido seleccionado: " + clickedSound.getName());

            if (clickedSound.getId() != null) {
                DatabaseReference statusRef = mDatabase.child("sounds").child(clickedSound.getId()).child("status");
                statusRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Boolean currentStatus = dataSnapshot.getValue(Boolean.class);
                        Log.d("DEBUG", "Estado actual: " + currentStatus);

                        if (currentStatus == null) {
                            Log.d("DEBUG", "Inicializando estado para: " + clickedSound.getName());
                            statusRef.setValue(false);
                            Toast.makeText(MainActivity.this, "Estado inicializado para: " + clickedSound.getName(), Toast.LENGTH_SHORT).show();
                        } else {
                            statusRef.setValue(!currentStatus);
                            Toast.makeText(MainActivity.this, "Has cambiado el estado de: " + clickedSound.getName(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.e("DEBUG", "Error al obtener el estado: " + databaseError.getMessage());
                        Toast.makeText(MainActivity.this, "Error al cambiar el estado.", Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                Toast.makeText(MainActivity.this, "El sonido clickeado tiene un identificador nulo.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_logout) {
            // Aquí debes implementar la lógica para cerrar la sesión
            // Por ejemplo, puedes llamar a un método performLogout().
            performLogout();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void performLogout() {
        // Agrega la lógica para cerrar la sesión aquí.
        // Puedes utilizar Firebase Authentication para cerrar la sesión.
    }
}
