package com.bcafbootcamp.tugasakhirbootcampandroid.Model

data class CustomerResponse(
    val content : List<Customer>,
    val number: Int,
    val totalPages: Int
)
