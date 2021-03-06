package com.pubnub.api.integration

import com.pubnub.api.CommonUtils.createInterceptor
import com.pubnub.api.Keys
import com.pubnub.api.PNConfiguration
import com.pubnub.api.PubNub
import com.pubnub.api.enums.PNLogVerbosity
import org.junit.After
import org.junit.Before
import org.slf4j.LoggerFactory
import java.util.UUID

abstract class BaseIntegrationTest {

    protected val logger = LoggerFactory.getLogger(this.javaClass.simpleName)

    lateinit var pubnub: PubNub
    lateinit var server: PubNub

    private var mGuestClients = mutableListOf<PubNub>()

    @Before
    fun before() {
        onPrePubnub()
        pubnub = createPubNub()
        if (needsServer()) {
            server = createServer()
        }
        onBefore()
    }

    @After
    fun after() {
        onAfter()
        for (guestClient in mGuestClients) {
            destroyClient(guestClient)
        }
        mGuestClients.clear()
    }

    protected fun createPubNub(): PubNub {
        var pnConfiguration = provideStagingConfiguration()
        if (pnConfiguration == null) {
            pnConfiguration = getBasicPnConfiguration()
        }
        val pubNub = PubNub(pnConfiguration)
        registerGuestClient(pubNub)
        return pubNub
    }

    private fun createServer(): PubNub {
        val pubNub = PubNub(getServerPnConfiguration())
        registerGuestClient(pubNub)
        return pubNub
    }

    private fun registerGuestClient(guestClient: PubNub) {
        mGuestClients.add(guestClient)
    }

    protected open fun getBasicPnConfiguration(): PNConfiguration {
        val pnConfiguration = PNConfiguration()
        if (!needsServer()) {
            pnConfiguration.subscribeKey = Keys.subKey
            pnConfiguration.publishKey = Keys.pubKey
        } else {
            pnConfiguration.subscribeKey = Keys.pamSubKey
            pnConfiguration.publishKey = Keys.pamPubKey
            pnConfiguration.authKey = provideAuthKey()!!
        }
        pnConfiguration.logVerbosity = PNLogVerbosity.NONE
        pnConfiguration.httpLoggingInterceptor = createInterceptor(logger)

        pnConfiguration.uuid = "client-${UUID.randomUUID()}"
        return pnConfiguration
    }

    private fun getServerPnConfiguration(): PNConfiguration {
        val pnConfiguration = PNConfiguration()
        pnConfiguration.subscribeKey = Keys.pamSubKey
        pnConfiguration.publishKey = Keys.pamPubKey
        pnConfiguration.secretKey = Keys.pamSecKey
        pnConfiguration.logVerbosity = PNLogVerbosity.NONE
        pnConfiguration.httpLoggingInterceptor = createInterceptor(logger)

        pnConfiguration.uuid = "server-${UUID.randomUUID()}"
        return pnConfiguration
    }

    private fun destroyClient(client: PubNub) {
        client.unsubscribeAll()
        client.forceDestroy()
    }

    private fun needsServer() = provideAuthKey() != null

    protected open fun onBefore() {}
    protected open fun onAfter() {}
    protected open fun onPrePubnub() {}

    protected open fun provideAuthKey(): String? = null

    protected open fun provideStagingConfiguration(): PNConfiguration? = null

    fun wait(seconds: Int = 3) {
        Thread.sleep((seconds * 1_000).toLong())
    }
}
