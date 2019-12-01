package com.example.chat_app;

import android.app.Activity;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.math.BigInteger;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.nio.ByteOrder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class MyActivity extends Activity {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    private List<ChatData> myDataset;

    private String name = "";

    private Button button_send;
    private EditText editText_inputtxt;

    private  DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //////////////////////////////////////////////////////////
        // IP + 시간 + 지역 정보 + 랜덤번호 부여
        // 시간
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm");

        // 지역
        Locale systemLocale = getApplicationContext().getResources().getConfiguration().locale;
        String strCountry = systemLocale.getCountry();

        // 랜덤 번호 부여
        Random random = new Random();
        name = random.nextInt(9999) + 1000 + "";

        //Log.d("     IP      : ",getLocalIpAddress() + "  /  " + sdf.format(date) + "  /  " + strCountry + " - " + name);
        Log.d("     IP true      : ", getIPAddress(true));
        Log.d("     IP false      : ", getIPAddress(false));
        Log.d("     IP      : ", wifiIpAddress());
        //////////////////////////////////////////////////////////

        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);

        button_send = findViewById(R.id.Button_Send);
        editText_inputtxt = findViewById(R.id.EditText_input);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef = database.getReference();

        button_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = editText_inputtxt.getText().toString();

                if(msg != null)
                {
                    ChatData chat = new ChatData();
                    chat.setName(name);
                    chat.setMsg(msg);
                    editText_inputtxt.setText("");

                    myRef.push().setValue(chat);
                }
            }
        });

        myDataset = new ArrayList<>();
        mAdapter = new MyAdapter(myDataset,name);
        recyclerView.setAdapter(mAdapter);

        // Read from the database
     myRef.addChildEventListener(new ChildEventListener() {
         @Override
         public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
             ChatData chat = dataSnapshot.getValue(ChatData.class);
             ((MyAdapter)mAdapter).addChat(chat);
             recyclerView.scrollToPosition(recyclerView.getAdapter().getItemCount()-1);
         }

         @Override
         public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

         }

         @Override
         public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

         }

         @Override
         public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

         }

         @Override
         public void onCancelled(@NonNull DatabaseError databaseError) {

         }
     });
    }

    // IP주소 얻기
    //기본
    public static String getLocalIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
                        return inetAddress.getHostAddress();
                    }
                }
            }
        } catch (SocketException ex) {
           Log.d("      IP Error    : ",ex.getMessage());
        }
        return null;
    }

    //이더넷
    public static String getIPAddress(boolean useIPv4)
    {
        try
        {
            List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface intf : interfaces)
            {
                List<InetAddress> addrs = Collections.list(intf.getInetAddresses());
                for(InetAddress addr:addrs)
                {
                    if(!addr.isLoopbackAddress()) {
                        String sAddr = addr.getHostAddress();
                        boolean isIPv4 = sAddr.indexOf(':')<0;

                        if(useIPv4)
                        {
                            if(isIPv4)
                                return sAddr;
                        }else
                        {
                            if(!isIPv4)
                            {
                                int delim = sAddr.indexOf('%');
                                return delim<0?sAddr.toUpperCase() : sAddr.substring(0,delim).toUpperCase();
                            }
                        }
                    }
                }
            }
        }
        catch (Exception e)
        {
            Log.d(" IP Error    : ", e.getMessage());
        }
        return "";
    }

    //와이파이
    public String wifiIpAddress()
    {
        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
        int ipAddress = wifiManager.getConnectionInfo().getIpAddress();

        if(ByteOrder.nativeOrder().equals(ByteOrder.LITTLE_ENDIAN))
        {
            ipAddress = Integer.reverseBytes(ipAddress);
        }

        byte[] ipByteArray = BigInteger.valueOf(ipAddress).toByteArray();

        String ipAddressString;
        try
        {
            ipAddressString = InetAddress.getByAddress(ipByteArray).getHostAddress();
        }
        catch (Exception e)
        {
            ipAddressString = null;
        }
        return  ipAddressString;
    }
}