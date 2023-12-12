package pl.polsl.kanjiapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setSupportActionBar(findViewById(R.id.my_toolbar));
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        FirebaseUser user = mAuth.getCurrentUser();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        int id = item.getItemId();
        if (id == R.id.action_add){
            if (user == null) {
                Toast.makeText(this, "log in to create custom sets!", Toast.LENGTH_SHORT).show();
            }
            else {
                navController.navigate(R.id.createNewSet);
            }
        }
        if (id == R.id.action_logout) {

            if (user != null){
                Toast.makeText(this, "logging out user "+ user.getEmail(), Toast.LENGTH_SHORT).show();
                mAuth.signOut();
            }
            Toast.makeText(this, "you are not logged in.", Toast.LENGTH_SHORT).show();
            navController.navigate(R.id.startMenu);
        }
        if (id == R.id.action_group) {
            if (user == null) {
                Toast.makeText(this, "log in to browse groups!", Toast.LENGTH_SHORT).show();
            }
            else {
                navController.navigate(R.id.GroupManagement);
            }
        }
        return true;
    }
}