package com.easyconnect.easyconnectapp.app.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;
import com.easyconnect.easyconnectapp.app.R;
import java.util.List;

/**
 * RecyclerView adapter that renders console-style status messages in the main activity.
 *
 * <p>Displays a scrollable list of log entries (e.g., "Scanning for configurators...",
 * "Detected configurator") using a simple single-{@link TextView} row layout
 * ({@code dialog_listitem}). The backing data is a shared {@link List} of strings
 * that is updated by {@link MainActivity} as the DPP workflow progresses.
 *
 * @see MainActivity#updateConsole(Context)
 */
public class LogcatListAdapter extends RecyclerView.Adapter<LogcatListAdapter.MyViewHolder> {

     LayoutInflater inflater;
     Context mContext;
     List<String> mConsoleList;

    /**
     * Creates a new adapter backed by the given list of console messages.
     *
     * @param context     the context used to obtain a {@link LayoutInflater}
     * @param consoleList the mutable list of status messages to display
     */
    public LogcatListAdapter(Context context, List<String> consoleList) {
        inflater = LayoutInflater.from(context);
        this.mContext = context;
        this.mConsoleList = consoleList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.dialog_listitem, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.configName.setText(mConsoleList.get(position));
    }

    @Override
    public int getItemCount() {
        return mConsoleList.size();
    }

    /** ViewHolder that binds a single console message string to a {@link TextView}. */
    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView configName;

        public MyViewHolder(View itemView) {
            super(itemView);
            configName = (TextView) itemView.findViewById(android.R.id.text1);
        }
    }
}
