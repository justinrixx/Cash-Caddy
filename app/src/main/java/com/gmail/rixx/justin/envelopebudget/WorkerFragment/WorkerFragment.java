package com.gmail.rixx.justin.envelopebudget.WorkerFragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;

/**
 * A base headless worker fragment for doing async tasks
 */
public class WorkerFragment<T> extends Fragment {

    protected boolean isRunning = true;

    /**
     * A reference to the activity in order to call all the callbacks in it
     */
    protected TaskCallbacks<T> mCallbacks;

    public boolean isRunning() {
        return isRunning;
    }

    /**
     * When the fragment attaches to the Activity, hold a reference to the activity in order to <p/>
     * call the callbacks.
     * @param activity The activity to attach to
     */
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mCallbacks = (TaskCallbacks<T>) activity;
    }

    /**
     * Set it up to retain the instance
     * @param savedInstanceState Probably not used
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // don't die!
        setRetainInstance(true);
    }

    /**
     * Forget the old activity to avoid a memory leak
     */
    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }
}
