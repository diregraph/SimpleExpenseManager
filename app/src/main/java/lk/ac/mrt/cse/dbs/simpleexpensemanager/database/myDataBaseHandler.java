package lk.ac.mrt.cse.dbs.simpleexpensemanager.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class myDataBaseHandler extends SQLiteOpenHelper {

    private static final int DB_version = 1;
    private static final String DB_name = "140120F.db";

    public static final String TABLE_account = "`account`";
    public static final String COLUMN_accountNo = "`accountNo`";
    public static final String COLUMN_bank_name = "`bankName`";
    public static final String COLUMN_account_holder = "`accountHolderName`";
    public static final String COLUMN_balance = "`balance`";

    public static final String TABLE_transaction = "`transaction`";
    public static final String COLUMN_date = "`date`";
    public static final String COLUMN_expense_type = "`expenseType`";
    public static final String COLUMN_amount = "`amount`";

    String create_table_account = "Create table " + TABLE_account + "(" + COLUMN_accountNo + " Varchar(6) Primary Key," +
            COLUMN_bank_name + " Varchar(40)," + COLUMN_account_holder + " Varchar(50)," + COLUMN_balance + " Double "+ ")";

    String create_table_transaction = "Create table " + TABLE_transaction + "(" + COLUMN_accountNo + " Varchar(6)," +
            COLUMN_date + " Varchar(10)," + COLUMN_expense_type + " Varchar(10), " + COLUMN_amount +" Double ," +
            "Foreign Key(" + COLUMN_accountNo + ") References "+ TABLE_account +"("+ COLUMN_accountNo +")"+")";

    private static myDataBaseHandler instance = null;

    public static myDataBaseHandler getInstance(Context context)
    {
        if(instance == null){
            instance = new myDataBaseHandler(context);
        }
        return instance;
    }

    private myDataBaseHandler(Context context) {
        super(context, DB_name, null, DB_version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(create_table_account);
        db.execSQL(create_table_transaction);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if " + table_account + "exists" );
        db.execSQL("drop table if " + table_transaction + "exists");
        onCreate(db);
    }
}