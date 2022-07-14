package com.digruttola.firebasestorage;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    // Reference to an image file in Cloud Storage
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private StorageReference pathReference = storage.getReferenceFromUrl("gs://fir-storage-c7c2d.appspot.com/Horarios/horario_1b.png");

    private Button btSubirImagen,btGaleria,btSiguiente;

    private ImageView view;
    private Uri path;


    int i = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        view = findViewById(R.id.firebaseimage);
        btSubirImagen = findViewById(R.id.btSubirImagen);
        btGaleria = findViewById(R.id.btBuscarImage);
        btSiguiente = findViewById(R.id.btSiguiente);

        ArrayList<StorageReference> listadoImagenes = new ArrayList<>();

        StorageReference listRef = storage.getReference().child("fotos");
        listRef.listAll().addOnSuccessListener(new OnSuccessListener<ListResult>() {
            @Override
            public void onSuccess(ListResult listResult) {
                for (StorageReference prefix : listResult.getPrefixes()) {
                    // All the prefixes under listRef.
                    // You may call listAll() recursively on them.
                    Log.d("getPrefixes(): ",String.valueOf(prefix));
                }

                for (StorageReference item : listResult.getItems()) {
                    // All the items under listRef.
                    Log.d("getItems(): ",String.valueOf(item));
                    listadoImagenes.add(item);
                }
            }
        });



        pathReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                // Got the download URL for 'users/me/profile.png'
                Log.d("TAG",String.valueOf(uri));

                Glide.with(MainActivity.this)
                        .load(uri)                      //URI
                        .override(620,620)  //Dimencion
                        .into(view);                    //ImageView
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });


        btSiguiente.setOnClickListener( view2 -> {
            i++;

            if(i >= listadoImagenes.size()){
                i = 0;
            }

            StorageReference storageReference = listadoImagenes.get(i);

            storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    // Got the download URL for 'users/me/profile.png'
                    Log.d("TAG",String.valueOf(uri));

                    Glide.with(MainActivity.this)
                            .load(uri)                      //URI
                            .override(620,620)  //Dimencion
                            .into(view);                    //ImageView
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle any errors
                }
            });
        } );

        btGaleria.setOnClickListener( view -> {
            cargarImagenes();
        });

        btSubirImagen.setOnClickListener( view -> {


            if(path != null){
                StorageReference filePath = storage.getReference().child("fotos").child(path.getLastPathSegment());
                filePath.putFile(path).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Toast.makeText(MainActivity.this, "Se guardo en la base de datos", Toast.LENGTH_SHORT).show();
                    }
                });
            }else
                Toast.makeText(this, "Buscar imagen en la galeria", Toast.LENGTH_SHORT).show();

        });







    }

    private void cargarImagenes() {
        Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        i.setType("image/");
        startActivityForResult(i.createChooser(i,"Seleccione app"),10);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK){
            path = data.getData();
            view.setImageURI(path);
        }
    }
}