package trainedge.social_wallpaper_app;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        FloatingActionButton fabNewUpload = (FloatingActionButton) findViewById(R.id.fabNewUpload);
        fabNewUpload.setOnClickListener(this);
        setSupportActionBar(toolbar);
        final RecyclerView rv = (RecyclerView) findViewById(R.id.rv);
        final List<SocialModel> dataset = new ArrayList<>();
        GridLayoutManager manager = new GridLayoutManager(this,1 );
        rv.setLayoutManager(manager);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("Images");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChildren()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        String url = snapshot.getValue(String.class);
                        dataset.add(new SocialModel(url));
                    }
                    rv.setAdapter(new InfoAdapter(dataset));
                } else {
                    Toast.makeText(HomeActivity.this, "No Image found", Toast.LENGTH_SHORT).show();
                }
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(HomeActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }

    @Override
    public void onClick(View v) {
        startActivity(new Intent(this, Browse.class));
    }
}
