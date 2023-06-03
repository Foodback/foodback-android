package com.example.foodback.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.foodback.data.FakeAddData
import com.example.foodback.databinding.ItemAddBinding

class AddAdapter(private val listAdd: List<FakeAddData>, private val onClick: (FakeAddData) -> Unit) : RecyclerView.Adapter<AddAdapter.ViewHolder>() {

    class ViewHolder(var binding: ItemAddBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        ItemAddBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.tvAddName.text = listAdd[holder.adapterPosition].name
        holder.binding.tvAddDesc.text = listAdd[holder.adapterPosition].desc

        holder.itemView.setOnClickListener {
            onClick(listAdd[holder.adapterPosition])
        }
    }

    override fun getItemCount(): Int = listAdd.size

}
