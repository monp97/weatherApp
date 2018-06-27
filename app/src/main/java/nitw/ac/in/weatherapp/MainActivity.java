package nitw.ac.in.weatherapp;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    String finalString;
    RelativeLayout layout;
    EditText editText;
    Button button;
    LinearLayout linearLayout;
    TextView textView;
    TextView view1,view2,view3,view4;


    public void back(View view)
    {
        linearLayout.setVisibility(View.INVISIBLE);
        layout.setBackgroundResource(R.drawable.main);
        editText.setVisibility(View.VISIBLE);
        button.setVisibility(View.VISIBLE);
    }

    public void clear(View view)
    {
        EditText textv=findViewById(view.getId());
        textv.setText("");
    }

    public class Weather extends AsyncTask<String,Void,String>{

        @Override
        protected String doInBackground(String... params) {


            try {
                URL url=new URL(params[0]);
                HttpURLConnection connection=(HttpURLConnection)url.openConnection();
                connection.connect();
                String content="";

                InputStream input=connection.getInputStream();
                InputStreamReader reader=new InputStreamReader(input);
                int i;

                while((i=reader.read())!=-1)
                {
                    content+=(char)i;
                }

                return content;


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onPostExecute(String s) {

            editText.setVisibility(View.INVISIBLE);
            button.setVisibility(View.INVISIBLE);
            linearLayout.setVisibility(View.VISIBLE);



            try {
                JSONObject object=new JSONObject(s);
                JSONObject temp=object.getJSONObject("main");

                String min=temp.getString("temp_min");
                String max=temp.getString("temp_max");
                String humid=temp.getString("humidity");
                double min_temp= Double.parseDouble(min)-273.15;
                double max_temp= Double.parseDouble(max)-273.15;
                JSONArray array=object.getJSONArray("weather");
                String main=array.getJSONObject(0).getString("main");


                //cloud,dust,haze,clear.

                if(main.toLowerCase().equals("clear"))
                {
                    layout.setBackgroundResource(R.drawable.sunny);
                }
                else if(main.toLowerCase().equals("haze"))
                {
                    layout.setBackgroundResource(R.drawable.hazy);
                }
                else if(main.toLowerCase().equals("dust"))
                {
                    layout.setBackgroundResource(R.drawable.dust);
                }
                else if(main.toLowerCase().equals("drizzle") || main.toLowerCase().equals("rain") )
                {
                    layout.setBackgroundResource(R.drawable.rainy);
                }
                else if(main.toLowerCase().equals("clouds"))
                {
                    layout.setBackgroundResource(R.drawable.clouds);
                }
                else
                layout.setBackgroundResource(R.drawable.main);



                view1.setText(main);
                view2.setText(min_temp+" "+(char)0x00B0+"C");
                view3.setText(max_temp+" "+(char)0x00B0+"C");
                view4.setText(humid);


            } catch (JSONException e) {
                e.printStackTrace();
            }



            super.onPostExecute(s);
        }
    }

    public void showWeather(View view) throws ExecutionException, InterruptedException {


        editText=findViewById(R.id.text);
        button=findViewById(R.id.ok);
        textView=findViewById(R.id.weather);
        layout=findViewById(R.id.layout1);
        linearLayout=findViewById(R.id.layout2);
        view1=findViewById(R.id.weatherInfo);
        view2=findViewById(R.id.minTempInfo);
        view3=findViewById(R.id.maxTemoInfo);
        view4=findViewById(R.id.humidityInfo);

        String cityname=editText.getText()+"";

        if(!cityname.isEmpty())
        {

            finalString="http://api.openweathermap.org/data/2.5/weather?q="+ cityname + "&APPID=2e0ed1ef612cb289f311a1f75ade7c70";



        Weather weather=new Weather();

        String content=weather.execute(finalString).get();
            Log.i("content is:",content);
        }

        else
        {
            Toast.makeText(this, "Please enter a city name!", Toast.LENGTH_SHORT).show();
        }


    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


    }
}
