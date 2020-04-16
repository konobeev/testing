package com.example.cme.service

import com.example.cme.service.domain.CellType
import com.example.cme.service.domain.PriceType
import com.example.cme.service.domain.UserFilter
import com.example.cme.service.domain.UserRequest
import io.reactivex.Flowable
import org.springframework.stereotype.Service
import javax.annotation.PostConstruct

@Service
class Main(private val gateway: Gateway) {
    @PostConstruct
    fun init() {
        val request = Flowable.just(UserRequest(0L,
                0L,
                "EURUSD", PriceType.prem,
                CellType.all,
                UserFilter()))
        Flowable.fromPublisher(gateway.processRequest(request)).subscribe { res ->
            println(res.cells.joinToString { "${it.optionCode} - ${it.bid}/${it.ask}" })
        }
    }

}