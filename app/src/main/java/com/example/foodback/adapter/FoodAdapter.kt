package com.example.foodback.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.foodback.data.remote.response.MenuItems
import com.example.foodback.databinding.ItemFoodBinding

class FoodAdapter(private val listFood: List<MenuItems>, private val onClick: (MenuItems) -> Unit) : RecyclerView.Adapter<FoodAdapter.ViewHolder>() {

    class ViewHolder(var binding: ItemFoodBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        ItemFoodBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.tvAddFoodName.text = listFood[holder.adapterPosition].title
        holder.binding.tvAddFoodDesc.text = listFood[holder.adapterPosition].nutrition.calories.toString()

        holder.itemView.setOnClickListener {
            onClick(listFood[holder.adapterPosition])
        }
    }

    override fun getItemCount(): Int = listFood.size

}
