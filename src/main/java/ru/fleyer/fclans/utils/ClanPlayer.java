package ru.fleyer.fclans.utils;

public class ClanPlayer {
    private int rank;
    private long jointime;
    private int deposit;

    public ClanPlayer(int rank, int deposit, long jointime) {
        this.rank = rank;
        this.deposit = deposit;
        this.jointime = jointime;
    }

    public int getRank() {
        return this.rank;
    }

    public long getJointime() {
        return this.jointime;
    }

    public int getDeposit() {
        return this.deposit;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public void setJointime(long jointime) {
        this.jointime = jointime;
    }

    public void setDeposit(int deposit) {
        this.deposit = deposit;
    }
}

