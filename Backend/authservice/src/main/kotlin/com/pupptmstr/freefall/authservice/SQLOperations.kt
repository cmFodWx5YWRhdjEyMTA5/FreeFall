package com.pupptmstr.freefall.authservice

import com.google.gson.GsonBuilder
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter
import java.sql.DriverManager
import java.sql.ResultSet
import java.sql.SQLException


fun auth(login: String, pass: String): String {
    var res = "error"
    Class.forName("org.postgresql.Driver")
    val connection = DriverManager.getConnection(DB_URL, USER, PASS)
    val statement = connection.createStatement()

    try {
        val resSet: ResultSet =
            statement.executeQuery("Select username from authtable where login='$login' AND password='$pass' AND delete_at IS NULL;")
        if (resSet.next())
            res = resSet.getString("username")
    } catch (e: SQLException) {
        res += "\n" + e.toString()
    }
    statement.close()
    connection.close()

    return res
}

fun reg(login: String, pass: String, username: String) {
    Class.forName("org.postgresql.Driver")
    val connection = DriverManager.getConnection(DB_URL, USER, PASS)
    val statement = connection.createStatement()
    val date = java.time.LocalDate.now().toString()

    try {
        statement.execute("INSERT INTO authtable (login, password, username, create_at, update_at) VALUES ('$login', '$pass', '$username', '$date','$date');")
        statement.execute("INSERT INTO playertable (level, clanid) values ('1', '0');")
        statement.execute("INSERT INTO statistics (kill, death) values ('0', '0');")
        statement.execute("INSERT INTO inventorytable (money, healing) values ('100', '0');")
        statement.execute("INSERT INTO onbodyslots (helmetid, swordid, shoesid, armourid) values ('2', '1', '1', '1');")
        statement.execute("INSERT INTO firstslots (helmetid, swordid, shoesid, armourid) values ('1', '1', '1', '1');")
        statement.execute("INSERT INTO secondslots (helmetid, swordid, shoesid, armourid) values ('1', '1', '1', '1');")
    } catch (e: SQLException) {
        println(e)
    }

    statement.close()
    connection.close()
}

fun delete(login: String, pass: String) {
    Class.forName("org.postgresql.Driver")
    val connection = DriverManager.getConnection(DB_URL, USER, PASS)
    val statement = connection.createStatement()
    val date = java.time.LocalDate.now().toString()

    try {
        statement.execute("UPDATE authtable SET delete_at='$date', update_at='$date' WHERE login='$login' AND password='$pass';")
    } catch (e: SQLException) {
        println(e)
    }

    statement.close()
    connection.close()
}

fun restoreAccount(login: String, pass: String) {
    Class.forName("org.postgresql.Driver")
    val connection = DriverManager.getConnection(DB_URL, USER, PASS)
    val statement = connection.createStatement()
    val date = java.time.LocalDate.now().toString()

    try {
        statement.execute("UPDATE authtable SET delete_at=NULL, update_at='$date' WHERE login='$login' AND password='$pass';")
    } catch (e: SQLException) {
        println(e)
    }

    statement.close()
    connection.close()
}

fun changePassword(login: String, pass: String, newPass: String) {
    Class.forName("org.postgresql.Driver")
    val connection = DriverManager.getConnection(DB_URL, USER, PASS)
    val statement = connection.createStatement()
    val date = java.time.LocalDate.now().toString()

    try {
        statement.execute("UPDATE authtable SET password='$newPass', update_at='$date' WHERE login='$login' AND password='$pass';")
    } catch (e: SQLException) {
        println(e)
    }

    statement.close()
    connection.close()
}

fun sendEmail(login: String) {

}

fun makeEmptyModel(): UserModel {
    return UserModel(
        id = 0,
        userName = "",
        level = 0,
        clanName = "",
        kill = 0,
        death = 0,
        clearDamage = 0,
        clearDefence = 0,
        additionalDamage = 0,
        additionalDefence = 0,
        fullDamage = 0,
        fullDefence = 0,
        health = 0,
        onBodyHelmetId = 0,
        onBodyArmourId = 0,
        onBodySwordId = 0,
        onBodyShoeId = 0,
        firstSlotHelmetId = 0,
        firstSlotArmourId = 0,
        firstSlotSwordId = 0,
        firstSlotShoeId = 0,
        secondSlotHelmetId = 0,
        secondSlotArmourId = 0,
        secondSlotSwordId = 0,
        secondSlotShoeId = 0,
        date = "",
        token = ""
    )
}

fun makeUserModel(login: String): UserModel {
    Class.forName("org.postgresql.Driver")
    val connection = DriverManager.getConnection(DB_URL, USER, PASS)
    val statement = connection.createStatement()

    var id = 0
    var level = 0
    var clanName = ""
    var kill = 0
    var death = 0
    var clearDamage = 0
    var clearDefence = 0
    var additionalDamage = 0
    var additionalDefence = 0
    var fullDamage = 0
    val fullDefence: Int
    var health = 0
    var onBodyHelmetId = 0
    var onBodyArmourId = 0
    var onBodySwordId = 0
    var onBodyShoeId = 0
    var firstSlotHelmetId = 0
    var firstSlotArmourId = 0
    var firstSlotSwordId = 0
    var firstSlotShoeId = 0
    var secondSlotHelmetId = 0
    var secondSlotArmourId = 0
    var secondSlotSwordId = 0
    var secondSlotShoeId = 0

    try {
        val resSet: ResultSet =
            statement.executeQuery("Select id from authtable where login='$login';")
        if (resSet.next())
            id = resSet.getInt("id")
    } catch (e: SQLException) {
        return makeEmptyModel()
    }

    try {
        val resSet: ResultSet =
            statement.executeQuery("SELECT * from playertable WHERE id='$id';")
        if (resSet.next()) {
            level = resSet.getInt("level")
            val clanId = resSet.getInt("clanId")
            val resSet1 = statement.executeQuery("SELECT clanname FROM clanstorage WHERE clanid='$clanId';")
            if (resSet1.next())
                clanName = resSet1.getString("clanname")
        }
    } catch (e: SQLException) {
        e.printStackTrace()
        return makeEmptyModel()
    }

    try {
        val resSet = statement.executeQuery("SELECT * FROM statistics WHERE id='$id';")
        if (resSet.next()) {
            kill = resSet.getInt("kill")
            death = resSet.getInt("death")
        }
    } catch (e: SQLException) {
        e.printStackTrace()
        return makeEmptyModel()
    }

    try {
        val resSet = statement.executeQuery("SELECT * FROM levelstorage WHERE id='$level';")
        if (resSet.next()) {
            clearDamage = resSet.getInt("damage")
            clearDefence = resSet.getInt("defence")
            health = resSet.getInt("health")
        }
    } catch (e: SQLException) {
        e.printStackTrace()
        return makeEmptyModel()
    }

    try {
        val resSet = statement.executeQuery("SELECT * FROM onbodyslots WHERE playerid='$id';")
        if (resSet.next()) {
            onBodyHelmetId = resSet.getInt("helmetid")
            onBodyArmourId = resSet.getInt("armourid")
            onBodySwordId = resSet.getInt("swordid")
            onBodyShoeId = resSet.getInt("shoesid")
        }
    } catch (e: SQLException) {
        e.printStackTrace()
        return makeEmptyModel()
    }

    try {
        val resSet = statement.executeQuery("SELECT * FROM firstslots WHERE playerid='$id';")
        if (resSet.next()) {
            firstSlotHelmetId = resSet.getInt("helmetid")
            firstSlotArmourId = resSet.getInt("armourid")
            firstSlotSwordId = resSet.getInt("swordid")
            firstSlotShoeId = resSet.getInt("shoesid")
        }
    } catch (e: SQLException) {
        e.printStackTrace()
        return makeEmptyModel()
    }

    try {
        val resSet = statement.executeQuery("SELECT * FROM secondslots WHERE playerid='$id';")
        if (resSet.next()) {
            secondSlotHelmetId = resSet.getInt("helmetid")
            secondSlotArmourId = resSet.getInt("armourid")
            secondSlotSwordId = resSet.getInt("swordid")
            secondSlotShoeId = resSet.getInt("shoesid")
        }
    } catch (e: SQLException) {
        e.printStackTrace()
        return makeEmptyModel()
    }

    try {
        val resSet = statement.executeQuery("SELECT points FROM inventorystorage WHERE itemid='$onBodySwordId';")
        if (resSet.next()) {
            additionalDamage = resSet.getInt("points")
            fullDamage = clearDamage + additionalDamage
        }
    } catch (e: SQLException) {
        e.printStackTrace()
        return makeEmptyModel()
    }

    try {
        val resSet = statement.executeQuery("SELECT points FROM inventorystorage WHERE itemid='$onBodyHelmetId';")
        if (resSet.next()) {
            additionalDefence += resSet.getInt("points")
        }
    } catch (e: SQLException) {
        e.printStackTrace()
        return makeEmptyModel()
    }

    try {
        val resSet = statement.executeQuery("SELECT points FROM inventorystorage WHERE itemid='$onBodyArmourId';")
        if (resSet.next()) {
            additionalDefence += resSet.getInt("points")
        }
    } catch (e: SQLException) {
        e.printStackTrace()
        return makeEmptyModel()
    }

    try {
        val resSet = statement.executeQuery("SELECT points FROM inventorystorage WHERE itemid='$onBodyShoeId';")

        if (resSet.next()) {
            additionalDefence += resSet.getInt("points")
        }
    } catch (e: SQLException) {
        e.printStackTrace()
        return makeEmptyModel()
    }

    fullDefence = clearDefence + additionalDefence

    val token = makeAToken(
        id = id,
        username = login,
        level = level,
        additionalDamage = additionalDamage,
        additionalDefence = additionalDefence,
        health = health,
        date = java.time.LocalDate.now().toString()
    )

    val res = UserModel(
        id = id,
        userName = login,
        level = level,
        clanName = clanName,
        kill = kill,
        death = death,
        clearDamage = clearDamage,
        clearDefence = clearDefence,
        additionalDamage = additionalDamage,
        additionalDefence = additionalDefence,
        fullDamage = fullDamage,
        fullDefence = fullDefence,
        health = health,
        onBodyHelmetId = onBodyHelmetId,
        onBodyArmourId = onBodyArmourId,
        onBodySwordId = onBodySwordId,
        onBodyShoeId = onBodyShoeId,
        firstSlotHelmetId = firstSlotHelmetId,
        firstSlotArmourId = firstSlotArmourId,
        firstSlotSwordId = firstSlotSwordId,
        firstSlotShoeId = firstSlotShoeId,
        secondSlotHelmetId = secondSlotHelmetId,
        secondSlotArmourId = secondSlotArmourId,
        secondSlotSwordId = secondSlotSwordId,
        secondSlotShoeId = secondSlotShoeId,
        date = java.time.LocalDate.now().toString(),
        token = token
    )

    val file = File("tokens/$token")
    if (file.createNewFile()) {
        val bufferedWriter = BufferedWriter(FileWriter(file))
        val gsonBuilder = GsonBuilder()
        val gson = gsonBuilder.create()
        bufferedWriter.write(gson.toJson(res))
        bufferedWriter.close()
    } else return makeEmptyModel()


    statement.close()
    connection.close()
    return res
}

fun makeAToken(
    id: Int,
    username: String,
    level: Int,
    additionalDamage: Int,
    additionalDefence: Int,
    health: Int,
    date: String
): String {
    try {
        val keyByte = KEY.toByteArray()
        val key = Keys.hmacShaKeyFor(keyByte)
        val jws: String = Jwts.builder()
            .setHeaderParam("alg", "HS256")
            .setIssuer("pupptmstr")
            .claim("id", id)
            .claim("username", username)
            .claim("level", level)
            .claim("additionalDamage", additionalDamage)
            .claim("additionalDefence", additionalDefence)
            .claim("health", health)
            .claim("date", date)
            .signWith(key)
            .compact()
        return jws
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return ""
}