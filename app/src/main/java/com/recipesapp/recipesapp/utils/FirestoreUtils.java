package com.recipesapp.recipesapp.utils;

import androidx.annotation.Nullable;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

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
}
