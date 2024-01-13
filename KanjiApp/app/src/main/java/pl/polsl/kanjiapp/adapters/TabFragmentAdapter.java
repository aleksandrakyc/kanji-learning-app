package pl.polsl.kanjiapp.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import pl.polsl.kanjiapp.views.CategoryListCustom;
import pl.polsl.kanjiapp.views.CategoryListHomework;
import pl.polsl.kanjiapp.views.CategoryListPredefined;

public class TabFragmentAdapter extends FragmentStateAdapter {
    public TabFragmentAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    public TabFragmentAdapter(@NonNull Fragment fragment) {
        super(fragment);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                return new CategoryListPredefined();
            case 1:
                return new CategoryListCustom();
            case 2:
                return new CategoryListHomework();
            default:
                return new CategoryListHomework();
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
