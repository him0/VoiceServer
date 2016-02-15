package net.him0.voiceserver;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.os.AsyncTask;

import java.util.Date;

import jp.ne.docomo.smt.dev.aitalk.AiTalkTextToSpeech;
import jp.ne.docomo.smt.dev.aitalk.data.AiTalkSsml;
import jp.ne.docomo.smt.dev.common.exception.SdkException;
import jp.ne.docomo.smt.dev.common.exception.ServerException;

/**
 * Created by him0 on 2016/02/14.
 */
public class TextToVoiceTask extends AsyncTask<String, Void, Void> {
    private AiTalkSsml ssml = null;
    private AiTalkTextToSpeech speech = null;
    private String text = "";
    private TimeLineAdapter timeLineAdapter;

    public TextToVoiceTask(TimeLineAdapter adapter){
        this.timeLineAdapter = adapter;
        this.ssml = new AiTalkSsml();
        this.speech = new AiTalkTextToSpeech();
    }

    @Override
    protected Void doInBackground(String... params) {
        this.text = params[0];
        this.playVoice();
        this.addToTimeLine();
        return null;
    }

    public void playVoice() {
        byte[] resultData = null;

        ssml.startVoice("nozomi");
        ssml.addText(text);
        ssml.endVoice();

        try {
            resultData = speech.requestAiTalkSsmlToSound(ssml.makeSsml());
        } catch (SdkException e) {
            e.printStackTrace();
        } catch (ServerException e) {
            e.printStackTrace();
        }

        int bufSize = AudioTrack.getMinBufferSize(
                16000,
                AudioFormat.CHANNEL_OUT_MONO,
                AudioFormat.ENCODING_PCM_16BIT);
        speech.convertByteOrder16(resultData);
        AudioTrack at = new AudioTrack(
                AudioManager.STREAM_MUSIC,
                16000,
                AudioFormat.CHANNEL_OUT_MONO,
                AudioFormat.ENCODING_PCM_16BIT,
                bufSize,
                AudioTrack.MODE_STREAM);
        at.play();
        at.write(resultData, 0, resultData.length);
        try {
            Thread.sleep(resultData.length / 32);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return;
    }

    public void addToTimeLine() {
        Statement statement = new Statement();
        statement.setContent(text);
        statement.setDateStamp(new Date());
        timeLineAdapter.add(statement);
        return;
    }
}
