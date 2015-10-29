package xcat.daiyonkaigi.guchiruna.negapozi;

import android.app.Activity;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Calendar;

import xcat.daiyonkaigi.guchiruna.db.GuchiCommonDBOpenHelper;


/**
 * ネガポジ判定、結果をDBに登録するためのクラスです。【奥村追加】
 */

public class AsyncHttpRequest extends AsyncTask<String, String, String> {
    private Activity mainActivity;

    public AsyncHttpRequest(Activity activity) {

        // 呼び出し元のアクティビティ
        this.mainActivity = activity;
    }

    @Override
    protected String doInBackground(String... str) {

        String kakikomi = new String();
        byte[] strByte = new byte[]{(byte) 0xF8, (byte) 0x9F};//UTF-8にするための変数
        StringBuilder builder = new StringBuilder();        //UTF-8にするための変数
        String result = new String();

        // TextView tvv = (TextView) mainActivity.findViewById(R.id.textView3);

        //書き込みをUTF-8に変換
        try {
            strByte = str[0].getBytes("UTF-8");
            for (byte b : strByte) {
                builder.append("%");
                builder.append(Integer.toHexString((b & 0xF0) >> 4));
                builder.append(Integer.toHexString(b & 0xF));
            }
            kakikomi = new String(builder);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        //Twitter感情分析所Ver
        //String Url = "http://mueki.net/twana/api.php?q=%e3%82%84%e3%81%a3%e3%81%9f%e3%81%ad%ef%bc%81";

        //ネガポジAPI用のURL作成
        String Url = "http://ap.mextractr.net/ma9/negaposi_analyzer?out=json&apikey=E2EA83F2B82564D9A4713321FC189C5C0C5091BD&text=" + kakikomi;

        //ネガポジAPI発動
        HttpClient httpClient = new DefaultHttpClient();
        HttpGet request = new HttpGet(Url);
        HttpResponse httpResponse = null;
        try {
            httpResponse = httpClient.execute(request);
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //APIの結果を取り出す
        int status = httpResponse.getStatusLine().getStatusCode();
        if (status == 200) {
            // HTTPレスポンスから値を取り出す
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            try {
                httpResponse.getEntity().writeTo(outputStream);
            } catch (IOException e) {
                e.printStackTrace();
            }
            result = outputStream.toString();//結果
        }

        return result;
    }


    // このメソッドは非同期処理の終わった後に呼び出されます
    @Override
    protected void onPostExecute(String result) {

        //★消してOK　4行
        // TextView tv = (TextView) mainActivity.findViewById(R.id.textView);
        //TextView tvv2 = (TextView) mainActivity.findViewById(R.id.textView4);
        // TextView tvv5 = (TextView) mainActivity.findViewById(R.id.textView5);
        // tv.setText(result);


        /*　結果からネガポジ度数部分のみ切り出し
            例）{"negaposi":2,"analyzed_text":"%E4%B・・・
                ２　の部分のみ切り出し。そのため、『～：』より後ろ、『,"anal～』より前の部分を切り出し
         */
        int index = result.indexOf(",\"ana");
        String a = result.substring(0, index);
        index = a.indexOf(":");
        String b = a.substring(index + 1);

        //切り出した度数を浮動小数点に変換
        float kekka = Float.parseFloat(b);
        // tvv5.setText("" + kekka);   //★消してOK

        // 日付の取得　年月日
        Calendar cal = Calendar.getInstance();
        String str = "" + cal.get(Calendar.YEAR);
        int yea = Integer.parseInt(str);
        str = "" + (cal.get(Calendar.MONTH) + 1);
        int mon = Integer.parseInt(str);
        str = "" + cal.get(Calendar.DATE);
        int dat = Integer.parseInt(str);


        //DBの初期化処理
        GuchiCommonDBOpenHelper helper = new GuchiCommonDBOpenHelper(mainActivity);
        SQLiteDatabase db = helper.getWritableDatabase();

        //ネガポジ度数が０より上：ポジティブ　　０以下：ネガティブ
        if (kekka > 0) {
            //tvv5.setText(kekka + "ポジティブ" + dat);    //★消してOK

            //DBに保存 ポジティブ度数、年月日
            ContentValues values = new ContentValues();
            values.put("Pozi", kekka);
            values.put("Year", yea);
            values.put("Month", mon);
            values.put("Day", dat);
            long ret;
            try {
                ret = db.insert("negapozi", null, values);
            } finally {
                db.close();
            }

        } else {
            //tvv5.setText(kekka + "ネガティブ" + dat);//★消してOK

            kekka = kekka * -1; //負数を正数へ
            //DBに保存 ネガティブ度数、年月日
            ContentValues values = new ContentValues();
            values.put("Nega", kekka);
            values.put("Year", yea);
            values.put("Month", mon);
            values.put("Day", dat);
            long ret;
            try {
                ret = db.insert("negapozi", null, values);
            } finally {
                db.close();
            }
        }
    }
}