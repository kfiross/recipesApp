package com.recipesapp.recipesapp.utils;

import android.net.Uri;

import androidx.annotation.Nullable;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.recipesapp.recipesapp.model.Recipe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class FirestoreUtils {
    public static Task<DocumentSnapshot> fetchUser(){
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        return FirebaseFirestore.getInstance().collection("users").document(uid).get();
    }

    public static Task<Void> addToMyFavs(String recipeId){
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        return FirebaseFirestore.getInstance().collection("users").document(uid).update(
                "favourites", FieldValue.arrayUnion(recipeId)
        );
    }

    public static Task<Void> removeFromMyFavs(String recipeId){
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        return FirebaseFirestore.getInstance().collection("users").document(uid).update(
                "favourites", FieldValue.arrayRemove(recipeId)
        );
    }

    public static DocumentSnapshot getRecipe(String id) throws ExecutionException, InterruptedException {
        Task<DocumentSnapshot> task = FirebaseFirestore.getInstance().collection("recipes").document(id).get();
        return Tasks.await(task);
    }

    public static Task<Void> initUserData(@Nullable String uid) {
        if(uid == null) {
            uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        }
        HashMap<String, Object> map = new HashMap<>();
        map.put("favourites", new ArrayList<String>());
        return FirebaseFirestore.getInstance()
                .collection("users")
                .document(uid)
                .set(map);
    }

    public static Task<DocumentReference> addRecipe(Recipe recipe) {
        return FirebaseFirestore.getInstance()
                .collection("recipes")
                .add(recipe.toJson());
    }

    public static Task<Void> addToMyRecipes(String recipeId){
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        return FirebaseFirestore.getInstance().collection("users").document(uid).update(
                "my", FieldValue.arrayUnion(recipeId)
        );
    }

    public static ArrayList<Recipe> searchRecipes(ArrayList<String> queries) throws ExecutionException, InterruptedException {
        ArrayList<Recipe> recipes = new ArrayList<>();

        Task<QuerySnapshot> task = FirebaseFirestore.getInstance().collection("recipes").get();
        Tasks.await(task);
        if(task.isSuccessful()){
            List<DocumentSnapshot> docs = task.getResult().getDocuments();
            for (DocumentSnapshot doc: docs){
                Recipe recipe = Recipe.fromDocument(doc);
                for(String q : queries){

                    // add this recipe
                    if(recipe.hasIngredientsWithName(q)){
                        recipes.add(recipe);
                        break;
                    }
                }
            }
        }

        return recipes;
    }

    public static UploadTask uploadPhoto(Uri imageUri, String path) {
        StorageReference imageRef = FirebaseStorage.getInstance().getReference(path);
        return imageRef.putFile(imageUri);
    }

    public static Task<Void> updateRecipe(Recipe recipe){
        return FirebaseFirestore.getInstance().collection("recipes")
                .document(recipe.getId())
                .update(recipe.toJson());
    }

    public static Task<Void> deleteRecipe(String recipeId){
        return FirebaseFirestore.getInstance().collection("recipes")
                .document(recipeId)
                .delete();
    }
}
