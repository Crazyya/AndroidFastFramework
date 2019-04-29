package com.yoriz.yorizutil.sql

import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.util.concurrent.atomic.AtomicInteger

/**
 * Created by yoriz
 * on 2019-04-29 09:43.
 */
abstract class BaseDao {
    protected var db: SQLiteDatabase? = null
    private var helper: SQLiteOpenHelper? = null
    private val mOpenCounter = AtomicInteger()

    /**
     * 必须实现Helper方法，如果不实现就执行内部方法就报错
     */
    protected fun setHelper(helper: SQLiteOpenHelper) {
        this.helper = helper
    }


    fun deleteTableData(nowSQLVersion: Int): Boolean {
        if (!openDB(1)) return false
        try {
            db!!.beginTransaction()
            helper!!.onUpgrade(db!!, nowSQLVersion, nowSQLVersion)
        } finally {
            closeDB()
        }
        return true
    }

    /**
     * 开启db
     */
    protected fun openDB(type: Int): Boolean {
        if (helper == null) throw Exception("Don't use setHelper() is not available")
        if (mOpenCounter.incrementAndGet() == 1 || db == null) {
            db = if (type == 0) {
                helper!!.readableDatabase
            } else {
                helper!!.writableDatabase
            }
        }
        return db != null
    }

    /**
     * 关闭db
     */
    protected fun closeDB(): Boolean {
        if (mOpenCounter.decrementAndGet() == 0) {
            try {
                db?.setTransactionSuccessful()
            } catch (e: Exception) {
            }
            try {
                db?.endTransaction()
            } catch (e: Exception) {
            }
            try {
                db?.close()
            } catch (e: Exception) {
            }
            db = null
        }
        return db == null
    }
}