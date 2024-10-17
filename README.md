# Larry Crofts Adventures

## Starting the Game
Make sure Java 21 or higher is default. In Eclipse you can change this with Window -> Preferences -> Java -> Installed JREs and tick java-22-openjdk.

1. In Eclipse, go File -> Import -> Existing Projects into Workspace and select the project as root directory.
2. The project should now run from `nz.ac.wgtn.swen225.lc.app.Main`. 

However, there might be issues, here are some troubleshooting fixes.
3. After doing Step 1. right click on larry-crofts-adventures in the Project Explorer and go Properties -> Java Build Path -> Source, and ensure 'Source folders on build path' is set to `larry-crofts-adventures/src`.
4. If 3. doesn't work, try going to Run -> Java Application -> Main, and ensure the Main class is `nz.ac.wgtn.swen225.lc.app.Main`.

## How to Play

- **Objectives**: 
    Alien: dodge them; 
    Key/Treasure: collect them all to unlock the exit
    Door: unlock them with matching keys/buttons
    Laser: Use mirror to reflect them
    Teleport: send you to another Teleport

- **Controls**: 
    'w' for upward, 
    's' for downward, 
    'a' for left, 
    'd' for right,
    'CTRL+1' load level 1,
    'CTRL+2' load level 2,

- **How to Win**: reach the Exist

- **How to lose**: touch the aliens/laser or does not pass the level within the time

- **Other functions**:
    load level (Top left)
    save level (Top left)
    load recording (Top left)
    save recording (Top left)
    stop/resume the game (right panel)
    mute the music (right panel)
    undo (right panel), 
    redo (right panel),
    restart (right panel), 