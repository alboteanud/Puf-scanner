package com.empact.android.pufin.viewModel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.empact.android.pufin.dataSource.Repository;
import com.empact.android.pufin.model.ResponseItem;

public class PhotoViewModel extends ViewModel {

    private MutableLiveData<Boolean> isLoading;
    private MutableLiveData<ResponseItem> responseItemMutableLiveData;
    private final Repository repository;

    public PhotoViewModel() {
        repository = Repository.getInstance();
    }

    public void doRegisterPhoto(String photoPath) {
        clearResponse();
        if (photoPath == null || photoPath.isEmpty()) return;
        repository.registerPhoto(photoPath);
    }

    public void doValidatePhoto(String photoPath) {
        clearResponse();
        if (photoPath == null || photoPath.isEmpty()) return;
        repository.validatePhoto(photoPath);
    }

    public MutableLiveData<Boolean> getIsLoading() {
        if (isLoading == null) {
            isLoading = repository.getIsLoading();
        }
        return isLoading;
    }

    public MutableLiveData<ResponseItem> getResponse() {
        if (responseItemMutableLiveData == null) {
            responseItemMutableLiveData = repository.getResponse();
        }
        return responseItemMutableLiveData;
    }

    public void clearResponse(){
        repository.clearResponse();
    }

}