package me.hgj.jetpackmvvm.demo.app.room.repository

import android.content.Context
import me.hgj.jetpackmvvm.demo.app.room.AppDatabase
import me.hgj.jetpackmvvm.demo.app.room.Image
import java.io.File
import kotlin.coroutines.suspendCoroutine

/**
 * Created by WangMaoBo.
 * Date: 2021/12/8
 */
class TestRepository(private val context: Context) {
    suspend fun updateFile(rootPath: String) {
        println("===path is $rootPath")
        searchAllFile(File(rootPath))
    }

    private fun searchAllFile(file: File) {
        if (!file.exists()) {
            return
        }
        val fileList = file.listFiles()
        fileList?.forEach {
            if (it.isDirectory) {
                searchAllFile(it)
            } else {
                AppDatabase.getDatabase(context).imageDao()
                    .insertAll(Image(it.absolutePath, it.lastModified(), it.name, it.name))
            }
        }
    }

    suspend fun getAllFile() = AppDatabase.getDatabase(context).imageDao().getAll()
}