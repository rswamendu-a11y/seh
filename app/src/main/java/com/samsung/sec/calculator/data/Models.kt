package com.samsung.sec.calculator.data

import androidx.room.Entity
import androidx.room.PrimaryKey

// Sales Data from Excel
data class RawSalesRow(
    val id: String,
    val brand: String,
    val model: String,
    val variant: String,
    val quantity: Int,
    val price: Double,
    val segment: String,
    val date: String
)

// Rules for Room DB
@Entity(tableName = "incentive_rules")
data class IncentiveRule(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val category: String,
    val minPrice: Double,
    val maxPrice: Double,
    val incentiveAmount: Double
)

// User Inputs for Manual Modules
data class ManualModuleInputs(
    val countCarePlusStandard: Int = 0,
    val countCarePlusPromax: Int = 0,
    val totalAccessoryValue: Double = 0.0,
    val totalPhoneValueForAcc: Double = 0.0,
    val countHostWatch: Int = 0,
    val countHostBuds: Int = 0
)

// Final Result
data class CalculationResult(
    val automatedIncentive: Double,
    val manualIncentive: Double,
    val grossTotal: Double,
    val cappedTotal: Double,
    val isCapApplied: Boolean
)
