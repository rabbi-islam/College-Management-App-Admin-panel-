package com.example.admincollege;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class UploadPdf extends AppCompatActivity {

    MaterialCardView addPdf;
    TextInputEditText pdfTitle;
    MaterialButton uploadPdfBtn;
    private TextView pdfNameTextView;
    private final int REQ = 1;
    private Uri pdfUri;
    private DatabaseReference databaseReference;
    private StorageReference storageReference;
    ProgressDialog pd;
    String downloadUrl = "";
    private String finalPdfName, pdfTitleName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_pdf);

        addPdf = findViewById(R.id.addPdf);
        pdfTitle = findViewById(R.id.ebookTitle);
        uploadPdfBtn = findViewById(R.id.uploadEbookButton);
        pdfNameTextView = findViewById(R.id.pdfNameShow);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        storageReference = FirebaseStorage.getInstance().getReference();
        pd = new ProgressDialog(this);

        addPdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery();
            }
        });

        uploadPdfBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pdfTitleName = pdfTitle.getText().toString();
                if (pdfTitleName.isEmpty()){
                    pdfTitle.setError("Title Must Not Be Empty");
                    pdfTitle.requestFocus();
                }else if (pdfUri==null){
                    Toast.makeText(UploadPdf.this, "please upload a pdf file!", Toast.LENGTH_SHORT).show();
                }else{
                    uploadPdfToFireStorage();
                }

            }
        });
    }

    private void uploadPdfToFireStorage() {
        pd.setTitle("please Wait...");
        pd.setMessage("Uploading Pdf");
        pd.show();
        StorageReference streference = storageReference.child("pdf/"+finalPdfName+"-"+System.currentTimeMillis()+".pdf");
        streference.putFile(pdfUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Task<Uri> uriTask  = taskSnapshot.getStorage().getDownloadUrl();
                while (!uriTask.isComplete());
                Uri uri = uriTask.getResult();
                uploadPdfData(String.valueOf(uri));
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                pd.dismiss();
                Toast.makeText(UploadPdf.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void uploadPdfData(String downloadUrl) {
        String uniqueKeys = databaseReference.child("pdf").push().getKey();

        HashMap data = new HashMap();
        data.put("title",pdfTitleName);
        data.put("url",downloadUrl);

        databaseReference.child("pdf").child(uniqueKeys).setValue(data).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                pd.dismiss();
                Toast.makeText(UploadPdf.this, "Uploaded Successfully!", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                pd.dismiss();
                Toast.makeText(UploadPdf.this, "Failed to upload pdf!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("application/pdf");
        startActivityForResult(intent, REQ);
    }

    @SuppressLint("Range")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQ && resultCode == RESULT_OK) {
            pdfUri = data.getData();

            if (pdfUri.toString().startsWith("content://")) {
                Cursor cursor = null;
                try {
                    cursor = UploadPdf.this.getContentResolver().query(pdfUri,null,null,null,null);
                    if (cursor !=null && cursor.moveToFirst()){
                        finalPdfName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else if (pdfUri.toString().startsWith("file://")) {
                finalPdfName = new File(pdfUri.toString()).getName();
            }

            pdfNameTextView.setText(finalPdfName);
        }
    }
}
