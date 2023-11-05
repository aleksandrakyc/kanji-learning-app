//package pl.polsl.kanjiapp;
//
//import android.content.Context;
//import android.os.Bundle;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ArrayAdapter;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.navigation.Navigation;
//import androidx.recyclerview.widget.RecyclerView;
//
//import java.util.ArrayList;
//
//import pl.polsl.kanjiapp.R;
//
//public class CategoryListAdapter extends RecyclerView.Adapter<CategoryListAdapter.ViewHolder> {
//
//    private String[] localDataSet;
//
//    /**
//     * Provide a reference to the type of views that you are using
//     * (custom ViewHolder)
//     */
//    public static class ViewHolder extends RecyclerView.ViewHolder {
//        private final TextView textView;
//
//        public ViewHolder(View view) {
//            super(view);
//            // Define click listener for the ViewHolder's View
//
//            textView = (TextView) view.findViewById(R.id.textView);
//        }
//
//        public TextView getTextView() {
//            return textView;
//        }
//    }
//
//    /**
//     * Initialize the dataset of the Adapter
//     *
//     * @param dataSet String[] containing the data to populate views to be used
//     * by RecyclerView
//     */
//    public CategoryListAdapter(String[] dataSet) {
//        localDataSet = dataSet;
//    }
//
//    // Create new views (invoked by the layout manager)
//    @Override
//    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
//        // Create a new view, which defines the UI of the list item
//        View view = LayoutInflater.from(viewGroup.getContext())
//                .inflate(R.layout.category_list_item, viewGroup, false);
//
//        return new ViewHolder(view);
//    }
//
//    // Replace the contents of a view (invoked by the layout manager)
//    @Override
//    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
//        int pos = position+1;
//        Bundle bundle = new Bundle();
//        viewHolder.textView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                bundle.putInt("level", pos);
//                Toast.makeText(view.getContext(), "Recycle Click" + pos, Toast.LENGTH_SHORT).show();
//                Navigation.findNavController(view).navigate(R.id.action_startMenu_to_kanjiListView, bundle);
//            }
//        });
//        // Get element from your dataset at this position and replace the
//        // contents of the view with that element
//        viewHolder.getTextView().setText(localDataSet[position]);
//    }
//
//    // Return the size of your dataset (invoked by the layout manager)
//    @Override
//    public int getItemCount() {
//        return localDataSet.length;
//    }
//}
