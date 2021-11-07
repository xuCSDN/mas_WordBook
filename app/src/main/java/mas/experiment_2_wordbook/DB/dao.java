package mas.experiment_2_wordbook.DB;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import mas.experiment_2_wordbook.diary.diary;


public class dao extends Application {
    private dbHelper dbhelper;
    private SQLiteDatabase db;

    public dao(Context context)
    {
        dbhelper = new dbHelper(context);// 初始化databaseHelper对象
    }

    /**
     * 插入
     * @param diary
     */
    public void insert(diary diary){
        String str="insert into diary (title,content,date,author,image) values ('"+diary.getTitle()+"','"+
                diary.getContent()+"','"+diary.getDate()+"','"+diary.getAuthor()+"','"+diary.getImage()+"')";
        db.execSQL(str);
    }

    /**
     * 更新
     * @param d
     */
    public void update(diary d) {
        db = dbhelper.getWritableDatabase();
        String sql = "update diary set title=?,content=?,image=? where date=? and author=?";
        Object bindArgs[] = new Object[] { d.getTitle(), d.getContent(),d.getImage(),d.getDate(),d.getAuthor() };
        db.execSQL(sql, bindArgs);
    }

    /**
     * 查找
     * @param
     * @return
     */
    @SuppressLint("Range")
    public ArrayList<diary> find(String str) {
        ArrayList<diary> diarylist = new ArrayList<>();
        db = dbhelper.getWritableDatabase();
        String sql = "select * from diary where author=?";
        String[] selectionArgs = new String[] { str };
        Cursor cursor = db.rawQuery(sql, selectionArgs);
        // 游标从头读到尾
        for (cursor.moveToFirst(); !(cursor.isAfterLast()); cursor.moveToNext()) {
                diary diary = new diary();
                diary.setAuthor(cursor.getString(cursor.getColumnIndex("author")));
                diary.setContent(cursor.getString(cursor.getColumnIndex("content")));
                diary.setTitle(cursor.getString(cursor.getColumnIndex("title")));
                diary.setDate(cursor.getString(cursor.getColumnIndex("date")));
                diary.setImage(cursor.getString(cursor.getColumnIndex("image")));
                diarylist.add(diary);
        }
        return diarylist;
    }

    /**
     * 删除
     * @param d
     */
    public void delete(diary d) {
        db = dbhelper.getWritableDatabase();
        String sql = "delete from diary where author=? and date=?";
        Object bindArgs[] = new Object[] { d.getAuthor(),d.getDate()};
        db.execSQL(sql, bindArgs);
    }

    /**
     * 退出
     */
    public void close(){
        db = dbhelper.getWritableDatabase();
        if(db != null){
            db.close();
        }
    }
}
