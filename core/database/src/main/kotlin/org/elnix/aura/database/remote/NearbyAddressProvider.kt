package org.elnix.aura.database.remote

import android.content.Context
import android.location.Location
import android.location.LocationManager
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
import okhttp3.OkHttpClient
import okhttp3.Request
import org.elnix.aura.base.json
import org.elnix.aura.database.models.AddressData
import java.net.URLEncoder
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Result of a "random address near me" request, so the UI can react to the
 * exact failure reason instead of guessing.
 */
public sealed class NearbyAddressResult {
    public data class Success(val address: AddressData) : NearbyAddressResult()
    public data object LocationUnavailable : NearbyAddressResult()
    public data object NoAddressFound : NearbyAddressResult()
    public data object ServiceUnreachable : NearbyAddressResult()
}

@Serializable
internal data class OverpassResponse(val elements: List<OverpassElement> = emptyList())

@Serializable
internal data class OverpassElement(
    val center: OverpassCenter? = null,
    val tags: Map<String, String> = emptyMap(),
)

@Serializable
internal data class OverpassCenter(val lat: Double = 0.0, val lon: Double = 0.0)

@Serializable
internal data class NominatimResponse(
    val address: NominatimAddress? = null,
    val displayName: String? = null,
)

@Serializable
internal data class NominatimAddress(
    val road: String? = null,
    val houseNumber: String? = null,
    val city: String? = null,
    val town: String? = null,
    val village: String? = null,
    val hamlet: String? = null,
    val suburb: String? = null,
    val postcode: String? = null,
    val state: String? = null,
    val country: String? = null,
)

/**
 * Finds a random street around the device location using the free
 * OpenStreetMap services.
 *
 * A street is picked from the Overpass API inside the requested radius, then
 * its center point is reversed geocoded with Nominatim to get full address
 * components. Every call happens on [Dispatchers.IO].
 */
@Singleton
public class NearbyAddressProvider @Inject constructor(
    @ApplicationContext private val ctx: Context,
) {
    private val client = OkHttpClient.Builder()
        .connectTimeout(15, TimeUnit.SECONDS)
        .readTimeout(15, TimeUnit.SECONDS)
        .build()

    public suspend fun randomNearbyAddress(radiusMeters: Int): NearbyAddressResult =
        withContext(Dispatchers.IO) {
            val location = lastKnownLocation()
                ?: return@withContext NearbyAddressResult.LocationUnavailable

            val point = randomStreetCenter(location, radiusMeters)
                ?: return@withContext NearbyAddressResult.NoAddressFound

            val address = reverseGeocode(point)
                ?: return@withContext NearbyAddressResult.ServiceUnreachable

            NearbyAddressResult.Success(address)
        }

    private fun lastKnownLocation(): Location? {
        val manager = ctx.getSystemService(Context.LOCATION_SERVICE) as? LocationManager
            ?: return null
        var best: Location? = null
        for (provider in listOf(LocationManager.GPS_PROVIDER, LocationManager.NETWORK_PROVIDER)) {
            runCatching { manager.getLastKnownLocation(provider) }
                .getOrNull()
                ?.takeIf { it.latitude != 0.0 && it.longitude != 0.0 }
                ?.let { candidate ->
                    if (best == null || candidate.time > best.time) {
                        best = candidate
                    }
                }
        }
        return best
    }

    private fun randomStreetCenter(location: Location, radiusMeters: Int): OverpassCenter? {
        val query = """
            [out:json];
            way["highway"~"^(primary|secondary|tertiary|residential|unclassified)$"]["name"]
              (around:$radiusMeters,${location.latitude},${location.longitude});
            out center 300;
        """.trimIndent()
        val url = "https://overpass-api.de/api/interpreter?data=${URLEncoder.encode(query, "UTF-8")}"

        val body = get(url) ?: return null
        val response = runCatching { json.decodeFromString<OverpassResponse>(body) }.getOrNull()
            ?: return null

        val candidates = response.elements
            .filter { it.center != null && it.tags["name"].isNullOrBlank().not() }
            .mapNotNull { it.center }
            .filter { it.lat != 0.0 && it.lon != 0.0 }

        return candidates.randomOrNull()
    }

    private fun reverseGeocode(point: OverpassCenter): AddressData? {
        val url = buildString {
            append("https://nominatim.openstreetmap.org/reverse")
            append("?format=jsonv2&zoom=18&addressdetails=1")
            append("&lat=${point.lat}&lon=${point.lon}")
        }
        val body = get(url) ?: return null
        val address = runCatching { json.decodeFromString<NominatimResponse>(body) }.getOrNull()
            ?.address ?: return null

        return AddressData(
            street = address.road,
            houseNumber = address.houseNumber,
            city = listOfNotNull(address.city, address.town, address.village, address.hamlet)
                .firstOrNull(),
            postalCode = address.postcode,
            state = address.state,
            country = address.country,
            additionalInfo = listOfNotNull(address.suburb).firstOrNull(),
        )
    }

    private fun get(url: String): String? = runCatching {
        val request = Request.Builder()
            .url(url)
            .header("User-Agent", "Aura/1.0 (disposable-identity-generator)")
            .header("Accept-Language", "en")
            .build()
        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) {
                return@use null
            }
            response.body?.string()
        }
    }.getOrNull()
}
