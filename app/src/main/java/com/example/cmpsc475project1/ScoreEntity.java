package com.example.cmpsc475project1;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName="scores")
public class ScoreEntity {
    public ScoreEntity(int id, String name, int score) {
        this.id = id;
        this.name = name;
        this.score = score;
    }

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "rowid")
    public int id;
    @ColumnInfo(name = "name")
    public String name;
    @ColumnInfo(name = "score")
    public int score;
}