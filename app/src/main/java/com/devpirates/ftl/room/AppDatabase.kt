package com.devpirates.ftl.room

import androidx.room.Database
import androidx.room.RoomDatabase
//import androidx.room.migration.Migration
//import androidx.sqlite.db.SupportSQLiteDatabase
import com.devpirates.ftl.room.ansyblconnection.AnsyblConnection
import com.devpirates.ftl.room.ansyblconnection.AnsyblConnectionDAO

@Database(entities = [AnsyblConnection::class], version = 2)
abstract class AppDatabase : RoomDatabase() {
    abstract fun ansyblConnectionDao(): AnsyblConnectionDAO
}

//val MIGRATION_1_2 = object : Migration(1, 2) {
//    override fun migrate(db: SupportSQLiteDatabase) {
//        db.execSQL("CREATE TABLE ansyblconnection_new (id TEXT, username TEXT, last_update INTEGER, PRIMARY KEY(id))");
//        // Copy the data
//        db.execSQL("INSERT INTO ansyblconnection_new (userid, username, last_update) SELECT userid, username, last_update FROM users");// Remove the old table
//        db.execSQL("DROP TABLE ansyblconnection");// Change the table name to the correct one
//        db.execSQL("ALTER TABLE ansyblconnection_new RENAME TO ansyblconnection");
//    }
//}