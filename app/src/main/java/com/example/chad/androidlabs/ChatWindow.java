package com.example.chad.androidlabs;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
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
    private static SQLiteDatabase chatDatabase;
    protected static final String ACTIVITY_NAME = "ChatWindow";

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
        Context chatCtx = getApplicationContext();
        ChatDatabaseHelper chatdbHelper = new ChatDatabaseHelper(chatCtx);
        chatDatabase = chatdbHelper.getWritableDatabase();
        final ContentValues contentValues = new ContentValues();

        Cursor cursor = chatDatabase.query(ChatDatabaseHelper.TABLE_NAME, new String[]{ChatDatabaseHelper.KEY_ID, ChatDatabaseHelper.KEY_MESSAGE},
                null, null , null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            String message = cursor.getString(cursor.getColumnIndex(ChatDatabaseHelper.KEY_MESSAGE));
            stringArr.add(message);
            Log.i(ACTIVITY_NAME, "SQL MESSAGE: "+message);
            Log.i(ACTIVITY_NAME, "Cursor ColumnCount = " + cursor.getColumnCount() );
            cursor.moveToNext();

        }

        for(int i=0; i<cursor.getColumnCount(); i++){
            Log.i(ACTIVITY_NAME,"ColumnName:" + cursor.getColumnName(i));
        }

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stringArr.add(editText.getText().toString());
                chatAdapter.notifyDataSetChanged(); // this restarts the process of getCount() & getView()
                contentValues.put(ChatDatabaseHelper.KEY_MESSAGE, editText.getText().toString());
                chatDatabase.insert(ChatDatabaseHelper.TABLE_NAME, null, contentValues);
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

    @Override
    public void onDestroy(){
        super.onDestroy();
        Log.i(ACTIVITY_NAME, "Calling onDestroy");
        chatDatabase.close();
    }

}
