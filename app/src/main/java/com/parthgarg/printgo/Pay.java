package com.parthgarg.printgo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.amazonaws.mobile.client.AWSMobileClient;
import com.amazonaws.mobileconnectors.lambdainvoker.*;
import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3Client;
import com.paytm.pgsdk.PaytmOrder;
import com.paytm.pgsdk.PaytmPGService;
import com.paytm.pgsdk.PaytmPaymentTransactionCallback;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Pay extends AppCompatActivity {

    int q=0;
    PaytmPGService Service;
    private File pdfuri=Resourses.pdfUri;
    String folder="amityfileupload-windows";
    //String uuid= UUID.randomUUID().toString();
    private static final String TAG = MainActivity.class.getSimpleName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);
        TextView file_name=(TextView)findViewById(R.id.File_name);
        TextView No_page=(TextView)findViewById(R.id.No_page);
        TextView Colored=(TextView)findViewById(R.id.Colored);
        TextView Amount=(TextView)findViewById(R.id.Amount);
        TextView OrderNo=(TextView)findViewById(R.id.Order_no);
        TextView Vendor=(TextView)findViewById(R.id.Vendor);

        file_name.setText(Resourses.File_name);
        No_page.setText(Resourses.No_page+"");
        if(Resourses.Colored==true)
            Colored.setText("Colored");
        else
            Colored.setText("Black & White");
        Amount.setText(Resourses.Amount+"");
        OrderNo.setText(Resourses.order_no);
        Vendor.setText(Resourses.Vendor);

        Service = PaytmPGService.getStagingService();
        Map<String, String> paramMap = new HashMap<String,String>();
        paramMap.put( "MID" , "drBZkM69560596451366");
// Key in your staging and production MID available in your dashboard
        paramMap.put( "ORDER_ID" , Resourses.order_no);
        paramMap.put( "CUST_ID" , Resourses.cust_id);
        paramMap.put( "MOBILE_NO" , Resourses.phone_no);
        paramMap.put( "EMAIL" , Resourses.email);
        paramMap.put( "CHANNEL_ID" , "WAP");
        paramMap.put( "TXN_AMOUNT" , Resourses.Amount+"");
        paramMap.put( "WEBSITE" , "WEBSTAGING");
// This is the staging value. Production value is available in your dashboard
        paramMap.put( "INDUSTRY_TYPE_ID" , "Retail");
// This is the staging value. Production value is available in your dashboard
        paramMap.put( "CALLBACK_URL", "https://securegw-stage.paytm.in/theia/paytmCallback?ORDER_ID=order1");
        paramMap.put( "CHECKSUMHASH" , Resourses.CheckSum);
        PaytmOrder Order = new PaytmOrder((HashMap<String, String>) paramMap);
        Service.initialize(Order, null);
        Button pay_button=(Button)findViewById(R.id.PayButton);
        pay_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(q==0)
               uploadWithTransferUtility();
                Service.startPaymentTransaction(view.getContext(), true, true, new PaytmPaymentTransactionCallback() {
                    /*Call Backs*/
                    public void someUIErrorOccurred(String inErrorMessage) {
                        Toast.makeText(getApplicationContext(), "UI Error " + inErrorMessage , Toast.LENGTH_LONG);
                        finish();
                    }
                    public void onTransactionResponse(Bundle inResponse) {
                        Toast.makeText(getApplicationContext(), "Payment Transaction response " + inResponse.toString(), Toast.LENGTH_LONG).show();
                        finish();
                    }
                    public void networkNotAvailable() {
                        Toast.makeText(getApplicationContext(), "Network connection error: Check your internet connectivity", Toast.LENGTH_LONG).show();
                        finish();
                    }
                    public void clientAuthenticationFailed(String inErrorMessage) {
                        Toast.makeText(getApplicationContext(), "Authentication failed: Server error" + inErrorMessage, Toast.LENGTH_LONG).show();
                        finish();
                    }
                    public void onErrorLoadingWebPage(int iniErrorCode, String inErrorMessage, String inFailingUrl) {
                        Toast.makeText(getApplicationContext(), "Unable to load webpage " + inErrorMessage, Toast.LENGTH_LONG).show();
                        finish();
                    }
                    public void onBackPressedCancelTransaction() {
                        Toast.makeText(getApplicationContext(), "Transaction cancelled" , Toast.LENGTH_LONG).show();
                        finish();
                    }
                    public void onTransactionCancel(String inErrorMessage, Bundle inResponse) {}
                });
            }
        });

    }


    public void uploadWithTransferUtility() {

        q=1;
        TransferUtility transferUtility =
                TransferUtility.builder()
                        .context(getApplicationContext())
                        .awsConfiguration(AWSMobileClient.getInstance().getConfiguration())
                        .s3Client(new AmazonS3Client(AWSMobileClient.getInstance()))
                        .build();



        TransferObserver uploadObserver =
                transferUtility.upload(folder,
                        "public/"+ Resourses.File_name,
                        pdfuri);

        // Attach a listener to the observer to get state update and progress notifications
        uploadObserver.setTransferListener(new TransferListener() {

            @Override
            public void onStateChanged(int id, TransferState state) {
                if (TransferState.COMPLETED == state) {
                    Toast.makeText(Pay.this,"finnaly",Toast.LENGTH_SHORT).show();
                    Intent i=new Intent(Pay.this,Pay.class);
                    //startActivity(i);
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
}
