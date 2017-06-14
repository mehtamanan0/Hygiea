package com.example.manan.hygiea;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.app.Fragment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import ai.api.AIDataService;
import ai.api.AIServiceException;
import ai.api.android.AIConfiguration;
import ai.api.model.AIRequest;
import ai.api.model.AIResponse;
import ai.api.model.Result;

import static android.R.attr.bitmap;
import static android.app.Activity.RESULT_OK;

public class MainActivity extends Fragment {
    private static final int SELECT_PHOTO = 100;
    ImageButton send;
    EditText messageET;
    TextView online;
    ArrayList<String> addarray = new ArrayList<String>();
    boolean myMessage = true;
    private List<ChatBubble> ChatBubbles;
    private ArrayAdapter<ChatBubble> adapter;
    ListView messages;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_main, container, false);
        ChatBubbles = new ArrayList<>();
        messageET = (EditText)view.findViewById(R.id.editText);
        messages = (ListView)view.findViewById(R.id.listView);
        send = (ImageButton)view.findViewById(R.id.button);
        online = (TextView)view.findViewById(R.id.online);
        adapter = new MessageAdapter(getActivity(), R.layout.left_chat, ChatBubbles);
        messages.setAdapter(adapter);
        ///messages.setAdapter(adapter);
        SharedPreferences sharedpreferences = this.getActivity().getSharedPreferences("signin", Context.MODE_PRIVATE);
        String name = sharedpreferences.getString("name","");
        ChatBubble ChatBubble = new ChatBubble("Hey there "+name+" what can I help you with?",myMessage);
        ChatBubbles.add(ChatBubble);
        adapter.notifyDataSetChanged();
        if (myMessage) {
            myMessage = false;
        } else {
            myMessage = true;
        }
        //addarray.add("BOT : Hey there "+name+" what can I help you with?");
        //ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity().getApplicationContext(), android.R.layout.simple_list_item_1 ,addarray);
      //  messages.setAdapter(adapter);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String messageTxt = messageET.getText().toString();
                if (messageTxt.equals("")){

                }
                else {
                    //addarray.add(messageTxt);
                    //ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity().getApplicationContext(), android.R.layout.simple_list_item_1 ,addarray);
                    adapter = new MessageAdapter(getActivity(), R.layout.right_chat, ChatBubbles);
                    messages.setAdapter(adapter);
                    ChatBubble ChatBubble = new ChatBubble(messageTxt,myMessage);
                    ChatBubbles.add(ChatBubble);
                    adapter.notifyDataSetChanged();
                    if (myMessage) {
                        myMessage = false;
                    } else {
                        myMessage = true;
                    }
                    messageET.setText("");
                    messages.setSelection(messages.getCount() - 1);
                    final AIConfiguration config = new AIConfiguration("7aadce78a9094a1faa4a7ef065a24394",
                            AIConfiguration.SupportedLanguages.English,
                            AIConfiguration.RecognitionEngine.System);
                    final AIDataService aiDataService = new AIDataService(config);
                    final AIRequest aiRequest = new AIRequest();
                    aiRequest.setQuery(messageTxt);
                    new AsyncTask<AIRequest, Void, AIResponse>() {
                        @Override
                        protected  void onPreExecute()
                        {
                            online.setTypeface(null, Typeface.ITALIC);
                            online.setText("typing...");

                        }
                        @Override
                        protected AIResponse doInBackground(AIRequest... requests) {
                            final AIRequest request = requests[0];
                            try {
                                final AIResponse response = aiDataService.request(aiRequest);
                                return response;
                            } catch (AIServiceException e) {
                            }
                            return null;
                        }
                        @Override
                        protected void onPostExecute(AIResponse aiResponse) {
                            if (aiResponse != null) {
                                online.setText("online");
                                Result result = aiResponse.getResult();
                                if (result.getFulfillment().getSpeech().equals(getString(R.string.ambulanceIntent))){
                                    adapter = new MessageAdapter(getActivity(), R.layout.right_chat, ChatBubbles);
                                    messages.setAdapter(adapter);
                                    ChatBubble ChatBubble = new ChatBubble(result.getFulfillment().getSpeech(),myMessage);
                                    ChatBubbles.add(ChatBubble);
                                    adapter.notifyDataSetChanged();
                                    if (myMessage) {
                                        myMessage = false;
                                    } else {
                                        myMessage = true;
                                    }
                                    messages.setSelection(messages.getCount() - 1);
                                    final Handler handler = new Handler();
                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + "1298"));
                                            startActivity(intent);
                                        }
                                    }, 1000);
                                }
                                else if (result.getFulfillment().getSpeech().equals(getString(R.string.secondary))){
                                    adapter = new MessageAdapter(getActivity(), R.layout.right_chat, ChatBubbles);
                                    messages.setAdapter(adapter);
                                    ChatBubble ChatBubble = new ChatBubble(result.getFulfillment().getSpeech(),myMessage);
                                    ChatBubbles.add(ChatBubble);
                                    adapter.notifyDataSetChanged();
                                    if (myMessage) {
                                        myMessage = false;
                                    } else {
                                        myMessage = true;
                                    }
                                    messages.setSelection(messages.getCount() - 1);
                                    final Handler handler = new Handler();
                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                                            photoPickerIntent.setType("image/*");
                                            startActivityForResult(photoPickerIntent, SELECT_PHOTO);
                                        }
                                    }, 1000);
                                    myMessage = true;
                                    final Handler mamu = new Handler();
                                    mamu.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            messages.setAdapter(adapter);
                                            ChatBubble hello = new ChatBubble("Medical record is set please Check in the profile tab",myMessage);
                                            ChatBubbles.add(hello);
                                            adapter.notifyDataSetChanged();
                                            messages.setSelection(messages.getCount() - 1);
                                        }
                                    }, 7000);

                                }

                                else {
                                    adapter = new MessageAdapter(getActivity(), R.layout.right_chat, ChatBubbles);
                                    messages.setAdapter(adapter);
                                    ChatBubble ChatBubble = new ChatBubble(result.getFulfillment().getSpeech(),myMessage);
                                    ChatBubbles.add(ChatBubble);
                                    adapter.notifyDataSetChanged();
                                    if (myMessage) {
                                        myMessage = false;
                                    } else {
                                        myMessage = true;
                                    }messages.setSelection(messages.getCount() - 1);
                                }
                            }
                        }
                    }.execute(aiRequest);

                }


            }
        });

        return view;

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

        switch(requestCode) {
            case SELECT_PHOTO:
                if(resultCode == RESULT_OK){
                    Uri selectedImage = imageReturnedIntent.getData();
                    InputStream imageStream = null;
                    try {
                        imageStream = getActivity().getContentResolver().openInputStream(selectedImage);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    Bitmap yourSelectedImage = BitmapFactory.decodeStream(imageStream);
                    ByteArrayOutputStream baos =new  ByteArrayOutputStream();
                    yourSelectedImage.compress(Bitmap.CompressFormat.PNG,100, baos);
                    byte [] b=baos.toByteArray();
                    String temp= Base64.encodeToString(b, Base64.DEFAULT);

                    SharedPreferences sharedpreferences = getActivity().getSharedPreferences("signin", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.putString("medical",temp);
                    editor.apply();
                    //Toast.makeText(getActivity().getApplicationContext(),temp,Toast.LENGTH_LONG).show();
                }
        }
    }

}
