package com.example.admincollege.notice;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.admincollege.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class DeleteNotice extends AppCompatActivity {

    private RecyclerView deleteRecyclerView;
    private ProgressBar progressBar;
    private ArrayList<NoticeData> list;
    private NoticeAdapter adapter;
    private DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_notice);

        reference = FirebaseDatabase.getInstance().getReference().child("Notice");
        deleteRecyclerView = findViewById(R.id.deleteRecyclerView);
        progressBar = findViewById(R.id.progressBar);

        deleteRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        deleteRecyclerView.setHasFixedSize(true);

        getNotice();
    }

    private void getNotice() {
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list = new ArrayList<>();
                for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                    NoticeData data = dataSnapshot.getValue(NoticeData.class);
                    list.add(data);
                }
                adapter = new NoticeAdapter(list,DeleteNotice.this);
                adapter.notifyDataSetChanged();
                progressBar.setVisibility(View.GONE);
                deleteRecyclerView.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(DeleteNotice.this, error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }
}