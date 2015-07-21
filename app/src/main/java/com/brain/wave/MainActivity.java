package com.brain.wave;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.code.scanner.*;

public class MainActivity extends ActionBarActivity implements View.OnClickListener{

    Button scanButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        scanButton=(Button)findViewById(R.id.scan_button);
        scanButton.setOnClickListener(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        if(view.getId()==R.id.scan_button){
            ScannerUtils.getInstance().setContext(this);
            ScannerUtils.getInstance().setScanMode(ScanMode.QRCODE);
            ScannerUtils.getInstance().setListener(new ScanListener() {
                @Override
                public void onSuccess(String scanData) {
                    Toast.makeText(MainActivity.this,scanData,Toast.LENGTH_LONG).show();
                }

                @Override
                public void onError(String errorMsg) {
                    Toast.makeText(MainActivity.this,errorMsg,Toast.LENGTH_LONG).show();
                }
            });

            ScannerUtils.getInstance().scan();
        }
    }
}
