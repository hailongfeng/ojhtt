package cn.ouju.htt.v2.db;

import com.raizlabs.android.dbflow.sql.language.SQLOperator;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.structure.BaseModel;

import java.util.List;

public class DbUtils {
    private static final String TAG="DbUtils";

    public static  <T extends BaseModel>  List<T> getModelList(Class<T> clazz, SQLOperator... where){
        return SQLite.select().from(clazz)
                .where(where)
                .queryList();
    }

    public static  <T extends BaseModel>  T getModelSingle(Class<T> clazz, SQLOperator... where){
        return SQLite.select().from(clazz)
                .where(where)
                .querySingle();
    }

    public static <T extends BaseModel>  void deleteModel(Class<T> clazz, SQLOperator... where){
        SQLite.delete(clazz).where(where).execute();
    }
    public static <T extends BaseModel>  void saveModel(T model){
        model.save();
    }
    public static <T extends BaseModel> void saveModelList(List<T> datas){
        for (T data:datas)
            data.save();
    }
}
