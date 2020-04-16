package com.example.cme.service.bus

import io.reactivex.Observable
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.util.concurrent.TimeUnit

data class BusModel(val optionCode: String, val bid: BigDecimal, val ask: BigDecimal)

@Service
class BusConnector {
    fun observable(): Observable<BusModel> {
        return Observable.interval(100, TimeUnit.MILLISECONDS)
                .map { createKomodoModel() }
    }

    fun start() {
    }

    private fun createKomodoModel(): BusModel {
        val code = "EU${10+(Math.random() * 10).toInt()}PO"
        return BusModel(code,
                BigDecimal.valueOf(Math.random()),
                BigDecimal.valueOf(Math.random()))
    }

}