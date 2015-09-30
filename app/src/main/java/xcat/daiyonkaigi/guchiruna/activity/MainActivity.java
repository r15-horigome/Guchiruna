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

import xcat.daiyonkaigi.guchiruna.R;
import xcat.daiyonkaigi.guchiruna.db.MyOpenHelper;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //DBの初期化処理
        MyOpenHelper helper = new MyOpenHelper(this);
        final SQLiteDatabase db = helper.getWritableDatabase();

        //テキスト及び関連する情報の内容を取得
        final EditText editText = (EditText) findViewById(R.id.editText);

        //TODO 日付などの情報を取得

        //ボタン押下時の登録処理
        Button confirmButton = (Button)this.findViewById(R.id.button);
        confirmButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                String article = editText.getText().toString();
                //TODO 日付を取得

                //記事を格納
                ContentValues articleInsertValues = new ContentValues();
                articleInsertValues.put("article", article);
                //TODO 日付を登録
                long id = db.insert("article", "null", articleInsertValues);

                //次画面での表示処理
                Intent dbIntent = new Intent(MainActivity.this,ShowDataBase.class);
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
}
