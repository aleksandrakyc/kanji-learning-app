package pl.polsl.kanjiapp.views;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import pl.polsl.kanjiapp.R;

public class Register extends Fragment {
    protected static final String TAG = "Register";
    Button btn;
    EditText login, password;
    private FirebaseAuth mAuth;
    private FirebaseFirestore mFstore;
    public Register() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_register, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        btn = getView().findViewById(R.id.registerBtn);
        login = getView().findViewById(R.id.editTextRegister);
        password = getView().findViewById(R.id.editTextPasswordRegister);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkField(login)&&checkField(password)){
                    mAuth.createUserWithEmailAndPassword(login.getText().toString(), password.getText().toString())
                    .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            Toast.makeText(getContext(),"registered successfully!",Toast.LENGTH_SHORT).show();
                            //add user to db
                        }

                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getContext(),"could not register!",Toast.LENGTH_SHORT).show();
                            Log.e(TAG, "onFailure: "+e );
                        }
                    });
                } else {
                    Toast.makeText(getContext(),"please enter email and password",Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
    private boolean checkField(EditText textField){
        boolean valid;
        if(textField.getText().toString().isEmpty()){
            textField.setError("Error");
            valid = false;
        }else {
            valid = true;
        }

        return valid;
    }
}