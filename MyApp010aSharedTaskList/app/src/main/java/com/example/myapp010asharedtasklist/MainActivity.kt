package com.example.myapp010asharedtasklist

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapp010asharedtasklist.databinding.ActivityMainBinding
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var db: FirebaseFirestore
    private lateinit var adapter: TaskAdapter

    private val collectionName = "tasks_2026"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            val pad = (16 * resources.displayMetrics.density).toInt()  // 16dp
            v.setPadding(systemBars.left + pad, systemBars.top + pad, systemBars.right + pad, systemBars.bottom + pad)
            insets
        }

        db = Firebase.firestore

        adapter = TaskAdapter(
            tasks = emptyList(),
            onChecked = { task -> toggleCompleted(task) },
            onDelete = { task -> deleteTask(task) }
        )
        binding.recyclerViewTasks.adapter = adapter
        binding.recyclerViewTasks.layoutManager = LinearLayoutManager(this)

        // CREATE
        binding.buttonAdd.setOnClickListener {
            val title = binding.inputTask.text.toString().trim()
            if (title.isNotEmpty()) {
                addTask(title)
                binding.inputTask.text.clear()
            }
        }

        // READ v reálném čase
        listenForTasks()
    }

    private fun addTask(title: String) {
        val task = Task(title = title, completed = false)
        db.collection(collectionName).add(task)
    }

    // UPDATE: najde dokumenty se stejným title a otočí completed
    private fun toggleCompleted(task: Task) {
        db.collection(collectionName)
            .whereEqualTo("title", task.title)
            .get()
            .addOnSuccessListener { docs ->
                for (doc in docs) {
                    db.collection(collectionName)
                        .document(doc.id)
                        .update("completed", !task.completed)
                }
            }
    }

    // DELETE
    private fun deleteTask(task: Task) {
        db.collection(collectionName)
            .whereEqualTo("title", task.title)
            .get()
            .addOnSuccessListener { docs ->
                for (doc in docs) {
                    db.collection(collectionName)
                        .document(doc.id)
                        .delete()
                }
            }
    }

    private fun listenForTasks() {
        db.collection(collectionName)
            .addSnapshotListener { snapshots, _ ->
                val taskList = snapshots?.toObjects(Task::class.java) ?: emptyList()
                adapter.submitList(taskList)
            }
    }
}
