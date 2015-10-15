package xcat.daiyonkaigi.guchiruna.activity;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import net.reduls.sanmoku.Morpheme;

import java.util.ArrayList;
import java.util.List;

import xcat.daiyonkaigi.guchiruna.R;
import xcat.daiyonkaigi.guchiruna.db.ArticleDBOpenHelper;
import xcat.daiyonkaigi.guchiruna.tokenize.StringToToken;

/**
 * データベースの実体を表示させるためのクラスです。
 * activity_timeline.xmlと紐付いたActivityです。
 *
 */
public class TimelineActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        setContentView(layout);

        ArticleDBOpenHelper helper = new ArticleDBOpenHelper(this);
        SQLiteDatabase db = helper.getReadableDatabase();

        // queryメソッドの実行例
        Cursor c = db.query("article", new String[] { "article, date" }, null,
        null, null, null, null);

        boolean mov = c.moveToFirst();

        TextView titleTimelineView  = new TextView(this);
        titleTimelineView.setText("\n -----タイムラインです-----");
        layout.addView(titleTimelineView);

        List<Morpheme> tokenLists = new ArrayList();
        while (mov){
            TextView textView = new TextView(this);
            //TODO 取得処理

            //文字列をテキストビューに設定
            String article = c.getString(0);
            String date = c.getString(1);
            textView.setText(date + "\n" + article);
            mov = c.moveToNext();
            layout.addView(textView);
            //TODO 処理順おかしい（画面遷移修正対応時にやる）
            tokenLists = StringToToken.tokenize(article);
        }
        c.close();
        db.close();

        //TODO デバッグ用（消す）
        TextView titleKrmjTextView  = new TextView(this);
        titleKrmjTextView.setText("\n\n\n----今回の入力でDBへ格納された文字列のみ表示します-----\n\n");
        layout.addView(titleKrmjTextView);

        //格納対象の
        for (Morpheme m : tokenLists) {
            if (judgeInsertToken(m.feature)){

                //TODO 以下はデバッグ用なので後で消す
                TextView tokenView = new TextView(this);
                String hinsi = m.feature.split(",")[0];
                tokenView.setText(m.surface + "が" + hinsi+ "なので、DBに格納されました。");
                layout.addView(tokenView);
            }
        }
    }

    /**
     * 単語１つの解析情報を受け取り、DBに格納対象であるかを判定するメソッドです。
     * 仕様上、品詞及び動詞を格納対象としています。
     * @return true(格納対象である) / false(格納対象)
     */
    private boolean judgeInsertToken(String token){
        String[] tokens = token.split(",");
        //品詞情報はトークン情報中、インデックス1に設定されています
        String hinsi = tokens[0];
        //クソ判定
        return ("動詞".equals(hinsi) || "名詞".equals(hinsi));
    }
}