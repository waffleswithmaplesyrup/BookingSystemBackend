package com.example.BookingSystemBackend;

import com.example.BookingSystemBackend.Utils.DatabaseHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Service;

@Service
public class DatabaseLoader implements ApplicationRunner {

    private final DatabaseHelper databaseHelper;

    @Autowired
    public DatabaseLoader(DatabaseHelper databaseHelper) {
        this.databaseHelper = databaseHelper;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {

        databaseHelper.saveUsers();
        databaseHelper.savePackages();
        databaseHelper.saveClasses();
        databaseHelper.savePurchasedPackages();
        databaseHelper.saveBookedClasses();
//        databaseHelper.saveWaitlist();

    }
}
