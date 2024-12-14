package com.bcafbootcamp.tugasakhirbootcampandroid.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bcafbootcamp.tugasakhirbootcampandroid.Model.Customer
import com.bcafbootcamp.tugasakhirbootcampandroid.R

class CustomerAdapter(private val customers: List<Customer>) :
    RecyclerView.Adapter<CustomerAdapter.CustomerViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomerViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_customer, parent, false)
        return CustomerViewHolder(view)
    }

    override fun onBindViewHolder(holder: CustomerViewHolder, position: Int) {
        val customer = customers[position]
        holder.bind(customer)
    }

    override fun getItemCount(): Int = customers.size

    inner class CustomerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvCustomerId: TextView = itemView.findViewById(R.id.tvCustomerId)
        private val tvCreditScore: TextView = itemView.findViewById(R.id.tvCreditScore)
        private val tvAge: TextView = itemView.findViewById(R.id.tvAge)
        private val tvTenure: TextView = itemView.findViewById(R.id.tvTenure)
        private val tvBalance: TextView = itemView.findViewById(R.id.tvBalance)
        private val tvVehicleType: TextView = itemView.findViewById(R.id.tvVehicleType)
        private val tvInstallmentAmount: TextView = itemView.findViewById(R.id.tvInstallmentAmount)
        private val tvPaymentHistory: TextView = itemView.findViewById(R.id.tvPaymentHistory)
        private val tvRiskScore: TextView = itemView.findViewById(R.id.tvRiskScore)
        private val tvRiskCluster: TextView = itemView.findViewById(R.id.tvRiskCluster)

        fun bind(customer: Customer) {
            tvCustomerId.text = customer.customerId
            tvCreditScore.text = customer.creditScore
            tvAge.text = customer.age
            tvTenure.text = customer.tenure
            tvBalance.text = customer.balance
            tvVehicleType.text = customer.vehicleType
            tvInstallmentAmount.text = customer.installmentAmount
            tvPaymentHistory.text = customer.paymentHistory
            tvRiskScore.text = customer.riskScore
            tvRiskCluster.text = customer.riskCluster
        }
    }
}
