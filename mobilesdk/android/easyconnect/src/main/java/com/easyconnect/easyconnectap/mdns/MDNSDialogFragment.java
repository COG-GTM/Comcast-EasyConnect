package com.easyconnect.easyconnectap.mdns;

import android.app.Dialog;
import android.content.Context;
import android.net.nsd.NsdServiceInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.easyconnect.easyconnectap.connection.IConfigurator;
import com.easyconnect.easyconnectap.connection.NSDDiscover;


import com.easyconnect.easyconnectap.util.RecyclerItemClickListener;
import com.easyconnect.easyconnectapp.R;

import java.util.List;

/**
 * Full-screen dialog fragment that displays a list of discovered DPP configurators
 * found via mDNS, allowing the user to select one.
 *
 * <p>Shown by {@link com.easyconnect.easyconnectapp.app.view.MainActivity#mDNSConfigList}
 * when multiple configurators are discovered. Presents a {@link RecyclerView} of
 * service names using {@link MDNSListAdapter}. When an item is tapped, the selected
 * {@link android.net.nsd.NsdServiceInfo} is resolved to an IP/port via
 * {@link com.easyconnect.easyconnectap.connection.NSDDiscover#getResolveListener},
 * and the dialog is dismissed.
 *
 * <p>Must be created via the {@link #newInstance} factory method to avoid issues
 * with fragment re-creation by the Android framework.
 *
 * @see MDNSListAdapter
 * @see com.easyconnect.easyconnectap.connection.NSDDiscover
 */
public class MDNSDialogFragment extends AppCompatDialogFragment {

    private RecyclerView mRecyclerView;
    private Context mContext;
    private List<NsdServiceInfo> mNSDServiceList;
    private IConfigurator mIConfigurator;

    public MDNSDialogFragment() {
        // Empty constructor is required for MDNSDialogFragment
        // Make sure not to add arguments to the constructor
        // Use `newInstance` instead as shown below
    }

    /**
     * Factory method to create a new dialog fragment with the required dependencies.
     *
     * @param context            the host activity context
     * @param imdnsDialog        callback to receive the selected configurator
     * @param nsdServiceInfoList the list of discovered mDNS services to display
     * @return a new MDNSDialogFragment instance
     */
    public static MDNSDialogFragment newInstance(Context context, IConfigurator imdnsDialog, List<NsdServiceInfo> nsdServiceInfoList) {
        MDNSDialogFragment frag = new MDNSDialogFragment();
        frag.mContext = context;
        frag.mIConfigurator = imdnsDialog;
        frag.mNSDServiceList = nsdServiceInfoList;

        return frag;
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setLayout(width, height);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.dialog_mdns, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getDialog().setCanceledOnTouchOutside(false);

        // Get field from view
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));

        MDNSListAdapter adapter = new MDNSListAdapter(mContext, mNSDServiceList);
        mRecyclerView.setAdapter(adapter);

        mRecyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(mContext, (view1, position) -> {

                    NSDDiscover.getInstance().getResolveListener(mContext, mNSDServiceList.get(position), mIConfigurator);

                    dismiss();
                }));
    }
}
