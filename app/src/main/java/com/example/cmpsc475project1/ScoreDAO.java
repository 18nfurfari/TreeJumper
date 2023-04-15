package com.example.cmpsc475project1;

import java.util.List;
import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface ScoreDAO {
    @Query("SELECT * FROM scores ORDER BY score COLLATE NOCASE, rowid")
    LiveData<List<ScoreEntity>> getAll();

    @Query("SELECT * FROM scores WHERE name = :name")
    ScoreEntity getByName(String name);

    @Insert
    void insert(ScoreEntity... scores);

    @Update
    void update(ScoreEntity... score);

    @Delete
    void delete(ScoreEntity... score);

    // TODO: delete all runs from certain user
    @Query("DELETE FROM scores WHERE name = :name")
    void deleteAllFromName(String name);
}
