package com.example.foodback.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.foodback.data.FakeAddButton
import com.example.foodback.data.FakeData
import com.example.foodback.databinding.ItemButtonBinding
import com.example.foodback.databinding.ItemListBinding
import com.example.foodback.databinding.ItemTypeBinding

class DiaryAdapter(private val data: List<Any>, private val onClickData: (FakeData) -> Unit, private val  onDeleteData: (FakeData) -> Unit, private val onAddFood: () -> Unit, private val onAddExercises: () -> Unit) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    inner class TypeViewHolder(var binding: ItemTypeBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(type: String){
            binding.itemType.text = type
        }
    }

    inner class DataViewHolder(var binding: ItemListBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(data: FakeData){
            binding.tvName.text = data.name
            binding.tvDecs.text = data.desc
            binding.btnRemove.setOnClickListener { onDeleteData(data) }
            this.itemView.setOnClickListener { onClickData(data) }
        }
    }

    inner class ButtonViewHolder(var binding: ItemButtonBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(button: FakeAddButton){
            binding.btnAdd.text = button.name
            if(button.isFood) binding.btnAdd.setOnClickListener { onAddFood() }
            else binding.btnAdd.setOnClickListener { onAddExercises() }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (data[position]) {
            is String -> ITEM_TYPE
            is FakeData -> ITEM_DATA
            is FakeAddButton -> ITEM_BUTTON
            else -> throw IllegalArgumentException("Undefined view type")
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ITEM_TYPE -> TypeViewHolder(ItemTypeBinding.inflate(LayoutInflater.from(parent.context), parent, false))
            ITEM_DATA -> DataViewHolder(ItemListBinding.inflate(LayoutInflater.from(parent.context), parent, false))
            ITEM_BUTTON -> ButtonViewHolder(ItemButtonBinding.inflate(LayoutInflater.from(parent.context), parent, false))
            else -> throw IllegalArgumentException("Undefined view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        when(holder.itemViewType){
            ITEM_TYPE -> {
                val typeHolder = holder as TypeViewHolder
                typeHolder.bind(data[position] as String)
            }
            ITEM_DATA -> {
                val dataHolder = holder as DataViewHolder
                dataHolder.bind(data[position] as FakeData)
            }
            ITEM_BUTTON -> {
                val buttonHolder = holder as ButtonViewHolder
                buttonHolder.bind(data[position] as FakeAddButton)
            }
            else -> throw IllegalArgumentException("Undefined view type")
        }
    }

    override fun getItemCount(): Int = data.size

    companion object {
        private const val ITEM_TYPE = 0
        private const val ITEM_DATA = 1
        private const val ITEM_BUTTON = 2
    }

}