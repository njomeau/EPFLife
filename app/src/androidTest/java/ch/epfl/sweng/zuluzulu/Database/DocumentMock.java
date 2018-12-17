package ch.epfl.sweng.zuluzulu.Database;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.Map;

import ch.epfl.sweng.zuluzulu.Firebase.Database.DatabaseCollection;
import ch.epfl.sweng.zuluzulu.Firebase.Database.DatabaseDocument;
import ch.epfl.sweng.zuluzulu.Firebase.Database.OperationWithFirebaseMap;
import ch.epfl.sweng.zuluzulu.Firebase.FirebaseMapDecorator;
import ch.epfl.sweng.zuluzulu.IdlingResource.IdlingResourceFactory;
import ch.epfl.sweng.zuluzulu.Utility;

public class DocumentMock implements DatabaseDocument {
    private static final String TAG = "DocumentMock";

    @Override
    public Task<Void> set(@NonNull Map<String, Object> data) {
 android.util.Log.d("Function called", "set");

        // Just print instead of pushing to firebase
        for (String key : data.keySet()) {
            Log.d(TAG, key);
        }

        return new TaskMock<>();
    }

    @Override
    public Task<Void> update(String field, Object value, Object... moreFieldAndValues) {
 android.util.Log.d("Function called", "update");
        Log.d(TAG, field + " -> " + value);
        return new TaskMock<>();
    }

    @Override
    public Task<Void> update(Map<String, Object> data) {
 android.util.Log.d("Function called", "update");
        return new TaskMock<>();
    }

    @Override
    public Task<DocumentSnapshot> getAndAddOnSuccessListener(OperationWithFirebaseMap listener) {
 android.util.Log.d("Function called", "getAndAddOnSuccessListener");
        Map<String, Object> map = Utility.createMapWithAll();

        FirebaseMapDecorator fmap = new FirebaseMapDecorator(map);
        listener.apply(fmap);
        IdlingResourceFactory.incrementCountingIdlingResource();
        return new TaskMock<>();
    }


    @Override
    public DatabaseCollection collection(String messages) {
 android.util.Log.d("Function called", "collection");
        return new CollectionMock();
    }

    @Override
    public String getId() {
 android.util.Log.d("Function called", "getId");
        return "1";
    }
}
