package com.devpirates.ftl.room.ansyblconnection

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface AnsyblConnectionDAO {
    @Query("SELECT * FROM ansyblconnection")
    fun getAll(): List<AnsyblConnection>

    @Query("SELECT * FROM ansyblconnection WHERE id IN (:connectionIds)")
    fun loadAllByIds(connectionIds: IntArray): List<AnsyblConnection>

    @Insert
    fun insertAll(vararg connections: AnsyblConnection)

    @Delete
    fun delete(connection: AnsyblConnection)
}