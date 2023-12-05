package pl.polsl.kanjiapp.views;

import static pl.polsl.kanjiapp.types.CategoryType.Custom;
import static pl.polsl.kanjiapp.types.CategoryType.intToCategoryType;

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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import pl.polsl.kanjiapp.R;
import pl.polsl.kanjiapp.models.CharacterModel;
import pl.polsl.kanjiapp.types.CategoryType;
import pl.polsl.kanjiapp.utils.DataBaseAdapter;
import pl.polsl.kanjiapp.utils.Question;
import pl.polsl.kanjiapp.utils.QuestionGenerator;


public class Learn extends Fragment {
    protected static final String TAG = "Learn";
    int mLevel, turns;
    String setId;
    boolean wordEnabled, sentenceEnabled, contentLoaded;
    CategoryType type;
    TextView questionNumber, questionType, questionDetails, evaluation;
    EditText answer;
    Button checkBtn;
    int currentQ, correctAnswrs = 0;
    DataBaseAdapter dataBaseAdapter;
    ArrayList<CharacterModel> characters;
    ArrayList<Question> questions;
    Question question;
    private FirebaseAuth mAuth;
    private FirebaseFirestore mFstore;
    FirebaseUser user;
    ProgressBar progressBar;

    public Learn() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        mFstore = FirebaseFirestore.getInstance();
        user = mAuth.getCurrentUser();
        contentLoaded = false;
        if (getArguments() != null) {
            //get data for session
            dataBaseAdapter = new DataBaseAdapter(getContext());
            dataBaseAdapter.open();

            type = intToCategoryType(getArguments().getInt("categoryType"));
            if (type == CategoryType.Custom){
                setId = getArguments().getString("level");
                Log.d(TAG, "onCreate: " + getArguments().getString("level"));

            }
            else{
                mLevel = getArguments().getInt("level");
                characters = dataBaseAdapter.getKanjiByLevel(type, mLevel);
            }
            turns = getArguments().getInt("turns");
            wordEnabled = (getArguments().getInt("wordEnabled")==1);
            sentenceEnabled = (getArguments().getInt("sentenceEnabled")==1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_learn, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //new idea:
        //generate all questions at the beginning and store them so that the random questions make sense
        //sample 10 kanjis for 10 questions for example

        //findViewById
        questionNumber = getView().findViewById(R.id.questionNumber);
        questionType = getView().findViewById(R.id.questionType);
        questionDetails = getView().findViewById(R.id.question);

        evaluation = getView().findViewById(R.id.textViewAnswer);
        answer = getView().findViewById(R.id.editTextAnswer);

        checkBtn = getView().findViewById(R.id.checkBtn);

        progressBar = getView().findViewById(R.id.progressBar);

        //navigation
        Bundle bundle = new Bundle();
        //question handling
        if (type == Custom){
            answer.setVisibility(View.GONE);
            checkBtn.setVisibility(View.GONE);
            getCharactersCustomSet();
        }
        else{
            loadQuestion(0);
        }
        checkBtn.setOnClickListener(new View.OnClickListener() {

            int n = 0;
            @Override
            public void onClick(View v) {
                //evaluate answer
                checkAnswer();
                n++;
                loadQuestion(n);
            }
        });

    }

    private void updateEasinessFactors(String kanji){
        DocumentReference df = mFstore.collection("Users").document(user.getUid()).collection("Sets").document(setId);
        double EF = 0;

        df.update(
                "Kanji_list."+kanji, EF
        );
    }

    private void getCharactersCustomSet(){
        Log.d(TAG, "getCharactersCustomSet: " + setId);
        DocumentReference df = mFstore.collection("Users").document(user.getUid()).collection("Sets").document(setId);

        //get kanji details from list of characters
        Set<String> characterSet = new TreeSet<>();
        df.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                //TODO check this cast
                HashMap<String, Double> kanjiList = (HashMap<String, Double>) documentSnapshot.get("Kanji_list");

                kanjiList.forEach((key, value) -> {
                    characterSet.add(key);
                    Log.d(TAG, "onSuccess: " + value);
                });
                Log.d(TAG, "onSuccess: " + characterSet);

                answer.setVisibility(View.VISIBLE);
                checkBtn.setVisibility(View.VISIBLE);
                characters = dataBaseAdapter.getKanjiDetailsFromSet(characterSet);
                QuestionGenerator generator = new QuestionGenerator(characters, kanjiList, turns, wordEnabled, sentenceEnabled);
                questions = generator.generateQuestions();
                loadQuestion(0);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), "No such set", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadQuestion(int n){
        Log.d(TAG, "loadQuestion: "+n);
        question = questions.get(n);
        questionNumber.setText((n+1)+"/"+turns);
        questionType.setText(question.getQuestion());
        questionDetails.setText(question.getQuestionDetails());
    }

    private void checkAnswer(){
        String userAnswer = answer.getText().toString();
        if(question.checkAnswer(userAnswer)){
            evaluation.setText("Correct");
        }
        else{
            evaluation.setText("Incorrect. Correct answers: " + question.getAnswer());
        }
    }

}