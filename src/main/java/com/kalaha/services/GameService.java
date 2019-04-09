package com.kalaha.services;




import com.kalaha.models.Game;
import com.kalaha.repositories.GameRepository;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GameService {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final GameRepository gameRepository;
    @Autowired
    public GameService(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    public boolean isValidMove(Game game, String index){
        int row = getInt(index.charAt(0));
        int col = getInt(index.charAt(1));
      return  !game.getIsOver() && isValidIndex(row, col) && game.getState()[row][col] != 0 && game.getNextMove() == (index.charAt(0) - '0');
    }

    private int getInt(char charAt) {
        return charAt - '0';
    }

    private boolean isValidIndex(int row, int col) {
        return (0<= row && row <= 1) && (0<= col && col<=5);
    }

    public void makeAmove(Game game,  String index) {
        int[][] grid = game.getState();
        int r = getInt(index.charAt(0));
        int c = getInt(index.charAt(1));
        int stones = grid[r][c]; // reset the stones to zero
        grid[r][c] = 0;
        String lastIndex;
        int oppIndexStones;
        if (r ==0 ){
            lastIndex = moveForPlayer_0(r, c-1, stones, grid, game);
            logger.info("last index and its stones {}, {}", lastIndex);
            if (lastIndex.length() == 1){//ended in his big pit
                game.setNextMove(0);
            }else {
                int countInLastIndex = grid[getInt(lastIndex.charAt(0))][getInt(lastIndex.charAt(1))];
                if (getInt(lastIndex.charAt(0)) == 0 && countInLastIndex == 1) {//move the opposite box stones to his big pit.
                    oppIndexStones = grid[getInt(lastIndex.charAt(0)) + 1][getInt(lastIndex.charAt(1))];
                    game.setPlayer_0_pit_count(game.getPlayer_0_pit_count() + oppIndexStones);
                    grid[getInt(lastIndex.charAt(0)) + 1][getInt(lastIndex.charAt(1))] = 0; // clear the stones
                }
                game.setNextMove(1);
            }
        } else{
          lastIndex =  moveForPlayer_1(r, c+1, stones, grid, game);
            logger.info("last index and its stones {}, {}", lastIndex);
            if (lastIndex.length() == 1){//ended in his big pit
                game.setNextMove(1);
            }else {
                int countInLastIndex = grid[getInt(lastIndex.charAt(0))][getInt(lastIndex.charAt(1))];
                if (getInt(lastIndex.charAt(0)) == 1 && countInLastIndex == 1) {//move the opposite box stones to his big pit.
                    oppIndexStones = grid[getInt(lastIndex.charAt(0)) - 1][getInt(lastIndex.charAt(1))];
                    game.setPlayer_1_pit_count(game.getPlayer_1_pit_count() + oppIndexStones);
                    grid[getInt(lastIndex.charAt(0)) - 1][getInt(lastIndex.charAt(1))] = 0; //clear the opposite index stones
                }
                game.setNextMove(0);
            }

        }
        gameRepository.save(game);

    }

    private String moveForPlayer_1(int r, int c, int stones, int[][] grid, Game game) {
        while (stones > 0 ){//player 1
           // c++;
            while(c<=5 && stones >0){
                grid[r][c]++;
                c++;
                stones--;
            }
            if (stones == 0) return ""+r+(c-1);
            if (stones > 0){
                game.setPlayer_1_pit_count(game.getPlayer_1_pit_count()+1);
                stones--;
            }
            if (stones == 0) return "r";
            if (c>5 && stones >0) {
                c--;r--;
            }

            while(c>=0 && stones >0){
                grid[r][c]++;
                c--;
                stones--;
            }
            if (stones == 0) return ""+r+(c+1);
            if (c < 0 && stones >0)c++;
            if (r ==0 && stones >0) r++;

        }
       return null;
    }

    private String moveForPlayer_0(int r, int c, int stones, int[][] grid, Game game) {
        while (stones > 0 ){//player 0
          //  c--;
            while(c>=0 && stones >0){
                grid[r][c]++;
                c--;
                stones--;
            }
            if (stones == 0) return ""+r+(c+1);
            if (stones > 0){
                game.setPlayer_0_pit_count(game.getPlayer_0_pit_count()+1);
                stones--;
            }
            if (stones == 0) return "l";
            if (c<0 && stones >0){
                c++;r++;
            }
            while(c<=5 && stones >0){
                grid[r][c]++;
                c++;
                stones--;
            }
            if (stones == 0) return ""+r+(c-1);
            if (c >5 && stones >0)c--;
            if (r ==1 && stones >0) r--;

        }
     //   game.setNextMove(1);

        return null;
    }

    public boolean isGameOver(Game game) {
        int p0_score = game.getPlayer_0_pit_count();
        int p1_score = game.getPlayer_1_pit_count();
        if (p0_score > 36){
            game.setWonBy("Player_0");
            game.setOver(true);
        }else if (p1_score > 36){
            game.setWonBy("Player_1");
            game.setOver(true);
        }
        int[] p0 = game.getState()[0];
        int[] p1 = game.getState()[1];
        int p0_count=0;
        int p1_count=0;
        boolean p0_empty = true;
        boolean p1_empty = true;

            for (int row : p0){
                if (row != 0) {
                    p0_empty = false;
                    break;
                }
            }

            for (int row : p1){
                if (row != 0) {
                    p1_empty = false;
                    break;
                }
            }

          if (p0_empty ){
              for (int row : p1){
                p1_count += row;
              }
              game.setPlayer_1_pit_count(game.getPlayer_1_pit_count() + p1_count);
              setWinner(game);
          }else if (p1_empty){
              for (int row : p0){
                  p0_count += row;
              }
              game.setPlayer_0_pit_count(game.getPlayer_0_pit_count() + p0_count);
              setWinner(game);
          }


        return false;
    }

    private void setWinner(Game game) {
        if(game.getPlayer_0_pit_count() > game.getPlayer_1_pit_count()){
            game.setWonBy("Player_0");
        }else {
            game.setWonBy("Player_1");
        }
        game.setOver(true);
    }
}
