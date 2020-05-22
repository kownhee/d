
package com.example.project01;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Color;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

class DBHepler extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "graphData.db";
    private static final int DATABASE_VERSION = 2;

    public DBHepler(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE contacts (_id INTEGER PRIMARY KEY"+" AUTOINCREMENT, name TEXT,data TEXT);");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS contacts");

    }
}

public class graphActivity extends AppCompatActivity {
    private static final String TAG = "ppp";
    String Receive;
    int sw=0;

    ArrayList<Integer> countArray = new ArrayList<>();


    SharedPreferences sp = null;
    SharedPreferences.Editor editor = null;

    private LineChart lineChart;

    DBHepler hepler;
    SQLiteDatabase db;
    String name = "graph";
    Cursor cursor;


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Intent intent = getIntent();

        Receive = intent.getStringExtra("Count");
      //  Log.d(TAG, Receive + "값받나");

        setContentView(R.layout.graph);
        hepler = new DBHepler(this);

        try{
            db= hepler.getWritableDatabase();
        } catch (SQLiteException ex){
            db = hepler.getReadableDatabase();
        }
        if(Receive!=null) {
            db.execSQL("INSERT INTO contacts VALUES(null,'"+name+"','"+Receive+"');");
        }

        cursor = db.rawQuery("SELECT name, data FROM contacts WHERE name='"+name+"';",null);

        if (cursor.moveToFirst()) {
            do {
                String num = cursor.getString(1);
               countArray.add(Integer.parseInt(num));
            } while (cursor.moveToNext());
        }

        Chart(countArray);
    }

    public void Chart(ArrayList<Integer> data){
        lineChart = (LineChart)findViewById(R.id.chart);
        List<Entry> entries = new ArrayList<>();

        for(int i=0; i<data.size();i++ ){
            entries.add(new Entry(i,data.get(i)));
        }

        LineDataSet lineDataSet = new LineDataSet(entries, "속성명1");
        lineDataSet.setLineWidth(2);
        lineDataSet.setCircleRadius(6);
        lineDataSet.setCircleColor(Color.parseColor("#FFA1B4DC"));
        lineDataSet.setCircleColorHole(Color.BLUE);
        lineDataSet.setColor(Color.parseColor("#FFA1B4DC"));
        lineDataSet.setDrawCircleHole(true);
        lineDataSet.setDrawCircles(true);
        lineDataSet.setDrawHorizontalHighlightIndicator(false);
        lineDataSet.setDrawHighlightIndicators(false);
        lineDataSet.setDrawValues(false);

        LineData lineData = new LineData(lineDataSet);
        lineChart.setData(lineData);

        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextColor(Color.BLACK);
        xAxis.enableGridDashedLine(8, 24, 0);

        YAxis yLAxis = lineChart.getAxisLeft();
        yLAxis.setTextColor(Color.BLACK);

        YAxis yRAxis = lineChart.getAxisRight();
        yRAxis.setDrawLabels(false);
        yRAxis.setDrawAxisLine(false);
        yRAxis.setDrawGridLines(false);

        Description description = new Description();
        description.setText("");

        lineChart.setDoubleTapToZoomEnabled(false);
        lineChart.setDrawGridBackground(false);
        lineChart.setDescription(description);
        lineChart.animateY(2000, Easing.EasingOption.EaseInCubic);
        lineChart.invalidate();

    }


    protected void onPause() {
        super.onPause();
    }

}