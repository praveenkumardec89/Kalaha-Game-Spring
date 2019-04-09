package com.kalaha.models;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Getter
@Setter
@ToString
@Data
@Builder
@AllArgsConstructor
public class Game implements Serializable {

    private static final long serialVersionUID = 7156526077883281623L;

    @Id
    @SequenceGenerator(name = "SEQ_GEN", sequenceName = "SEQ_GAME", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_GEN")
    private Long id;
    private int[][] state;
    private int player_0_pit_count;
    private int player_1_pit_count;
    private  int nextMove;
    private boolean isOver;
    private String wonBy;

    public Game() {
        this.state = new int[][]{{6,6,6,6,6,6},{6,6,6,6,6,6}};
        this.player_0_pit_count=0;
        this.player_1_pit_count=0;
        this.nextMove=0;
        this.isOver=false;
        this.wonBy="";
    }

    public int[][] getState() {
        return state;
    }

    public void setState(int[][] state) {
        this.state = state;
    }

    public int getPlayer_0_pit_count() {
        return player_0_pit_count;
    }

    public void setPlayer_0_pit_count(int player_0_pit_count) {
        this.player_0_pit_count = player_0_pit_count;
    }

    public int getPlayer_1_pit_count() {
        return player_1_pit_count;
    }

    public void setPlayer_1_pit_count(int player_1_pit_count) {
        this.player_1_pit_count = player_1_pit_count;
    }

    public int getNextMove() {
        return nextMove;
    }

    public void setNextMove(int nextMove) {
        this.nextMove = nextMove;
    }

    public boolean getIsOver() {
        return this.isOver;
    }

    public void setOver(boolean over) {
        this.isOver = over;
    }

    public String getWonBy() {
        return this.wonBy;
    }

    public void setWonBy(String wonBy) {
        this.wonBy = wonBy;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

}
