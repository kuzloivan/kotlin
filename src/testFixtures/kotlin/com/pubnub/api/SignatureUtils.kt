package com.pubnub.api

import com.github.tomakehurst.wiremock.verification.LoggedRequest
import com.pubnub.api.vendor.Base64
import com.pubnub.api.vendor.Crypto
import okhttp3.HttpUrl
import okhttp3.Request
import okio.Buffer
import org.junit.Assert.assertEquals
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.URLEncoder
import java.nio.charset.Charset
import java.security.InvalidKeyException
import java.security.NoSuchAlgorithmException
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

object SignatureUtils {

    fun decomposeAndVerifySignature(configuration: PNConfiguration, request: LoggedRequest) {
        decomposeAndVerifySignature(
            configuration = configuration,
            url = request.absoluteUrl,
            method = request.method.name,
            body = request.bodyAsString
        )
    }

    fun decomposeAndVerifySignature(configuration: PNConfiguration, request: Request) {
        decomposeAndVerifySignature(
            configuration = configuration,
            url = request.url().toString(),
            method = request.method(),
            body = requestBodyToString(request)
        )
    }

    private fun decomposeAndVerifySignature(
        configuration: PNConfiguration,
        url: String,
        method: String,
        body: String = ""
    ) {
        val httpUrl = HttpUrl.parse(url)
        println(httpUrl)

        val sortedQueryString = httpUrl!!.run {
            queryParameterNames()
                .filter { it != "signature" }
                .map { it to pamEncode(queryParameterValues(it).first()) }
                .toMap()
                .toSortedMap()
                .map { "${it.key}=${it.value}" }
                .joinToString("&")
        }

        var v2Signature = true

        val input =
            if (!(httpUrl.encodedPath().startsWith("/publish") && method.toLowerCase() == "post")) {
                """
                ${method.toUpperCase()}
                ${configuration.publishKey}
                ${httpUrl.encodedPath()}
                $sortedQueryString
                $body
            """.trimIndent()
            } else {
                v2Signature = false
                """
                ${configuration.subscribeKey}
                ${configuration.publishKey}
                ${httpUrl.encodedPath()}
                $sortedQueryString
                """.trimIndent()
            }

        val actualSignature = httpUrl.queryParameter("signature")
        val verifiedSignature = verifyViaPython(configuration.secretKey, input, v2Signature)

        val rebuiltSignature = signSHA256(configuration.secretKey, input).run {
            if (v2Signature) "v2.${this.trim('=')}"
            else this
        }

        println("originalTimestamp:\t${httpUrl.queryParameter("timestamp")}")
        println("signatureInput:\t$input")
        println("actualSignature:\t$actualSignature")
        println("rebuiltSignature:\t$rebuiltSignature")
        println("verifiedSignature:\t$verifiedSignature")

        assertEquals(actualSignature, rebuiltSignature)
        assertEquals(actualSignature, verifiedSignature)
    }

    private fun signSHA256(key: String, data: String): String {
        val sha256HMAC: Mac
        val hmacData: ByteArray
        val secretKey = SecretKeySpec(key.toByteArray(charset("UTF-8")), "HmacSHA256")
        sha256HMAC = try {
            Mac.getInstance("HmacSHA256")
        } catch (e: NoSuchAlgorithmException) {
            throw Crypto.newCryptoError(0, e)
        }
        try {
            sha256HMAC.init(secretKey)
        } catch (e: InvalidKeyException) {
            throw Crypto.newCryptoError(0, e)
        }
        hmacData = sha256HMAC.doFinal(data.toByteArray(charset("UTF-8")))
        val signedd = String(Base64.encode(hmacData, 0), Charset.forName("UTF-8"))
            .replace('+', '-')
            .replace('/', '_')
            .replace("\n", "")
        return signedd
    }

    private fun verifyViaPython(key: String, input: String, v2Signature: Boolean): String {
        val i = input.replace("\n", "###")
        val path = ClassLoader.getSystemClassLoader().getResource("hmacsha256.py")!!.path
        val command = "python $path $key $i $v2Signature"
        val process = Runtime.getRuntime().exec(command)
        return BufferedReader(InputStreamReader(process.inputStream)).readLine()
    }

    private fun requestBodyToString(request: Request): String {
        if (request.body() == null) {
            return ""
        }
        try {
            val buffer = Buffer()
            request.body()!!.writeTo(buffer)
            return buffer.readUtf8()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return ""
    }

    private fun pamEncode(stringToEncode: String, alreadyPercentEncoded: Boolean = false): String {
        /* !'()*~ */

        return if (alreadyPercentEncoded) {
            stringToEncode
        } else {
            URLEncoder.encode(stringToEncode, "UTF-8")
                .replace("+", "%20")
        }.run {
            replace("*", "%2A")
        }
    }
}
