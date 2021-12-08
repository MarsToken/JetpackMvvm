package me.hgj.jetpackmvvm.demo.app.room

import androidx.room.*

/**
 * Created by WangMaoBo.
 * Date: 2021/12/8
 */
@Dao
interface ImageDao {
    @Query("SELECT * FROM image")
    fun getAll(): List<Image>

    @Query("SELECT * FROM image WHERE ex_name IN (:exNames)")
    fun loadAllImageByExNames(exNames: Array<String>): List<Image>

    @Query("SELECT * FROM image WHERE display_name LIKE :displayName AND update_time>:updateTime")
    fun findByName(displayName: String, updateTime: Long): Image

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg image: Image)

    @Delete
    fun delete(image: Image)
}