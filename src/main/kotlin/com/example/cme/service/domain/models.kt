package com.example.cme.service.domain

data class User(val username: String)
enum class PriceType { vol, prem }
enum class CellType { all, otm }
class UserFilter
class UserRequest(val subscriptionId: Long,
                  val revision: Long,
                  val currencyPair: String,
                  val priceType: PriceType,
                  val cellType: CellType,
                  val filters: UserFilter)

enum class MsgType { full, inc }
class Cell(val optionCode: String,
           val strike: String,
           val expiry: String,
           val bid: String,
           val ask: String)

class UserResponse(val subscriptionId: Long,
                   val revision: Long,
                   val currencyPair: String,
                   val msgType: MsgType,
                   val cols: List<String>,
                   val rows: List<String>,
                   val cells: List<Cell>)
