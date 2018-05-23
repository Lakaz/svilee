package com.mobiwarez.laki.sville.ui.chat;

import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.evernote.android.job.JobManager;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mobiwarez.laki.sville.R;
import com.mobiwarez.laki.sville.connections.Connection;
import com.mobiwarez.laki.sville.connections.ConnectionsLocalDataSource;
import com.mobiwarez.laki.sville.prefs.GetUserData;
import com.mobiwarez.laki.sville.connections.Connection;
import com.mobiwarez.laki.sville.connections.ConnectionsLocalDataSource;
import com.mobiwarez.laki.sville.prefs.GetUserData;
/*
import com.mobiwarez.sache.MatchesSampleData;
import com.mobiwarez.sache.R;
import com.mobiwarez.sache.connections.ConnectionsLocalDataSource;
import com.mobiwarez.sache.eventMessages.OnlineStatusEvent;
import com.mobiwarez.sache.jobs.GetOnlineStatusJob;
import com.mobiwarez.sache.models.Connection;
import com.mobiwarez.sache.models.SacheChatMessage;
import com.mobiwarez.sache.user.GetUserData;
import com.mobiwarez.sache.utils.CheckInternet;
import com.mobiwarez.sache.utils.CreateCircularImage;
import com.mobiwarez.sache.utils.OnlineStatusTask;

*/
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

//import com.firebase.ui.database.FirebaseRecyclerAdapter;

public class FireChatActivity extends AppCompatActivity {

    protected static Query chatQuery;


    private static final String TAG = "MainActivity";
    public static final String MESSAGES_CHILD = "messages";
    private static final int REQUEST_INVITE = 1;
    private static final int REQUEST_IMAGE = 2;
    public static final int DEFAULT_MSG_LENGTH_LIMIT = 10;
    public static final String ANONYMOUS = "anonymous";
    private static final String MESSAGE_SENT_EVENT = "message_sent";
    private static final String MESSAGE_URL = "http://friendlychat.firebase.google.com/message/";
    private static final String LOADING_IMAGE_URL = "https://www.google.com/images/spin-32.gif";


    private String mUsername;
    private String mPhotoUrl;
    private SharedPreferences mSharedPreferences;

    private Button mSendButton;
    private RecyclerView mMessageRecyclerView;
    private LinearLayoutManager mLinearLayoutManager;
    private FirebaseRecyclerAdapter<SacheChatMessage, MessageViewHolder> mFirebaseAdapter;
    private ProgressBar mProgressBar;
    private DatabaseReference mFirebaseDatabaseReference;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private FirebaseAnalytics mFirebaseAnalytics;
    private EditText mMessageEditText;
    private ImageView mAddMessageImageView;
    private String chatRoom;
    //private AdView mAdView;
    //private FirebaseRemoteConfig mFirebaseRemoteConfig;
    private GoogleApiClient mGoogleApiClient;

    private RecyclerView matchRecyclerview;
    private String connectEmail;
    private String connectUsername;

    private Toolbar toolbar;
    private int jobId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fire_chat);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Chat");
        setSupportActionBar(toolbar);

        JobManager.create(this);

        Intent intent = getIntent();
        chatRoom = intent.getStringExtra("chatRoom");
        connectEmail = intent.getStringExtra("uuid");
        connectUsername = intent.getStringExtra("name");

        //jobId = GetOnlineStatusJob.scheduleCheckStatus(connectEmail);

        toolbar.setTitle(connectUsername);

        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();

        chatQuery = FirebaseDatabase.getInstance().getReference().child(chatRoom).limitToLast(50);


/*
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
*/




        mUsername = GetUserData.Companion.getUserDisplayName(this);
        mPhotoUrl = GetUserData.Companion.getUserPhotoUrl(this);

        // Initialize Firebase Measurement.
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);


        mMessageRecyclerView = findViewById(R.id.messageRecyclerView);
        mLinearLayoutManager = new LinearLayoutManager(this);
        mLinearLayoutManager.setStackFromEnd(true);
        mMessageRecyclerView.setLayoutManager(mLinearLayoutManager);
        mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference();
        setUpFirebaseAdapter();
        mMessageRecyclerView.setAdapter(mFirebaseAdapter);


        //mMessageRecyclerView.setOnDragListener(new MyDrag());


        mMessageEditText = (EditText) findViewById(R.id.messageEditText);
        //mMessageEditText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(mSharedPreferences
        //      .getInt(CodelabPreferences.FRIENDLY_MSG_LENGTH, DEFAULT_MSG_LENGTH_LIMIT))});
        mMessageEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().trim().length() > 0) {
                    mSendButton.setEnabled(true);
                } else {
                    mSendButton.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        mAddMessageImageView = findViewById(R.id.addMessageImageView);

        mAddMessageImageView.setOnClickListener(view -> {
            Intent intent1 = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent1.addCategory(Intent.CATEGORY_OPENABLE);
            intent1.setType("image");
            startActivityForResult(intent1, REQUEST_IMAGE);
        });

        mSendButton = findViewById(R.id.sendButton);
        mSendButton.setOnClickListener(view -> {
            SacheChatMessage sacheChatMessage = new SacheChatMessage(mMessageEditText.getText().toString(), mUsername, mPhotoUrl, null, GetUserData.Companion.getUserEmail(FireChatActivity.this));
            mFirebaseDatabaseReference.child(chatRoom).push().setValue(sacheChatMessage, (databaseError, databaseReference) -> {

                if (databaseError != null) {
                    System.out.println(databaseError.getMessage());
                }
            });
            mMessageEditText.setText("");
            mFirebaseAnalytics.logEvent(MESSAGE_SENT_EVENT, null);
        });




        updateUi();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }

    private boolean chatroomExists(String chatRoom) {
        ConnectionsLocalDataSource connectionsLocalDataSource = ConnectionsLocalDataSource.getInstance(this);
        List<Connection> connectionList = connectionsLocalDataSource.getAllConnections();
        for (Connection connection : connectionList) {
            if (connection.getChatRoom().equals(chatRoom)) {
                return true;
            }
        }



        return false;
    }


/*
    @Subscribe
    public void updateOnlineStatus(OnlineStatusEvent event) {

        Log.d("CHAT", "got presence notification");

        if (event.status.equals("online")) {
            toolbar.setSubtitle("online");
        }

        else {
            toolbar.setSubtitle("");
        }


    }
*/




    @Override
    protected void onStart() {
        super.onStart();
        //EventBus.getDefault().register(this);
/*
        if (CheckInternet.isInternetAvailable(this)) {
            OnlineStatusTask onlineStatusTask = new OnlineStatusTask();
            onlineStatusTask.execute(GetUserData.getUserEmail(this), "online");
        }
*/


    }

    @Override
    protected void onStop() {
        //EventBus.getDefault().unregister(this);
        //OnlineStatusTask onlineStatusTask = new OnlineStatusTask();
        //onlineStatusTask.execute(GetUserData.getUserEmail(this), "offline");
        //GetOnlineStatusJob.cancelJob(jobId);

        super.onStop();
    }

    private void setUpFirebaseAdapter() {


/*
        mFirebaseAdapter = new FirebaseRecyclerAdapter<SacheChatMessage, MessageViewHolder>(
                SacheChatMessage.class,
                R.layout.item_message,
                MessageViewHolder.class,
                mFirebaseDatabaseReference.child(chatRoom)) {
*/


        FirebaseRecyclerOptions<SacheChatMessage> options =
                new FirebaseRecyclerOptions.Builder<SacheChatMessage>()
                .setQuery(chatQuery, SacheChatMessage.class)
                .setLifecycleOwner(this)
                .build();


            mFirebaseAdapter = new FirebaseRecyclerAdapter<SacheChatMessage, MessageViewHolder>(options) {


            @Override
            public MessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                return new MessageViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_message, parent, false));
            }

            @Override
            protected void onBindViewHolder(@NonNull MessageViewHolder viewHolder, int position, @NonNull SacheChatMessage sacheChatMessage) {

                if (sacheChatMessage.getEmail().equals(GetUserData.Companion.getUserEmail(FireChatActivity.this))){
                    viewHolder.outgoing.setVisibility(View.VISIBLE);
                    viewHolder.incoming.setVisibility(View.GONE);

                    if (sacheChatMessage.getText() != null) {
                        viewHolder.mymessageTextView.setText(sacheChatMessage.getText());
                        viewHolder.mymessageTextView.setVisibility(View.VISIBLE);
                        viewHolder.mymessageImageView.setVisibility(View.GONE);
                    }



                    else {
                        String imageUrl = sacheChatMessage.getImageUrl();
                        if (imageUrl.startsWith("gs://")) {
                            StorageReference storageReference = FirebaseStorage.getInstance()
                                    .getReferenceFromUrl(imageUrl);

                            storageReference.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {
                                    if (task.isSuccessful()) {
                                        String downloadUrl = task.getResult().toString();
                                        Glide.with(FireChatActivity.this)
                                                .load(downloadUrl)
                                                .into(viewHolder.mymessageImageView);
                                    } else {
                                        Log.w(TAG, "Getting download url was not successful.", task.getException());
                                    }
                                }
                            });

                        } else {
                            Glide.with(FireChatActivity.this)
                                    .load(sacheChatMessage.getImageUrl())
                                    .into(viewHolder.mymessageImageView);
                        }
                        viewHolder.mymessageImageView.setVisibility(ImageView.VISIBLE);
                        viewHolder.mymessageTextView.setVisibility(TextView.GONE);
                    }




                    viewHolder.messengerTextView.setText(sacheChatMessage.getName());
                    if (sacheChatMessage.getPhotoUrl() == null) {
                        viewHolder.messengerImageView.setImageDrawable(ContextCompat.getDrawable(FireChatActivity.this,
                                R.drawable.ic_account_circle_black_36dp));
                    } else {
                        Glide.with(FireChatActivity.this)
                                .load(sacheChatMessage.getPhotoUrl())
                                .into(viewHolder.messengerImageView);
                    }






                }

                else {

                    viewHolder.outgoing.setVisibility(View.GONE);
                    viewHolder.incoming.setVisibility(View.VISIBLE);

                    if (sacheChatMessage.getText() != null) {
                        viewHolder.messageTextView.setText(sacheChatMessage.getText());
                        viewHolder.messageTextView.setVisibility(TextView.VISIBLE);
                        viewHolder.messageImageView.setVisibility(ImageView.GONE);
                    } else {
                        String imageUrl = sacheChatMessage.getImageUrl();
                        if (imageUrl.startsWith("gs://")) {
                            StorageReference storageReference = FirebaseStorage.getInstance()
                                    .getReferenceFromUrl(imageUrl);

                            storageReference.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {
                                    if (task.isSuccessful()) {
                                        String downloadUrl = task.getResult().toString();
                                        Glide.with(viewHolder.messageImageView.getContext())
                                                .load(downloadUrl)
                                                .into(viewHolder.messageImageView);
                                    } else {
                                        Log.w(TAG, "Getting download url was not successful.", task.getException());
                                    }
                                }
                            });

                        } else {
                            Glide.with(viewHolder.messageImageView.getContext())
                                    .load(sacheChatMessage.getImageUrl())
                                    .into(viewHolder.messageImageView);
                        }
                        viewHolder.messageImageView.setVisibility(ImageView.VISIBLE);
                        viewHolder.messageTextView.setVisibility(TextView.GONE);
                    }


                    viewHolder.messengerTextView.setText(sacheChatMessage.getName());
                    if (sacheChatMessage.getPhotoUrl() == null) {
                        viewHolder.messengerImageView.setImageDrawable(ContextCompat.getDrawable(FireChatActivity.this,
                                R.drawable.ic_account_circle_black_36dp));
                    } else {
                        Glide.with(FireChatActivity.this)
                                .load(sacheChatMessage.getPhotoUrl())
                                .into(viewHolder.messengerImageView);
                    }

                }




            }

/*
            @Override
            protected void populateViewHolder(final MessageViewHolder viewHolder, SacheChatMessage sacheChatMessage, int position) {

                if (sacheChatMessage.getEmail().equals(GetUserData.Companion.getUserEmail(FireChatActivity.this))){
                    viewHolder.outgoing.setVisibility(View.VISIBLE);
                    viewHolder.incoming.setVisibility(View.GONE);

                    if (sacheChatMessage.getText() != null) {
                        viewHolder.mymessageTextView.setText(sacheChatMessage.getText());
                        viewHolder.mymessageTextView.setVisibility(View.VISIBLE);
                        viewHolder.mymessageImageView.setVisibility(View.GONE);
                    }



                    else {
                        String imageUrl = sacheChatMessage.getImageUrl();
                        if (imageUrl.startsWith("gs://")) {
                            StorageReference storageReference = FirebaseStorage.getInstance()
                                    .getReferenceFromUrl(imageUrl);

                            storageReference.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {
                                    if (task.isSuccessful()) {
                                        String downloadUrl = task.getResult().toString();
                                        Glide.with(FireChatActivity.this)
                                                .load(downloadUrl)
                                                .into(viewHolder.mymessageImageView);
                                    } else {
                                        Log.w(TAG, "Getting download url was not successful.", task.getException());
                                    }
                                }
                            });

                        } else {
                            Glide.with(FireChatActivity.this)
                                    .load(sacheChatMessage.getImageUrl())
                                    .into(viewHolder.mymessageImageView);
                        }
                        viewHolder.mymessageImageView.setVisibility(ImageView.VISIBLE);
                        viewHolder.mymessageTextView.setVisibility(TextView.GONE);
                    }




                    viewHolder.messengerTextView.setText(sacheChatMessage.getName());
                    if (sacheChatMessage.getPhotoUrl() == null) {
                        viewHolder.messengerImageView.setImageDrawable(ContextCompat.getDrawable(FireChatActivity.this,
                                R.drawable.ic_account_circle_black_36dp));
                    } else {
                        Glide.with(FireChatActivity.this)
                                .load(sacheChatMessage.getPhotoUrl())
                                .into(viewHolder.messengerImageView);
                    }






                }

                else {

                    viewHolder.outgoing.setVisibility(View.GONE);
                    viewHolder.incoming.setVisibility(View.VISIBLE);

                    if (sacheChatMessage.getText() != null) {
                        viewHolder.messageTextView.setText(sacheChatMessage.getText());
                        viewHolder.messageTextView.setVisibility(TextView.VISIBLE);
                        viewHolder.messageImageView.setVisibility(ImageView.GONE);
                    } else {
                        String imageUrl = sacheChatMessage.getImageUrl();
                        if (imageUrl.startsWith("gs://")) {
                            StorageReference storageReference = FirebaseStorage.getInstance()
                                    .getReferenceFromUrl(imageUrl);

                            storageReference.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {
                                    if (task.isSuccessful()) {
                                        String downloadUrl = task.getResult().toString();
                                        Glide.with(viewHolder.messageImageView.getContext())
                                                .load(downloadUrl)
                                                .into(viewHolder.messageImageView);
                                    } else {
                                        Log.w(TAG, "Getting download url was not successful.", task.getException());
                                    }
                                }
                            });

                        } else {
                            Glide.with(viewHolder.messageImageView.getContext())
                                    .load(sacheChatMessage.getImageUrl())
                                    .into(viewHolder.messageImageView);
                        }
                        viewHolder.messageImageView.setVisibility(ImageView.VISIBLE);
                        viewHolder.messageTextView.setVisibility(TextView.GONE);
                    }


                    viewHolder.messengerTextView.setText(sacheChatMessage.getName());
                    if (sacheChatMessage.getPhotoUrl() == null) {
                        viewHolder.messengerImageView.setImageDrawable(ContextCompat.getDrawable(FireChatActivity.this,
                                R.drawable.ic_account_circle_black_36dp));
                    } else {
                        Glide.with(FireChatActivity.this)
                                .load(sacheChatMessage.getPhotoUrl())
                                .into(viewHolder.messengerImageView);
                    }

                }

*/
/*
                if (friendlyMessage.getText() != null) {
                    // write this message to the on-device index
                    FirebaseAppIndex.getInstance().update(getMessageIndexable(friendlyMessage));
                }
*//*




                // log a view action on it
                //FirebaseUserActions.getInstance().end(getMessageViewAction(friendlyMessage));



            }
*/
        };

        mFirebaseAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                int friendlyMessageCount = mFirebaseAdapter.getItemCount();
                int lastVisiblePosition = mLinearLayoutManager.findLastCompletelyVisibleItemPosition();
                // If the recycler view is initially being loaded or the user is at the bottom of the list, scroll
                // to the bottom of the list to show the newly added message.
                if (lastVisiblePosition == -1 ||
                        (positionStart >= (friendlyMessageCount - 1) && lastVisiblePosition == (positionStart - 1))) {
                    mMessageRecyclerView.scrollToPosition(positionStart);
                }
            }
        });



    }


    private void updateUi(){
        //matcherAdapter = new MatchersViewAdapter(MatchesSampleData.matches(), this);
        //matchRecyclerview.setAdapter(matcherAdapter);


    }


    public static class MessageViewHolder extends RecyclerView.ViewHolder {
        TextView messageTextView;
        ImageView messageImageView;
        TextView messengerTextView;
        CircleImageView messengerImageView;

        TextView mymessageTextView;
        ImageView mymessageImageView;
        TextView mymessengerTextView;

        LinearLayout incoming;
        LinearLayout outgoing;


        public MessageViewHolder(View v) {
            super(v);
            messageTextView = (TextView) itemView.findViewById(R.id.messageTextView);
            messageImageView = (ImageView) itemView.findViewById(R.id.messageImageView);
            messengerTextView = (TextView) itemView.findViewById(R.id.messengerTextView);
            messengerImageView = (CircleImageView) itemView.findViewById(R.id.messengerImageView);

            mymessageTextView = (TextView) itemView.findViewById(R.id.mymessageTextView);
            mymessageImageView = (ImageView) itemView.findViewById(R.id.mymessageImageView);
            mymessengerTextView = (TextView) itemView.findViewById(R.id.mymessengerTextView);

            incoming = (LinearLayout) itemView.findViewById(R.id.others);
            outgoing = (LinearLayout) itemView.findViewById(R.id.mine);


        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult: requestCode=" + requestCode + ", resultCode=" + resultCode);

        if (requestCode == REQUEST_IMAGE) {
            if (resultCode == RESULT_OK) {
                if (data != null) {
                    final Uri uri = data.getData();
                    Log.d(TAG, "Uri: " + uri.toString());

                    SacheChatMessage tempMessage = new SacheChatMessage(null, mUsername, mPhotoUrl, LOADING_IMAGE_URL, GetUserData.Companion.getUserEmail(FireChatActivity.this));
                    mFirebaseDatabaseReference.child(chatRoom).push()
                            .setValue(tempMessage, (databaseError, databaseReference) -> {
                                if (databaseError == null) {
                                    String key = databaseReference.getKey();
                                    StorageReference storageReference =
                                            FirebaseStorage.getInstance()
                                                    .getReference(mFirebaseUser.getUid())
                                                    .child(key)
                                                    .child(uri.getLastPathSegment());

                                    putImageInStorage(storageReference, uri, key);
                                } else {
                                    Log.w(TAG, "Unable to write message to database.",
                                            databaseError.toException());
                                }
                            });
                }
            }
        } else if (requestCode == REQUEST_INVITE) {
            if (resultCode == RESULT_OK) {
                // Use Firebase Measurement to log that invitation was sent.
                Bundle payload = new Bundle();
                payload.putString(FirebaseAnalytics.Param.VALUE, "inv_sent");

                // Check how many invitations were sent and log.
                //String[] ids = AppInviteInvitation.getInvitationIds(resultCode, data);
                //Log.d(TAG, "Invitations sent: " + ids.length);
            } else {
                // Use Firebase Measurement to log that invitation was not sent
                Bundle payload = new Bundle();
                payload.putString(FirebaseAnalytics.Param.VALUE, "inv_not_sent");
                mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SHARE, payload);

                // Sending failed or it was canceled, show failure message to the user
                Log.d(TAG, "Failed to send invitation.");
            }


        }

    }

    private void putImageInStorage(StorageReference storageReference, Uri uri, final String key) {
        storageReference.putFile(uri).addOnCompleteListener(this,
                task -> {
                    if (task.isSuccessful()) {
                        //String url = task.getResult().getDownloadUrl().toString();
                        //SacheChatMessage friendlyMessage = new SacheChatMessage(null, mUsername, mPhotoUrl, task.getResult().getDownloadUrl().toString());
                        //mFirebaseDatabaseReference.child(MESSAGES_CHILD).child(key).setValue(friendlyMessage);
                    } else {
                        Log.w(TAG, "Image upload task was not successful.",
                                task.getException());
                    }
                });
    }





}
