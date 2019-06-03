package com.pupptmstr.freefall.matchmakingservice

import io.ktor.application.ApplicationCall
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.features.ContentNegotiation
import io.ktor.gson.gson
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.post
import io.ktor.routing.routing
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import redis.clients.jedis.Jedis
import java.io.File
import java.util.*
import kotlin.Comparator
import kotlin.math.abs

class Server(key: String) {

    private val successMessage = 0
    private val wrongKeyError = 1
    private val cancelMMError = 2
    private val unknownError = 3
    private val map = mutableMapOf<Int, ApplicationCall>()
    private val channelToRequest = Channel<Pair<Int, Int>>()
    private val channelToRespond = Channel<Triple<Int, Int, String>>()
    private val treeSet = TreeSet<Pair<Int, Int>>(Comparator<Pair<Int, Int>> {
            pair: Pair<Int, Int>, pair1: Pair<Int, Int> ->
        if (pair.first == pair1.first) pair.second - pair1.second else pair.first - pair1.first
    })


    fun start() {
        embeddedServer(Netty, 9992) {
            install(ContentNegotiation) {
                gson {
                    setPrettyPrinting()
                    serializeNulls()
                }
            }

            CoroutineScope(Dispatchers.IO).launch {
                for (msg in channelToRespond){
                    val redisWriting = async { writeToRedis(msg.first, msg.second, msg.third) }
                    val responding = async { respond(msg.first, msg.second, msg.third) }
                    awaitAll(redisWriting, responding)
                }
            }

            CoroutineScope(Dispatchers.Default).launch {
                for (msg in channelToRequest) {
                    //msg.second = lvl. Мапа сортируется по ключам, поэтому и берем лвл
                    treeSet.add(msg.second to msg.first)
                    makeNewPairs(channelToRespond)
                }
            }

            routing {
                post("/mm") {
                    val user = call.receive<UserModel>()
                    val file = File("tokens/${user.token}")
                    if (file.exists()) {
                        map[user.id] = call
                        channelToRequest.send(user.id to user.level)
                    } else {
                        call.respond(errorMessage(wrongKeyError))
                    }
                }

                post("/cancelMm") {
                    val user = call.receive<UserModel>()
                    val file = File("tokens/${user.token}")
                    if (file.exists()) {
                        map.remove(user.id)
                        call.respond(cancelMMError)
                    } else {
                        call.respond(errorMessage(wrongKeyError))
                    }
                }
            }
        }.start(wait = true)
    }

    private suspend fun respond(id1: Int, id2: Int, key: String) {
        if(map.contains(id1) && map.contains(id2)) {
            map[id1]!!.respond(successMessage(key))
            map[id2]!!.respond(successMessage(key))
            map.remove(id1)
            map.remove(id2)
        }
    }

    private fun writeToRedis(id1: Int, id2: Int, key: String) {
        val jedis = Jedis()
        jedis.set(key, id1.toString())
        jedis.set(key, id2.toString())
    }

    /**
     * Коды ошибок:
     * 1 - неверный ключ
     * 2 - отмена поиска
     * 3 - неизвестная ошибка
     * */
    private fun errorMessage(type: Int): RespondModel {
        return when (type) {
            wrongKeyError -> RespondModel(wrongKeyError, "Неверный ключ пользователя")
            cancelMMError -> RespondModel(cancelMMError, "Поиск был отменен")
            else -> RespondModel(unknownError, "Неизвестная ошибка. Упс, кажется что-то пошло не так")
        }
    }

    /**
     * Код успешного сообщения(Противник найден): 0
     * */
    private fun successMessage(battleKey: String): RespondModel {
        return RespondModel(successMessage, battleKey)
    }

    private suspend fun makeNewPairs(channelToWrite: Channel<Triple<Int, Int, String>>) {
        if (treeSet.size > 1) {
            val list = treeSet.toMutableList()
            for (i in 0 until treeSet.size) {
                val playerNow = list[i]
                val playerNext = list[i + 1]
                if (abs(playerNow.first - playerNext.first) < 2) {
                    val key = generateABattleKey(playerNow.first, playerNext.first, playerNow.second, playerNext.second)
                    treeSet.remove(playerNow)
                    treeSet.remove(playerNext)
                    list.remove(playerNow)
                    list.remove(playerNext)
                    channelToWrite.send(Triple(playerNow.second, playerNext.second, key))
                }
            }
        }
    }

    private fun generateABattleKey(lvl1: Int, lvl2: Int, id1: Int, id2: Int): String {
        return "battle-${lvl1.toByte()}-${lvl2.toByte()} ${id1.toByte()} ${id2.toByte()}"
    }

}