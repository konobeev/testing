package com.example.cme.service.session

import com.example.cme.service.UserPreferences
import com.example.cme.service.domain.*
import com.example.cme.service.storage.ActionType
import com.example.cme.service.storage.Storage
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.subjects.PublishSubject
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.math.RoundingMode
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.concurrent.TimeUnit

@Service
class SessionFactory(private val storage: Storage,
                     private val preferences: UserPreferences) {

    fun create(publisher: Flowable<UserRequest>, user: User): Flowable<UserResponse> {
        return publisher.switchMap { req ->
            val session = Session(req, storage, preferences, user)
            session.connect()
            session.observable()
        }
    }

}

class Session(private val request: UserRequest,
              private val storage: Storage,
              private val preferences: UserPreferences,
              private val user: User) {
    private val publisher = PublishSubject.create<UserResponse>()

    fun strike(value: BigDecimal): String {
        return value.setScale(2, RoundingMode.HALF_EVEN).toString()
    }

    fun expiry(value: LocalDate): String {
        return value.format(DateTimeFormatter.BASIC_ISO_DATE)
    }

    fun connect() {
        storage.observable(request.currencyPair)
                .buffer(1, TimeUnit.SECONDS)
                .filter { it.isNotEmpty() }
                .subscribe {
                    val cells = it.map { c ->
                        val (bid, ask) =
                                when (c.actionType) {
                                    ActionType.remove -> "    " to "    "
                                    else -> {
                                        if (request.priceType == PriceType.prem)
                                            strike(c.cell.bidPrice) to strike(c.cell.askPrice)
                                        else
                                            strike(c.cell.bidVol) to strike(c.cell.askVol)
                                    }
                                }

                        Cell(c.cell.optionCode,
                                strike(c.cell.strike),
                                expiry(c.cell.expiry),
                                bid, ask)

                    }
                    val response = UserResponse(
                            request.subscriptionId,
                            request.revision,
                            request.currencyPair,
                            MsgType.inc,
                            emptyList(),
                            emptyList(),
                            cells.sortedBy { c -> c.optionCode }
                    )
                    publisher.onNext(response)
                }
    }


    fun observable(): Flowable<out UserResponse> {
        return publisher.toFlowable(BackpressureStrategy.LATEST)
    }
}
