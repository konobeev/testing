package com.example.cme.service.jms

import io.reactivex.Observable
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.math.RoundingMode
import java.time.LocalDate
import java.util.concurrent.TimeUnit

data class JmsQuote(val optionCode: String,
                    val expiry: LocalDate,
                    val strike: BigDecimal,
                    val bidVol: BigDecimal,
                    val askVol: BigDecimal,
                    val bidPrice: BigDecimal,
                    val askPrice: BigDecimal)

data class JmsModel(val currencyPair: String, val items: List<JmsQuote>)

@Service
class JmsConnector {
    fun observable(): Observable<JmsModel> {
        return Observable.interval(10, TimeUnit.SECONDS).map { createSnapshot() }
    }

    fun start() {
    }

    private fun createSnapshot(): JmsModel {
        return JmsModel("EURUSD", createList())
    }

    private fun createList(): List<JmsQuote> {
        return (1..5).map {
            val code = "EU${10+(Math.random() * 10).toInt()}PO"
            JmsQuote(code,
                    LocalDate.now(),
                    BigDecimal.valueOf(1 + Math.random()).setScale(5, RoundingMode.HALF_EVEN),
                    BigDecimal.valueOf(Math.random() * 100).setScale(2, RoundingMode.HALF_EVEN),
                    BigDecimal.valueOf(Math.random() * 100).setScale(2, RoundingMode.HALF_EVEN),
                    BigDecimal.valueOf(Math.random()),
                    BigDecimal.valueOf(Math.random())
            )
        }
    }
}