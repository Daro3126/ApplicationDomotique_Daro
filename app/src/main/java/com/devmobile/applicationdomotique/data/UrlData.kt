package com.devmobile.applicationdomotique.data

class UrlData {
    fun login(): String = "https://polyhome.lesmoulinsdudev.com/api/users/auth"
    fun register(): String = "https://polyhome.lesmoulinsdudev.com/api/users/register"
    fun listHouse(): String = "https://polyhome.lesmoulinsdudev.com/api/houses"

    fun accessHouse(houseId: Int): String = "https://polyhome.lesmoulinsdudev.com/api/houses/$houseId/users"
    fun deleteAccess(houseId: Int): String = "https://polyhome.lesmoulinsdudev.com/api/houses/$houseId/users"

    fun device(houseId: Int): String = "https://polyhome.lesmoulinsdudev.com/api/houses/$houseId/devices"
    fun listHouseAccessUser(houseId: Int): String = "https://polyhome.lesmoulinsdudev.com/api/houses/$houseId/users"

    fun command(houseId: Int, deviceId: String): String =
        "https://polyhome.lesmoulinsdudev.com/api/houses/$houseId/devices/$deviceId/command"
}