package com.tobiask.dataStores

class AdminDataStore {
    var openConnections = 0
    var bannedIps: MutableList<String> = mutableListOf()
}