package com.vezdecode.api.base

import com.vezdecode.App
import com.vezdecode.R
import retrofit2.Response
import java.io.*

abstract class BaseDataSource {
    protected suspend fun <T> getResult(call: suspend () -> Response<T>): Resource<T> {
        return try {
            val response = call()
            if (response.isSuccessful) {
                val body = response.body()!!
                Resource.Success(body)
            } else {
                Resource.Error(Exception("${response.code()}: ${response.message()}"))
            }
        } catch (e: Exception) {
            Resource.Error(e)
        }
    }

    protected suspend fun loadFromCDCard(filename: String): String? {
        return showPrivateData(filename)
    }

    suspend fun showPrivateData(filename: String): String {

        // GeeksForGeeks represent the folder name to access privately saved data
        val folder = App.applicationContext().getExternalFilesDir(
            App.applicationContext().getString(
                R.string.app_name
            )
        )

        // gft.txt is the file that is saved privately
        val file = File(folder, filename)
        val data = getData(file)
        return data
    }

    private suspend fun getData(myfile: File): String {
        val fileInputStream: FileInputStream? = null
        val buffer = StringBuffer()
        try {
            BufferedReader(InputStreamReader(FileInputStream(myfile), "UTF-8")).use { br ->
                var sub = ""
                // var i = -1
                while (br.readLine()?.also { sub = it } != null) {
                    buffer.append(sub + "\n")
                }
            }

//            fileInputStream = FileInputStream(myfile)
//            var i = -1
//            val buffer = StringBuffer()
//            while (fileInputStream.read().also { i = it } != -1) {
//                buffer.append(i.toChar())
//            }

        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            fileInputStream?.close()
        }
        return buffer.toString()
    }
}