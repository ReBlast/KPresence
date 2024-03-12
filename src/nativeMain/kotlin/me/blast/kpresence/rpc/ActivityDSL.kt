@file:Suppress("unused")

package me.blast.kpresence.rpc

import me.blast.kpresence.epochMillis

@DslMarker
annotation class ActivityDSL

/**
 * Creates an Activity instance using the provided builder block.
 * @param block The builder block used to configure the Activity.
 * @return The constructed Activity instance.
 */
fun activity(block: ActivityBuilder.() -> Unit): Activity = ActivityBuilder().apply(block).build()

/**
 * Builder class for constructing Activity instances.
 */
@ActivityDSL
class ActivityBuilder {
  /**
   * Activity type. Defaults to [ActivityType.GAME].
   */
  var type: ActivityType = ActivityType.GAME
  
  /**
   * Stream URL, is validated when type is [ActivityType.STREAMING].
   */
  var url: String? = null
  
  /**
   * Unix timestamp (in milliseconds) of when the activity was added to the user's session.
   */
  var createdAt: Int = epochMillis()
  
  /**
   * Unix timestamps for start/end of the game.
   */
  var timestamps: ActivityTimestamps? = null
  
  /**
   * Application ID for the game.
   */
  var applicationId: Long? = null
  
  /**
   * What the player is currently doing.
   */
  var details: String? = null
  
  /**
   * User's current party status, or text used for a custom status.
   */
  var state: String? = null
  
  /**
   * Emoji used for a custom status, is validated when type is [ActivityType.CUSTOM].
   */
  var emoji: ActivityEmoji? = null
  
  /**
   * Information for the current party of the player.
   */
  var party: ActivityParty? = null
  
  /**
   * Images for the presence and their hover texts.
   */
  var assets: ActivityAssets? = null
  
  /**
   * Secrets for Rich Presence joining and spectating.
   */
  var secrets: ActivitySecrets? = null
  
  /**
   * Whether or not the activity is an instanced game session.
   */
  var instance: Boolean? = null
  
  /**
   * Activity flags.
   */
  var flags: UInt? = null
  
  private val buttons: MutableList<ActivityButton> = mutableListOf()

  /**
   * Configures the timestamps for the Activity.
   * @param block The builder block used to configure the ActivityTimestamps.
   */
  fun timestamps(block: ActivityTimestampsBuilder.() -> Unit) {
    timestamps = ActivityTimestampsBuilder().apply(block).build()
  }

  /**
   * Configures the emoji for the Activity.
   * @param block The builder block used to configure the ActivityEmoji.
   */
  fun emoji(block: ActivityEmojiBuilder.() -> Unit) {
    emoji = ActivityEmojiBuilder().apply(block).build()
  }

  /**
   * Configures the party information for the Activity.
   * @param block The builder block used to configure the ActivityParty.
   */
  fun party(block: ActivityPartyBuilder.() -> Unit) {
    party = ActivityPartyBuilder().apply(block).build()
  }

  /**
   * Configures the assets for the Activity.
   * @param block The builder block used to configure the ActivityAssets.
   */
  fun assets(block: ActivityAssetsBuilder.() -> Unit) {
    assets = ActivityAssetsBuilder().apply(block).build()
  }

  /**
   * Configures the secrets for the Activity.
   * @param block The builder block used to configure the ActivitySecrets.
   */
  fun secrets(block: ActivitySecretsBuilder.() -> Unit) {
    secrets = ActivitySecretsBuilder().apply(block).build()
  }

  /**
   * Adds a button to the Activity.
   * @param label The text shown on the button.
   * @param url The URL opened when clicking the button.
   */
  fun button(label: String, url: String) {
    buttons.add(ActivityButton(label, url))
  }

  /**
   * Builds the configured Activity instance.
   * @return The constructed Activity instance.
   */
  fun build(): Activity = Activity(
    type, url, createdAt, timestamps, applicationId, details, state, emoji, party, assets, secrets, instance, flags, buttons.takeIf { it.isNotEmpty() }?.toTypedArray()
  )
}

/**
 * Builder class for constructing ActivityTimestamps instances.
 */
@ActivityDSL
class ActivityTimestampsBuilder {
  /**
   * Unix time (in milliseconds) of when the activity started.
   */
  var start: Int? = null
  
  /**
   * Unix time (in milliseconds) of when the activity ends.
   */
  var end: Int? = null

  /**
   * Builds the configured ActivityTimestamps instance.
   * @return The constructed ActivityTimestamps instance.
   */
  fun build(): ActivityTimestamps = ActivityTimestamps(start, end)
}

/**
 * Builder class for constructing ActivityEmoji instances.
 */
@ActivityDSL
class ActivityEmojiBuilder {
  /**
   * Name of the emoji.
   */
  var name: String = ""
  
  /**
   * ID of the emoji.
   */
  var id: Long? = null
  
  /**
   * Whether the emoji is animated.
   */
  var animated: Boolean = false

  /**
   * Builds the configured ActivityEmoji instance.
   * @return The constructed ActivityEmoji instance.
   */
  fun build(): ActivityEmoji = ActivityEmoji(name, id, animated)
}

/**
 * Builder class for constructing ActivityParty instances.
 */
@ActivityDSL
class ActivityPartyBuilder {
  /**
   * ID of the party.
   */
  var id: String? = null
  private var currentSize: Int? = null
  private var maxSize: Int? = null

  /**
   * Configures the size of the party.
   * @param current The current size of the party.
   * @param max The maximum size of the party.
   */
  fun size(current: Int, max: Int) {
      currentSize = current
      maxSize = max
  }

  /**
   * Builds the configured ActivityParty instance.
   * @return The constructed ActivityParty instance.
   */
  fun build(): ActivityParty {
      val sizeArray = if (currentSize != null && maxSize != null) intArrayOf(currentSize!!, maxSize!!) else null
      return ActivityParty(id, sizeArray)
  }
}

/**
 * Builder class for constructing ActivityAssets instances.
 */
@ActivityDSL
class ActivityAssetsBuilder {
  /**
   * ID of the large image, or a URL.
   */
  var largeImage: String? = null
  
  /**
   * Text displayed when hovering over the large image of the activity.
   */
  var largeText: String? = null
  
  /**
   * ID of the small image, or a URL.
   */
  var smallImage: String? = null
  
  /**
   * Text displayed when hovering over the small image of the activity.
   */
  var smallText: String? = null

  /**
   * Builds the configured ActivityAssets instance.
   * @return The constructed ActivityAssets instance.
   */
  fun build(): ActivityAssets = ActivityAssets(largeImage, largeText, smallImage, smallText)
}

/**
 * Builder class for constructing ActivitySecrets instances.
 */
@ActivityDSL
class ActivitySecretsBuilder {
  /**
   * Secret for joining a party.
   */
  var join: String? = null
  
  /**
   * Secret for spectating a game.
   */
  var spectate: String? = null
  
  /**
   * Secret for a specific instanced match.
   */
  var match: String? = null

  /**
   * Builds the configured ActivitySecrets instance.
   * @return The constructed ActivitySecrets instance.
   */
  fun build(): ActivitySecrets = ActivitySecrets(join, spectate, match)
}