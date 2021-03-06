package com.pubnub.api

import com.pubnub.api.enums.PNHeartbeatNotificationOptions
import com.pubnub.api.enums.PNLogVerbosity
import com.pubnub.api.enums.PNReconnectionPolicy
import okhttp3.Authenticator
import okhttp3.CertificatePinner
import okhttp3.ConnectionSpec
import okhttp3.logging.HttpLoggingInterceptor
import org.slf4j.LoggerFactory
import java.net.Proxy
import java.net.ProxySelector
import java.util.UUID
import javax.net.ssl.HostnameVerifier
import javax.net.ssl.SSLSocketFactory
import javax.net.ssl.X509ExtendedTrustManager

/**
 * A storage for user-provided information which describe further PubNub client behaviour.
 * Configuration instance contains additional set of properties which
 * allow to perform precise PubNub client configuration.
 */
class PNConfiguration {

    private val log = LoggerFactory.getLogger("PNConfiguration")

    private companion object Constants {
        private const val DEFAULT_DEDUPE_SIZE = 100
        private const val PRESENCE_TIMEOUT = 300
        private const val MINIMUM_PRESENCE_TIMEOUT = 20
        private const val NON_SUBSCRIBE_REQUEST_TIMEOUT = 10
        private const val SUBSCRIBE_TIMEOUT = 310
        private const val CONNECT_TIMEOUT = 5
    }

    /**
     * The subscribe key from the admin panel.
     */
    lateinit var subscribeKey: String

    /**
     * The publish key from the admin panel (only required if publishing).
     */
    lateinit var publishKey: String

    /**
     * The secret key from the admin panel (only required for modifying/revealing access permissions).
     *
     * Keep away from Android.
     */
    lateinit var secretKey: String

    /**
     * If Access Manager is utilized, client will use this authKey in all restricted requests.
     */
    lateinit var authKey: String

    /**
     * If set, all communications to and from PubNub will be encrypted.
     */
    lateinit var cipherKey: String

    /**
     * Custom origin if needed.
     *
     * Defaults to `ps.pndsn.com`
     */
    lateinit var origin: String

    /**
     * UUID to use. You should set a unique UUID to identify the user or the device that connects to PubNub.
     *
     * Defaults to an SDK generated UUID.
     */
    var uuid = "pn-${UUID.randomUUID()}"

    /**
     * If set to `true`,  requests will be made over HTTPS.
     *
     * Deafults to `true`.
     */
    var secure = true

    /**
     * Set to [PNLogVerbosity.BODY] to enable logging of network traffic, otherwise se to [PNLogVerbosity.NONE].
     */
    var logVerbosity = PNLogVerbosity.NONE

    /**
     * Set Heartbeat notification options.
     *
     * By default, the SDK alerts on failed heartbeats (equivalent to [PNHeartbeatNotificationOptions.FAILURES]).
     */
    var heartbeatNotificationOptions = PNHeartbeatNotificationOptions.FAILURES

    /**
     * Set to [PNReconnectionPolicy.LINEAR] for automatic reconnects.
     *
     * Use [PNReconnectionPolicy.NONE] to disable automatic reconnects.
     *
     * Use [PNReconnectionPolicy.EXPONENTIAL] to set exponential retry interval.
     *
     * Defaults to [PNReconnectionPolicy.NONE].
     */
    var reconnectionPolicy = PNReconnectionPolicy.NONE

    /**
     * Sets the custom presence server timeout.
     *
     * The value is in seconds, and the minimum value is 20 seconds.
     *
     * Also sets the value of [heartbeatInterval]
     */
    var presenceTimeout = PRESENCE_TIMEOUT
        set(value) {
            field =
                if (value < MINIMUM_PRESENCE_TIMEOUT) {
                    log.warn("Presence timeout is too low. Defaulting to: $MINIMUM_PRESENCE_TIMEOUT")
                    MINIMUM_PRESENCE_TIMEOUT
                } else value
            heartbeatInterval = (presenceTimeout / 2) - 1
        }

    /**
     * How often the client will announce itself to server.
     *
     * The value is in seconds.
     */
    var heartbeatInterval = 0

    /**
     * The subscribe request timeout.
     *
     * The value is in seconds.
     *
     * Defaults to 310.
     */
    var subscribeTimeout = SUBSCRIBE_TIMEOUT

    /**
     * How long before the client gives up trying to connect with a subscribe call.
     *
     * The value is in seconds.
     *
     * Defaults to 5.
     */
    var connectTimeout = CONNECT_TIMEOUT

    /**
     * For non subscribe operations (publish, herenow, etc),
     * how long to wait to connect to PubNub before giving up with a connection timeout error.
     *
     * The value is in seconds.
     *
     * Defaults to 10.
     */
    var nonSubscribeRequestTimeout = NON_SUBSCRIBE_REQUEST_TIMEOUT

    /**
     * If operating behind a misbehaving proxy, allow the client to shuffle the subdomains.
     *
     * Defaults to `false`.
     */
    var cacheBusting = false

    /**
     * When `true` the SDK doesn't send out the leave requests.
     *
     * Defaults to `false`.
     */
    var suppressLeaveEvents = false

    /**
     * Feature to subscribe with a custom filter expression.
     */
    lateinit var filterExpression: String

    /**
     * Whether to include a [PubNub.instanceId] with every request.
     *
     * Defaults to `false`.
     */
    var includeInstanceIdentifier = false

    /**
     * Whether to include a [PubNub.requestId] with every request.
     *
     * Defaults to `true`.
     */
    var includeRequestIdentifier = true

    /**
     * Sets how many times to retry to reconnect before giving up.
     * Must be used in combination with [reconnectionPolicy].
     *
     * The default value is `-1` which means unlimited retries.
     */
    var maximumReconnectionRetries = -1

    /**
     * @see [okhttp3.Dispatcher.setMaxRequestsPerHost]
     */
    var maximumConnections: Int? = null

    /**
     * If the number of messages into the payload is above this value,
     *
     * [PNStatusCategory.PNRequestMessageCountExceededCategory] is thrown.
     */
    var requestMessageCountThreshold: Int? = null

    /**
     * Enable Google App Engine networking.
     *
     * Defaults to `false`.
     */
    var googleAppEngineNetworking = false

    /**
     * Whether to start a separate subscriber thread when creating the instance.
     *
     * Defaults to `true`.
     */
    var startSubscriberThread = true

    /**
     * Instructs the SDK to use a proxy configuration when communicating with PubNub servers.
     *
     * @see [Proxy]
     */
    var proxy: Proxy? = null

    /**
     * @see [ProxySelector]
     */
    var proxySelector: ProxySelector? = null

    /**
     * @see [Authenticator]
     */
    var proxyAuthenticator: Authenticator? = null

    /**
     * @see [CertificatePinner]
     */
    var certificatePinner: CertificatePinner? = null

    /**
     * Sets a custom [HttpLoggingInterceptor] for logging network traffic.
     *
     * @see [HttpLoggingInterceptor]
     */
    var httpLoggingInterceptor: HttpLoggingInterceptor? = null

    /**
     * @see [SSLSocketFactory]
     */
    var sslSocketFactory: SSLSocketFactory? = null

    /**
     * @see [X509ExtendedTrustManager]
     */
    var x509ExtendedTrustManager: X509ExtendedTrustManager? = null

    /**
     * @see [okhttp3.ConnectionSpec]
     */
    var connectionSpec: ConnectionSpec? = null

    /**
     * @see [javax.net.ssl.HostnameVerifier]
     */
    var hostnameVerifier: HostnameVerifier? = null

    var dedupOnSubscribe = false
    var maximumMessagesCacheSize = DEFAULT_DEDUPE_SIZE

    internal fun isSubscribeKeyValid() = ::subscribeKey.isInitialized && !subscribeKey.isBlank()
    internal fun isAuthKeyValid() = ::authKey.isInitialized && !authKey.isBlank()
    internal fun isCipherKeyValid() = ::cipherKey.isInitialized && !cipherKey.isBlank()
    internal fun isPublishKeyValid() = ::publishKey.isInitialized && !publishKey.isBlank()
    internal fun isSecretKeyValid() = ::secretKey.isInitialized && !secretKey.isBlank()
    internal fun isOriginValid() = ::origin.isInitialized && !origin.isBlank()
    internal fun isFilterExpressionKeyValid(function: String.() -> Unit) {
        if (::filterExpression.isInitialized && !filterExpression.isBlank()) {
            function.invoke(filterExpression)
        }
    }
}
