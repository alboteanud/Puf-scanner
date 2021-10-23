package com.empact.android.pufin.dataSource;

import androidx.lifecycle.MutableLiveData;

import com.empact.android.pufin.model.ResponseItem;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;


public class Repository {
    private static Repository repository;
    private final NetworkDataSource networkDataSource;
    private final AppExecutor mExecutor;

    // private constructor restricted to this class itself
    private Repository(NetworkDataSource networkDataSource, AppExecutor appExecutor) {
        this.mExecutor = appExecutor;
        this.networkDataSource = networkDataSource;
    }

    // static method to create instance of Repository class
    public static Repository getInstance() {
        if (repository == null) {
            NetworkDataSource networkDataSource = NetworkDataSource.getInstance();
            Executor executor = Executors.newSingleThreadExecutor();
            AppExecutor appExecutor = new AppExecutor(executor);
            repository = new Repository(networkDataSource, appExecutor);
        }

        return repository;
    }

    private boolean isFetchNeeded() {
        // TODO check timestamp - latest data added to DB
        return true;
    }

    public MutableLiveData<Boolean> getIsLoading() {
        return networkDataSource.isLoading;
    }

    public void registerPhoto(String photoPath) {
        mExecutor.execute(() -> networkDataSource.registerPhoto(photoPath));
    }

    public void validatePhoto(String photoPath) {
        mExecutor.execute(() -> networkDataSource.validatePhoto(photoPath));
    }

    public MutableLiveData<ResponseItem> getResponse() {
        return networkDataSource.responseLiveData;
    }

    public void clearResponse() {
        networkDataSource.responseLiveData.postValue(null);
    }
}
