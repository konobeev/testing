package com.example.cme.service.storage

import com.example.cme.service.domain.Item
import com.example.cme.service.domain.Snapshot
import com.example.cme.service.domain.Quote
import io.reactivex.Observable
import org.springframework.stereotype.Service
import java.time.format.DateTimeFormatter
import java.util.concurrent.ConcurrentHashMap

enum class ActionType { add, update, remove }
data class CmeCell(val cell: Item, val actionType: ActionType)

@Service
class Storage {
    private val storageMap = ConcurrentHashMap<String, StorageItem>()
    private val staticMap = ConcurrentHashMap<String, String>()

    fun updateSnapshot(snapshot: Snapshot) {
        val item = storageItem(snapshot.currencyPair)
        item.updateItems(snapshot.items)
        snapshot.items.forEach {
            staticMap[it.optionCode] = snapshot.currencyPair
        }
    }

    fun updateQuote(quote: Quote) {
        staticMap[quote.optionCode]?.let {
            val item = storageItem(it)
            item.updateQuote(quote)
        }
    }

    fun getCurrencyPairList(): List<String> {
        return storageMap.keys.toList()
    }

    fun getExpiryDateList(currencyPair: String): List<String> {
        val item = storageItem(currencyPair)
        return item.getExpiryDateList().map { it.format(DateTimeFormatter.BASIC_ISO_DATE) }
    }

    private fun storageItem(currencyPair: String): StorageItem {
        return storageMap.getOrPut(currencyPair) { StorageItem(currencyPair) }
    }

    fun observable(currencyPair: String): Observable<CmeCell> {
        val storageItem = storageItem(currencyPair)
        return storageItem.observable()
    }
}

