package pl.polsl.kanjiapp;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.Navigation;

import java.util.ArrayList;

import pl.polsl.kanjiapp.R;
import pl.polsl.kanjiapp.models.CharacterModel;

public class CharacterAdapter extends ArrayAdapter<CharacterModel> {

    public CharacterAdapter(@NonNull Context context, ArrayList<CharacterModel> charModelArrayList) {
        super(context, 0, charModelArrayList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View listitemView = convertView;
        if (listitemView == null) {
            listitemView = LayoutInflater.from(getContext()).inflate(R.layout.kanji_grid_element, parent, false);
        }

        CharacterModel characterModel = getItem(position);
        TextView kanji = listitemView.findViewById(R.id.kanji_grid_element_kanji);
        TextView comp_meaning = listitemView.findViewById(R.id.kanji_grid_element_compact_meaning);

        kanji.setText(characterModel.getKanji());
        comp_meaning.setText(characterModel.getCompact_meaning());
        Bundle bundle = new Bundle();
        listitemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bundle.putString("character", characterModel.getKanji());
                Navigation.findNavController(v).navigate(R.id.action_kanjiListView_to_singleKanjiView, bundle);

            }
        });
        return listitemView;
    }
}