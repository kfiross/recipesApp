package com.recipesapp.recipesapp.utils;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

public class FirestoreUtils {
    public static Task<DocumentSnapshot> fetchMyFavs(){
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
}
