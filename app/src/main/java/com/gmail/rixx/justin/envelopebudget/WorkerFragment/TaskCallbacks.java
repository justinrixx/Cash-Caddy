package com.gmail.rixx.justin.envelopebudget.WorkerFragment;

/**
 * TaskCallbacks
 * This interface is used in activities that have worker threads residing in headless fragments
 */
public interface TaskCallbacks {

    void onPreExecute();
    void onProgressUpdate(int percent);
    void onCancelled();
    void onPostExecute();

}
