package com.example.teqelmasr.network

class Client : RemoteSource {
     companion object{
            private var instance: Client? = null
            fun getInstance(): Client{
                return  instance?: Client()
            }
        }

}