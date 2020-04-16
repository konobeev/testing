package com.example.cme.service.domain

import java.math.BigDecimal
import java.time.LocalDate


data class Quote(val optionCode: String, val bid: BigDecimal, val ask: BigDecimal)
data class Snapshot(val currencyPair: String, val items: List<Item>)
data class Item(val optionCode: String,
                val expiry: LocalDate,
                val strike: BigDecimal,
                val bidVol: BigDecimal,
                val askVol: BigDecimal,
                val bidPrice: BigDecimal,
                val askPrice: BigDecimal)