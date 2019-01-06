package com.richie.utils.greendao;

import android.content.Context;

import com.richie.utils.greendao.generated.DaoMaster;
import com.richie.utils.greendao.generated.UserDao;

import org.greenrobot.greendao.database.Database;

/**
 * @author Richie on 2018.12.02
 */
public class DbOpenHelper extends DaoMaster.OpenHelper {

    public DbOpenHelper(Context context, String name) {
        super(context, name);
    }

    @Override
    public void onUpgrade(Database db, int oldVersion, int newVersion) {
        super.onUpgrade(db, oldVersion, newVersion);
        switch (newVersion) {
            case 1:
                db.execSQL("ALTER TABLE " + UserDao.TABLENAME + " ADD COLUMN " + UserDao.Properties.Name.columnName + " TEXT DEFAULT 'DEFAULT_VAL'");
                break;
            // ...
            default:
        }
    }
}
