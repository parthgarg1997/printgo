package com.parthgarg.printgo;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.amazonaws.mobile.client.AWSMobileClient;
import com.amazonaws.mobile.client.Callback;
import com.amazonaws.mobile.client.UserStateDetails;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserDetails;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.GetDetailsHandler;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.services.s3.AmazonS3Client;

import java.io.File;
import java.util.Map;
import java.util.UUID;

public class print extends AppCompatActivity {

    private File pdfuri;
    TextView status;
    Switch colorSwitch;
    String folder="amityfileupload-windows";
    String uuid=UUID.randomUUID().toString();
    private static final String TAG = MainActivity.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_print);
        Button SelectFile=(Button)findViewById(R.id.SelectFile);
        colorSwitch=(Switch)findViewById(R.id.Colored);
        status=(TextView)findViewById(R.id.ShowStatus);

      /*  GetDetailsHandler getDetailsHandler = new GetDetailsHandler() {
            @Override
            public void onSuccess(CognitoUserDetails cognitoUserDetails) {
                Toast.makeText(print.this,cognitoUserDetails.getAttributes().getAttributes().get("email"),Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Exception exception) {
                // Fetch user details failed, check exception for the cause
            }
        };
        */



        SelectFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ContextCompat.checkSelfPermission(print.this, Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED)
                {
                    uploadfile();
                }
                else
                {
                    ActivityCompat.requestPermissions(print.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},9);
                }
            }
        });

        Button upload=(Button)findViewById(R.id.Upload);
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (pdfuri == null || pdfuri.isDirectory() || !pdfuri.exists()) {
                    Toast.makeText(print.this,"invalid",Toast.LENGTH_SHORT).show();
                }
                else {
                    uploadWithTransferUtility();
                    RequestClass req = new RequestClass();
                    req.setColored(colorSwitch.isChecked());
                    req.setCustomer_id("a");
                    req.setEmail("abc@kyz.com");
                    req.setMobile_no("+91999999999");
                    req.setFolder(folder);
                    req.setFile_name(uuid);
                }
            }
        });
     /*   AWSMobileClient.getInstance().initialize(getApplicationContext(), new Callback<UserStateDetails>() {
            @Override
            public void onResult(UserStateDetails userStateDetails) {
                Log.i(TAG, "AWSMobileClient initialized. User State is " + userStateDetails.getUserState());
            }

            @Override
            public void onError(Exception e) {
                Log.e(TAG, "Initialization error.", e);
            }
        });*/
    }
    public void uploadWithTransferUtility() {

        TransferUtility transferUtility =
                TransferUtility.builder()
                        .context(getApplicationContext())
                        .awsConfiguration(AWSMobileClient.getInstance().getConfiguration())
                        .s3Client(new AmazonS3Client(AWSMobileClient.getInstance()))
                        .build();



        TransferObserver uploadObserver =
                transferUtility.upload(folder,
                        "public/"+ uuid,
                        pdfuri);

        // Attach a listener to the observer to get state update and progress notifications
        uploadObserver.setTransferListener(new TransferListener() {

            @Override
            public void onStateChanged(int id, TransferState state) {
                if (TransferState.COMPLETED == state) {
                    Toast.makeText(print.this,"finnaly",Toast.LENGTH_SHORT).show();
                    Intent i=new Intent(print.this,Pay.class);
                    startActivity(i);
                    pdfuri=null;
                }
            }

            @Override
            public void onProgressChanged(int id, long bytesCurrent, long bytesTotal) {
                float percentDonef = ((float) bytesCurrent / (float) bytesTotal) * 100;
                int percentDone = (int)percentDonef;

                Log.d("YourActivity", "ID:" + id + " bytesCurrent: " + bytesCurrent
                        + " bytesTotal: " + bytesTotal + " " + percentDone + "%");
            }
            @Override
            public void onError(int id, Exception ex) {
                // Handle errors
            }
        });

        // If you prefer to poll for the data, instead of attaching a
        // listener, check for the state and progress in the observer.
        if (TransferState.COMPLETED == uploadObserver.getState()) {
            // Handle a completed upload.
        }

        Log.d("YourActivity", "Bytes Transferred: " + uploadObserver.getBytesTransferred());
        Log.d("YourActivity", "Bytes Total: " + uploadObserver.getBytesTotal());
    }

    public void uploadfile()
    {
        Intent intent=new Intent();
        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,86);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==86 && resultCode==RESULT_OK && data!=null)
        {
            File file=new File(PathUtils.getPath(this, data.getData())) ;//new File("/storage/emulated/0/Xender/abc.pdf");//new File(data.getData().getPath());
            pdfuri= file;
            status.setText("Your File Selected is  "+file.getName());
            if (pdfuri == null || pdfuri.isDirectory() || !pdfuri.exists()) {
                Toast.makeText(print.this,"invalid",Toast.LENGTH_SHORT);
            }
        }
        else
            Toast.makeText(this, "Please select atleast one file", Toast.LENGTH_SHORT).show();
    }
}

