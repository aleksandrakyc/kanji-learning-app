package pl.polsl.kanjiapp.views;

import static pl.polsl.kanjiapp.types.CategoryType.Custom;
import static pl.polsl.kanjiapp.types.CategoryType.intToCategoryType;
import static pl.polsl.kanjiapp.types.QuestionType.CHAR_ONE_OFF;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
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
    boolean wordEnabled, sentenceEnabled;
    CategoryType type;
    TextView questionNumber, questionType, questionDetails, evaluation;
    EditText answer;
    Button checkBtn;
    DataBaseAdapter dataBaseAdapter;
    ArrayList<CharacterModel> characters;
    ArrayList<Question> questions;
    HashMap<String, Pair<Integer, Integer>> scores;
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
                //characters = dataBaseAdapter.getKanjiByLevel(type, mLevel);
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

        //findViewById
        questionNumber = getView().findViewById(R.id.questionNumber);
        questionType = getView().findViewById(R.id.questionType);
        questionDetails = getView().findViewById(R.id.question);

        evaluation = getView().findViewById(R.id.textViewAnswer);
        answer = getView().findViewById(R.id.editTextAnswer);

        checkBtn = getView().findViewById(R.id.checkBtn);

        progressBar = getView().findViewById(R.id.progressBar);

        scores = new HashMap<>();

        answer.setVisibility(View.GONE);
        checkBtn.setVisibility(View.GONE);
        //question handling
        if (type == Custom){
            getCharactersCustomSet();
        }
        else{
            getPredefinedSetUserData();
        }
        checkBtn.setOnClickListener(new View.OnClickListener() {
            int n = 0;
            @Override
            public void onClick(View v) {

                checkAnswer();
                //evaluate answer
                if(n==turns-1){
                    evaluate();
                    Log.d(TAG, "onClick: bye");
                    Bundle bundle = new Bundle();
                    bundle.putInt("turns", turns);
                    //bundle.putInt("correctAnswrs", Collections.frequency(answers, Boolean.TRUE));
                    Navigation.findNavController(view).navigate(R.id.action_learn_to_learningStats, bundle);
                }
                n++;
                if (n==turns-1){
                    checkBtn.setText("Finish");
                    Log.d(TAG, "onClick: finish" + n);
                }
                if (n<=turns-1)
                    loadQuestion(n);
            }
        });

    }

    private void getCharactersCustomSet(){
        Log.d(TAG, "getCharactersCustomSet: " + setId);
        DocumentReference df = mFstore.collection("Users").document(user.getUid()).collection("Sets").document(setId);

        //get kanji details from list of characters
        Set<String> characterSet = new TreeSet<>();
        df.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                getSetDataFromFstore(documentSnapshot);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), "No such set", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void changeView() {

        ViewFlipper viewFlipper = getView().findViewById(R.id.viewFlipper);
        switch (question.getQuestionType()) {
            case CHAR_ONE_OFF:
                Log.d(TAG, "changeView: changing to buttons");
                viewFlipper.setDisplayedChild(viewFlipper.indexOfChild(getView().findViewById(R.id.choiceBtns)));

                break;
            default:
                Log.d(TAG, "changeView: changing to edittext");
                viewFlipper.setDisplayedChild(viewFlipper.indexOfChild(getView().findViewById(R.id.editTextAnswer)));
        }
    }
    private void loadQuestion(int n){
        Log.d(TAG, "loadQuestion: "+n);
        question = questions.get(n);
        questionNumber.setText((n+1)+"/"+turns);
        questionType.setText(question.getQuestion());
        questionDetails.setText(question.getQuestionDetails());

        changeView();
    }

    private void checkAnswer(){
        String userAnswer = answer.getText().toString();
        int correct;
        if(question.checkAnswer(userAnswer)){
            evaluation.setText("Correct");
            correct = 1;
        }
        else{
            evaluation.setText("Incorrect. Correct answers: " + question.getAnswer());
            correct = 0;
        }
        Pair<Integer, Integer> pair = scores.get(question.getKanji());
        if (pair == null)
            scores.put(question.getKanji(), new Pair<>(1,correct));
        else
            scores.put(question.getKanji(), new Pair<>(pair.first+1,pair.second+correct));

    }

    private void updateEasinessFactors(HashMap<String, Double> kanjiEFMap){
        DocumentReference df;
        if(type == Custom)
            df = mFstore.collection("Users").document(user.getUid()).collection("Sets").document(setId);
        else{
            df = mFstore.collection("Users").document(user.getUid()).collection("Predefined").document(type.name()+mLevel);
        }
        df.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                //foreach
                kanjiEFMap.forEach((kanji, EF) -> {
                    double oldEF = (double) documentSnapshot.get("Kanji_list."+kanji);
                    Log.d(TAG, "onSuccess: "+oldEF);
                    double newEF = oldEF + EF;
                    if(newEF<1.3)
                        newEF = 1.3;
                    df.update(
                            "Kanji_list."+kanji, newEF
                    );
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }
    private void evaluate(){
        HashMap<String, Double> kanjiEFMap = new HashMap<>();
        scores.forEach((kanji, pair) -> {
            double q = ((double) pair.second/(double) pair.first)*5;
            double newValue = 0.1 - (5-q)*(0.08+(5-q)*0.02);
            Log.d(TAG, "evaluate: " + q);
            kanjiEFMap.put(kanji, newValue);
        });
        //change this so that updates all at once
        if(user!=null){
            updateEasinessFactors(kanjiEFMap);
        }
    }
    private void createPredefinedSetUserData(){
        //create set
        characters = dataBaseAdapter.getKanjiByLevel(type, mLevel);
        QuestionGenerator generator;
        if (user != null) {
            String docId = type.name()+mLevel;
            DocumentReference df = mFstore.collection("Users").document(user.getUid()).collection("Predefined").document(docId);

            Map<String, Object> setInfo = new HashMap<>();
            Map<String, Double> kanjiInfo = new HashMap<>();
            characters.forEach(c->kanjiInfo.put(c.getKanji(), 2.5));

            setInfo.put("Owner", user.getUid());
            setInfo.put("Kanji_list", kanjiInfo);
            setInfo.put("Set_name", docId);
            df.set(setInfo);
            generator = new QuestionGenerator(characters, kanjiInfo, turns, wordEnabled, sentenceEnabled);

        }
        else {
            generator = new QuestionGenerator(characters, turns, wordEnabled, sentenceEnabled);
        }
        //proceed to learning
        questions = generator.generateQuestions();
        turns = generator.getTurns();
        answer.setVisibility(View.VISIBLE);
        checkBtn.setVisibility(View.VISIBLE);
        loadQuestion(0);
    }

    private void getSetDataFromFstore(DocumentSnapshot document){
        Set<String> characterSet = new TreeSet<>();
        HashMap<String, Double> kanjiList = (HashMap<String, Double>) document.get("Kanji_list");

        kanjiList.forEach((key, value) -> {
            characterSet.add(key);
            Log.d(TAG, "onSuccess: " + value);
        });
        Log.d(TAG, "onSuccess: " + characterSet);

        answer.setVisibility(View.VISIBLE);
        checkBtn.setVisibility(View.VISIBLE);
        if(type == Custom)
            characters = dataBaseAdapter.getKanjiDetailsFromSet(characterSet);
        else{
            characters = dataBaseAdapter.getKanjiByLevel(type, mLevel);
        }
        QuestionGenerator generator = new QuestionGenerator(characters, kanjiList, turns, wordEnabled, sentenceEnabled);
        questions = generator.generateQuestions();
        turns = generator.getTurns();
        loadQuestion(0);
    }

    private void getPredefinedSetUserData(){
        if (user == null){
            createPredefinedSetUserData();
        }
        else {
            String docId = type.name()+mLevel;
            DocumentReference df = mFstore.collection("Users").document(user.getUid()).collection("Predefined").document(docId);
            df.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            Log.d(TAG, "Document exists!");
                            //get data
                            getSetDataFromFstore(document);
                        } else {
                            Log.d(TAG, "Document does not exist!");
                            //create new document
                            createPredefinedSetUserData();
                        }
                    } else {
                        Log.d(TAG, "Failed with: ", task.getException());
                    }
                }
            });
        }
    }
}