TileBreaker
===========

OVERVIEW:
TileBreaker is an easy to use brick breaker game based off of a classic arcade game known as breakout. UI provides easy navigation, gameplay allows multiple input methods (touch and accelerometer paddle control), and upgrades allow the game to have a lot more flavor and replayability. This game has one continuous level. The goal is to fight off the bricks for as long as you can using the appropriate combination of upgrades to earn your spent money back, and maximize your profit.

SPECIFICATIONS:
There is no min-api stated in the manifest, and we have not run into an api that this app does not run on.  We all have relatively current versions, going as far back as Jellybean.  There are no special packages used, just a vanilla API on a device with moderate to high high pixel count (ldpi renders poorly and in some extreme cases makes the app not functional).

CONTRIBUTIONS:
Austin Williams contributed to the main screen view, accelerometer control of the paddle, and UI navigation. The main screen allows you to start a new game, or continue on from your previous game. Amidst gameplay, you can instantly switch between accelerometer and touch controls for the paddle to optimize your gameplay strategy.

Logan Swango was in charge of the upgrade activity.  He implemented the upgrade view, created a drag and drop interface for upgrade selection, and implemented the “shake to clear” functionality via the device accelerometer to reset selected upgrades.  He also implemented the upgrade pricing system and helped make the game visually appealing.

Jonathan Kilpatrick created Pause Dialog Fragment and implemented Shared Preferences to use throughout the application. Pause dialog can be used throughout the application when called correctly. Shared Preferences stores the score and upgrades for persistent use. This allows the user to continue from the point they left off, even if they close the application. Storing boolean values gives the Tilebreaker Activity the correct true or false values for paid for in-game bonuses.

Luke Brown contributed the majority of the gameplay code. He developed the game view, game physics, game play objects (ball,paddle,blocks), and touch control of the paddle.

Everyone contributed to bug fixes and overall optimization of the app.

KNOWN ISSUES:
Sometimes the ball will go straight through one block and hit the one behind it.
Sometimes when switching between accelerometer and touch paddle control, the paddle jumps awkwardly to your finger.
Infrequent game crashes on previous versions, has not been an issue in the last few updates, but that could be coincidental.

DEMONSTRATION VIDEO:
https://www.youtube.com/watch?v=5gPddkdWfjE