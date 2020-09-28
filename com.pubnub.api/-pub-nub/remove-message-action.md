[pubnub-kotlin](../../index.md) / [com.pubnub.api](../index.md) / [PubNub](index.md) / [removeMessageAction](./remove-message-action.md)

# removeMessageAction

`fun removeMessageAction(channel: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, messageTimetoken: `[`Long`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)`, actionTimetoken: `[`Long`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)`): `[`RemoveMessageAction`](../../com.pubnub.api.endpoints.message_actions/-remove-message-action/index.md)

Remove a previously added action on a published message. Returns an empty response.

### Parameters

`channel` - Channel to remove message actions from.

`messageTimetoken` - The publish timetoken of the original message.

`actionTimetoken` - The publish timetoken of the message action to be removed.