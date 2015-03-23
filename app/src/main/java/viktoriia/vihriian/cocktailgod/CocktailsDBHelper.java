package viktoriia.vihriian.cocktailgod;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by Администратор on 04.03.2015.
 */

public class CocktailsDBHelper extends SQLiteOpenHelper {

    private static String DB_NAME = "cocktails2";
    private static final int SCHEMA = 1;
    private static String DB_PATH = "/data/data/viktoriia.vihriian.cocktailgod/databases/";

    private SQLiteDatabase database;

    private final Context context;
    static final String TABLE_NAME_1 = "cocktails";
    static final String TABLE_NAME_2 = "images";
    static final String ID = "_id";
    static final String COCKTAIL_NAME = "name";
    static final String INGREDIENTS = "ingredients";
    static final String INSTRUCTIONS = "instructions";
    static final String IMAGE = "url";
    static final String DIFFICULTY = "difficulty";


    public CocktailsDBHelper(Context context) {
        super(context, DB_NAME, null, SCHEMA);
        this.context = context;
        openDataBase();
    }


        public SQLiteDatabase getDb() {
            return database;
        }


        //This piece of code will create a database if it’s not yet created
        public void createDataBase() {
            boolean dbExist = checkDataBase();
            if (!dbExist) {
                this.getReadableDatabase();
                try {
                    copyDataBase();
                } catch (IOException e) {
                    Log.e(this.getClass().toString(), "Copying error");
                    throw new Error("Error copying database!");
                }
            } else {
                Log.i(this.getClass().toString(), "Database already exists");
            }
        }

        //Performing a database existence check
        private boolean checkDataBase() {
            SQLiteDatabase checkDb = null;
            try {                String path = DB_PATH + DB_NAME;
                checkDb = SQLiteDatabase.openDatabase(path, null,
                        SQLiteDatabase.OPEN_READONLY);
            } catch (SQLException e) {
                Log.e(this.getClass().toString(), "Error while checking db");
            }
            //Android doesn’t like resource leaks, everything should
            // be closed
            if (checkDb != null) {
                checkDb.close();
            }
            return checkDb != null;
        }

        //Method for copying the database
        private void copyDataBase() throws IOException {
            //Open a stream for reading from our ready-made database
            //The stream source is located in the assets
            InputStream externalDbStream = context.getAssets().open(DB_NAME);

            //Path to the created empty database on your Android device
            String outFileName = DB_PATH + DB_NAME;

            //Now create a stream for writing the database byte by byte
            OutputStream localDbStream = new FileOutputStream(outFileName);

            //Copying the database
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = externalDbStream.read(buffer)) > 0) {
                localDbStream.write(buffer, 0, bytesRead);
            }
            //Don’t forget to close the streams
            localDbStream.close();
            externalDbStream.close();
        }

        public SQLiteDatabase openDataBase() throws SQLException {
            String path = DB_PATH + DB_NAME;
            if (database == null) {
                createDataBase();
                database = SQLiteDatabase.openDatabase(path, null,
                        SQLiteDatabase.OPEN_READWRITE);
            }
            return database;
        }

        @Override
        public synchronized void close() {
            if (database != null) {
                database.close();
            }
            super.close();
        }
        @Override
        public void onCreate(SQLiteDatabase db) {}
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {}

}
