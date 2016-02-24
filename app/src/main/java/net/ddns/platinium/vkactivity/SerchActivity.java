package net.ddns.platinium.vkactivity;

import android.os.*;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.*;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.*;
import java.net.URL;

public class SerchActivity extends AppCompatActivity {

    Handler handle;
    Button start;
    EditText ID;
    EditText Result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.serch_layout);
        start = (Button)findViewById(R.id.Start);
        ID = (EditText)findViewById(R.id.UserID);
        Result = (EditText)findViewById(R.id.Results);
        handle = new Handler();
        //getData();
    }

    private void getData()
    {
        new Thread(new Connect("https://api.vk.com/method/friends.get.xml?user_id=173852943",ID.getText().toString())).start();
    }

    public void startSerch(View view) {
        getData();
    }

    class Connect implements Runnable
    {
        private String APIPath;
        private String Needle;
        Connect(String Path,String needle)
        {
            Needle = needle;
            APIPath = Path;
        }

        @Override
        public void run() {
            try
            {
                URL url = new URL(APIPath);//&fields=online");
                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                XmlPullParser parser = factory.newPullParser();
                parser.setInput(new InputStreamReader(url.openStream()));
                String tmp,result = null;
                while(parser.getEventType() != XmlPullParser.END_DOCUMENT)
                {
                    switch(parser.getEventType())
                    {
                        case XmlPullParser.START_TAG:
                            if(parser.getName().equalsIgnoreCase("uid"))
                            {
                                parser.next();
                                if(parser.getText().equals(Needle))
                                {
                                    result = "Result Mached";
                                    break;
                                }
                            }
                            break;
                        case XmlPullParser.TEXT:

                            break;
                    }
                    parser.next();
                }
                if(result != null)
                {
                    final String s = result;
                    handle.post(new Runnable() {
                        @Override
                        public void run() {
                            Result.append(s+" - serched id!\n");
                        }
                    });
                }
                else
                {
                    handle.post(new Runnable() {
                        @Override
                        public void run() {
                            Result.append("failed\n");
                        }
                    });
                }
            }
            catch (Exception e)
            {
                final String s = e.getMessage();
                handle.post(new Runnable() {
                    @Override
                    public void run()
                    {
                        Toast.makeText(getBaseContext(), s, Toast.LENGTH_LONG).show();
                    }
                });
            }
        }
    }
}

