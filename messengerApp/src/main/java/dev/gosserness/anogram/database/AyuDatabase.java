/*
 * This is the source code of AyuGram for Android.
 *
 * We do not and cannot prevent the use of our code,
 * but be respectful and credit the original author.
 *
 * Copyright @Radolyn, 2023
 */

package dev.gosserness.anogram.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import dev.gosserness.anogram.database.dao.DeletedMessageDao;
import dev.gosserness.anogram.database.dao.EditedMessageDao;
import dev.gosserness.anogram.database.entities.DeletedMessage;
import dev.gosserness.anogram.database.entities.DeletedMessageReaction;
import dev.gosserness.anogram.database.entities.EditedMessage;

@Database(entities = {
        EditedMessage.class,
        DeletedMessage.class,
        DeletedMessageReaction.class
}, version = 21)
public abstract class AyuDatabase extends RoomDatabase {
    public abstract EditedMessageDao editedMessageDao();

    public abstract DeletedMessageDao deletedMessageDao();
}