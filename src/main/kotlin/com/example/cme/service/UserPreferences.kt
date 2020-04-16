package com.example.cme.service

import com.example.cme.service.domain.User
import org.springframework.stereotype.Service
import java.math.BigDecimal

data class Settings(val notional: BigDecimal, val distance: BigDecimal)

@Service
class UserPreferences {
    private val defaultSettings = Settings(BigDecimal.valueOf(20), BigDecimal.valueOf(50))
    private val userMap = HashMap<User, Settings>()

    fun set(settings: Settings, user: User) {
        userMap[user] = settings
    }

    fun get(user: User): Settings {
        return userMap.getOrPut(user) {defaultSettings}
    }
}
