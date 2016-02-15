package net.him0.voiceserver;

import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import jp.ne.docomo.smt.dev.common.http.AuthApiKey;

public class MainActivity extends AppCompatActivity {
    private static final int PORT = 8888;
    private MyHttpd server;
    private Router router = new Router();

    private ListView listView;
    private ArrayList<Statement> statements;
    private TimeLineAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // https://dev.smt.docomo.ne.jp/
        // create api key and paste
        // 「音声合成【Powerd by エーアイ】」 permission is required
        AuthApiKey.initializeAuth("{docomo developer support api key}");

        // Timeline List
        this.listView = (ListView)findViewById(R.id.listView);
        this.statements = new ArrayList<>();

        this.adapter = new TimeLineAdapter(MainActivity.this);

        Statement statement = new Statement();
        statement.setContent("Server Start!");
        statement.setDateStamp(new Date());
        this.statements.add(statement);
        this.adapter.setStatements(statements);

        this.listView.setAdapter(this.adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // show IP address
        TextView textIpaddr = (TextView) findViewById(R.id.ipaddr);
        WifiManager wifiManager = (WifiManager) getSystemService(WIFI_SERVICE);
        int ipAddress = wifiManager.getConnectionInfo().getIpAddress();
        final String formatedIpAddress = String.format("%d.%d.%d.%d", (ipAddress & 0xff), (ipAddress >> 8 & 0xff),
                (ipAddress >> 16 & 0xff), (ipAddress >> 24 & 0xff));
        textIpaddr.setText("Please access! http://" + formatedIpAddress + ":" + PORT);

        try {
            server = new MyHttpd();
            server.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (server != null) {
            server.stop();
        }
    }

    private class MyHttpd extends NanoHTTPD {
        public  MyHttpd() throws IOException {
            super(PORT);
        }

        @Override
        public Response serve(IHTTPSession session) {
            router.accept(session);

            String html = "<html><head>" +
                    "<title>Reichan Huose Sever</title>" +
                    "</head><body>" +
                    "<h1>Hello, World!</h1>" +
                    "<p>path: " + session.getUri() + "</p>" +
                    "<p>parms: " + session.getParms().toString() + "</p>" +
                    "</body></html>";
            return new NanoHTTPD.Response(Response.Status.OK, MIME_HTML, html);
        }
    }

    private class Router {
        public void accept(NanoHTTPD.IHTTPSession session) {
            if (session.getUri().equals("/say")) {
                String text = session.getParms().get("text");
                if (!text.equals("")) {
                    TextToVoiceTask textToVoiceTask = new TextToVoiceTask(adapter);
                    textToVoiceTask.execute(text);
                    Log.d("Router path", "/say");
                    Log.d("Router text", "text: " + text);
                }
                return;
            }
        }
    }
}
