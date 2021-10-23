package com.empact.android.pufin.dataSource;

import androidx.lifecycle.MutableLiveData;

import com.empact.android.pufin.BuildConfig;
import com.empact.android.pufin.model.ResponseItem;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import timber.log.Timber;

public class NetworkDataSource {
    private static NetworkDataSource networkDataSource;


    public final MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    public final MutableLiveData<ResponseItem> responseLiveData = new MutableLiveData<>();

    private NetworkDataSource() {
    }

    public static NetworkDataSource getInstance() {
        if (networkDataSource == null) {
            networkDataSource = new NetworkDataSource();
        }
        return networkDataSource;
    }

    public void registerPhoto(String photoPath) {
        String url = BuildConfig.DEBUG ? Configuration.photoUrlRegisterTest : Configuration.photoUrlRegisterTest;
        ResponseItem responseItem = new ResponseItem(ResponseItem.Type.REGISTRATION);
        uploadPhoto(photoPath, url, responseItem);
    }

    public void validatePhoto(String photoPath) {
        String url = BuildConfig.DEBUG ? Configuration.photoUrlValidateTest : Configuration.photoUrlValidateTest;
        ResponseItem responseItem = new ResponseItem(ResponseItem.Type.VALIDATION);
        uploadPhoto(photoPath, url, responseItem);
    }

    // Upload the image to the remote database
    private void uploadPhoto(String photoPath, String url, ResponseItem responseItem) {
        isLoading.postValue(true);
        File file = new File(photoPath);
        RequestBody requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("file", file.getName(), RequestBody.create(file, MediaType.parse("multipart/form-data")))
                .build();

        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();

        OkHttpClient client = new OkHttpClient();
        client.newCall(request).enqueue(new Callback() {

            @Override
            public void onFailure(final okhttp3.Call call, final IOException e) {
                // Handle the error
                Timber.e("failed upload");
                isLoading.postValue(false);
                responseItem.errorType = ResponseItem.ResponseErrorType.NETWORK_ERROR;
                responseLiveData.postValue(responseItem);
            }

            @Override
            public void onResponse(final Call call, final Response response) throws IOException {
                if (response.isSuccessful()) {
                    parseResponse(responseItem, response.body());
                } else {
                    // Handle the error
                    Timber.e("NOT uploaded");
                    responseItem.errorType = ResponseItem.ResponseErrorType.UPLOAD_ERROR;
                    responseLiveData.postValue(responseItem);
                }
                isLoading.postValue(false);
            }
        });
    }

    private void parseResponse(ResponseItem responseItem, ResponseBody body) {
        if (body == null) {
            responseItem.errorType = ResponseItem.ResponseErrorType.NULL_RESPONSE;
            responseLiveData.postValue(responseItem);
            return;
        }
        try {
            JSONObject jsonObject = new JSONObject(body.string());
            String message = jsonObject.getString("message");
            if (jsonObject.has("id")) {
                responseItem.id = jsonObject.getString("id");
            }
            Timber.d("Upload successful: " + message);
            responseItem.message = message;
            responseLiveData.postValue(responseItem);
        } catch (JSONException | IOException e) {
            e.printStackTrace();
            responseItem.errorType = ResponseItem.ResponseErrorType.PROCESSING_ERROR;
            responseLiveData.postValue(responseItem);
        }
    }


}
