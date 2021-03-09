package com.foodiz.app.utils;

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
import com.foodiz.app.model.Recipe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class FirestoreUtils {


    public static Task<DocumentReference> addRecipe(Recipe recipe) {
        return FirebaseFirestore.getInstance()
                .collection("recipes")
                .add(recipe.toJson());
    }




    public static Task<DocumentSnapshot> getUser(){

        return FirebaseFirestore.getInstance().collection("users").
                document(
                        FirebaseAuth.getInstance().
                                getCurrentUser().
                                getUid()
                ).get();
    }

    public static Task<Void> addRecipeToFev(String recipeId){

        return FirebaseFirestore.getInstance().collection("users").
                document(
                        FirebaseAuth.getInstance()
                                .getCurrentUser()
                                .getUid()
                ).update(
                "favourites", FieldValue.arrayUnion(recipeId)
        );
    }

    public static Task<Void> delRecipeFromFev(String recipeId){

        return FirebaseFirestore.getInstance().collection("users").
                document(
                        FirebaseAuth.getInstance()
                                .getCurrentUser()
                                .getUid()
                ).update(
                "favourites", FieldValue.arrayRemove(recipeId)
        );
    }

    public static DocumentSnapshot recipeFromDb(String id) throws ExecutionException, InterruptedException {

        return Tasks.await(FirebaseFirestore.getInstance().collection("recipes").document(id).get());
    }





    public static Task<Void> addToMyRecipes(String recipeId){
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        return FirebaseFirestore.getInstance().collection("users").document(uid).update(
                "my", FieldValue.arrayUnion(recipeId)
        );
    }


    public static UploadTask uploadPhoto(Uri imageUri, String path) {
        StorageReference imageRef = FirebaseStorage.getInstance().getReference(path);
        return imageRef.putFile(imageUri);
    }

    public static ArrayList<Recipe> searchRecipes(ArrayList<String> results) throws ExecutionException, InterruptedException {
        ArrayList<Recipe> tempRecipes = new ArrayList<>();

        Task<QuerySnapshot> taskRes = FirebaseFirestore.getInstance().collection("recipes").get();
        Tasks.await(taskRes);
        if(taskRes.isSuccessful()){
            List<DocumentSnapshot> docs = taskRes.getResult().getDocuments();
            for (DocumentSnapshot doc: docs){
                Recipe recipe = Recipe.fromDocument(doc);
                for(String res : results){


                    if(recipe.hasIngredientsWithName(res))
                    {
                        tempRecipes.add(recipe);
                        break;
                    }
                }
            }
        }

        return tempRecipes;
    }




    public static Task<Void> deleteRecipe(String recipeId){
        return FirebaseFirestore.getInstance().collection("recipes")
                .document(recipeId)
                .delete();
    }

    public static Task<Void> updateRecipe(Recipe recipe){
        return FirebaseFirestore.getInstance().collection("recipes")
                .document(recipe.getId())
                .update(recipe.toJson());
    }




    public static Task<Void> initalizinguserdata(@Nullable String userId) {
        if(userId == null) {
            userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        }

        HashMap<String, Object> map = new HashMap<>();
        map.put("favourites", new ArrayList<String>());
        return FirebaseFirestore.getInstance()
                .collection("users")
                .document(userId)
                .set(map);
    }
}
