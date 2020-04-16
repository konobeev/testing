package com.example.cme.service.jms

import com.example.cme.service.domain.Item
import com.example.cme.service.domain.Snapshot
import com.example.cme.service.storage.Storage
import io.reactivex.disposables.Disposable
import org.springframework.stereotype.Service
import javax.annotation.PostConstruct
import javax.annotation.PreDestroy

@Service
class JmsProcessor(private val jmsConnector: JmsConnector,
                   private val storage: Storage) {
    private lateinit var subscription:Disposable

    @PostConstruct
    fun start(){
        subscription = jmsConnector.observable().subscribe{handle(it)}
        jmsConnector.start()
    }

    @PreDestroy
    fun close(){
        subscription.dispose()
    }

    private fun handle(model: JmsModel) {
        val items = model.items.map{
            val (a,b,c,d,e,f,g) = it
            Item(a,b,c,d,e,f,g)
        }
        val snapshot = Snapshot(model.currencyPair, items)
        storage.updateSnapshot(snapshot)
    }

}