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

import pl.polsl.kanjiapp.R;
import pl.polsl.kanjiapp.utils.FirestoreAdapter;

public class Register extends Fragment {
    protected static final String TAG = "Register";
    Button btn;
    EditText login, password;
    Switch isTeacherRequest;
    boolean request = false;
    private FirestoreAdapter firestore;
    public Register() {    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firestore = new FirestoreAdapter();
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
        isTeacherRequest = getView().findViewById(R.id.teacherSwitch);

        if (firestore.getUser() != null){
            Toast.makeText(getContext(),"log out first to create a new account.",Toast.LENGTH_SHORT).show();
            Navigation.findNavController(view).navigate(R.id.action_startMenu_to_CategoryList);
        }
        isTeacherRequest.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    request = true;
                } else {
                    request = false;
                }
            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkField(login)&&checkField(password)){
                    firestore.createUser(login.getText().toString(), password.getText().toString(), request);
                    //todo check if user exists
                    if(firestore.getUserInfo(login.getText().toString())!=null)
                        Navigation.findNavController(view).navigate(R.id.action_register_to_login);
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

