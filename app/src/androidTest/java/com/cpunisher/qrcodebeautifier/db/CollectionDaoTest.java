package com.cpunisher.qrcodebeautifier.db;

import android.content.Context;
import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import com.cpunisher.qrcodebeautifier.db.dao.CollectionDao;
import com.cpunisher.qrcodebeautifier.db.entity.Collection;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class CollectionDaoTest {

    private CollectionDao collectionDao;
    private AppDatabase appDatabase;

    @Before
    public void createDb() {
        Context context = ApplicationProvider.getApplicationContext();
        appDatabase = Room.inMemoryDatabaseBuilder(context, AppDatabase.class).build();
        collectionDao = appDatabase.collectionDao();
    }

    @After
    public void closeDb() {
        appDatabase.close();
    }

    @Test
    public void getCollectionsWhenNoCollectionInserted() {
        List<Collection> collections = collectionDao.getAll();
        assertTrue(collections.isEmpty());
    }

    @Test
    public void getCollectionsAfterInserted() {
        collectionDao.insertStyles(TestData.STYLE);
        List<Collection> collections = collectionDao.getAll();
        assertThat(collections.size(), is(1));
    }
}
