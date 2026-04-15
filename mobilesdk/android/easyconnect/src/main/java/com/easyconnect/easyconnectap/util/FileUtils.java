package com.easyconnect.easyconnectap.util;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * Singleton utility for reading and writing console log messages to internal storage.
 *
 * <p>Maintains a persistent {@code console.txt} file in the app's private storage
 * directory. Messages are appended line-by-line via {@link #writeToFile(String, Context)}
 * and read back as a list via {@link #readFromFile(Context)} for display in the
 * console {@link android.widget.ListView} / {@link androidx.recyclerview.widget.RecyclerView}.
 *
 * <p>The {@link OutputStreamWriter} is kept open across writes for efficiency;
 * the first call creates the file, and subsequent calls append to it.
 *
 * @see com.easyconnect.easyconnectapp.app.view.MainActivity#writeTexttoFile(String)
 */
public class FileUtils {

    private static FileUtils fileUtils;
    OutputStreamWriter outputStreamWriter;

    public static synchronized FileUtils getInstance() {

        if (fileUtils == null) {

            fileUtils = new FileUtils();
        }

        return fileUtils;
    }

    /**
     * Appends a status message to the {@code console.txt} file.
     *
     * <p>On the first call, creates the file and writes the message. On subsequent
     * calls, appends a newline followed by the message. The stream is flushed
     * after each write to ensure data is persisted.
     *
     * @param data    the status message to append
     * @param context the context used to open the file output stream
     */
    public void writeToFile(String data, Context context) {

        try {

            if (outputStreamWriter == null) {

                outputStreamWriter = new OutputStreamWriter(context.openFileOutput("console.txt", Context.MODE_PRIVATE));
                outputStreamWriter.write(data);
            } else {

                outputStreamWriter.write("\n");
                outputStreamWriter.append(data);
            }

            outputStreamWriter.flush();

        } catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }

    /**
     * Reads all console messages from {@code console.txt} and returns them as a list.
     *
     * <p>Each line in the file becomes one entry in the returned list. If the file
     * does not exist (e.g., on first launch before any writes), returns an empty list.
     *
     * @param context the context used to open the file input stream
     * @return a list of console messages, one per line; never {@code null}
     */
    public List<String> readFromFile(Context context) {

        List<String> consoleList = new ArrayList<>();
        try {
            InputStream inputStream = context.openFileInput("console.txt");

            if (inputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ((receiveString = bufferedReader.readLine()) != null) {
                    stringBuilder.append(receiveString);
                    consoleList.add(receiveString);
                }

                inputStream.close();
            }
        } catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }

        return consoleList;
    }
}
