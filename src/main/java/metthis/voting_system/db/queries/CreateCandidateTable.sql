CREATE TABLE IF NOT EXISTS Candidate (
    PersonID VARCHAR(50) NOT NULL,
    registartionDate DATE NOT NULL,
    withdrawalDate DATE,
    lostThisElection BOOLEAN DEFAULT 0,
    PRIMARY KEY (PersonID),
    FOREIGN KEY (PersonID) REFERENCES Person(ID) ON DELETE CASCADE ON UPDATE CASCADE
);