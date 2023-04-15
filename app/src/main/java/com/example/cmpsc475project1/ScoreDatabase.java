package com.example.cmpsc475project1;

import android.content.Context;
import android.os.AsyncTask;
import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.google.android.material.color.utilities.Score;

@Database(entities = {ScoreEntity.class}, version = 1, exportSchema = false)
public abstract class ScoreDatabase extends RoomDatabase {
    private static final String dbName = "score_database";
    private static ScoreDatabase scoreDatabase;

    //void onScoreReturned(ScoreEntity score);

    public abstract ScoreDAO scoreDAO();
    private static ScoreDatabase INSTANCE;

    public static synchronized  ScoreDatabase getScoreDatabase(Context context) {
        if (scoreDatabase == null) {
            scoreDatabase = Room.databaseBuilder(context, ScoreDatabase.class, dbName)
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return scoreDatabase;
    }


    // Error creating callback
    // could not find DefaultContent.NAME (from notes) or ScoreEntity.NAME etc

//    public static ScoreDatabase getDatabase(final Context context) {
//        if (INSTANCE == null) {
//            synchronized (ScoreDatabase.class) {
//                if (INSTANCE == null) {
//                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
//                                    ScoreDatabase.class, "joke_database")
//                            .addCallback(createScoreDatabaseCallback)
//                            .build();
//                }
//            }
//        }
//        return INSTANCE;
//    }
//
//    private static RoomDatabase.Callback createJokeDatabaseCallback =
//            new RoomDatabase.Callback() {
//                public void onCreate(@NonNull SupportSQLiteDatabase db) {
//                    super.onCreate(db);
//                    for (int i = 0; i < ScoreEntity.NAME.length; i++) {
//                        insert(new ScoreEntity(0, ScoreEntity.NAME[i], ScoreEntity.SCORE[i], false));
//                    }
//                }
//            };

    public static void insert(ScoreEntity score) {
        (new Thread( () ->
                INSTANCE.scoreDAO().insert(score))).start();
    }

    public static void delete(ScoreEntity score) {
        (new Thread( () ->
                INSTANCE.scoreDAO().delete(score))).start();
    }

    public static void update(ScoreEntity score) {
        (new Thread( () ->
                INSTANCE.scoreDAO().update(score))).start();
    }
}



