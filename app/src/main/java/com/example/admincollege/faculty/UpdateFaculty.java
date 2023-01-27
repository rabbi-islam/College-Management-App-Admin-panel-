package com.example.admincollege.faculty;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.admincollege.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class UpdateFaculty extends AppCompatActivity {
    FloatingActionButton fab;
    private RecyclerView csDepartment,softwareDepartment,electricalDepartment,nfeDepartment;
    private LinearLayout csNoData, softwareNoData,electricalNoData,nfeNoData;
    private List<TeacherData> list1, list2, list3, list4;
    private DatabaseReference databaseReference,dbRef;
    private TeacherAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_faculty);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("teacher");
        fab = findViewById(R.id.fab);
        csDepartment = findViewById(R.id.csDepartment);
        softwareDepartment = findViewById(R.id.softwareDepartment);
        electricalDepartment = findViewById(R.id.electricalDepartment);
        nfeDepartment = findViewById(R.id.nfeDepartment);

        csNoData = findViewById(R.id.csNoData);
        softwareNoData = findViewById(R.id.softwareNoData);
        electricalNoData = findViewById(R.id.electricalNoData);
        nfeNoData = findViewById(R.id.nfeNoData);

        csDepartment();
        sweDepartment();
        eeeDepartment();
        nfeDepartment();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(UpdateFaculty.this,AddTeacher.class));
            }
        });
    }

    private void csDepartment() {
        dbRef = databaseReference.child("CSE");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list1 = new ArrayList<>();
                if (!snapshot.exists()){
                    csNoData.setVisibility(View.VISIBLE);
                    csDepartment.setVisibility(View.GONE);
                }else{
                    csNoData.setVisibility(View.GONE);
                    csDepartment.setVisibility(View.VISIBLE);
                    for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                        TeacherData data = dataSnapshot.getValue(TeacherData.class);
                        list1.add(data);
                    }
                    csDepartment.setHasFixedSize(true);
                    csDepartment.setLayoutManager(new LinearLayoutManager(UpdateFaculty.this));
                    adapter = new TeacherAdapter(list1,UpdateFaculty.this,"CSE");
                    csDepartment.setAdapter(adapter);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(UpdateFaculty.this, error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }
    private void sweDepartment() {
        dbRef = databaseReference.child("SWE");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list2 = new ArrayList<>();
                if (!snapshot.exists()){
                    softwareNoData.setVisibility(View.VISIBLE);
                    softwareDepartment.setVisibility(View.GONE);
                }else{
                    softwareNoData.setVisibility(View.GONE);
                    softwareDepartment.setVisibility(View.VISIBLE);
                    for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                        TeacherData data = dataSnapshot.getValue(TeacherData.class);
                        list2.add(data);
                    }
                    softwareDepartment.setHasFixedSize(true);
                    softwareDepartment.setLayoutManager(new LinearLayoutManager(UpdateFaculty.this));
                    adapter = new TeacherAdapter(list2,UpdateFaculty.this,"SWE");
                    softwareDepartment.setAdapter(adapter);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(UpdateFaculty.this, error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }
    private void eeeDepartment() {
        dbRef = databaseReference.child("EEE");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list3 = new ArrayList<>();
                if (!snapshot.exists()){
                    electricalNoData.setVisibility(View.VISIBLE);
                    electricalDepartment.setVisibility(View.GONE);
                }else{
                    electricalNoData.setVisibility(View.GONE);
                    electricalDepartment.setVisibility(View.VISIBLE);
                    for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                        TeacherData data = dataSnapshot.getValue(TeacherData.class);
                        list3.add(data);
                    }
                    electricalDepartment.setHasFixedSize(true);
                    electricalDepartment.setLayoutManager(new LinearLayoutManager(UpdateFaculty.this));
                    adapter = new TeacherAdapter(list3,UpdateFaculty.this,"EEE");
                    electricalDepartment.setAdapter(adapter);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(UpdateFaculty.this, error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }
    private void nfeDepartment() {
        dbRef = databaseReference.child("NFE");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list4 = new ArrayList<>();
                if (!snapshot.exists()){
                    nfeNoData.setVisibility(View.VISIBLE);
                    nfeDepartment.setVisibility(View.GONE);
                }else{
                    nfeNoData.setVisibility(View.GONE);
                    nfeDepartment.setVisibility(View.VISIBLE);
                    for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                        TeacherData data = dataSnapshot.getValue(TeacherData.class);
                        list4.add(data);
                    }
                    nfeDepartment.setHasFixedSize(true);
                    nfeDepartment.setLayoutManager(new LinearLayoutManager(UpdateFaculty.this));
                    adapter = new TeacherAdapter(list4,UpdateFaculty.this,"NFE");
                    nfeDepartment.setAdapter(adapter);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(UpdateFaculty.this, error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }
}