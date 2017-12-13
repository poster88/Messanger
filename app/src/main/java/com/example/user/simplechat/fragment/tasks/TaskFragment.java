package com.example.user.simplechat.fragment.tasks;


import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.user.simplechat.R;
import com.example.user.simplechat.activity.MainActivity;
import com.example.user.simplechat.utils.Const;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

/**
 * Created by POSTER on 12.12.2017.
 */

public class TaskFragment extends DialogFragment {

    /**
     * Callback interface through which the fragment will report the
     * task's progress and results back to the Activity.
     */
    public interface TaskCallbacks {
        void onPreExecute();
        void onProgressUpdate(int percent);
        void onCancelled();
        void onPostExecute();
    }

    private TaskCallbacks mCallbacks;
    private DummyTask mTask;
    private ProgressBar mProgressBar;
    private Context context;
    /**
     * Hold a reference to the parent Activity so we can report the
     * task's current progress and results. The Android framework
     * will pass us a reference to the newly created Activity after
     * each configuration change.
     */

    public void setTask(DummyTask dummyTask){
        mTask = dummyTask;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mCallbacks = (TaskCallbacks) context;

        // Retain this fragment across configuration changes.
        //setRetainInstance(true);
        //mTask = new DummyTask();
        //mTask.execute();
        // Create and execute the background task.

        Log.d(Const.MY_LOG, "TaskFragment: onAttach - new DummyTask(). setRetainInstance(true);");
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        /*if (mTask != null){
            mTask.execute();
        }*/
        /*mTask = new DummyTask();
        mTask.execute();*/

        if (mCallbacks != null){
            mCallbacks.onPreExecute();
        }

    }

    /*@Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_task, container, false);
        mProgressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        mProgressBar.setVisibility(View.VISIBLE);
        //getDialog().setTitle("progress dialog");
        //getDialog().setCanceledOnTouchOutside(false);
        return view;
    }*/

    /*@Override
    public void onDestroyView() {
        if (getDialog() != null && getRetainInstance()){
            getDialog().setDismissMessage(null);
        }
        super.onDestroyView();
    }*/

    /*@Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        if (mTask != null){
            mTask.cancel(false);
        }
    }*/

    /*@Override
    public void onResume() {
        super.onResume();
        if (mTask == null){
            dismiss();
        }
    }*/

    /**
     * Set the callback to null so we don't accidentally leak the
     * Activity instance.
     */
    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
        Log.d(Const.MY_LOG, "TaskFragment: onDetach - mCallbacks = null");
    }
    /**
     * A dummy task that performs some (dumb) background work and
     * proxies progress updates and results back to the Activity.
     *
     * Note that we need to check if the callbacks are null in each
     * method in case they are invoked after the Activity's and
     * Fragment's onDestroy() method have been called.
     */
    public class DummyTask extends AsyncTask<Void, Integer, Void> {

        @Override
        protected void onPreExecute() {
            if (mCallbacks != null){
                mCallbacks.onPreExecute();
            }
        }

        /**
         * Note that we do NOT call the callback object's methods
         * directly from the background thread, as this could result
         * in a race condition.
         */

        @Override
        protected Void doInBackground(Void... voids) {
            Log.d(Const.MY_LOG, "run in the method");


            /*for (int i = 0; !isCancelled() && i < 100; i++) {
                Log.d(Const.MY_LOG, "DummyTask (doInBackground): i = " + i);
                SystemClock.sleep(100);
                publishProgress(i);
            }*/
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            if (mCallbacks != null){
                mCallbacks.onProgressUpdate(values[0]);
            }
        }

        @Override
        protected void onCancelled() {
            if (mCallbacks != null){
                mCallbacks.onCancelled();
            }
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if (mCallbacks != null){
                mCallbacks.onPostExecute();
            }
        }
    }
}


