package xcat.daiyonkaigi.guchiruna.activity;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import xcat.daiyonkaigi.guchiruna.R;
import xcat.daiyonkaigi.guchiruna.db.RankingDBOpenHelper;

/**
 * Created by anago on 2015/10/13.
 */
public class RankingActivity extends Activity implements OnClickListener {
    private Button menuButton;
    private Button guchiButton;
    private Button negapoziButton;
    private Intent intent;
    private ListView listView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ranking);

        listView = (ListView)findViewById(R.id.showData);

        //DB作成
        RankingDBOpenHelper rankingDB = new RankingDBOpenHelper(this);
        SQLiteDatabase db = rankingDB.getReadableDatabase();

        //SQL
        Cursor c = db.rawQuery("select word as _id, count(*) from rankings group by word", null);

        String[] from = {"_id","count(*)"};

        int[] to = {android.R.id.text1,android.R.id.text2};

        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this,android.R.layout.simple_list_item_2,c,from,to,0);

        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String s1 = ((TextView)view.findViewById(android.R.id.text1)).getText().toString();

                String s2 = ((TextView)view.findViewById(android.R.id.text2)).getText().toString();

                Log.v("tama", "position=" + s1);
                Log.v("tama", "position=" + s2);
            }
        });


        menuButton = (Button) findViewById(R.id.menu);
        menuButton.setOnClickListener(this);

        guchiButton = (Button) findViewById(R.id.guchi);
        guchiButton.setOnClickListener(this);

        negapoziButton = (Button) findViewById(R.id.negapozi);
        negapoziButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == menuButton) {
            intent = new Intent(this, xcat.daiyonkaigi.guchiruna.activity.InputActivity.class);
            startActivity(intent);
        } else if (v == guchiButton) {
            intent = new Intent(this, xcat.daiyonkaigi.guchiruna.activity.InputActivity.class);
            startActivity(intent);
        } else if (v == negapoziButton) {
            intent = new Intent(this, xcat.daiyonkaigi.guchiruna.activity.InputActivity.class);
            startActivity(intent);
        }
    }
}
