[pubnub-kotlin](../../index.md) / [com.pubnub.api](../index.md) / [PubNub](index.md) / [setChannelMetadata](./set-channel-metadata.md)

# setChannelMetadata

`fun setChannelMetadata(channel: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, name: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`? = null, description: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`? = null, custom: `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`? = null, includeCustom: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)` = false): `[`SetChannelMetadata`](../../com.pubnub.api.endpoints.objects.channel/-set-channel-metadata/index.md)

Set metadata for a Channel in the database, optionally including the custom data object for each.

### Parameters

`channel` - Channel name.

`name` - Name of a channel.

`description` - Description of a channel.

`custom` - Object with supported data types.

`includeCustom` - Include respective additional fields in the response.