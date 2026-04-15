package com.easyconnect.easyconnectap.util;

import android.content.Context;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

/**
 * Touch listener that translates single-tap gestures on a {@link RecyclerView}
 * into item click callbacks.
 *
 * <p>Attaches to a RecyclerView via
 * {@link RecyclerView#addOnItemTouchListener(RecyclerView.OnItemTouchListener)}.
 * When a single tap is detected on a child view, the registered
 * {@link OnItemClickListener} is invoked with the tapped view and its adapter position.
 *
 * <p>Used by {@link com.easyconnect.easyconnectap.mdns.MDNSDialogFragment} to handle
 * configurator selection from the mDNS discovery list.
 */
public class RecyclerItemClickListener implements RecyclerView.OnItemTouchListener {

    private OnItemClickListener mListener;
    /** Callback interface for RecyclerView item click events. */
    public interface OnItemClickListener {
        /**
         * Called when an item in the RecyclerView is tapped.
         *
         * @param view     the clicked child view
         * @param position the adapter position of the clicked item
         */
        public void onItemClick(View view, int position);
    }
    GestureDetector mGestureDetector;
    public RecyclerItemClickListener(Context context, OnItemClickListener listener) {
        mListener = listener;
        mGestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }
        });
    }
    @Override
    public boolean onInterceptTouchEvent(RecyclerView view, MotionEvent e) {
        View childView = view.findChildViewUnder(e.getX(), e.getY());
        if (childView != null && mListener != null && mGestureDetector.onTouchEvent(e)) {
            mListener.onItemClick(childView, view.getChildAdapterPosition(childView));
        }
        return false;
    }

    @Override
    public void onTouchEvent(RecyclerView view, MotionEvent motionEvent) {
        //TODO
    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        //TODO
    }

}
