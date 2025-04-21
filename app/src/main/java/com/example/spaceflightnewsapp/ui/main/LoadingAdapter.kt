package com.example.spaceflightnewsapp.ui.main

import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.recyclerview.widget.RecyclerView

class LoadingAdapter : RecyclerView.Adapter<LoadingAdapter.LoadingViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LoadingViewHolder {
        val progressBar = ProgressBar(parent.context)
        progressBar.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        progressBar.isIndeterminate = true
        progressBar.setPadding(0, 32, 0, 32)
        return LoadingViewHolder(progressBar)
    }

    override fun onBindViewHolder(holder: LoadingViewHolder, position: Int) {}

    override fun getItemCount(): Int = 1

    class LoadingViewHolder(view: View) : RecyclerView.ViewHolder(view)
}