package mas.experiment_2_wordbook.DB;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class dbHelper extends SQLiteOpenHelper {
    public static final String CREATE_DIARY = "create table diary ("+
            "id integer primary key autoincrement,title text,content text"+
            ",date Date,author text,image text)";
    private Context context;
    public dbHelper(@Nullable Context context) {
        super(context, "diary.db", null, 1);
        this.context=context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_DIARY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

}
