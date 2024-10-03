package com.example.kt4

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*

class CameraFragment : Fragment() {

    private var imageCapture: ImageCapture? = null
    private lateinit var photosDir: File
    private lateinit var dateFile: File

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_camera, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val viewFinder = view.findViewById<PreviewView>(R.id.viewFinder)
        val captureButton = view.findViewById<Button>(R.id.cameraCaptureButton)

        // Инициализация папки и файла
        setupStorage()

        startCamera(viewFinder)

        // Установка действия на кнопку фотографирования
        captureButton.setOnClickListener {
            saveDateAndTime()
        }
    }

    // Метод для инициализации папки и файла
    private fun setupStorage() {
        photosDir = File(requireContext().filesDir, "photos")

        // Проверяем, существует ли папка, если нет - создаем
        if (!photosDir.exists()) {
            photosDir.mkdir()
        }

        // Создаем файл date.txt внутри папки photos
        dateFile = File(photosDir, "date.txt")

        // Проверяем, существует ли файл, если нет - создаем
        if (!dateFile.exists()) {
            dateFile.createNewFile()
        }
    }

    private fun startCamera(viewFinder: PreviewView) {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())
        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()
            val preview = androidx.camera.core.Preview.Builder().build().also {
                it.setSurfaceProvider(viewFinder.surfaceProvider)
            }

            imageCapture = ImageCapture.Builder().build()

            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageCapture)
            } catch (exc: Exception) {
                Log.e("CameraFragment", "Use case binding failed", exc)
            }
        }, ContextCompat.getMainExecutor(requireContext()))
    }

    // Метод для сохранения даты и времени
    private fun saveDateAndTime() {
        val currentDate = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US)
            .format(System.currentTimeMillis())

        // Записываем дату и время в файл date.txt
        val fos = FileOutputStream(dateFile, true)
        fos.write("$currentDate\n".toByteArray())
        fos.close()

        Toast.makeText(requireContext(), "Date and time saved: $currentDate", Toast.LENGTH_SHORT).show()

        // Уведомление фрагмента списка о необходимости обновления данных
        notifyListFragmentToUpdate()
    }

    // Метод для уведомления фрагмента списка о необходимости обновления
    private fun notifyListFragmentToUpdate() {
        val listFragment = parentFragmentManager.findFragmentById(R.id.fragment_container) as? ListFragment
        listFragment?.updateData()
    }
}