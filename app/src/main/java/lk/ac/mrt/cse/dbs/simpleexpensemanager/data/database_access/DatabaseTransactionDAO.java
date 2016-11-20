package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.database_access;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.database.Cursor;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.LinkedList;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.database.myDataBaseHandler;

public class DatabaseTransactionDAO implements TransactionDAO {
    myDataBaseHandler dbHandler = null;

    public DatabaseTransactionDAO(DataBaseHandler dbHandler) {
        this.dbHandler = dbHandler;
    }

    @Override
    public void logTransaction(Date date, String accountNo, ExpenseType expenseType, double amount) {
        Transaction transaction = new Transaction(date, accountNo, expenseType, amount);

        ContentValues values = new ContentValues();
        values.put(DataBaseHandler.COLUMN_accountNo, accountNo);

        SimpleDateFormat simple = new SimpleDateFormat("dd-MM-yyyy");
        String formatted_date = simple.format(date);
        values.put(myDataBaseHandler.COLUMN_date, formatted_date);
        values.put(myDataBaseHandler.COLUMN_expense_type , expenseType.toString());
        values.put(myDataBaseHandler.COLUMN_amount, amount);

        SQLiteDatabase db = dbHandler.getWritableDatabase();
        db.insert(myDataBaseHandler.TABLE_transaction, null, values);
        db.close();
    }

    @Override
    public List<Transaction> getAllTransactionLogs() {

        String query = "Select * from " + myDataBaseHandler.TABLE_transaction ;
        SQLiteDatabase db = dbHandler.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        List<Transaction> transactionsList =  new LinkedList<>();

        if (cursor.moveToFirst()) {
            do{
                Transaction transaction = null;
                Date date = null;
                String dateString = cursor.getString(0);
                SimpleDateFormat simple = new SimpleDateFormat("dd-MM-yyyy");
                try {
                    date = simple.parse(dateString);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                transaction= new Transaction(date, cursor.getString(1) , ExpenseType.valueOf(cursor.getString(2)), cursor.getDouble(3));
                transactionsList.add(transaction);
            }while (cursor.moveToNext());

        }
        cursor.close();
        db.close();
        return transactionsList;
    }

    @Override
    public List<Transaction> getPaginatedTransactionLogs(int limit) {

        String query = "Select * from " + myDataBaseHandler.TABLE_transaction + " order by " + myDataBaseHandler.COLUMN_date + " limit " + limit ;
        SQLiteDatabase db = dbHandler.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        List<Transaction> transactionsList =  new LinkedList<>();

        if (cursor.moveToFirst()) {
            do{
                Transaction transaction = null;
                Date date = null;
                String dateString = cursor.getString(1);
                SimpleDateFormat simple = new SimpleDateFormat("dd-MM-yyyy");
                try {
                    date = simple.parse(dateString);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                transaction= new Transaction(date, cursor.getString(0) , ExpenseType.valueOf(cursor.getString(2)), cursor.getDouble(3));
                transactionsList.add(transaction);
            }
            while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return transactionsList;
    }
}
