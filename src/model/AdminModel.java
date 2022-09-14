package model;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;

@SuppressWarnings("unused")
public class AdminModel {
    private String username;
    private String password;

    private String firstName;
    private String lastName;
    private String middleName;

    private Date dateCreated;


    private final String file = "src/db/logins.txt";
    private final String delimiter = ",";

    /**
     * Register login credentials
     *
     * @param username username to be registered
     * @param password password to be registered
     */
    public void register(String username, String password) throws IOException {
        FileReader in = new FileReader(file);
        FileWriter out = new FileWriter(file, true);

        try (in; out) {
            out.write(username + delimiter + password);
        }
    }

    /**
     * Check for login credentials
     *
     * @param username username to be validated
     * @param password password to be validated
     * @return `true` if credentials are correct, else `false`
     */
    public boolean login(String username, String password) throws IOException {
        FileReader in = new FileReader(file);
        BufferedReader reader = new BufferedReader(in);

        try (in; reader) {
            String line;

            //noinspection LoopStatementThatDoesntLoop | false positive
            while ((line = reader.readLine()) != null) {
                String[] credentials = line.split(delimiter);

                return (credentials[0].equals(username) && credentials[1].equals(password));
            }
        }

        return false;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
