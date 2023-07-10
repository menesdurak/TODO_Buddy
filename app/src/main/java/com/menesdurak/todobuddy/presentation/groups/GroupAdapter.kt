package com.menesdurak.todobuddy.presentation.groups

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.menesdurak.todobuddy.data.local.entity.Group
import com.menesdurak.todobuddy.databinding.ItemGroupBinding

class GroupAdapter(
    private val onItemClick : (String) -> Unit
) : RecyclerView.Adapter<GroupAdapter.GroupHolder>() {

    private val itemList = mutableListOf<Group>()

    inner class GroupHolder(private val binding: ItemGroupBinding) :
        RecyclerView.ViewHolder(binding.root) {

            fun bind(group: Group) {
                binding.apply {
                    this.tvGroupName.text = group.name
                }

                binding.root.setOnClickListener {
                    onItemClick.invoke(group.name)
                }
            }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupHolder {
        val bind = ItemGroupBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return GroupHolder(bind)
    }

    override fun getItemCount(): Int = itemList.size

    override fun onBindViewHolder(holder: GroupHolder, position: Int) {
        holder.bind(itemList[position])
    }

    fun updateGroupList(newList: List<Group>) {
        itemList.clear()
        itemList.addAll(newList)
        notifyDataSetChanged()
    }
}