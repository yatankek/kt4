package com.example.kt4

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.io.File

class ListFragment : Fragment() {

    private lateinit var adapter: DateAdapter
    private lateinit var recyclerView: RecyclerView
    private var dates: MutableList<String> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.recyclerView)

        // Инициализация списка данных
        dates = readDatesFromFile().toMutableList()

        // Инициализация адаптера
        adapter = DateAdapter(dates.reversed()) // Показать последние даты сверху
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter
    }

    // Метод для обновления данных в списке
    fun updateData() {
        dates.clear()
        dates.addAll(readDatesFromFile())
        dates.reverse() // Чтобы новые записи отображались сверху
        adapter.notifyDataSetChanged()
    }

    // Метод для чтения дат из файла
    private fun readDatesFromFile(): List<String> {
        val file = File(requireContext().filesDir, "photos/date.txt")
        return if (file.exists()) {
            file.readLines()
        } else {
            emptyList()
        }
    }
}