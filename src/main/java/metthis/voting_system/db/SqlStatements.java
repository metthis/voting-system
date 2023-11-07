package metthis.voting_system.db;

public class SqlStatements {
    public static final String CREATE_PERSON_TABLE;
    public static final String CREATE_CANDIDATE_TABLE;
    public static final String CREATE_VOTER_TABLE;
    public static final String CREATE_DB_VOTINGSYSTEM;
    public static final String USE_DB_VOTINGSYSTEM;
    public static final String DROP_PERSON_TABLE;
    public static final String DROP_CANDIDATE_TABLE;
    public static final String DROP_VOTER_TABLE;
    public static final String INSERT_PERSON;
    public static final String INSERT_CANDIDATE;
    public static final String INSERT_VOTER;
    public static final String SELECT_ALL_CANDIDATES;
    public static final String SELECT_ALL_VOTERS;

    static {
        CREATE_PERSON_TABLE = """
                CREATE TABLE Person (
                    ID VARCHAR(50) NOT NULL,
                    name VARCHAR(50) NOT NULL,
                    dateOfBirth CHAR(10) NOT NULL,
                    isCitizen BOOLEAN NOT NULL,
                    PRIMARY KEY (ID),
                    KEY (name)
                );
                    """;

        CREATE_CANDIDATE_TABLE = """
                CREATE TABLE Candidate (
                    PersonID VARCHAR(50) NOT NULL,
                    registrationDate CHAR(10) NOT NULL,
                    withdrawalDate CHAR(10),
                    lostThisElection BOOLEAN DEFAULT 0,
                    PRIMARY KEY (PersonID),
                    FOREIGN KEY (PersonID) REFERENCES Person(ID) ON DELETE CASCADE ON UPDATE CASCADE
                );
                    """;

        CREATE_VOTER_TABLE = """
                CREATE TABLE Voter (
                    PersonID VARCHAR(50) NOT NULL,
                    lastVotedRound TINYINT UNSIGNED NOT NULL DEFAULT 0,
                    PRIMARY KEY (PersonID),
                    FOREIGN KEY (PersonID) REFERENCES Person(ID) ON DELETE CASCADE ON UPDATE CASCADE
                );
                    """;

        CREATE_DB_VOTINGSYSTEM = """
                CREATE DATABASE VotingSystem;
                    """;

        USE_DB_VOTINGSYSTEM = """
                USE VotingSystem;
                    """;

        DROP_PERSON_TABLE = """
                DROP TABLE IF EXISTS Person;
                    """;

        DROP_CANDIDATE_TABLE = """
                DROP TABLE IF EXISTS Candidate;
                    """;

        DROP_VOTER_TABLE = """
                DROP TABLE IF EXISTS Voter;
                    """;

        INSERT_PERSON = """
                REPLACE INTO Person (ID, name, dateOfBirth, isCitizen)
                VALUES
                    (?, ?, ?, ?);
                    """;

        INSERT_CANDIDATE = """
                REPLACE INTO Candidate (
                    PersonID,
                    registrationDate,
                    withdrawalDate,
                    lostThisElection
                )
                VALUES
                    (?, ?, ?, ?);
                    """;

        INSERT_VOTER = """
                REPLACE INTO Voter (PersonID, lastVotedRound)
                VALUES
                    (?, ?);
                    """;

        SELECT_ALL_CANDIDATES = """
                SELECT
                    ID,
                    name,
                    dateOfBirth,
                    isCitizen,
                    registrationDate,
                    withdrawalDate,
                    lostThisElection
                FROM
                    Person
                    INNER JOIN Candidate ON Person.ID = Candidate.PersonID;
                    """;

        SELECT_ALL_VOTERS = """
                SELECT
                ID, name, dateOfBirth, isCitizen, lastVotedRound
                FROM
                    Person
                    INNER JOIN Voter ON Person.ID = Voter.PersonID;
                    """;
    }
}
