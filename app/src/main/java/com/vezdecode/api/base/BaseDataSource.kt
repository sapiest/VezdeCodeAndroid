package com.vezdecode.api.base

import com.vezdecode.App
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
        return showPrivateData()
    }

    private fun showPrivateData(): String? {

        // GeeksForGeeks represent the folder name to access privately saved data
        val folder = App.applicationContext().getExternalFilesDir("GeeksForGeeks")

        // gft.txt is the file that is saved privately
        val file = File(folder, "test.json")
        val data = getData(file)
        return data
    }

    private fun getData(myfile: File): String {
        val fileInputStream: FileInputStream? = null
        val buffer = StringBuffer()
        try {
            BufferedReader(InputStreamReader(FileInputStream(myfile), "UTF-8")).use { br ->
                var sub = ""
               // var i = -1
                while (br.readLine().also { sub = it } != null) {
                    buffer.append(sub + "\n")
                }
            }

//            fileInputStream = FileInputStream(myfile)
//            var i = -1
//            val buffer = StringBuffer()
//            while (fileInputStream.read().also { i = it } != -1) {
//                buffer.append(i.toChar())
//            }

        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        } finally {
            if (fileInputStream != null) {
                try {
                    fileInputStream.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
        return buffer.toString()
    }
}