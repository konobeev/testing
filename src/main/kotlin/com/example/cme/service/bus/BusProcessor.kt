package com.example.cme.service.bus

import com.example.cme.service.domain.Quote
import com.example.cme.service.storage.Storage
import io.reactivex.disposables.Disposable
import org.springframework.stereotype.Service
import javax.annotation.PostConstruct

@Service
class BusProcessor(private val busConnector: BusConnector,
                   private val storage: Storage) {
    private lateinit var subscription: Disposable

    @PostConstruct
    fun init() {
        subscription = busConnector.observable().subscribe { handle(it) }
        busConnector.start()
    }

    private fun handle(model: BusModel) {
        val (a, b, c) = model
        storage.updateQuote(Quote(a, b, c))
    }
}