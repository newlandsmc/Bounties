<h1 align="center">Welcome to Bounties 👋</h1>
<p>
  <img alt="Version" src="https://img.shields.io/badge/version-1.0-blue.svg?cacheSeconds=2592000" />
</p>

> The plugin bounties is a custom made plugin tailored for the need of Semi-Vanilla MC.The plugin marks a player as bounty when a player kills another one.There will be rewards for players who kills the bounty. The rewards are based on kills. rewards for kills are defined in the config.


### 🏠 [Homepage](https://github.com/SemiVanilla-MC/Bounties)

## Author

👤 **Alen_Alex, SemiVanilla-MC**

* Website: https://semivanilla.com
* Github: [@SemiVanilla-MC](https://github.com/SemiVanilla-MC)

## Commands

All the commands the plugin feature is marked down below with their permission

|Command   |Description   |Permission   |
|---|---|---|
|/bounty add name   |Adds bounty to the specified player   |bounty.command.add   |
|/bounty add name mins   |Adds bounty to the specified player for the specified time   |bounty.command.add   |
|/bounty remove name  |Removes bounty from a player if it exists   |bounty.command.remove   |
|/bounty bypass name   |Adds a temporary bypass to the player. This is not persistent, will be lost once the server restarts   |bounty.command.bypass   |
|/bounty reload  |Reloads the configuration of the plugin   |bounty.command.reload   |
|/bounty list   |Opens a GUI with active bounties   |bounty.command.list   |
|/bounties   |Opens a GUI with active bounties   |bounty.command.list   |
|/bounties set name kills |Set the kills for a bounty to the specified value |bounty.command.set|

## Permissions

Some other permissions the plugin have

|Permission   |What for?   |
|---|---|
|bounty.admin   |This permission adds the ability to teleport to a player using GUI   |
|bounty.bypass   |This permission bypass from creating bounties or rewarding bouties. If the killer or killed have this permission that will not be counted and skipped   |

## Placeholders

  The plugin has some placeholders available. This will be registered only if placeholderAPI is
  enabled on server startup

| Placeholder      | What it returns   ?                                                                   |
|------------------|---------------------------------------------------------------------------------------|
  | %bs_tag%         | Returns a text set in the config if the player bounty, else will return an empty text |
 | %bs_kills%       | Returns the no of kills a player has if he is a bounty else 0                         |
 | %bs_timeleft%    | Return how much time left on the bounty if he has a bounty else 0                     |
| %bs_level%       | Returns the possible level that matches with the configured kills                     |
| %bs_isbounty%    | Returns `true` if the player is bounty else `false`                                   |
| %bs_bountykills% | Returns no of bounties that a player has killed in his entire playtime on the server  |
| %bs_online%            | Returns the no of bounties online on the server                                       |
## Configurations

### This is not the config. It just explains what all are what. You can check out the config [here](https://github.com/SemiVanilla-MC/Bounties/blob/master/src/main/resources/config.yml)

```YAML
##Prefix of the plugin
##You can leave it blank if you don't wish to set one
prefix: ''

##Duration of a bounty in sec. How long does a bounty stay on a player
bounty-duration-in-sec: No of sec's a bounty should last in a secs

#Reward levels = number of kills needed

#currently available options:
# [XP]:amount -> Gives player definite amount of XP
# [CONSOLE]:command -> Execute commands from console
# [PLAYER]:command -> Execute command as a player
# [MESSAGE]:message -> Send a message to the player who killed the bounty
# [XPLVL]:amount -> Gives player the specified levels worth of XP
# [BROADCAST]:message -> Broadcast a message to the entire server
#Available Placeoholders For Command and Messages
# %dead-bounty% -> The player who is dead or the player who was the bounty
# %killer% -> Player who killed the bounty
rewards:
  level:
    kill-count:
      - 'ACTION:TRIGGER'


##The message that would parse when %bs_tag% is used if the player is bounty
##NOTE: PlaceholderAPI doesn't support MiniMessage, so use Legacy Color Codes
placeholder-tag-message: " (bounty)"

##Configuration for GUI's
#name -> Name of the GUI
#rows -> How many rows the plugin should have without the bottom navbar
#previous -> Display name for the previous-button
#next -> Display name for the next-button. %player% -> displays the playername
#name-player-placeholder -> The text to be displayed on each bounty head
#lore-placeholder -> The lore to be added on to each bounty had
gui:
  name: "<red>Bounties"
  rows: 3
  previous: "<color:#FFE400>« Previous"
  next: "<color:#FFE400>» Next"
  name-player-placeholder: "<color:#00FFF0>%player%"
  lore-placeholder:
    - "<color:#00FFF0>Time left <color:#A9A7A2>» %timeleft%"
    - "<color:#00FFF0>Current Kills <color:#A9A7A2>» %kills%"

##Amount of time in ticks wait before sending in each message.
interval-during-each-message-in-ticks: How many ticks should the plugin wait before sending a message from its previous one.

##The radius of marker that comes up on the square map plugin.
##NOTE FOR Devs: These values are passed along side the META_DATA `bounty`, so this value can be fetched from anywhere
square-map-radius:
  Kill: Radius

#################################################################
#Messages that are used in the game. Almost all the messages support multiple placeholders. The placeholders for each
#message has been documented above its key.
#All the messages in the config support MiniMessages from kyori's Adventure API.
#All the messages down here support PlaceholderAPI Support...The placeholders are parsed for the player to whom the
#message has been sent. Not the killer or Dead Player. This is done to make consistency in the plugin messaging
messages:
  #Available Placeholders:
  #%dead_player% -> Name of the dead player
  #%killer% -> Name of the killer
  #%rand_loc% -> A random location of radius 50 of the bounty player
  new-bounty-broadcast:
    - This message is send when a player gets a new bounty on his head
  #Available Placeholders:
  #%dead_player% -> Name of the dead player
  #%killer% -> Name of the killer
  #%rand_loc% -> A random location of radius 50 of the bounty player
  #%kills% -> Gets no of kills the killer player have
  existing-bounty-broadcast:
    - This message is send when a player with an existing bounty kills another player
  #Available Placeholders:
  #%dead_player% -> Name of the dead player
  #%player_x% -> X Location X of the dead bounty
  #%player_y% -> Y Location Y of the dead bounty
  #%player_z% -> Y Location Z of the dead bounty
  #%killer% -> Name of the killer
  #%player_kills% -> The consecutive kills bounty had
  #%player_level% -> The current reward level the player had
  player-bounty-released-broadcast:
    - This message is send when a player has killed another player who has a bounty on his head.
```
> ### 📎 Things To Note While Config

* The entire plugin is written to only support [Kyori's-MiniMessage](https://docs.adventure.kyori.net/minimessage).
The plugin uses Adventure API's *4.10.0-SNAPSHOT*, This version and possible newer versions removed Markdown flavouring. So [Markdowns](https://docs.adventure.kyori.net/minimessage#markdown) are disabled by default and won't work on the plugin.

* The only configuration that supports Legacy Color Codes or *&c, &d* type of format is *placeholder-tag-message*. This is due to the reason that we can only pass String to PlaceholderAPI's request

* Any messages that is put blank won't be send. So if you don't need one, you can just put it as blank

## 🔧 Building

The plugin is written on Java and uses Maven as its build configurator.

one can run `clean package` from the root folder to produce the jar

## 📜 To Developers

* I have written this plugin with future upgradability in mind. Currently the plugin utilizes JSON for storing persitent datas. In any future if you would like to extend the plugin to SQL Storage or NoSQL or even H2, you can do it without too much work. There are 2 types of cache the plugin stores

  ### 1. How much bounty a player has killed. (Simillar to player data)
  
    This stores only 2 data's. The player UUID and how much bounties the player killed. In future if you wish to add more storage option, you can just implement the [DataImpl](https://github.com/SemiVanilla-MC/Bounties/blob/master/src/main/java/com/semivanilla/bounties/data/DataImpl.java) interface located at  *com.semivanilla.bounties.data;*

    This have few methods

    ```JAVA
      /**
      * This method is used to initialize the data storage system the plugin features.
      * This method is called after initializing {@link Configuration#loadConfigData()} method. So one can call data even
      * from the configuration
      *
      * NOTE: If the connection fails, one should return false so that the plugin can be properly disabled
      * @return boolean Whether the connection or cache storage was successful
      */
      boolean initCacheSystem();

      /**
      * This method is used to check whether a player with provided UUID exists in the storage/
      * @param player The UUID of the player that need to be checked
      * @return boolean If the player exists or not
      * TODO return {@link java.util.concurrent.CompletableFuture} instead of plain boolean
      */
      boolean containsStorageForPlayer(@NotNull UUID player);

      /**
      * Add the data for the player if the storage does not have the player registered
      * The no of kills should be default to 0
      * @param player The UUID of the player that need to be added.
      */
      void insertPlayerData(@NotNull UUID player);

      /**
      * Adds a kill to the bounty player. This method should increment the current value to 1
      * @param player The UUID of the player that need to be added.
      */
      void addBountyKill(@NotNull UUID player);

      /**
      * Sets the kill of a player to a specified amount.
      * This is also called when the player leaves the server to save the data to what he has currently
      * @param player The UUID of the player that need to be set
      * @param kills No of kills
      */
      void setPlayerKill(@NotNull UUID player, int kills);

      /**
      * Remove the kill data from the database
      * NOTE: Didn't added it yet anywhere
      * @param player The UUID of the player that need to be set
      */
      void removePlayerKill(@NotNull UUID player);

      /**
      * Get the no of kills a player has
      * @param player The UUID of the player that need to be set
      * @return int no of kills a player has
      * TODO return {@link java.util.concurrent.CompletableFuture} instead of plain int 
      */
      int getPlayerData(@NotNull UUID player);

      /**
      * Called when the server stops.
      * Can be used to save all the data of the online player and close the active connection if there is any
      */
      void stop();
    ```

  ### 2. The details about a Bounty (No. of kills he has, remaining time )
   
    This stores data about a bounty or specifically [Bounty](https://github.com/SemiVanilla-MC/Bounties/blob/master/src/main/java/com/semivanilla/bounties/model/Bounty.java). It stores player with their **UUID, No. of Kills, Remaining time**.

    This can be implemented by using the interface [CacheImpl](https://github.com/SemiVanilla-MC/Bounties/blob/master/src/main/java/com/semivanilla/bounties/data/cache/CacheImpl.java) located at *com.semivanilla.bounties.data.cache*

    This too have few methods

    ```JAVA
        /**
      * This method is used to initialize the data storage system the plugin features.
      * This method is called after initializing {@link Configuration#loadConfigData()} method. So one can call data even
      * from the configuration
      *
      * NOTE: If the connection fails, one should return false so that the plugin can be properly disabled
      * @return boolean Whether the connection or cache storage was successful
      */
      boolean initCacheSystem();

      /**
      * This method is used to check whether a player with provided UUID exists in the storage/
      * @param playerID The UUID of the player that need to be checked
      * @return boolean If the player exists or not
      * TODO return {@link java.util.concurrent.CompletableFuture} instead of plain boolean
      */
      boolean containsCacheForPlayer(@NotNull UUID playerID);

      /**
      * Add the data for the player if the storage does not have the player registered
      * @param bounty The bounty object
      */
      void insertCache(@NotNull Bounty bounty);

      /**
      * Removes the current bounty of the player
      * @param uuid The UUID of the player
      */
      void removeCache(@NotNull UUID uuid);

      /**
      * Returns an Optional value for bounty
      * @param uuid The UUID of the player
      * @return Optional value for bounty
      * @see {@link Optional}
      * @see {@link Bounty}
      */
      Optional<Bounty> getBounty(@NotNull UUID uuid);

      /**
      * Updates the details of Bounty.
      * This method is called when a player leaves the server
      * @param bounty The bounty object
      */
      void updateBounty(@NotNull Bounty bounty);

      /**
      * Called when the server stops.
      * Can be used to save all the data of the online player and close the active connection if there is any
      */
      void stop();
    ```

* The plugin does not yet have a major API. But it does have an event. `BountyStatusChange`. 
This event gets triggered when a player gets a bounty and when his bounty gets removed. Its results in 2 status enums [BountyStatus ](https://github.com/SemiVanilla-MC/Bounties/blob/master/src/main/java/com/semivanilla/bounties/api/enums/BountyStatus.java)

  #### 1. PLAYER_BECAME_BOUNTY
  When the player becomes a bounty

  #### 2. PLAYER_NO_LONGER_BOUNTY
  When his bounty gets removed

* The plugin also utilizes Bukkits Meta-data. If a player is a bounty, he will have a Metadata of `bounty`.
  The value for the meta-data has been actually written to favour [SquaremapPlayers](https://github.com/SemiVanilla-MC/SquaremapPlayers/tree/bounty-hook). The value results the radius of the player. But still one can check if a player is bounty by 

  ```JAVA
    if(player.hasMetadata("bounty"))
      return true;
    else return false;
  ```

* The plugin uses caching as much as possible. All the configuration values are cached. The player data for a player is cached for around 30 sec even after the player disconnects. Its saved instantly, the plugin reserves a copy of the data in cache to reduce I/O operations for quick reconnects.

## Show your support

Give a ⭐️ if this project helped you!

***
