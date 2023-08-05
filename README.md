# Dungeon Cracker
Updated version of the dungeon cracking code by Neil and Kinomora. Allows reversing dungeon floors that contain air blocks.

Supported versions: 1.13 - 1.17

# Installing the cracker
- Download latest release
- To run the cracker, either double click the jar file or type the following in the command prompt:

java -jar UpdatedDungeonCracker-v1_0_0.jar

# Usage instructions

# GUI - dungeon floor tiles
<img src= "https://github.com/Kludwisz/DungeonCracker/assets/131913157/8ea08c2b-696b-46f5-beff-f5f2fe98a953" width=20% height=20%>

**UNKNOWN SOLID** - The default floor tile. Used to mark a block that generated as either cobblestone or mossy cobblestone.

<img src= "https://github.com/Kludwisz/DungeonCracker/assets/131913157/24dfa717-a79b-45c7-90ce-e6864687b95a" width=20% height=20%>

**MOSSY COBBLESTONE** - Used to mark a block that generated as mossy cobblestone. Yields around 0.4 bits of information.

<img src= "https://github.com/Kludwisz/DungeonCracker/assets/131913157/b7455eea-9515-4664-8f8f-fabe5a9e2e95" width=20% height=20%>

**COBBLESTONE** - Used to mark a block that generated as cobblestone. This is the most vital information type, as it yields 2 bits of information

<img src= "https://github.com/Kludwisz/DungeonCracker/assets/131913157/ec1e9e4e-79ea-49ae-a8c8-c61b21e2b56a" width=20% height=20%>

**AIR** - Used to mark a block that generated as air. 

<img src= "https://github.com/Kludwisz/DungeonCracker/assets/131913157/26354794-f0bb-4175-b4cc-c1e0da8e616e" width=20% height=20%>

**UNKNOWN** - Used to mark blocks that could have either generated as air or a solid block. 
WARNING! "Unknown" markers multiply the amount of tasks to be proccessed and should only be used if a combination of the other 4 markers failed to reverse the seed.
