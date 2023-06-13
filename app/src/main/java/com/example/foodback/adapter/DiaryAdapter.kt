package com.example.foodback.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.foodback.data.AddButton
import com.example.foodback.data.DiaryType
import com.example.foodback.data.remote.response.Exercise
import com.example.foodback.data.remote.response.Meal
import com.example.foodback.databinding.ItemButtonBinding
import com.example.foodback.databinding.ItemListBinding
import com.example.foodback.databinding.ItemTypeBinding

class DiaryAdapter(private val data: List<Any>, private val onClickData: (Any) -> Unit, private val  onDeleteData: (Any) -> Unit, private val onAddFood: (String) -> Unit, private val onAddExercises: () -> Unit) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    inner class TypeViewHolder(var binding: ItemTypeBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(type: DiaryType){
            binding.itemTypeCalorie.text = "${type.calorie} Cal"
            binding.imgType.setImageResource(type.image)
            binding.itemType.text = type.name
        }
    }

    inner class FoodViewHolder(var binding: ItemListBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(data: Meal){
            binding.tvNameList.text = data.name
            binding.tvDescList.text = "${data.amount} Gram"
            binding.tvCalorieList.text = "${data.calories} Cal"
//            binding.btnRemove.setOnClickListener { onDeleteData(data) }
            this.itemView.setOnClickListener { onClickData(data) }
        }
    }

    inner class ExerciseViewHolder(var binding: ItemListBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(data: Exercise){
            binding.tvNameList.text = data.name
            binding.tvDescList.text = "${data.duration} Minute"
            binding.tvCalorieList.text = "${data.calories} Cal"
//            binding.btnRemove.setOnClickListener { onDeleteData(data) }
            this.itemView.setOnClickListener { onClickData(data) }
        }
    }

    inner class ButtonViewHolder(var binding: ItemButtonBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(button: AddButton){
            binding.tvItemAdd.text = button.hint
            if(button.isFood) binding.layoutAddDiary.setOnClickListener { onAddFood(button.label) }
            else binding.layoutAddDiary.setOnClickListener { onAddExercises() }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (data[position]) {
            is DiaryType -> ITEM_TYPE
            is Meal -> ITEM_FOOD
            is Exercise -> ITEM_EXERCISE
            is AddButton -> ITEM_BUTTON
            else -> throw IllegalArgumentException("Undefined view type")
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ITEM_TYPE -> TypeViewHolder(ItemTypeBinding.inflate(LayoutInflater.from(parent.context), parent, false))
            ITEM_FOOD -> FoodViewHolder(ItemListBinding.inflate(LayoutInflater.from(parent.context), parent, false))
            ITEM_EXERCISE -> ExerciseViewHolder(ItemListBinding.inflate(LayoutInflater.from(parent.context), parent, false))
            ITEM_BUTTON -> ButtonViewHolder(ItemButtonBinding.inflate(LayoutInflater.from(parent.context), parent, false))
            else -> throw IllegalArgumentException("Undefined view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        when(holder.itemViewType){
            ITEM_TYPE -> {
                val typeHolder = holder as TypeViewHolder
                typeHolder.bind(data[position] as DiaryType)
            }
            ITEM_FOOD -> {
                val dataHolder = holder as FoodViewHolder
                dataHolder.bind(data[position] as Meal)
            }
            ITEM_EXERCISE -> {
                val dataHolder = holder as ExerciseViewHolder
                dataHolder.bind(data[position] as Exercise)
            }
            ITEM_BUTTON -> {
                val buttonHolder = holder as ButtonViewHolder
                buttonHolder.bind(data[position] as AddButton)
            }
            else -> throw IllegalArgumentException("Undefined view type")
        }
    }

    override fun getItemCount(): Int = data.size

    companion object {
        private const val ITEM_TYPE = 0
        private const val ITEM_FOOD = 1
        private const val ITEM_EXERCISE = 2
        private const val ITEM_BUTTON = 3
    }
}