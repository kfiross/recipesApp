package com.recipesapp.recipesapp.views.adapters;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.recipesapp.recipesapp.MainActivity;
import com.recipesapp.recipesapp.R;
import com.recipesapp.recipesapp.databinding.ItemCategoryLayoutBinding;
import com.recipesapp.recipesapp.model.Category;

import java.util.ArrayList;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ItemViewHolder>{

    private ArrayList<Category> mCategories;

    public CategoryAdapter(ArrayList<Category> categories) {
        mCategories = categories;
    }

    @NonNull
    @Override
    public CategoryAdapter.ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ItemCategoryLayoutBinding itemBinding =
                ItemCategoryLayoutBinding.inflate(layoutInflater, parent, false);

        return new ItemViewHolder(itemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryAdapter.ItemViewHolder holder, int position) {
        holder.bind(mCategories.get(position));
    }

    @Override
    public int getItemCount() {
        return mCategories.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder{

        private final NavController mNavController;
        private final ItemCategoryLayoutBinding itemBinding;

        public ItemViewHolder(ItemCategoryLayoutBinding itemBinding) {
            super(itemBinding.getRoot());
            this.itemBinding = itemBinding;
            mNavController = Navigation.findNavController(
                    (MainActivity) itemBinding.getRoot().getContext(), R.id.nav_host_fragment);
        }

        public void bind(Category category) {
            itemBinding.setName(category.getName().get("he"));

//            int resId = Category.getBgById(category.getId());
//            itemBinding.setBackground(
//                    ResourcesCompat.getDrawable(itemView.getResources(), resId, null)
//
//            );

            String imageBg = "";
            switch (category.getId()){
                case "0":
                    imageBg = "https://happykitchen.co.il/wp-content/uploads/2019/01/%D7%A2%D7%95%D7%92%D7%AA-%D7%9E%D7%9E%D7%AA%D7%A7%D7%99%D7%9D.jpg";
                    break;

                case "1":
                    imageBg = "https://images.immediate.co.uk/production/volatile/sites/30/2020/08/butter-chicken-cf6f9e2.jpg";
                    break;

                case "2":
                    imageBg = "https://www.simplyhappyfoodie.com/wp-content/uploads/2019/08/instant-pot-beef-tips-1.jpg";
                    break;

                case "3":
                    imageBg = "https://images.summitmedia-digital.com/yummyph/images/2017/03/24/cilantro-calamansi-fish.jpg";
                    break;

                case "4":
                    imageBg = "https://cdn77-s3.lazycatkitchen.com/wp-content/uploads/2019/07/vegan-summer-pasta-close-up-800x1200.jpg";
                    break;

            }

            itemBinding.setImage(imageBg);

            itemBinding.getRoot().setOnClickListener(v -> {
                Bundle args = new Bundle();
                args.putInt("category_id", Integer.parseInt(category.getId()));
                args.putString("category_name", category.getName().get("he"));
                mNavController.navigate(R.id.recipesFragment, args);
            });
        }
    }
}
