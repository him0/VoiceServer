package net.him0.voiceserver;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by him0 on 2016/02/14.
 */
public class TimeLineAdapter extends BaseAdapter {

    private LayoutInflater layoutInflater;
    private ArrayList<Statement> statements;

    public TimeLineAdapter(Context context) {
        this.layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setStatements(ArrayList<Statement> statements) {
        this.statements = statements;
    }

    @Override
    public int getCount() {
        return statements.size();
    }

    @Override
    public Object getItem(int position) {
        return statements.get(position);
    }

    @Override
    public long getItemId(int position) {
        return statements.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = layoutInflater.inflate(R.layout.statementrow,parent,false);

        ((TextView)convertView.findViewById(R.id.timestamp)).setText(statements.get(position).getTimeStamp());
        ((TextView)convertView.findViewById(R.id.content)).setText(statements.get(position).getContent());

        return convertView;
    }

    public void add(final Statement s) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                if (statements.add(s)) {
                    notifyDataSetChanged();
                }
            }
        });
    }
}
