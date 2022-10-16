package com.ketchup.app

import android.os.Bundle
import com.ketchup.app.R.drawable.*
import com.ketchup.app.models.UserData

class UserList {
    companion object{
        val userList = listOf<UserData>(
            UserData("Arkidon", "Mustard hype","https://img.buzzfeed.com/buzzfeed-static/static/enhanced/webdr02/2013/9/9/10/original-grid-image-32330-1378738045-33.jpg?crop=750:750;0,64"),
            UserData("Daviis", "Me quiero morir con el volley","https://media.karousell.com/media/photos/products/2022/1/19/haikyuu_nekoma_cosplay_jacket_1642613569_821cae80_progressive.jpg"),
            UserData("Alice", "Sushi UwU","https://i.pinimg.com/474x/ff/0d/f4/ff0df44c4cd43c7cd964e36b4354e56b.jpg"),
            UserData("Theo", "It is what it is","https://pbs.twimg.com/media/E_XBP6iWYAIJpZk?format=png&name=small"),
            UserData("Badeline", "Muahaha","https://pbs.twimg.com/media/E_XBP6YXsAoOJHN?format=png&name=small"),
            UserData("Mom", "This is a dream","https://tcrf.net/images/6/63/CelesteMomPhoneNormal00.png"),
            UserData("Mr Oshiro", "Best hotel","https://static.miraheze.org/celestewiki/thumb/2/24/Oshiro_face.png/200px-Oshiro_face.png"),
            UserData("Granny", "The Mountain doesn't pull any punches","https://static.wikia.nocookie.net/celestegame/images/7/71/Grannyface.png/revision/latest/scale-to-width-down/250?cb=20190921014357")
        )
    }
}