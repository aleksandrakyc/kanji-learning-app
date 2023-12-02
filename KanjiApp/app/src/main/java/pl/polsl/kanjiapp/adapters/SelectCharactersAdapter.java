package pl.polsl.kanjiapp.adapters;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Set;
import java.util.TreeSet;

import pl.polsl.kanjiapp.R;
import pl.polsl.kanjiapp.models.CharacterModel;

public class SelectCharactersAdapter extends RecyclerView.Adapter<SelectCharactersAdapter.ViewHolder> {

    private ArrayList<CharacterModel> charModelArrayList;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    private Set<CharacterModel> selectedCharacters;

    public SelectCharactersAdapter(Context context, ArrayList<CharacterModel> charModelArrayList) {
        this.mInflater = LayoutInflater.from(context);
        this.charModelArrayList = charModelArrayList;
        this.selectedCharacters = new TreeSet<>();
    }

    // inflates the cell layout from xml when needed
    @Override
    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.kanji_grid_element, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each cell
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CharacterModel character = charModelArrayList.get(position);
        holder.kanji.setText(character.getKanji());
        holder.meaning.setText(character.getCompact_meaning());
        //todo doesnt work
        if(selectedCharacters.contains(character)){
            holder.itemView.setBackgroundColor(Color.parseColor("#A8DADC"));
        }
        else {
            holder.itemView.setBackgroundColor(Color.parseColor("#F1FAEE"));
        }
        //notifyItemChanged(position);
    }

    // total number of cells
    @Override
    public int getItemCount() {
        return charModelArrayList.size();
    }

    public void setSelectedCharacters(Set<CharacterModel> selectedCharacters) {
        this.selectedCharacters = selectedCharacters;
    }

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView kanji;
        TextView meaning;

        ViewHolder(View itemView) {
            super(itemView);
            kanji = itemView.findViewById(R.id.kanji_grid_element_kanji);
            meaning = itemView.findViewById(R.id.kanji_grid_element_compact_meaning);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // convenience method for getting data at click position
    CharacterModel getItem(int id) {
        return charModelArrayList.get(id);
    }

    // allows clicks events to be caught
    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}