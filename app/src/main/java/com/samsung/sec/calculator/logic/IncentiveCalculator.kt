package com.samsung.sec.calculator.logic

import com.samsung.sec.calculator.data.CalculationResult
import com.samsung.sec.calculator.data.IncentiveRule
import com.samsung.sec.calculator.data.ManualModuleInputs
import com.samsung.sec.calculator.data.RawSalesRow
import kotlin.math.min

object IncentiveCalculator {
    private const val CAP_GLOBAL_MAX = 150000.0

    fun calculate(
        salesData: List<RawSalesRow>,
        rules: List<IncentiveRule>,
        manualInputs: ManualModuleInputs
    ): CalculationResult {

        // 1. Filter Brand
        val samsungSales = salesData.filter { it.brand.equals("Samsung", ignoreCase = true) }

        // 2. Automated Calculation
        var totalAutomated = 0.0
        samsungSales.forEach { sale ->
            val rule = rules.find { rule ->
                rule.category.equals(sale.segment, ignoreCase = true) &&
                sale.price >= rule.minPrice &&
                sale.price < rule.maxPrice
            }
            val incentivePerUnit = rule?.incentiveAmount ?: 0.0
            totalAutomated += (incentivePerUnit * sale.quantity)
        }

        // 3. Manual Calculation
        // Module D: Care+
        val moduleD = (manualInputs.countCarePlusStandard * 100.0) +
                      (manualInputs.countCarePlusPromax * 200.0)

        // Module E: Accessories (>10% ratio)
        val accRatio = if (manualInputs.totalPhoneValueForAcc > 0)
            manualInputs.totalAccessoryValue / manualInputs.totalPhoneValueForAcc else 0.0
        val moduleE = if (accRatio > 0.10) 500.0 else 0.0

        // Module F: Bundles
        val moduleF = (manualInputs.countHostWatch * 300.0) +
                      (manualInputs.countHostBuds * 150.0)

        val totalManual = moduleD + moduleE + moduleF
        val grossTotal = totalAutomated + totalManual
        val finalTotal = min(grossTotal, CAP_GLOBAL_MAX)

        return CalculationResult(totalAutomated, totalManual, grossTotal, finalTotal, grossTotal > CAP_GLOBAL_MAX)
    }
}
