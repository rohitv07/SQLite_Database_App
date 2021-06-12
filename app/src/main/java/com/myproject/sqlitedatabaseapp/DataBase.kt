package com.myproject.sqlitedatabaseapp

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DataBase(context: Context) : SQLiteOpenHelper(context,"ACDB",null,1) {

    override fun onCreate(db: SQLiteDatabase?) {

        db?.execSQL("CREATE TABLE ACTABLE(_id integer primary key autoincrement, NAME TEXT, MEANING TEXT)")
        db?.execSQL("INSERT INTO ACTABLE(NAME,MEANING)VALUES('WWW', 'World Wide Web')")
        db?.execSQL("INSERT INTO ACTABLE(NAME,MEANING)VALUES('GDG', 'Google Developer Group')")
        db?.execSQL("INSERT INTO ACTABLE(NAME,MEANING)VALUES('AVD', 'Android Virtual Device')")

    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        TODO("Not yet implemented")
    }
}