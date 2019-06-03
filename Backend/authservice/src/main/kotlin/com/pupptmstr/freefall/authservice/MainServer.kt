package com.pupptmstr.freefall.authservice

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
import java.io.File

/**
 * @author pupptmstr
 * */

val settings = File("settings.txt").readLines()
const val DB_URL = "jdbc:postgresql://127.0.0.1:5432/"
val USER = settings[0].split(" : ")[1]
val PASS = settings[1].split(" : ")[1]
val KEY = settings[2].split(" : ")[1]

fun main() {


    embeddedServer(Netty, 9991) {
        install(ContentNegotiation) {
            gson {
                setPrettyPrinting()
                serializeNulls()
            }
        }

        routing {
            post("/auth") {
                val user = call.receive<AuthModel>()
                if (checkFormat(user.password) && checkFormat(user.login) && checkFormat(user.additionalInfo)) {
                    val username = auth(user.login, user.password)
                    if (username.split(" ")[0] == "error")
                    //На клиенте делать проверку, если все поля пустые(если id = 0), значит авторизация не удалась
                    // (Sorry, wrong username or password)
                        call.respond(makeEmptyModel())
                    else
                        call.respond(makeUserModel(user.login))
                }
            }

            post("/reg") {
                val user = call.receive<AuthModel>()
                if (checkFormat(user.password) && checkFormat(user.login) && checkFormat(user.additionalInfo)) {
                    reg(user.login, user.password, user.additionalInfo)
                    call.respond(makeUserModel(user.login))
                }
            }

            post("/delete") {
                val user = call.receive<AuthModel>()
                if (checkFormat(user.password) && checkFormat(user.login) && checkFormat(user.additionalInfo)) {
                    delete(user.login, user.password)
                    call.respond(makeUserModel(user.login))
                }
            }

            post("/newpass") {
                val user = call.receive<AuthModel>()
                if (checkFormat(user.password) && checkFormat(user.login) && checkFormat(user.additionalInfo)) {
                    changePassword(user.login, user.password, user.additionalInfo)
                    call.respond(makeUserModel(user.login))
                }
            }

            post("/restoreacc") {
                val user = call.receive<AuthModel>()
                if (checkFormat(user.password) && checkFormat(user.login) && checkFormat(user.additionalInfo)) {
                    restoreAccount(user.login, user.password)
                    call.respond(makeUserModel(user.login))
                }
            }

            post("/forgotpass") {
                val user = call.receive<AuthModel>()
                if (checkFormat(user.password) && checkFormat(user.login) && checkFormat(user.additionalInfo)) {
                    sendEmail(user.login)
                }
            }

        }
    }.start(wait = true)
}

fun checkFormat(expression: String): Boolean {
    return Regex("[A-Za-z0-9_\\-]+").matches(expression)
}