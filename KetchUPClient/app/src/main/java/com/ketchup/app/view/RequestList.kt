package com.ketchup.app.view

import com.ketchup.app.models.RequestData

class RequestList() {
    companion object {
        var requestList = listOf<RequestData>(
            RequestData("Juan", "No ve", "https://static.tvtropes.org/pmwiki/pub/images/maddyandtheo.png")
        )
    }
}