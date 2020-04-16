package com.example.cme.service

import com.example.cme.service.domain.User
import com.example.cme.service.domain.UserRequest
import com.example.cme.service.domain.UserResponse
import com.example.cme.service.session.SessionFactory
import com.example.cme.service.storage.Storage
import io.reactivex.Flowable
import org.reactivestreams.Publisher
import org.springframework.stereotype.Service

@Service
class Gateway(private val storage: Storage,
              private val sessionFactory: SessionFactory,
              private val userPreferences: UserPreferences) {

    fun getCurrencyPairList(): List<String> {
        return storage.getCurrencyPairList()
    }

    fun getExpiryDateList(currencyPair: String): List<String> {
        return storage.getExpiryDateList(currencyPair)
    }

    fun getTooltip(id: String): String {
        return "[$id] not implemented"
    }

    fun amendUserSettings(settings: Settings) {
        val user = User("")
        userPreferences.set(settings, user)
    }

    fun processRequest(publisher: Publisher<UserRequest>): Publisher<UserResponse> {
        val user = User("")
        return sessionFactory.create(Flowable.fromPublisher(publisher), user)
    }
}