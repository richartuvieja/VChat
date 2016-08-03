package com.kuikos.vchat.fragments;


import android.database.DataSetObserver;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.kuikos.vchat.ChatApplication;
import com.firebase.androidchat.R;
import com.kuikos.vchat.activities.MainActivity;
import com.kuikos.vchat.adapters.ChatListAdapter;
import com.kuikos.vchat.adapters.FirebaseListAdapter;
import com.kuikos.vchat.model.Chat;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChatFragment extends Fragment {

    private ValueEventListener mConnectedListener;
    private FirebaseListAdapter mChatListAdapter;

    private FirebaseDatabase database;
    private DatabaseReference myRef;

    EditText inputText;
    ListView listView;
    FrameLayout progress;

    MainActivity activity;
    ChatApplication application;

    private FirebaseAnalytics mFirebaseAnalytics;

    public ChatFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_chat, container, false);

        inputText = (EditText) v.findViewById(R.id.messageInput);
        listView = (ListView) v.findViewById(R.id.listview);
        progress = (FrameLayout) v.findViewById(R.id.fragment_chat_loading);
        activity = ((MainActivity)this.getActivity());
        application= (ChatApplication) activity.getApplication();
        this.getActivity().setTitle("");

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("chat");

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this.getActivity());

        // Setup our input methods. Enter key on the keyboard or pushing the send button
        EditText inputText = (EditText) v.findViewById(R.id.messageInput);
        inputText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_NULL && keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
                    sendMessage();
                }
                return true;
            }
        });

        v.findViewById(R.id.sendButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMessage();
            }
        });
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();

        mChatListAdapter = new ChatListAdapter(myRef.limitToLast(50), this.getActivity(), R.layout.item_message, application.getUser().reg_username,application.getUser());

        listView.setAdapter(mChatListAdapter);
        //listView.setScrollContainer(false);
        mChatListAdapter.registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                listView.setSelection(mChatListAdapter.getCount() - 1);
            }
        });

        // Finally, a little indication of connection status
        mConnectedListener = myRef.getRoot().child(".info/connected").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                boolean connected = (Boolean) dataSnapshot.getValue();
                if (connected) {
                    progress.setVisibility(View.GONE);
                    //Toast.makeText(ChatFragment.this.getActivity(), "Connected to Firebase", Toast.LENGTH_SHORT).show();
                } else {
                    //Toast.makeText(ChatFragment.this.getActivity(), "Se", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("MSJ", "Error: "  + databaseError.getMessage());
            }
        });
    }

    @Override
    public void onStop() {
        super.onStop();
        myRef.getRoot().child(".info/connected").removeEventListener(mConnectedListener);
        mChatListAdapter.cleanup();
    }


    private void sendMessage() {

        String input = inputText.getText().toString();
        if (!input.equals("")) {
            Chat chat = new Chat(input, application.getUser().reg_username,application.getUser().genero);
            myRef.push().setValue(chat).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(activity,"Error: " + e.getMessage(),Toast.LENGTH_LONG).show();
                }
            });
            inputText.setText("");
        }
    }

}
