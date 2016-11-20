package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.database_access;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.List;
import java.util.LinkedList;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.InvalidAccountException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.database.myDataBaseHandler;

public class DB_AccountDAO implements AccountDAO {

    myDataBaseHandler dbHandler = null;

    public DB_AccountDAO(myDataBaseHandler dbHandler){
        this.dbHandler = dbHandler;
    }

    @Override
    public List<String> getAccountNumbersList() {

        String query = "Select" + myDataBaseHandler.COLUMN_accountNo + "from " + myDataBaseHandler.TABLE_account ;
        SQLiteDatabase db = dbHandler.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        List<String> accountsList =  new LinkedList<>();

        if (cursor.moveToFirst()) {
            do{
                accountsList.add(cursor.getString(0));
            }
            while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return accountsList;
    }

    @Override
    public List<Account> getAccountsList() {

        String query = "Select * from " + myDataBaseHandler.TABLE_account ;
        SQLiteDatabase db = dbHandler.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        List<Account> accountsList =  new LinkedList<>();

        if (cursor.moveToFirst()) {
            do{
                Account account = null;
                account= new Account(cursor.getString(0), cursor.getString(1) , cursor.getString(2), cursor.getDouble(3));
                accountsList.add(account);
            }
            while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return accountsList;
    }

    @Override
    public Account getAccount(String accountNo) throws InvalidAccountException {

        String query = "Select * from " + myDataBaseHandler.TABLE_account + " where " + myDataBaseHandler.COLUMN_accountNo + " = " + "'" + accountNo + "'"  ;
        SQLiteDatabase db = dbHandler.getWritableDatabase();

        Cursor cursor = db.rawQuery(query, null);
        Account account = null;

        if (cursor.moveToFirst()) {
            account= new Account(cursor.getString(0), cursor.getString(1) , cursor.getString(2), cursor.getDouble(3));
        }
        cursor.close();
        db.close();
        return account;
    }

    @Override
    public void addAccount(Account account) {
        ContentValues values = new ContentValues();
        values.put(myDataBaseHandler.COLUMN_accountNo, account.getAccountNo());
        values.put(myDataBaseHandler.COLUMN_bank_name, account.getBankName());
        values.put(myDataBaseHandler.COLUMN_account_holder, account.getAccountHolderName());
        values.put(myDataBaseHandler.COLUMN_balance, account.getBalance());

        SQLiteDatabase db = dbHandler.getWritableDatabase();
        db.insert(myDataBaseHandler.TABLE_account, null, values);
        db.close();
    }

    @Override
    public void removeAccount(String accountNo) throws InvalidAccountException {

        String query = "Select * from " + myDataBaseHandler.TABLE_account + " where " + myDataBaseHandler.COLUMN_accountNo + " = " + "'" + accountNo + "'";

        SQLiteDatabase db = dbHandler.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            String accountNumber = (cursor.getString(0));
            db.delete(myDataBaseHandler.table_account,  myDataBaseHandler.column_accountNo + " = ?",
                    new String[] { accountNumber });
            cursor.close();
        }
        db.close();
    }

    @Override
    public void updateBalance(String accountNo, ExpenseType expenseType, double amount) throws InvalidAccountException {
        Account account = getAccount(accountNo);

        ContentValues contentValues = new ContentValues();
        contentValues.put(myDataBaseHandler.COLUMN_accountNo, account.getAccountNo());
        contentValues.put(myDataBaseHandler.COLUMN_bank_name, account.getBankName());
        contentValues.put(myDataBaseHandler.COLUMN_account_holder , account.getBankName());

        if (expenseType == ExpenseType.EXPENSE){
            contentValues.put(myDataBaseHandler.COLUMN_balance , account.getBalance() - amount);

        }else if (expenseType == ExpenseType.INCOME){
            contentValues.put(myDataBaseHandler.COLUMN_balance , account.getBalance()+ amount);
        }
        SQLiteDatabase db = dbHandler.getWritableDatabase();
        db.update(myDataBaseHandler.TABLE_account , contentValues , myDataBaseHandler.COLUMN_accountNo + " = " + "'" + accountNo + "'" , null);
    }
}
