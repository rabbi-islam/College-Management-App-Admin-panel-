package com.example.admincollege.faculty;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.admincollege.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;

public class UpdateTeacher extends AppCompatActivity {

    private ImageView updateTeacherImageView;
    private EditText updateName, updatePost,updateEmail;
    private Button updateTeacherButton,deleteTeacherButton;
    private String name,post,email,image;
    private final int REQ = 1;
    private Bitmap bitmap = null;
    private StorageReference storageReference;
    private DatabaseReference reference;
    private String downloadUrl,uniqueKey,category;
    private ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_teacher);

        name = getIntent().getStringExtra("name");
        post = getIntent().getStringExtra("post");
        email = getIntent().getStringExtra("email");
        image = getIntent().getStringExtra("image");

        updateTeacherImageView = findViewById(R.id.updateImageView);
        updateName = findViewById(R.id.updateName);
        updatePost = findViewById(R.id.updatePost);
        updateEmail = findViewById(R.id.updateEmail);
        updateTeacherButton = findViewById(R.id.updateTeacherButton);
        deleteTeacherButton = findViewById(R.id.deleteTeacherButton);
        pd = new ProgressDialog(this);

        reference = FirebaseDatabase.getInstance().getReference().child("teacher");
        storageReference = FirebaseStorage.getInstance().getReference();

         uniqueKey = getIntent().getStringExtra("key");
         category  = getIntent().getStringExtra("category");

        updateName.setText(name);
        updatePost.setText(post);
        updateEmail.setText(email);
        try {
            Picasso.get().load(image).into(updateTeacherImageView);
        }catch (Exception e){
            e.printStackTrace();
        }

        updateTeacherImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery();
            }
        });

        updateTeacherButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name = updateName.getText().toString();
                post = updatePost.getText().toString();
                email = updateEmail.getText().toString();
                checkValidation();
            }
        });
        deleteTeacherButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteData();
            }
        });
    }

    private void deleteData() {
        reference.child(category).child(uniqueKey).removeValue()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(UpdateTeacher.this, "Deleted successfully!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(UpdateTeacher.this,UpdateFaculty.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(UpdateTeacher.this, "something Went Wrong!", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void checkValidation() {

        if (name.isEmpty()){
            updateName.setError("Empty!");
            updateName.requestFocus();
        }else if (post.isEmpty()){
            updatePost.setError("Empty!");
            updatePost.requestFocus();
        }else if (email.isEmpty()){
            updateEmail.setError("Empty!");
            updateEmail.requestFocus();
        }else if (bitmap==null){
            updateData(image);
        }else{
            uploadImage();
        }
    }

    private void updateData(String s) {

        HashMap map = new HashMap();
        map.put("name",name);
        map.put("post",post);
        map.put("email",email);
        map.put("image",s);


        reference.child(category).child(uniqueKey).updateChildren(map).addOnSuccessListener(new OnSuccessListener() {
            @Override
            public void onSuccess(Object o) {
                Toast.makeText(UpdateTeacher.this, "Updated successfully!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(UpdateTeacher.this,UpdateFaculty.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(UpdateTeacher.this, "something went wrong!", Toast.LENGTH_SHORT).show();
            }
        });


    }
    private void uploadImage() {
        pd.setMessage("Uploading");
        pd.show();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos);
        byte[] finalImage = baos.toByteArray();
        final StorageReference filePath;
        filePath = storageReference.child("Notice").child(finalImage + "jpg");
        final UploadTask uploadTask = filePath.putBytes(finalImage);
        uploadTask.addOnCompleteListener(UpdateTeacher.this, new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if (task.isSuccessful()) {
                    uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            filePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    downloadUrl = String.valueOf(uri);
                                    updateData(downloadUrl);
                                    pd.dismiss();
                                }
                            });
                        }
                    });
                } else {
                    pd.dismiss();
                    Toast.makeText(UpdateTeacher.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void openGallery() {
        Intent pickImage = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickImage, REQ);
    }
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQ && resultCode == RESULT_OK) {
            Uri uri = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
            } catch (IOException e) {
                e.printStackTrace();
            }

            updateTeacherImageView.setImageBitmap(bitmap);
        }
    }
}