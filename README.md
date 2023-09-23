# Java2DMMO 🧙‍♂️
Java2DMMO (name is still up for contention) is a massively multiplayer online experience programmed exclusively in Java.
![demo_animation](https://github.com/palanjian/java-2d-mmo/assets/134035492/7019b5ad-af3d-406d-9ee3-1688fa7d103d)

## Features 📚
* **Low-Latency Replication:** Java2DMMO utilizes multithreading to implement replication of movement, game states, and chat between up to 200 players on a single server.
* **User Customization:** Allows for users customize their pet and player sprites, which are then served to all other clients.
* **Dynamic Mapping:** Maps and their corresponding spritesheets and collision sheets are generated and served dynamically, allowing seamless level changes.

## Roadmap 🗺️
Java2DMMO is a personal hobby project. Much is still in the works, including database support, mobs, skills, and more. Working as I go whenever I have the time :)

## Usage 🛠️
1. Begin by cloning the server repository into your local workspace.  
``` git clone https://github.com/palanjian/java-2d-mmo-server.git ```.  
2. Clone this repository (client) to your local machine or download the ZIP archive.  
``` git clone https://github.com/palanjian/java-2d-mmo.git ```.  
3. Ensure port 4000 is open on your local machine and run the server.
4. Run the client, enter a username in the console, and the game window should open automatically.
