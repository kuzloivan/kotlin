package com.pubnub.api.legacy.endpoints.message_actions

import com.github.tomakehurst.wiremock.client.WireMock.aResponse
import com.github.tomakehurst.wiremock.client.WireMock.delete
import com.github.tomakehurst.wiremock.client.WireMock.deleteRequestedFor
import com.github.tomakehurst.wiremock.client.WireMock.findAll
import com.github.tomakehurst.wiremock.client.WireMock.get
import com.github.tomakehurst.wiremock.client.WireMock.noContent
import com.github.tomakehurst.wiremock.client.WireMock.stubFor
import com.github.tomakehurst.wiremock.client.WireMock.urlMatching
import com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo
import com.pubnub.api.CommonUtils.assertPnException
import com.pubnub.api.CommonUtils.emptyJson
import com.pubnub.api.CommonUtils.failTest
import com.pubnub.api.PubNubError
import com.pubnub.api.enums.PNOperationType
import com.pubnub.api.legacy.BaseTest
import com.pubnub.api.listen
import com.pubnub.api.param
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Test
import java.util.concurrent.atomic.AtomicBoolean

class RemoveMessageActionEndpointTest : BaseTest() {

    @Test
    fun testSyncSuccess() {
        stubFor(
            delete(urlPathEqualTo("/v1/message-actions/mySubscribeKey/channel/coolChannel/message/123/action/100"))
                .willReturn(
                    aResponse().withBody(
                        """
                        {
                          "status": 200,
                          "data": {}
                        }
                    """.trimIndent()
                    )
                )
        )

        pubnub.removeMessageAction(
            channel = "coolChannel",
            messageTimetoken = 123L,
            actionTimetoken = 100L
        ).sync()!!
    }

    @Test
    fun testAsyncSuccess() {
        stubFor(
            delete(urlPathEqualTo("/v1/message-actions/mySubscribeKey/channel/coolChannel/message/123/action/100"))
                .willReturn(
                    aResponse().withBody(
                        """
                        {
                          "status": 200,
                          "data": {}
                        }
                    """.trimIndent()
                    )
                )
        )

        val success = AtomicBoolean()

        pubnub.removeMessageAction(
            channel = "coolChannel",
            messageTimetoken = 123L,
            actionTimetoken = 100L
        ).async { _, status ->
            assertFalse(status.error)
            assertEquals(PNOperationType.PNDeleteMessageAction, status.operation)
            success.set(true)
        }

        success.listen()
    }

    @Test
    fun testMalformedJson() {
        stubFor(
            delete(urlPathEqualTo("/v1/message-actions/mySubscribeKey/channel/coolChannel/message/123/action/100"))
                .willReturn(
                    aResponse().withBody(
                        """
                        {
                          "status": 200
                          "data": {
                        }
                    """.trimIndent()
                    )
                )
        )

        try {
            pubnub.removeMessageAction(
                channel = "coolChannel",
                messageTimetoken = 123L,
                actionTimetoken = 100L
            ).sync()!!
        } catch (e: Exception) {
            failTest()
        }
    }

    @Test
    fun testEmptyBody() {
        stubFor(
            delete(urlPathEqualTo("/v1/message-actions/mySubscribeKey/channel/coolChannel/message/123/action/100"))
                .willReturn(emptyJson())
        )

        try {
            pubnub.removeMessageAction(
                channel = "coolChannel",
                messageTimetoken = 123L,
                actionTimetoken = 100L
            ).sync()!!
        } catch (e: Exception) {
            failTest()
        }
    }

    @Test
    fun testNoBody() {
        stubFor(
            delete(urlPathEqualTo("/v1/message-actions/mySubscribeKey/channel/coolChannel/message/123/action/100"))
                .willReturn(noContent())
        )

        try {
            pubnub.removeMessageAction(
                channel = "coolChannel",
                messageTimetoken = 123L,
                actionTimetoken = 100L
            ).sync()!!
        } catch (e: Exception) {
            failTest()
        }
    }

    @Test
    fun testNoData() {
        stubFor(
            delete(urlPathEqualTo("/v1/message-actions/mySubscribeKey/channel/coolChannel/message/123/action/100"))
                .willReturn(
                    aResponse().withBody(
                        """
                        {
                          "status": 200
                        }
                    """.trimIndent()
                    )
                )
        )

        try {
            pubnub.removeMessageAction(
                channel = "coolChannel",
                messageTimetoken = 123L,
                actionTimetoken = 100L
            ).sync()!!
        } catch (e: Exception) {
            failTest()
        }
    }

    @Test
    fun testNullData() {
        stubFor(
            delete(urlPathEqualTo("/v1/message-actions/mySubscribeKey/channel/coolChannel/message/123/action/100"))
                .willReturn(
                    aResponse().withBody(
                        """
                        {
                          "status": 200,
                          "data": null
                        }
                    """.trimIndent()
                    )
                )
        )

        try {
            pubnub.removeMessageAction(
                channel = "coolChannel",
                messageTimetoken = 123L,
                actionTimetoken = 100L
            ).sync()!!
        } catch (e: Exception) {
            failTest()
        }
    }

    @Test
    fun testBlankChannel() {
        try {
            pubnub.removeMessageAction(
                channel = " ",
                messageTimetoken = 123L,
                actionTimetoken = 100L
            ).sync()!!
            failTest()
        } catch (e: Exception) {
            assertPnException(PubNubError.CHANNEL_MISSING, e)
        }
    }

    @Test
    fun testInvalidSubKey() {
        pubnub.configuration.subscribeKey = " "
        try {
            pubnub.removeMessageAction(
                channel = "coolChannel",
                messageTimetoken = 123L,
                actionTimetoken = 100L
            ).sync()!!
            failTest()
        } catch (e: Exception) {
            assertPnException(PubNubError.SUBSCRIBE_KEY_MISSING, e)
        }
    }

    @Test
    fun testAuthKeyRequired() {
        stubFor(
            delete(urlPathEqualTo("/v1/message-actions/mySubscribeKey/channel/coolChannel/message/123/action/100"))
                .willReturn(
                    aResponse().withBody(
                        """
                        {
                          "status": 200,
                          "data": {}
                        }
                    """.trimIndent()
                    )
                )
        )

        pubnub.configuration.authKey = "authKey"

        pubnub.removeMessageAction(
            channel = "coolChannel",
            messageTimetoken = 123L,
            actionTimetoken = 100L
        ).sync()!!

        val requests = findAll(deleteRequestedFor(urlMatching("/.*")))
        assertEquals(1, requests.size)

        assertEquals("authKey", requests[0].queryParameter("auth").firstValue())
    }

    @Test
    fun testTelemetryParam() {
        val success = AtomicBoolean()

        stubFor(
            delete(urlPathEqualTo("/v1/message-actions/mySubscribeKey/channel/coolChannel/message/123/action/100"))
                .willReturn(
                    aResponse().withBody(
                        """
                        {
                          "status": 200,
                          "data": {}
                        }
                    """.trimIndent()
                    )
                )
        )

        stubFor(
            get(urlMatching("/time/0.*"))
                .willReturn(aResponse().withBody("[1000]"))
        )

        lateinit var telemetryParamName: String

        pubnub.removeMessageAction(
            channel = "coolChannel",
            messageTimetoken = 123L,
            actionTimetoken = 100L
        ).async { _, status ->
            assertFalse(status.error)
            assertEquals(PNOperationType.PNDeleteMessageAction, status.operation)
            telemetryParamName = "l_${status.operation.queryParam}"
            assertEquals("l_msga", telemetryParamName)
            success.set(true)
        }

        success.listen()

        pubnub.time().async { _, status ->
            assertFalse(status.error)
            assertNotNull(status.param(telemetryParamName))
            success.set(true)
        }

        success.listen()
    }
}
