[pubnub-kotlin](../../index.md) / [com.pubnub.api.endpoints.push](../index.md) / [RemoveAllPushChannelsForDevice](./index.md)

# RemoveAllPushChannelsForDevice

`class RemoveAllPushChannelsForDevice : `[`Endpoint`](../../com.pubnub.api/-endpoint/index.md)`<`[`Void`](https://docs.oracle.com/javase/6/docs/api/java/lang/Void.html)`, `[`PNPushRemoveAllChannelsResult`](../../com.pubnub.api.models.consumer.push/-p-n-push-remove-all-channels-result/index.md)`>`

**See Also**

[PubNub.removeAllPushNotificationsFromDeviceWithPushToken](../../com.pubnub.api/-pub-nub/remove-all-push-notifications-from-device-with-push-token.md)

### Properties

| Name | Summary |
|---|---|
| [deviceId](device-id.md) | `val deviceId: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [environment](environment.md) | `val environment: `[`PNPushEnvironment`](../../com.pubnub.api.enums/-p-n-push-environment/index.md) |
| [pushType](push-type.md) | `val pushType: `[`PNPushType`](../../com.pubnub.api.enums/-p-n-push-type/index.md) |
| [topic](topic.md) | `val topic: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?` |

### Functions

| Name | Summary |
|---|---|
| [createResponse](create-response.md) | `fun createResponse(input: Response<`[`Void`](https://docs.oracle.com/javase/6/docs/api/java/lang/Void.html)`>): `[`PNPushRemoveAllChannelsResult`](../../com.pubnub.api.models.consumer.push/-p-n-push-remove-all-channels-result/index.md) |
| [doWork](do-work.md) | `fun doWork(queryParams: `[`HashMap`](https://docs.oracle.com/javase/6/docs/api/java/util/HashMap.html)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`>): Call<`[`Void`](https://docs.oracle.com/javase/6/docs/api/java/lang/Void.html)`>` |
| [operationType](operation-type.md) | `fun operationType(): PNRemoveAllPushNotificationsOperation` |
| [validateParams](validate-params.md) | `fun validateParams(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |