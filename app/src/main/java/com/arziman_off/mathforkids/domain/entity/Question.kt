package com.arziman_off.mathforkids.domain.entity

data class Question(
    val sum: Int,
    val visibleNumber: Int,
    val options: List<Int>
) {
    val rightAns: Int
        get() = this.sum - this.visibleNumber
}