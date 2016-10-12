package com.example.tianhuihuang.top10downloader;

import android.os.AsyncTask;
import android.support.annotation.MainThread;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.util.Log;
import android.net.URL;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import android.R.*; //下面引用了R文件,所以不是这里的问题,而且这里还是灰色的,根本没有用过
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.tianhuihuang.top10downloader.R;


public class MainActivity extends AppCompatActivity {

    //private TextView xmlTextView;
    private String mFileContents;
    private Button btnParse;
    private ListView listApps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //xmlTextView=(TextView) findViewById(R.id.xmlTextView);
        //这一步是将前端的按钮,与后端的关联起来
        btnParse=(Button) findViewById(R.id.btnParse);
        //按钮点击之后发生什么事情,这个写法比较新奇,要注意
        btnParse.setOnClickListener(new View.OnClickListener(){

            @Override
            public  void onClick(View v){

                //TODO: Add parse activation <code>
                ParseApplications parseApplications=new ParseApplications(mFileContents);
                parseApplications.process();
                //arrayAdapter 就是后端的数据库与前端的界面进行链接的interface
                ArrayAdapter<Application> arrayAdaper =new ArrayAdapter<Application>(
                        MainActivity.this,R.layout.list_item,parseApplications.getApplications());
                listApps.setAdapter(arrayAdaper);

            }

        });
        listApps=(ListView)findViewById(R.id.xmlListView);
        DownloadData downloadData=new DownloadData();
        downloadData.execute("http://ax.itunes.apple.com/WebObjects/MZStoreServices.woa/ws/RSS/topfreeapplications/limit=10/xml");

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        //Inflate the nemu; this adds items to the action bar if it is present
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        //handle action bar item clicks here. The action bar will automatically handle clicks
        //on the home/UP button, so long as you specify a parent activity
        //in the AndroidManifest.xml
        int id=item.getItemId();
        if(id==R.id.action_settings){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private class DownloadData extends AsyncTask<String,Void,String>{


        //@Override
        protected String doInBackGround(String...para){
            mFileContents=downloadXMLFile(param[0]);
            if (mFileContents==null){
                Log.d("DownloadData","Error downloading");
            }
            return mFileContents;
        }

        @Override
        protected void onPostExcute(String result){
            super.onPostExecute(result);
            Log.d("DownloadData","Result was: "+result);
            //将下载下来的XML文件存储下
            //这一步的结果就是将下载下来的XML文件在屏幕上显示出来
            xmlTextView.setText(mFileContents);
        }

        private String downloadXMLFile(String urlPath){
            StringBuilder tempBuffer=new StringBuilder();
            try{
                URL url=new URL(urlPath);
                //open the url link
                HttpURLConnection connection=(HttpURLConnection) url.openConnection();
                int response = connection.getResponseCode();
                Log.d("DownloadData","The response code was" + response);
                InputStream is=connection.getInputStream();
                InputStreamReader isr=new InputStreamReader(is);
                //the first step is to use the URL to connect and then initialy the charReader to get those strings

                int charRead;
                char[] inputBuffer=new char[500];
                while(true){
                   charRead=isr.read(inputBuffer);
                    if(charRead<=0){
                        break;
                    }
                    tempBuffer.append(String.copyValueOf(inputBuffer,0,charRead));
                }

                return tempBuffer.toString();
            }catch (IOException e){
                Log.d("DownloadData","IO Exception reading data: "+e.getMessage());
            }catch (SecurityException e){
                Log.d("DownloadData","Security exception. Needs permissions?"+e.getMessage());
            }

            //here is a method for debugging if no files then should be catch an exception.
            return null;
        }
    }
}
