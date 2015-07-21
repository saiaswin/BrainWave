package com.code.scanner;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;


public class ScannerUtils extends Activity {

    private static Context mContext;
    private static ScanMode mScanMode;
    private static ScanListener mListener;

    private static ScannerUtils mInstance;

    private static final int QR_CODE_SCANNER=100;
    private static final String ZXING_BAR_CODE_SCANNER_PACKAGE_INTENT ="com.google.zxing.client.android.SCAN";
    private static final String ZXING_BAR_CODE_SCANNER_PACKAGE_NAME ="com.google.zxing.client.android";
    private static final String SCAN_MODE="SCAN_MODE";
    private static final String SCAN_RESULT="SCAN_RESULT";

    public static ScannerUtils getInstance(){
        if(mInstance==null){
            mInstance=new ScannerUtils();
        }

        return mInstance;
    }

    public void setContext(Context context){
        mContext=context;
    }

    public void setScanMode(ScanMode scanMode){
        mScanMode=scanMode;
    }

    public void setListener(ScanListener listener){
        mListener=listener;
    }

    public ScannerUtils(){

    }

    public void scan(){
        if(mListener==null){
            throw new RuntimeException("Calling Activity must implement ScanListener");
        }else if(mScanMode==null){
            mListener.onError("Please specify a scan mode");
            return;
        }else if(mContext == null){
            mListener.onError("Please specify an activity");
            return;
        }

        if(isAppInstalled()){
            Intent intent=new Intent(mContext,ScannerUtils.class);
            mContext.startActivity(intent);
        }else{
            AlertDialog.Builder builder=new AlertDialog.Builder(mContext)
                    .setMessage("To scan the QR code, you will need to install the Barcode Scanner app. Would you like to install it from the Google PlayStore?")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Uri marketUri = Uri.parse("market://details?id="+ ZXING_BAR_CODE_SCANNER_PACKAGE_NAME);
                            Intent marketIntent = new Intent(Intent.ACTION_VIEW,marketUri);
                            mContext.startActivity(marketIntent);
                        }
                    })
                    .setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                        }
                    });
            builder.show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = new Intent(ZXING_BAR_CODE_SCANNER_PACKAGE_INTENT);
        intent.putExtra(SCAN_MODE, mScanMode.toString());
        startActivityForResult(intent, QR_CODE_SCANNER);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==QR_CODE_SCANNER){
            if(resultCode==RESULT_OK){
                String contents = data.getStringExtra(SCAN_RESULT);
                mListener.onSuccess(contents);
            }else{
                mListener.onError("Unable to scan the image");
            }

            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
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



    private boolean isAppInstalled() {
        PackageManager pm = mContext.getPackageManager();
        boolean app_installed = false;
        try {
            pm.getPackageInfo(ZXING_BAR_CODE_SCANNER_PACKAGE_NAME, PackageManager.GET_ACTIVITIES);
            app_installed = true;
        }
        catch (PackageManager.NameNotFoundException e) {
            app_installed = false;
        }
        return app_installed ;
    }
}
