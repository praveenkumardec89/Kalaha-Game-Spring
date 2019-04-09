# Kalaha Game With Spring Boot & Redis Cache

Project is implemented as Rest APIs, UI is not added yet.

### Prerequisites

you need to have a local redis instance running on the system for this project to work
```
docker pull redis:latest
jave 1.8
maven 
```

### Installing

Based on your redis server and port configuration change the parameters on the application.properties

```
# Redis Config
spring.cache.type=redis
spring.redis.host=192.168.99.100
spring.redis.port=32768
```

## Running the tests

Groovy's Spock framework is used for unit testing 
coverage is nowhere close to acceptable level yet ;p
### Functional Explanation
Kalaha game is nothing but a 2 * 6 matrix with a integer count on each cell

----------------------
6| 6 | 6 | 6 | 6 | 6 |
----------------------
6| 6 | 6 | 6 | 6 | 6 |
----------------------

grid is indexed starting [0][0] top left to [1][5] bottom right

game always starts with player_0 who owns row -0
    - unless last stone ends in the big pit
    - the next turn will be player_1
    - this repeats for both players till either side has zero stones in pits.

once you run the spring boot application by default it comoes up on 8090
So 
goto http://localhost:8090/start - POST for starting a game
    -- this will give the game id in the location
    http://localhost:8090/{gameID}/{index}- PUT to make the move
    http://localhost:8090/{gameID} -GET to see the game
    http://localhost:8090/{gameID} - DELETE to delete the game.

Api Response will have the complete game status:
{"id":1,"state":[[0,6,6,6,6,6],
                [7,7,7,7,7,6]],
                "player_0_pit_count":1,
                "player_1_pit_count":0,
                "nextMove":1,
                "wonBy":"",
                "over":false}
## Deployment
On the root directory of the project run below command
mvn spring-boot:run
## License

This project is licensed under the MIT License - see the [LICENSE.md](LICENSE.md) file for details
