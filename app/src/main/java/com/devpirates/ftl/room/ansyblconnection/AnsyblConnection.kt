package com.devpirates.ftl.room.ansyblconnection

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class AnsyblConnection(
    @PrimaryKey val id: String,
    @ColumnInfo(name = "summary") val summary: String?,
    @ColumnInfo(name = "url") val url: String,
    @ColumnInfo(name = "last_update") val lastUpdate: String,
    @ColumnInfo(name = "json") val json: String
)
