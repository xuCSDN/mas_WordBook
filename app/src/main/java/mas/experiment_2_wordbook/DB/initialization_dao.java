package mas.experiment_2_wordbook.DB;

import android.app.Application;

public class initialization_dao extends Application {
    private mas.experiment_2_wordbook.DB.dao dao;

    /**
     * 创建时调用
     * */
    @Override
    public void onCreate() {
        super.onCreate();
        dao = new dao(this);
    }

    /**
     * 后台进程终止，前台程序需要内存时调用此方法，用于释放内存
     * 用于关闭数据库连接
     * */
    @Override
    public void onLowMemory() {
        super.onLowMemory();
        dao.close();
    }

    public mas.experiment_2_wordbook.DB.dao getDao() {
        return dao;
    }
}
