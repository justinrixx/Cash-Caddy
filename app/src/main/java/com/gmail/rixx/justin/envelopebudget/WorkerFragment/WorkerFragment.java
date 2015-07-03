package com.gmail.rixx.justin.envelopebudget.WorkerFragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;

/**
 * A base headless worker fragment for doing async tasks
 */
public class WorkerFragment extends Fragment {

    /**
     * A reference to the activity in order to call all the callbacks in it
     */
    private TaskCallbacks mCallbacks;

    /**
     * When the fragment attaches to the Activity, hold a reference to the activity in order to <p/>
     * call the callbacks.
     * @param activity The activity to attach to
     */
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mCallbacks = (TaskCallbacks) activity;
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
