package com.example.lyricfinder;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.safety.Whitelist;
import org.jsoup.select.Elements;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private Button mybtn;
    private EditText myinput;
    private TextView myoutput;
    private String mylyrics ="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mybtn = findViewById(R.id.btn_submit);
        myinput = findViewById(R.id.edit_input);
        myoutput = findViewById(R.id.text_output);

        mybtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Description().execute();
            }
        });
    }

    private class Description extends AsyncTask<Void, Void, Void> {
        String desc;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            myoutput.setText("Loading");
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                String url="https://www.musixmatch.com/es/search/";
                String input = myinput.getText().toString();
                input = input.replace(" ", "%20");
                url += input;
                System.out.println(url);
                Document doc = Jsoup.connect(url).get();

                Element link = doc.select("a.title").first();
                String absHref = link.attr("abs:href");
                System.out.println("2");
                doc = Jsoup.connect(absHref).get();
                doc.outputSettings().prettyPrint(false);
                Elements lyrics = doc.select("span.lyrics__content__ok, span.lyrics__content__warning");
                mylyrics="";
                for(int i=0;i<lyrics.size();i++){
                    String hey = doc.select("span.lyrics__content__ok, span.lyrics__content__warning").eq(i).toString();

                    hey = hey.replace((char)10, '\n');
                    hey = hey.replace("<span class=\"lyrics__content__ok\">", "");
                    hey = hey.replace("<span class=\"lyrics__content__warning\">", "");
                    hey = hey.replace("</span>", "");

                    mylyrics += hey;
                }

            } catch (Exception e) {
                mylyrics = "Lo siento, no he podido encontrar la canción :(";
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            myoutput.setMovementMethod(new ScrollingMovementMethod());
            myoutput.setText(mylyrics);
        }
    }
}
