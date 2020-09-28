[pubnub-kotlin](../../index.md) / [com.pubnub.api](../index.md) / [PubNub](index.md) / [getUUIDMetadata](./get-u-u-i-d-metadata.md)

# getUUIDMetadata

`fun getUUIDMetadata(uuid: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`? = null, includeCustom: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)` = false): `[`GetUUIDMetadata`](../../com.pubnub.api.endpoints.objects.uuid/-get-u-u-i-d-metadata/index.md)

Returns metadata for the specified UUID, optionally including the custom data object for each.

### Parameters

`uuid` - Unique user identifier. If not supplied then current user’s uuid is used.

`includeCustom` - Include respective additional fields in the response.