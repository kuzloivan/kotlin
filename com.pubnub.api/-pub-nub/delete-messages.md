[pubnub-kotlin](../../index.md) / [com.pubnub.api](../index.md) / [PubNub](index.md) / [deleteMessages](./delete-messages.md)

# deleteMessages

`fun deleteMessages(channels: `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`>, start: `[`Long`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)`? = null, end: `[`Long`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)`? = null): `[`DeleteMessages`](../../com.pubnub.api.endpoints/-delete-messages/index.md)

Removes messages from the history of a specific channel.

NOTE: There is a setting to accept delete from history requests for a key,
which you must enable by checking the Enable `Delete-From-History` checkbox
in the key settings for your key in the Administration Portal.

Requires Initialization with secret key.

### Parameters

`channels` - Channels to delete history messages from.

`start` - Timetoken delimiting the start of time slice (exclusive) to delete messages from.

`end` - Time token delimiting the end of time slice (inclusive) to delete messages from.