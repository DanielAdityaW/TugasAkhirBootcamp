package com.bcafbootcamp.tugasakhirbootcampandroid.Model

data class PredictionRequest(
    val creditScore: Int,
    val age: Int,
    val tenure: Int,
    val balance: Double,
    val vehicleType: String,
    val installmentAmount: Double,
    val paymentHistory: Int
)
