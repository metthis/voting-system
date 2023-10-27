CREATE TABLE Voter (
    PersonID VARCHAR(50) NOT NULL,
    lastVotedRound TINYINT UNSIGNED NOT NULL DEFAULT 0,
    PRIMARY KEY (PersonID),
    FOREIGN KEY (PersonID) REFERENCES Person(ID) ON DELETE CASCADE ON UPDATE CASCADE
);

INSERT INTO
    Person (ID, name, dateOfBirth, isCitizen)
VALUES
    ("ID1", "Name1", "1960-12-02", 1);

INSERT INTO
    Voter (PersonID)
VALUES
    ("ID1");