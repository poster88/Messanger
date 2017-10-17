package com.example.user.messager.fragment;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.user.messager.R;
import com.example.user.messager.utils.Utils;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by User on 011 11.10.17.
 */

public class RegistrationFragment extends BaseFragment implements TaskListener {
    @BindView(R.id.regUserImage) ImageView userImage;
    @BindView(R.id.regUserNameEdit) EditText userName;
    @BindView(R.id.regUserEmailEdit) EditText userEmail;

    private RegistrationTask registrationAsyncTask;
    private boolean isTaskRunning = false;

    private DialogInterface.OnClickListener cancelDialogListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
            registrationAsyncTask.cancel(true);
            progressDialog.dismiss();
        }
    };

    public static RegistrationFragment newInstance(){
        return new RegistrationFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.registration_fragment, container, false);
        bindFragment(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (isTaskRunning){
            showProgressDialog(getActivity(), "Registration", "Please wait a moment!",
                    true, false, cancelDialogListener, "Cancel", null, null);
        }
    }

    @OnClick(R.id.setImageBtn)
    public void setImageAction(){
        requestPermissions();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == Utils.REQUEST_READ_PERMISSION){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                openFilePicker();
            }
        }
    }

    private void openFilePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, Utils.PHOTO_REQUEST);
    }

    private void requestPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, Utils.REQUEST_READ_PERMISSION);
                return;
            }
        }
        openFilePicker();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Utils.PHOTO_REQUEST){
            if (resultCode == Utils.RESULT_OK){
                super.setRoundImageToView(data.getData(), userImage);
            }
        }
    }

    @OnClick({R.id.regOkBtn, R.id.regCancelBtn})
    public void buttonAction(Button button){
        if (button.getId() == R.id.regCancelBtn){
            getActivity().onBackPressed();
            return;
        }
        if (!TextUtils.isEmpty(userName.getText().toString()) && !TextUtils.isEmpty(userEmail.getText().toString())){
            registrationAsyncTask = new RegistrationTask(this);
            registrationAsyncTask.execute();
        }
    }

    @Override
    public void onTaskStarted() {
        isTaskRunning = true;
        showProgressDialog(getActivity(), "Registration", "Please wait a moment!",
                true, false, cancelDialogListener, "Cancel", null, null);
    }

    @Override
    public void onTaskFinished(String result) {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
        isTaskRunning = false;
    }

    @Override
    public void onDetach() {
        hideProgressDialog();
        super.onDetach();
    }

    private class RegistrationTask extends AsyncTask<Integer, Integer, String> {
        private final TaskListener listener;

        public RegistrationTask(TaskListener listener) {
            this.listener = listener;
        }

        @Override
        protected void onPreExecute() {
            listener.onTaskStarted();
        }

        @Override
        protected String doInBackground(Integer... integers) {
            for (int i = 0; i < 8; i++) {
                try {
                    Thread.sleep(1000);
                }catch (Exception e){
                    e.printStackTrace();
                }
                if (isCancelled()){
                    isTaskRunning = false;
                    return null;
                }
                System.out.println(i);
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
           listener.onTaskFinished(result);
        }
    }
}
