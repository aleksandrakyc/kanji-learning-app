package pl.polsl.kanjiapp.views;

import static pl.polsl.kanjiapp.types.CategoryType.intToCategoryType;

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
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

import pl.polsl.kanjiapp.R;
import pl.polsl.kanjiapp.models.CharacterModel;
import pl.polsl.kanjiapp.models.SentenceModel;
import pl.polsl.kanjiapp.models.WordModel;
import pl.polsl.kanjiapp.types.CategoryType;
import pl.polsl.kanjiapp.utils.DataBaseAdapter;
import pl.polsl.kanjiapp.utils.Question;


public class Learn extends Fragment {
    protected static final String TAG = "Learn";
    int mLevel, turns;
    boolean wordEnabled, sentenceEnabled;
    CategoryType type;
    TextView questionNumber, questionType, questionDetails, evaluation;
    EditText answer;
    Button checkBtn;
    int currentQ, correctAnswrs = 0;
    DataBaseAdapter dataBaseAdapter;
    ArrayList<CharacterModel> characters;
    ArrayList<WordModel> words;
    ArrayList<SentenceModel> sentences;
    Question question;

    public Learn() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mLevel = getArguments().getInt("level");
            type = intToCategoryType(getArguments().getInt("categoryType"));
            turns = getArguments().getInt("turns");
            wordEnabled = (getArguments().getInt("wordEnabled")==1);
            sentenceEnabled = (getArguments().getInt("sentenceEnabled")==1);
            Log.d(TAG, "onCreate: level: " + mLevel + " type: " + type + " turns: " + turns + " words: " + wordEnabled + " sentences: " + sentenceEnabled);

            //get data for session
            dataBaseAdapter = new DataBaseAdapter(getContext());
            dataBaseAdapter.open();

            characters = dataBaseAdapter.getKanjiByLevel(type, mLevel);
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

        //navigation
        Bundle bundle = new Bundle();

        //question handling
        currentQ = 1;
        question = new Question(characters.get(currentQ%characters.size()));

        questionNumber.setText(currentQ+"/"+turns);

        questionType.setText(question.getQuestion());
        questionDetails.setText(question.getQuestionDetails());
        checkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //evaluate answer
                if (question.checkAnswer(answer.getText().toString())){
                    evaluation.setText("Correct");
                    correctAnswrs+=1;
                }
                else{
                    evaluation.setText("Incorrect. Correct answer: " + question.getAnswer());
                }
                //check if last question
                currentQ+=1;
                if(currentQ == turns){
                    checkBtn.setText("Finish");
                }
                if(currentQ>turns){
                    bundle.putInt("correctAnswrs", correctAnswrs);
                    bundle.putInt("turns", turns);
                    Navigation.findNavController(view).navigate(R.id.action_learn_to_learningStats, bundle);
                }
                else{
                    //generate next question
                    question = new Question(characters.get(currentQ%characters.size()));
                    questionType.setText(question.getQuestion());
                    questionDetails.setText(question.getQuestionDetails());
                    questionNumber.setText(currentQ+"/"+turns);
                }

            }
        });
    }


}