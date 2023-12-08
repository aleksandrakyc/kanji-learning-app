package pl.polsl.kanjiapp.views;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import pl.polsl.kanjiapp.R;

public class Register extends Fragment {
    protected static final String TAG = "Register";
    Button btn;
    EditText login, password;
    Switch isTeacher;
    int isTeacherValue = 0;
    private FirebaseAuth mAuth;
    private FirebaseFirestore mFstore;
    public Register() {    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        mFstore = FirebaseFirestore.getInstance();
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
        isTeacher = getView().findViewById(R.id.teacherSwitch);

        if (mAuth.getCurrentUser() != null){
            Toast.makeText(getContext(),"log out first to create a new account.",Toast.LENGTH_SHORT).show();
            Navigation.findNavController(view).navigate(R.id.action_startMenu_to_CategoryList);
        }
        isTeacher.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    isTeacherValue = 1;
                } else {
                    isTeacherValue = 0;
                }
            }
        });

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
                            FirebaseUser user = mAuth.getCurrentUser();
                            DocumentReference df = mFstore.collection("Users").document(user.getUid());
                            Map<String, Object> userInfo = new HashMap<>();
                            userInfo.put("isTeacher", ""+isTeacherValue);
                            df.set(userInfo);
                            Navigation.findNavController(view).navigate(R.id.action_register_to_login);
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

