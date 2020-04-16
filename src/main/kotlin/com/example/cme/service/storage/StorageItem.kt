package com.example.cme.service.storage

import com.example.cme.service.domain.Item
import com.example.cme.service.domain.Quote
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import java.time.LocalDate
import java.util.concurrent.ConcurrentHashMap

class StorageItem(private val currencyPair: String) {
    private var quotes = emptyList<Item>()
    private val itemMap = ConcurrentHashMap<String, Item>()
    private val publisher = PublishSubject.create<CmeCell>()

    fun updateItems(items: List<Item>) {
        val remove = quotes.minus(items)
        val add = items.minus(quotes)
        val update = quotes.intersect(items)
        quotes = items
        add.forEach { publisher.onNext(CmeCell(it, ActionType.add)) }
        update.forEach { publisher.onNext(CmeCell(it, ActionType.update)) }
        remove.forEach { publisher.onNext(CmeCell(it, ActionType.remove)) }

        remove.forEach { itemMap.remove(it.optionCode) }
        items.forEach { itemMap[it.optionCode] = it }
    }

    fun updateQuote(quote: Quote) {
        itemMap[quote.optionCode]?.let {
            val cme = it.copy(bidPrice = quote.bid, askPrice = quote.ask)
            publisher.onNext(CmeCell(cme, ActionType.update))
        }
    }

    fun getExpiryDateList(): List<LocalDate> {
        return quotes.groupBy { it.expiry }.keys.toList()
    }

    fun observable(): Observable<CmeCell> {
        return publisher
    }
}