package com.example.foodback.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.foodback.data.remote.response.ExerciseData
import com.example.foodback.databinding.ItemFoodBinding

class ExerciseAdapter(private val listExercise: List<ExerciseData>, private val onClick: (ExerciseData) -> Unit) : RecyclerView.Adapter<ExerciseAdapter.ViewHolder>() {

    class ViewHolder(var binding: ItemFoodBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        ItemFoodBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.tvAddFoodName.text = listExercise[holder.adapterPosition].name
        holder.binding.tvAddFoodDesc.text = listExercise[holder.adapterPosition].total_calories.toString()

        holder.itemView.setOnClickListener {
            onClick(listExercise[holder.adapterPosition])
        }
    }

    override fun getItemCount(): Int = listExercise.size

}