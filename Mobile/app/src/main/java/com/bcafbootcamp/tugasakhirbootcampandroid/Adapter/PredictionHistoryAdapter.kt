package com.bcafbootcamp.tugasakhirbootcampandroid.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bcafbootcamp.tugasakhirbootcampandroid.Model.PredictionHistory
import com.bcafbootcamp.tugasakhirbootcampandroid.R

class PredictionHistoryAdapter(private val predictionHistoryList: List<PredictionHistory>) :
    RecyclerView.Adapter<PredictionHistoryAdapter.PredictionHistoryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PredictionHistoryViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_prediction_history, parent, false)
        return PredictionHistoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: PredictionHistoryViewHolder, position: Int) {
        val predictionHistory = predictionHistoryList[position]
        holder.bind(predictionHistory)
    }

    override fun getItemCount(): Int = predictionHistoryList.size

    inner class PredictionHistoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val startTimeText: TextView = itemView.findViewById(R.id.tvStartTime)
        private val endTimeText: TextView = itemView.findViewById(R.id.tvEndTime)
        private val statusText: TextView = itemView.findViewById(R.id.tvStatus)

        fun bind(predictionHistory: PredictionHistory) {
            startTimeText.text = predictionHistory.startTime
            endTimeText.text = predictionHistory.endTime ?: "In Progress"
            statusText.text = predictionHistory.status
        }
    }
}
