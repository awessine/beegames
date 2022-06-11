package com.beegames.Models;

public class User {
    private String name, email, score_2048, score_minesw, score_tictac, score_tetris;


    public User(String name, String email) {
        this.email = email;
        this.name = name;
        this.score_2048 = "0";
        this.score_minesw = "0";
        this.score_tictac = "0";
        this.score_tetris = "0";
    }

    public User(){
        this.email = "";
        this.name = "";
        this.score_2048 = "0";
        this.score_minesw = "0";
        this.score_tictac = "0";
        this.score_tetris = "0";
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getScore_2048() {
        return score_2048;
    }

    public String getScore_minesw() {
        return score_minesw;
    }

    public String getScore_tictac() {
        return score_tictac;
    }

    public String getScore_tetris() {
        return score_tetris;
    }

    public void setScore_tictac(String score_tictac) {
        this.score_tictac = score_tictac;
    }

    public void setScore_2048(String score_2048) {
        this.score_2048 = score_2048;
    }

    public void setScore_minesw(String score_minesw) {
        this.score_minesw = score_minesw;
    }

    public void setScore_tetris(String score_tetris) {
        this.score_tetris = score_tetris;
    }
}
