package com.vezdecode.ui.splash

import android.Manifest
import android.media.MediaScannerConnection
import android.media.MediaScannerConnection.OnScanCompletedListener
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.vezdecode.R
import com.vezdecode.utils.Constants
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.io.*
import kotlin.coroutines.CoroutineContext


class SplashFragment : Fragment(R.layout.splash_fragment), CoroutineScope {
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + Job()

    private var job: Job? = null

    val singlePermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            when {
                granted -> {
                    doWriting()
                }
                !shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE) -> {
                    Toast.makeText(
                        requireContext(),
                        "Недостаточно прав для записи, переустановите приложение!",
                        Toast.LENGTH_LONG
                    ).show()
                }
                else -> {

                }
            }
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {
            Toast.makeText(
                requireContext(),
                "Требуется записать JSON на SD карту",
                Toast.LENGTH_SHORT
            ).show()
        } else {
            singlePermission.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }
    }

    private fun doWriting() {
        job = launch {
            writeOnSDCard(Constants.INCIDENTS_FILENAME)
            findNavController().navigate(R.id.action_splashFragment_to_mainFragment)
        }
    }


    private fun writeOnSDCard(filename: String) {
        val inputStream = resources.openRawResource(R.raw.incidents)
        val bufferedReader = BufferedReader(InputStreamReader(inputStream))
        var eachline: String = bufferedReader.readLine()

        var data: String = ""
        try {
            while (eachline != null) {
                data += "$eachline\n"
                eachline = bufferedReader.readLine()
            }
        }catch (e: Exception){

        }finally {
            savePrivately(data, filename)
        }
    }

    fun savePrivately(data: String, filename: String) {

        // Creating folder with name GeekForGeeks
        val folder = requireActivity().getExternalFilesDir("GeeksForGeeks")

        // Creating file with name gfg.txt
        val file = File(folder, "test.json")
        writeTextData(file, data)
    }


    private fun writeTextData(file: File, data: String) {
        var fileOutputStream: FileOutputStream? = null
        try {
            fileOutputStream = FileOutputStream(file)
            fileOutputStream.write(data.toByteArray())
            Toast.makeText(requireContext(), "Done" + file.absolutePath, Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
    }
}