package com.empact.android.pufin.ui.photo;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.empact.android.pufin.R;
import com.empact.android.pufin.dataSource.NetworkDataSource;
import com.empact.android.pufin.model.ResponseItem;
import com.empact.android.pufin.util.Util;
import com.empact.android.pufin.viewModel.PhotoViewModel;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;

import static android.app.Activity.RESULT_OK;
import static com.empact.android.pufin.model.ResponseItem.Type.VALIDATION;

public class PhotoFragment extends Fragment implements View.OnClickListener {

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private Button buttonTakePicture;
    private Button buttonValidatePicture;
    private Button buttonRegisterPicture;
    private ImageView imageView;
    private ProgressBar progressBar;

    private PhotoViewModel viewModel;
    private String currentPhotoPath = null;
    private DialogFragment dialogFragment;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_photo, container, false);
        progressBar = root.findViewById(R.id.progressBar);
        imageView = root.findViewById(R.id.imageView);
        buttonTakePicture = root.findViewById(R.id.buttonTakePicture);
        buttonRegisterPicture = root.findViewById(R.id.buttonRegister);
        buttonValidatePicture = root.findViewById(R.id.buttonValidate);

        buttonTakePicture.setOnClickListener(this);
        buttonRegisterPicture.setOnClickListener(this);
        buttonValidatePicture.setOnClickListener(this);

        viewModel = new ViewModelProvider(this).get(PhotoViewModel.class);

        return root;
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel.getIsLoading().observe(getViewLifecycleOwner(), isLoading -> {
            progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
            buttonRegisterPicture.setEnabled(!isLoading);
            buttonValidatePicture.setEnabled(!isLoading);
        });
        viewModel.getResponse().observe(getViewLifecycleOwner(), responseItem -> {
            showDialog(responseItem);
            viewModel.clearResponse();
        });
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        PackageManager packageManager = requireActivity().getPackageManager();
        if (takePictureIntent.resolveActivity(packageManager) == null) {
            Toast.makeText(getContext(), getString(R.string.info_no_camera_app), Toast.LENGTH_LONG).show();
            return;  // no activity to take pictures
        }

        // Create the File where the photo should go
        File photoFile = null;
        try {
            photoFile = Util.createImageFile(requireContext());
            // Save a file: path for use with ACTION_VIEW intents
            currentPhotoPath = photoFile.getAbsolutePath();
        } catch (IOException e) {
            // Error occurred while creating the File
            e.printStackTrace();
            Toast.makeText(getContext(), getString(R.string.info_error_creating_file), Toast.LENGTH_LONG).show();
        }

        // Continue only if the File was successfully created
        if (photoFile != null) {
            Uri photoURI = FileProvider.getUriForFile(requireContext(), "com.empact.android.pufin.fileprovider", photoFile);
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
//            viewModel.clearResponse();
            Util.setPic(currentPhotoPath, imageView);
//            Util.galleryAddPic(requireContext(), currentPhotoPath);
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.buttonTakePicture) {
            dispatchTakePictureIntent();
        } else if (id == R.id.buttonRegister) {
//            verifyStoragePermissions(getActivity());
            viewModel.doRegisterPhoto(currentPhotoPath);
        } else if (id == R.id.buttonValidate) {
            viewModel.doValidatePhoto(currentPhotoPath);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (dialogFragment!=null){
            dialogFragment.dismiss();
            dialogFragment = null;
        }

    }

    void showDialog(ResponseItem responseItem) {
        if (responseItem==null) return;
        String message;
        if (responseItem.errorType != null ) {
            message = responseItem.getErrorMessage(requireContext(), responseItem);
        } else {
            message = responseItem.message;
            if (responseItem.id!=null && !responseItem.id.isEmpty()){
                String matchingId = getString(R.string.matchingId);
                message = message + "\n" + matchingId + "\n" + responseItem.id;
            }
        }

        String title= "";
        if (responseItem.type == ResponseItem.Type.REGISTRATION){
            title = getString(R.string.register_dialog_title);
        } else if (responseItem.type ==  ResponseItem.Type.VALIDATION){
            title = getString(R.string.validation_dialog_title);
        }

        dialogFragment = MyDialogFragment.newInstance(title, message);
        dialogFragment.show(requireFragmentManager(), "dialog");
    }

}
