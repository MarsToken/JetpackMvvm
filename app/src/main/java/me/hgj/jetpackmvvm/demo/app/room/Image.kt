package me.hgj.jetpackmvvm.demo.app.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Created by WangMaoBo.
 * Date: 2021/12/8
 */
@Entity(tableName = "image")
data class Image(
    @PrimaryKey
    val path: String,
    @ColumnInfo(name = "update_time")
    val updateTime: Long,
    @ColumnInfo(name = "ex_name")
    val exName: String,
    @ColumnInfo(name = "display_name")
    val displayName: String
)