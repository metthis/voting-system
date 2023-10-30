CREATE TABLE Candidate (
    PersonID VARCHAR(50) NOT NULL,
    registartionDate DATE NOT NULL,
    withdrawalDate DATE,
    lostThisElection BOOLEAN DEFAULT 0,
    PRIMARY KEY (PersonID),
    FOREIGN KEY (PersonID) REFERENCES Person(ID) ON DELETE CASCADE ON UPDATE CASCADE
);

INSERT INTO
    Person (ID, name, dateOfBirth, isCitizen)
VALUES
    ("ID2", "Name1", "1960-12-02", 1);

INSERT INTO
    Candidate (PersonID, registartionDate)
VALUES
    ("ID2", "2022-11-07");