package pl.polsl.kanjiapp.views;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import pl.polsl.kanjiapp.R;

public class StartMenu extends Fragment {
    private FirebaseAuth mAuth;
    Button login, register, guest;
    public StartMenu() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_start_menu, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null){
            Toast.makeText(getContext(),"you are logged in as: " + mAuth.getCurrentUser().getEmail(),Toast.LENGTH_SHORT).show();
            Navigation.findNavController(view).navigate(R.id.action_startMenu_to_CategoryList);
        }
        login = getView().findViewById(R.id.buttonLogin);
        register = getView().findViewById(R.id.buttonRegister);
        guest = getView().findViewById(R.id.buttonGuest);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.action_startMenu_to_login);
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.action_startMenu_to_register);
            }
        });

        guest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.action_startMenu_to_CategoryList);
            }
        });
    }
}