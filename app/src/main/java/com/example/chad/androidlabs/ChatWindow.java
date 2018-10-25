package com.example.chad.androidlabs;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class ChatWindow extends Activity {

    ListView listView;
    EditText editText;
    Button button;
    ArrayList<String> stringArr;
    ChatAdapter chatAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_window);

        listView = findViewById(R.id.ListView);
        editText = findViewById(R.id.chatText);
        button = findViewById(R.id.sendButton);
        stringArr = new ArrayList<>();
        chatAdapter = new ChatAdapter(this);
        listView.setAdapter(chatAdapter);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stringArr.add(editText.getText().toString());
                chatAdapter.notifyDataSetChanged(); // this restarts the process of getCount() & getView()
                editText.setText("");
            }
        });

    }


    private class ChatAdapter extends ArrayAdapter<String> {

        public ChatAdapter(Context cts) {
            super(cts, 0);

        }

        public int getCount(){

            return stringArr.size();
        }

        public String getItem(int position){


            return stringArr.get(position);
        }

        public View getView(int position, View convertView, ViewGroup parent){

            LayoutInflater inflater = ChatWindow.this.getLayoutInflater();

            View result = null;
                if(position%2 == 0)
                        result = inflater.inflate(R.layout.chat_row_incoming, null);
                    else
                        result = inflater.inflate(R.layout.chat_row_outgoing, null);
            TextView message = (TextView)result.findViewById(R.id.message_text);
            message.setText(getItem(position));

            return result;
        }

        public long getItemId(int position){

            long pos = position;
            return pos;
        }


    }
}
