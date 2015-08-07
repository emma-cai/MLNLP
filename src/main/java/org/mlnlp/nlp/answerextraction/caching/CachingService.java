package org.mlnlp.nlp.answerextraction.caching;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.ImmutableMap;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

/**
 * Cache data so that corpus tests can be run more quickly.
 */
public class CachingService {
    // FIXME: Can a lot of this be put into an interface or base class implementation?

    public static final String TABLE_NAME = "parses";

    public static final String COLUMN_1_NAME = "sentence";
    public static final String COLUMN_2_NAME = "parse";

    public static final int COLUMN_1_MAXSIZE_INT = 350;
    private static final String COLUMN_1_MAXSIZE_STRING = Integer.toString(COLUMN_1_MAXSIZE_INT);

    private static final String CREATE_TABLE_STATEMENT = "(id INT PRIMARY KEY AUTO_INCREMENT, " +
            COLUMN_1_NAME + " VARCHAR(" + COLUMN_1_MAXSIZE_STRING + "), " +
            COLUMN_2_NAME + " CLOB," +
            "UNIQUE KEY name (" + COLUMN_1_NAME + "))";

    private static final String INSERT_STATEMENT = "INSERT INTO " + TABLE_NAME +
            " (" + COLUMN_1_NAME + ", " + COLUMN_2_NAME + ") VALUES (?,?)";

    private static final Logger LOG = LoggerFactory.getLogger(CachingService.class);

    private final Cache<String, String> parseCache = CacheBuilder.newBuilder()
            .maximumSize(50000)
            //.expireAfterWrite(3600, TimeUnit.SECONDS)
            .build();

    private static final String TEMP_DIR_PREFIX = "corpusCache";

    private Connection conn = null;

    public void init(String dir) {
        try {
            Class.forName("org.h2.Driver");
            conn = DriverManager.getConnection("jdbc:h2:" + dir + ";DB_CLOSE_DELAY=-1");
        } catch (ClassNotFoundException | SQLException e) {
            LOG.error("Error: Database init error.", e);
        }

        if (! this.isTableExists()) {
            createTable();
        }
        else    {
            loadData();
        }
    }

    /**
     * Clear the current cache, and reload from the database.
     * Operation is undefined if object hasn't already been initialized.
     */
    public void reloadData()   {
        parseCache.invalidateAll();
        loadData();
    }

    public boolean closeConnection() {
        try {
            if (conn != null) {
                conn.commit();
                conn.close();
                return true;
            }
        } catch (SQLException e) {
            LOG.error("Error: Database connection close error.", e);
        }
        return false;
    }

    private void createTable() {
        try {
            Statement statement = conn.createStatement();
            statement.execute("DROP TABLE IF EXISTS " + TABLE_NAME);
            statement.execute("CREATE TABLE IF NOT EXISTS " + TABLE_NAME + CREATE_TABLE_STATEMENT);
        } catch (SQLException e) {
            LOG.error("Error: Database create table error.", e);
            throw new IllegalStateException("Database create table error: " + e.getMessage());
        }
    }

    /**
     * Write the new data to both the in-memory cache and to the database.
     * @param data
     */
    public void put(Map<String, String> data) {

        try {
            PreparedStatement prep = conn.prepareStatement(INSERT_STATEMENT);
            data.entrySet().stream().forEach(entry -> {
                try {

                    // Massage the sentence field.
                    String sentence = validateInput(entry.getKey(), CachingService.COLUMN_1_MAXSIZE_INT);
                    String value = validateInput(entry.getValue(), Integer.MAX_VALUE);

                    // Don't try to insert if the validation failed.
                    if (sentence == null || value == null)  {
                        return;
                    }

                    parseCache.put(sentence, value);

                    prep.setString(1, sentence);
                    prep.setString(2, value);

                    prep.addBatch();
                } catch (SQLException e) {
                    LOG.error("Error: Database insert error.", e);
                    throw new IllegalStateException(e.getMessage());
                }
            });

            LOG.debug("Finished creating prepare statement.");
            prep.executeBatch();

            conn.commit();
        } catch (SQLException e) {
            LOG.error("Error: Insert into database failed.", e);
            throw new IllegalStateException(e.getMessage());
        }
    }


    /**
     * Get the parse from the cache for the given sentence.
     * @param input
     * @return
     *  null if parse is not cached
     */
    public String getParseFromCache(final String input) {
        String sentence = validateInput(input, CachingService.COLUMN_1_MAXSIZE_INT);
        if (sentence == null)   {
            return null;
        }

        String parse = parseCache.getIfPresent(sentence);
        if (parse != null)  {
            parse = parse.replaceAll("''", "'");
        }
        return parse;
    }

    /**
     * Return an immutable copy of the parses.
     * @return
     */
    public ImmutableMap<String, String> getParses( )     {
        return parseCache.getAllPresent(parseCache.asMap().keySet());
    }

    /**
     * Read the database and load the parses into the cache.
     */
    private void loadData() {
        Map<String, String> results = new HashMap<>();

        try {
            ResultSet rs = conn.createStatement().executeQuery("SELECT * FROM " + TABLE_NAME);

            while (rs.next()) {
                results.put(rs.getString(COLUMN_1_NAME), rs.getString(COLUMN_2_NAME));
            }

        } catch (SQLException e) {
            LOG.error("Error: Database query error in loadData().", e);
        }

        parseCache.putAll(results);
    }

    public boolean isTableExists() {
        try {
            ResultSet rs = conn.createStatement().executeQuery("SHOW TABLES;");
            return rs.next();
        } catch (SQLException e) {
            LOG.error("Error: Database query error in isTableExists().", e);
        }

        return false;
    }

    /**
     * Create a temporary directory of the given name which will delete itself and all contained files on normal exit.
     * @param dirName
     * @return
     */
    // FIXME: Put this into a shared util class.
    public static Path getTempDir(String dirName) {

        try {
            final Path tempDir = Files.createTempDirectory(dirName);

            /** Normal deleteOnExit will not delete non-empty directories. */
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                try {
                    FileUtils.deleteDirectory(tempDir.toFile());
                } catch (IOException e) {
                    LOG.warn("Failed to cleanup temp files at ", tempDir, e);
                }
            }));
            return tempDir;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Clean the given input, modifying it in various ways so that it conforms to database expectations.
     * Return null if after cleaning the input still contains non-ascii characters.
     * @param input
     *  the input to validate
     * @param maxLength
     *  the maximum allowable length the input should take; use Integer.MAX_VALUE if it doesn't matter
     * @return
     *  the cleaned version of the input; or null if after basic cleaning the input contains non-ascii characters
     */
    public static String validateInput(String input, int maxLength)   {
        String output = input.replaceAll("'", "''");

        output = output.trim().replaceAll(" +", " ");

        output = AsciiMapper.replace(output);

        if(output.length() > maxLength) {
            output = output.substring(0, maxLength - 1);
        }

        if (AsciiMapper.containsNonAscii(output))   {
            return null;
        }

        return output;
    }


}
