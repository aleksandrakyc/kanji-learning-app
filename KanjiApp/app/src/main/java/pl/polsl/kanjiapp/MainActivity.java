package pl.polsl.kanjiapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.HashMap;

import pl.polsl.kanjiapp.models.SetModel;
import pl.polsl.kanjiapp.types.CategoryType;

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
        if (id == R.id.action_search) {
            //dialog insert search
            //kanjilistview
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            final View dialogLayout = getLayoutInflater().inflate(R.layout.fragment_set_name_dialog, null);
            builder.setView(dialogLayout);
            builder.setTitle("Type meaning: ");
            builder.setPositiveButton("Search", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    EditText name = dialogLayout.findViewById(R.id.editTextSetName);
                    String search = name.getText().toString();

                    Bundle bundle = new Bundle();
                    bundle.putInt("categoryType", CategoryType.Search.getValue());
                    bundle.putString("level", search);
                    navController.navigate(R.id.kanjiListView, bundle);
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        }
        return true;
    }
}