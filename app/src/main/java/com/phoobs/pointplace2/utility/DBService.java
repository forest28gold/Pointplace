package com.phoobs.pointplace2.utility;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.phoobs.pointplace2.models.Global;

public class DBService extends SQLiteOpenHelper
{
	private final static int DATABASE_VERSION = 3;
	private final static String DATABASE_NAME = Global.LOCAL_DB_NAME;

	public void onCreate(SQLiteDatabase db)
	{
		String sql_favorite = "CREATE TABLE [" + Global.LOCAL_TABLE_FAVORITE + "] ("
				+ "[id] AUTOINC,"
				+ "[" + Global.LOCAL_FIELD_FAVORITE_CATEGORY + "] TEXT NOT NULL ON CONFLICT FAIL,"
				+ "[" + Global.LOCAL_FIELD_FAVORITE_CID + "] TEXT ,"
				+ "[" + Global.LOCAL_FIELD_FAVORITE_IMAGE + "] TEXT ,"
				+ "[" + Global.LOCAL_FIELD_FAVORITE_TITLE + "] TEXT ,"
				+ "[" + Global.LOCAL_FIELD_FAVORITE_SUBCATEGORY + "] TEXT ,"
				+ "[" + Global.LOCAL_FIELD_FAVORITE_DISTANCE + "] TEXT )";
		
		db.execSQL(sql_favorite);
	}

	public DBService(Context context)
	{
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
	{
		String sql_favorite = "drop table if exists [" + Global.LOCAL_TABLE_FAVORITE + "]";
		db.execSQL(sql_favorite);
		
		onCreate(db);
	}

	public void execSQL(String sql, Object[] args)
	{
		SQLiteDatabase db = this.getWritableDatabase();
		db.execSQL(sql, args);
	}

	public Cursor query(String sql, String[] args)
	{
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(sql, args);
		return cursor;
	}
}
