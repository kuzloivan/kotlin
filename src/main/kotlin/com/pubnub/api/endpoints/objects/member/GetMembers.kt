package com.pubnub.api.endpoints.objects.member

import com.pubnub.api.Endpoint
import com.pubnub.api.PubNub
import com.pubnub.api.endpoints.objects.internal.ReturningCollection
import com.pubnub.api.endpoints.objects.internal.ReturningUUIDDetailsCustom
import com.pubnub.api.enums.PNOperationType
import com.pubnub.api.models.consumer.objects.PNPage
import com.pubnub.api.models.consumer.objects.member.PNMember
import com.pubnub.api.models.consumer.objects.member.PNMemberArrayResult
import com.pubnub.api.models.server.objects_api.EntityArrayEnvelope
import retrofit2.Call
import retrofit2.Response
import java.util.HashMap

/**
 * @see [PubNub.getMembers]
 */
class GetMembers internal constructor(
    pubnub: PubNub,
    private val channel: String,
    private val returningCollection: ReturningCollection,
    private val withUUIDDetailsCustom: ReturningUUIDDetailsCustom
) : Endpoint<EntityArrayEnvelope<PNMember>, PNMemberArrayResult>(pubnub) {

    override fun doWork(queryParams: HashMap<String, String>): Call<EntityArrayEnvelope<PNMember>> {
        val params = queryParams + returningCollection.createCollectionQueryParams() +
                withUUIDDetailsCustom.createIncludeQueryParams()
        return pubnub.retrofitManager.objectsService.getMembers(
            channel = channel,
            subKey = pubnub.configuration.subscribeKey,
            options = params
        )
    }

    override fun createResponse(input: Response<EntityArrayEnvelope<PNMember>>): PNMemberArrayResult? {
        return input.body()?.let { arrayEnvelope ->
            PNMemberArrayResult(
                    status = arrayEnvelope.status,
                    data = arrayEnvelope.data,
                    totalCount = arrayEnvelope.totalCount,
                    next = arrayEnvelope.next?.let { PNPage.PNNext(it) },
                    prev = arrayEnvelope.prev?.let { PNPage.PNPrev(it) }
            )
        }
    }

    override fun operationType(): PNOperationType {
        return PNOperationType.PNGetMembershipsOperation
    }
}
