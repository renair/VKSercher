package net.ddns.platinium.vkactivity;

import android.app.PendingIntent;
import android.content.Intent;
import android.os.Handler;
import android.provider.Telephony;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.InputStream;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    EditText text;
    Handler handle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        text = (EditText)findViewById(R.id.mainText);
        handle = new Handler();
        //SmsManager s = SmsManager.getDefault();
        //s.sendTextMessage("+380963004654",null,"tested sms",null,null);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId())
        {
            case R.id.Refresh:
                getData();
                break;
            case R.id.WaySerch:
                startActivity(new Intent(getBaseContext(),SerchActivity.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void getData()
    {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL("https://api.vk.com/method/users.get.xml?user_id=173852943&fields=last_seen,online");
                    InputStream stream = url.openStream();
                    final byte[] buf = new byte[1024];
                    while (stream.available() > 0) {
                        stream.read(buf, 0, buf.length);
                        handle.post(new Runnable() {
                            @Override
                            public void run() {
                                text.setText("");
                                text.append(new String(buf) + "\n");
                            }
                        });
                    }
                }
                catch (Exception e)
                {
                    final String s = e.getMessage();
                    handle.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getBaseContext(), s, Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        }).start();
    }
}
