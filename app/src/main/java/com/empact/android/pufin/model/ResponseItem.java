package com.empact.android.pufin.model;


import android.content.Context;


public class ResponseItem {
    private static final String INFO_UPLOAD_ERROR = "Upload error";
    private static final String INFO_PROCESSING_ERROR = "Error processing response";
    private static final String INFO_NETWORK_ERROR = "Network error";
    private static final String INFO_ERROR = "Error: ";
    private static final String MATCHING_ID = "Marching ID: ";

    public String id;
    public String message;
    public Type type;
    public ResponseErrorType errorType;

    public String getErrorMessage(Context context, ResponseItem responseItem) {
        if (responseItem.errorType==ResponseErrorType.NETWORK_ERROR)
            return INFO_NETWORK_ERROR;
        if (responseItem.errorType==ResponseErrorType.UPLOAD_ERROR)
            return INFO_UPLOAD_ERROR;
        if (responseItem.errorType==ResponseErrorType.NULL_RESPONSE)
            return "Response was null";
        if (responseItem.errorType==ResponseErrorType.PROCESSING_ERROR)
            return INFO_PROCESSING_ERROR;
        return "Error";
    }

    public enum Type{
        REGISTRATION, VALIDATION
    }
 public enum ResponseErrorType{
        NETWORK_ERROR, UPLOAD_ERROR, NULL_RESPONSE, PROCESSING_ERROR;
 }

    public ResponseItem(Type type) {
        this.type = type;
    }
}
