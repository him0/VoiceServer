# VoiceServer
To make an android smart phone REST server to speak.

## SETTING

ドコモディベロッパーサポートのAPIキーが必要です．

MainActivity.java を編集し，APIキーを埋めてください．

docomo developer support API key is required.

please edit MainActivity and bind the API key.

```
// https://dev.smt.docomo.ne.jp/
// create api key and paste
// 「音声合成【Powerd by エーアイ】」 permission is required
AuthApiKey.initializeAuth("{docomo developer support api key}");
```

## Default REST API
### GET /say
#### params
  text : what want to say

If you request ```http://XXX.XXX.XXX.XXX:8888/say?text=Hello```

Android say "Hello!"
