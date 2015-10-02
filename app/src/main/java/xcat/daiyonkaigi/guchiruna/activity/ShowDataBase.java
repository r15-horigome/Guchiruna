package xcat.daiyonkaigi.guchiruna.activity;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.atilika.kuromoji.Token;

import java.util.ArrayList;
import java.util.List;

import xcat.daiyonkaigi.guchiruna.R;
import xcat.daiyonkaigi.guchiruna.db.MyOpenHelper;
import xcat.daiyonkaigi.guchiruna.tokenize.StringToToken;

/**
 * データベースの実体を表示させるためのクラスです。
 * show_database.xmlと紐付いたActivityです。
 *
 */
public class ShowDataBase extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_database);

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        setContentView(layout);

        MyOpenHelper helper = new MyOpenHelper(this);
        SQLiteDatabase db = helper.getReadableDatabase();

        // queryメソッドの実行例
        Cursor c = db.query("article", new String[] { "article" }, null,
        null, null, null, null);

        boolean mov = c.moveToFirst();

        List<List<Token>> tokenLists = new ArrayList();
        while (mov){
            TextView textView = new TextView(this);
            //TODO 取得処理

            //文字列をテキストビューに設定
            String article = c.getString(0);
            textView.setText(article);
            mov = c.moveToNext();
            layout.addView(textView);

            //TODO Exception回避
            tokenLists.add(StringToToken.tokenize(article));
        }
        c.close();
        db.close();

        //タイトル
        TextView titleKrmjTextView  = new TextView(this);
        titleKrmjTextView.setText("以下は構文分析の結果");
        layout.addView(titleKrmjTextView);

        //トークン化した内容を表示します
        TextView textView = new TextView(this);
        for(List<Token> list : tokenLists) {
            for (Token token : list) {
                //TODO デバッグ用なので直す
                String reading = token.getSurfaceForm() + token.getAllFeatures();
                TextView tokenView = new TextView(this);
                tokenView.setText(reading);
                layout.addView(tokenView);
            }
        }
    }
}