TileBreaker
===========

TileBreaker is an easy to use brick breaker game based off of a classic arcade game known as breakout. UI provides easy navigation, game play allows multiple input methods (touch and accelerometer paddle control), and upgrades allow the game to have a bit more flavor. This game has one continuous level. The goal is to fight off the bricks for as long as you can using the appropriate combination of upgrades to achieve a high score.

Austin Williams contributed to the main screen view, accelerometer control of the paddle, and UI  navigation. The main screen allows you to start a new game, or continue on from your previous game. Midst gameplay, you can seamlessly switch between accelerometer and touch controls for the paddle to optimize your game play strategy. 

Logan implemented the upgrade view, created a drag and drop interface for upgrade selection, implemented “shake to clear” via the device accelerometer to reset selected upgrades,  and helped to fine tune the overall look of the game.

Jonathan created Pause Dialog Fragment and implemented Shared Preferences to use throughout the application. Pause dialog can be used throughout the application when called correctly. Shared Preferences stores the score and upgrades for persistent use. This allows the user to continue from previous use. Storing boolean values gives the Tilebreaker Activity the correct true or false values for paid for in-game bonuses. 

Luke contributed to the majority of gameplay code. He developed the game view, game physics, game play objects (ball,paddle,blocks), and touch control of the paddle.

Everyone contributed to bug fixes and overall optimization of the app.

Known Issues:
Sometimes the ball will go straight through one block and hit the one behind it.
Sometimes when switching between accelerometer and touch paddle control, the paddle jumps awkwardly to your finger.


TileBreaker Demo Video:
https://www.youtube.com/watch?v=5gPddkdWfjE
