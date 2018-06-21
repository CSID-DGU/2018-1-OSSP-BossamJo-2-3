package ng.dat.ar;

import android.app.ProgressDialog;
import android.content.Context;
import android.hardware.Camera;
import android.hardware.SensorManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceView;
import android.widget.FrameLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class SubActivity extends AppCompatActivity {

    final static String TAG = "SubActivity";

    private static final String TAG_JSON="APInfo";
    private static final String TAG_INFO="info";
    private static final String TAG_MAC="mac";

    ArrayList<HashMap<String, String>> mArrayList = new ArrayList<>();
    String mJsonString;
    String currentAPMacAddress;

    //--------------UI variables------------------------
    private SurfaceView surfaceView;
    private FrameLayout cameraContainerLayout;
    // private AROverlayView arOverlayView;
    private Camera camera;
    private ARCamera arCamera;
    private TextView tvCurrentLocation;
    private SubActivity.GetData task;
    private SensorManager sensorManager;
    private TextView textView;
    private String _mac;
    private String _info;

    private final static int REQUEST_CAMERA_PERMISSIONS_CODE = 11;
    public static final int REQUEST_LOCATION_PERMISSIONS_CODE = 0;
    //------------------------------------------------------------------------------
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 0; // 10 meters
    private static final long MIN_TIME_BW_UPDATES = 0;//1000 * 60 * 1; // 1 minute


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub);

        textView=(TextView)findViewById(R.id.textview);
        task = new SubActivity.GetData();
        task.execute("http://13.125.248.203/connect.php");

        sensorManager = (SensorManager) this.getSystemService(SENSOR_SERVICE);

        SubActivity.BackThread thread = new SubActivity.BackThread();
        thread.setDaemon(true);
        thread.start();
    }

    String a="NewEngineeringBuilding3rd";
    String b="NewEngineeringBuilding9th";
    String c="NewEngineeringBuilding8th";
    String x;

    class BackThread extends Thread{
        @Override
        public void run(){
            while(true){
                currentAPMacAddress=getMacId().toUpperCase();
                for (HashMap<String, String> entry : mArrayList) {
                    _mac = entry.get(TAG_MAC).toString();
                    if (_mac.equals(currentAPMacAddress)) {
                        _info = entry.get(TAG_INFO).toString();
                        handler.sendEmptyMessage(0);
                        if(_info.equals(a)){
                            x="실험실습실, 강의실, 택배실";
                        }
                        if(_info.equals(b)){
                            x="학과사무실, 교수연구실";
                        }
                        if(_info.equals(c)){
                            x="대학원연구실, 교수연구실";
                        }

                    }
                }
                try {
                    Thread.sleep(1000);
                }catch(InterruptedException e){
                    e.printStackTrace();
                }
            }
        }
    }
    Handler handler=new Handler(){
        public void handleMessage(Message msg){
            if(msg.what==0)
            {
                    textView.setText(x);
            }
            else
            {
                textView.setText("no match");
            }
        }
    };

    //AsyncTask 추상 클래스
    //백그라운드로 GetData 클래스 실행
    private class GetData extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;
        String errorString = null;

        //초기 실행
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(SubActivity.this,
                    "Please Wait", null, true, true);
        }

        //백그라운드 실행
        //전달받은 서버의 IP 주소를 받아서 connection 생성
        @Override
        protected String doInBackground(String... params) {

            String serverURL = params[0];//서버의 IP 주소


            try {

                URL url = new URL(serverURL);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();


                httpURLConnection.setReadTimeout(5000);
                httpURLConnection.setConnectTimeout(5000);
                httpURLConnection.connect();


                int responseStatusCode = httpURLConnection.getResponseCode();
                Log.d(TAG, "response code - " + responseStatusCode);

                InputStream inputStream;
                if (responseStatusCode == HttpURLConnection.HTTP_OK) {
                    inputStream = httpURLConnection.getInputStream();
                } else {
                    inputStream = httpURLConnection.getErrorStream();
                }


                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                StringBuilder sb = new StringBuilder();
                String line;

                while ((line = bufferedReader.readLine()) != null) {
                    sb.append(line);
                }


                bufferedReader.close();

                //읽어온 데이터를 문자열로 전환
                // return sb.toString().trim();
                mJsonString = sb.toString().trim();
                stringToJSON();

//이거 반환 값 조정 필요
                return null;
            } catch (Exception e) {

                Log.d(TAG, "InsertData: Error ", e);
                errorString = e.toString();

                return null;
            }

        }

        //백그라운드 종료될 때 실행
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            progressDialog.dismiss();
            //           mTextViewResult.setText(result); 문자열 화면에 출력
            //           Log.d(TAG, "response  - " + result);

            if (result == null){
                //에러 표시 출력문
//               mTextViewResult.setText(errorString);
            }
            else {
                // 여기 조정 필요
                //JSON으로 변환
                //mJsonString = result;
                //stringToJSON();
                //Intent a = new Intent(getApplicationContext(),ARActivity.class);
                //startActivity(a);
            }
        }
    }

    //string을 JSON으로 변환 후에 ArrayList에 저장, ArrayList에 자료형은 HashMap.
    private void stringToJSON(){
        try {
            JSONObject jsonObject = new JSONObject(mJsonString);
            JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);

            for(int i=0;i<jsonArray.length();i++){

                JSONObject item = jsonArray.getJSONObject(i);

                String info = item.getString(TAG_INFO);
                String mac = item.getString(TAG_MAC);


                HashMap<String,String> hashMap = new HashMap<>();

                hashMap.put(TAG_MAC, mac);
                hashMap.put(TAG_INFO,info);

                mArrayList.add(hashMap);
                Log.i("arraylistsize", mArrayList.size() + "");

            }

        } catch (JSONException e) {

            Log.d(TAG, "showResult : ", e);
        }

    }

    private String getMacId() { //ap address를 불러옵니다.
        ConnectivityManager connManager=(ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if(networkInfo.isConnected()){
            final WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
            final WifiInfo connectionInfo=wifiManager.getConnectionInfo();

            return connectionInfo.getBSSID();
        }
        return null;
    }
}
