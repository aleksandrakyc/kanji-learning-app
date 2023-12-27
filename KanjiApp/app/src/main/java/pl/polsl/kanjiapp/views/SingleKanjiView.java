package pl.polsl.kanjiapp.views;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import pl.polsl.kanjiapp.R;
import pl.polsl.kanjiapp.utils.DataBaseAdapter;
import pl.polsl.kanjiapp.models.*;
import pl.polsl.kanjiapp.types.Jlpt;
import pl.polsl.kanjiapp.utils.FirestoreAdapter;

public class SingleKanjiView extends Fragment {
    protected static final String TAG = "SingleKanjiView";
    private String mCharacter;
    CharacterModel character;
    private Button button;
    private FirestoreAdapter firestore;
    private ArrayList<String> setChoices;
    DataBaseAdapter dataBaseAdapter;
    TextView kanjiView, kunyomiView, onyomiView, meaningView, wordView, sentenceView;

    public SingleKanjiView() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mCharacter = getArguments().getString("character");
        }
        firestore = new FirestoreAdapter();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_single_kanji_view, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        dataBaseAdapter = new DataBaseAdapter(getContext());
//        dataBaseAdapter.createDatabase();
        dataBaseAdapter.open();
        //TODO ADD CHECKS, SOMETIMES CRASHES
        character = dataBaseAdapter.getKanjiByCharacter(mCharacter.charAt(0));

        kanjiView = getView().findViewById(R.id.kanjiView);
        kanjiView.setText(character.getKanji());

        onyomiView = getView().findViewById((R.id.onyomiView));
        onyomiView.setText(String.join(", ", character.getOnyomi()));

        kunyomiView = getView().findViewById((R.id.kunyomiView));
        kunyomiView.setText(String.join(", ", character.getKunyomi()));

        meaningView = getView().findViewById((R.id.meaningView));
        meaningView.setText(String.join(", ", character.getMeaning()));

        wordView = getView().findViewById(R.id.wordView);
        wordView.setMovementMethod(new ScrollingMovementMethod());
        String wordText = "";
        List<WordModel> wordModelList = dataBaseAdapter.getWordsByKanjiAndLevelOrLower(character.getKanji().charAt(0), character.getJlpt());
        if(!wordModelList.isEmpty())
            for (WordModel word: wordModelList){
                wordText += (word.getWordAndMeaning() + "\n");
            }
        wordView.setText(wordText);
        List<SentenceModel> sentenceModelList = dataBaseAdapter.getSentencesByKanji(character.getKanji());
        if(!sentenceModelList.isEmpty()){
            SentenceModel sentence = sentenceModelList.get(0);
            sentenceView = getView().findViewById((R.id.exampleSentenceView));
            sentenceView.setText(sentence.getJapanese()+"\n"+sentence.getEnglish());
        }

        //button
        button = getView().findViewById(R.id.buttonSet);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setChoices = new ArrayList<>();
                firestore.getUserSets().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<DocumentSnapshot> documents = queryDocumentSnapshots.getDocuments();
                        documents.forEach(documentSnapshot -> setChoices.add(documentSnapshot.getId()));
                        Log.d(TAG, "onSuccess: " + setChoices);

                        String[] choices = new String[setChoices.size()];
                        for (int i = 0; i<setChoices.size(); i++){
                            choices[i] = setChoices.get(i).substring(28);
                        }

                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                        builder
                                .setTitle("Add character to set")
                                .setPositiveButton("Add", (dialog, which) -> {
                                    int selectedPosition = ((AlertDialog)dialog).getListView().getCheckedItemPosition();
                                    addCharacterToSet(setChoices.get(selectedPosition));
                                })
                                .setNegativeButton("Cancel", (dialog, which) -> {

                                })
                                .setSingleChoiceItems(choices, 0, (dialog, which) -> {

                                });

                        AlertDialog dialog = builder.create();
                        dialog.show();
                    }
                });
            }
        });
    }
    private void addCharacterToSet(String setId){
        Log.d(TAG, "addCharacterToSet: "+setId);
        firestore.addCharacterToSet(setId, character).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(getContext(), "Added character " + character.getKanji() + "to set", Toast.LENGTH_SHORT).show();
            }
        });
    }
}