package xcat.daiyonkaigi.guchiruna.activity;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import xcat.daiyonkaigi.guchiruna.R;
import xcat.daiyonkaigi.guchiruna.db.ArticleDBOpenHelper;


public class InputActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //DBの初期化処理
        ArticleDBOpenHelper helper = new ArticleDBOpenHelper(this);
        final SQLiteDatabase db = helper.getWritableDatabase();

        //テキスト及び関連する情報の内容を取得
        final EditText editText = (EditText) findViewById(R.id.editText);

        //TODO 日付などの情報を取得

        //ボタン押下時の登録処理
        Button confirmButton = (Button)this.findViewById(R.id.button);
        confirmButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                String article = editText.getText().toString();

                //記事と日付を格納
                ContentValues articleInsertValues = new ContentValues();
                articleInsertValues.put("article", article);
                articleInsertValues.put("date", getCurrentDate());

                long id = db.insert("article", "null", articleInsertValues);

                //次画面での表示処理
                Intent dbIntent = new Intent(InputActivity.this,TimelineActivity.class);
                startActivity(dbIntent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * 現在日時をyyyy/MM/dd HH:mm:ss形式で取得するメソッドです。
     * @return yyyy/MM/dd HH:mm:ssで表記された時刻表記
     */
    private String getCurrentDate(){
        final DateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        final Date date = new Date(System.currentTimeMillis());
        return df.format(date);
    }
}
