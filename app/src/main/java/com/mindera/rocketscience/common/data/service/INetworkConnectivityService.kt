package com.mindera.rocketscience.common.data.service

interface INetworkConnectivityService {
    /**
     * @return true if it IS CONNECTED to the internet; false if it's NOT CONNECTED to the internet.
     */
    fun hasNetworkConnectivity() : Boolean
}