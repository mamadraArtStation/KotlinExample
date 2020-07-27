package ru.skillbranch.kotlinexample

import androidx.annotation.VisibleForTesting

object UserHolder {
    private val map = mutableMapOf<String, User>()

    fun registerUser(
        fullName: String,
        email: String,
        password: String
    ): User {
        if (!map.isEmpty()){
            map.keys.forEach {
                if (it == email.toLowerCase()){
                    throw IllegalArgumentException("A user with this email already exists")
                }
            }
            return User.makeUser(fullName, email = email, password = password)
                .also { user -> map[user.login] = user }
        }else
            return User.makeUser(fullName, email = email, password = password)
                .also { user -> map[user.login] = user }
    }


    fun registerUserByPhone(
        fullName: String,
        rawPhone: String
    ): User {
        if(isValidPhone(rawPhone)){
        val user=User.makeUser(fullName,phone=rawPhone)
        if (!map.isEmpty()){
            map.keys.forEach {
                if (it == user.login){
                    throw IllegalArgumentException("A user with this phone already exists")
                }
            }
            return user
                .also { user -> map[rawPhone] = user }
        }else
            return user
                .also { user -> map[rawPhone] = user }
        } else
            throw IllegalArgumentException("Enter a valid phone number starting with a + and containing 11 digits")
    }

    private fun isValidPhone(rawPhone: String): Boolean {
        val newPhone=rawPhone?.replace("[^+\\d]".toRegex(), "")
        return newPhone.length==12
    }


    fun loginUser(login: String, password: String): String? {
        return map[login.trim()]?.run {
            if (checkPassword(password)) this.userInfo
            else null
        }
    }

    fun requestAccessCode(login: String) : Unit{
        map[login.trim()]!!.updateAccessCode(map[login.trim()]!!)
    }

//    Реализуй метод requestAccessCode(login: String) : Unit, после выполнения данного метода у
//    пользователя с соответствующим логином должен быть сгенерирован новый код авторизации и
//    помещен в свойство accessCode, соответственно должен измениться и хеш пароля пользователя
//    (вызов метода loginUser должен отрабатывать корректно)

    @VisibleForTesting(otherwise = VisibleForTesting.NONE)
    fun clearHolder(){
        map.clear()
    }

    private fun generateAccessCode(): String {
        val possible = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890"
        return StringBuilder().apply {
            repeat(6) {
                (possible.indices).random().also { index ->
                    append(possible[index])
                }
            }
        }.toString()
    }
}