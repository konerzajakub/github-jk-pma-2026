package com.example.myapp010asharedtasklist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myapp010asharedtasklist.databinding.ItemTaskBinding

// Adapter zobrazuje seznam úkolů v RecyclerView
class TaskAdapter(
    private var tasks: List<Task>,
    private val onChecked: (Task) -> Unit,  // kliknutí na checkbox
    private val onDelete: (Task) -> Unit     // kliknutí na koš
) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    inner class TaskViewHolder(val binding: ItemTaskBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val binding = ItemTaskBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return TaskViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = tasks[position]

        holder.binding.textTitle.text = task.title

        // nejdřív odpojit starý listener, aby se nespustil při nastavení isChecked
        holder.binding.checkCompleted.setOnCheckedChangeListener(null)
        holder.binding.checkCompleted.isChecked = task.completed
        holder.binding.checkCompleted.setOnCheckedChangeListener { _, _ ->
            onChecked(task)
        }

        holder.binding.imageDelete.setOnClickListener {
            onDelete(task)
        }
    }

    override fun getItemCount() = tasks.size

    fun submitList(newList: List<Task>) {
        tasks = newList
        notifyDataSetChanged()
    }
}
