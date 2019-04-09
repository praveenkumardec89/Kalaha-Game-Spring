package com.kalaha.services

import com.kalaha.models.Game
import com.kalaha.repositories.GameRepository
import org.springframework.beans.factory.annotation.Autowired
import spock.lang.Specification

class GameServiceSpec extends Specification {

    Game game
    @Autowired
    GameRepository gameRepository

    def setup() {
        game = new Game()
        gameRepository = Mock()
    }

    def "Service should accept valid moves"() {
        given:
        GameService gameService = new GameService()

        when:
        def accept = gameService.isValidMove(game, "1", "00")

        then:
        accept

    }

    def "Service should reject invalid moves"() {
        given:
        GameService gameService = new GameService()

        when:
        game.getState()[0][0] = 0
        def accept = gameService.isValidMove(game, "1", "00")

        then:
        !accept

    }

    def "Service should make a valid move"() {
        given:
        GameService gameService = new GameService(gameRepository)

        when:
        gameService.makeAmove(game, "1", "00")

        then:
        1 * gameRepository.save(game)
        game.getPlayer_0_pit_count() == 1
        game.getState()[0][0] == 0

    }
}
