=========

## Tic Tac Toe REST app (prototype) 
### Author: Lauri Pekkala 

### Intro
This app is a REST API for playing Tic Tac Toe with computer. Computer places tokens randomly so its quite easy to win :)

### Technical info
The code supports grid sizes from 3-9 (also bigger if modified)

### Missing stuff
Misses plenty of tests: There should be eg. mock mvc tests and tests for different game states
This is a proto and refactoring to be done
Very little input validation or error checks
Uses embedded Redis, does not persist on disk 

### Create the Docker container
docker build .

### Run the server
docker run -d -p 8089:8089 [IMAGE-ID]

### Play the game
 - Start new game: 
 
 curl -d '{"name":"Lauri", "character":"X"}'  -H "Accept: application/json" -H "Content-Type: application/json" -X POST http://localhost:8089/start -w "\n"
 
 - Make a move: POST http://localhost:8089/game/[ID]/move
 
 curl -d '{"row":"A", "col":"C"}'  -H "Accept: application/json" -H "Content-Type: application/json" -X POST http://localhost:8089/game/ID/move -w "\n"
 
 - Check the game status: GET http://localhost:8089/game/[GAME-ID]
 
 curl -H "Accept: text/plain" -X GET http://localhost:8089/game/[GAME-ID] -w "\n"

