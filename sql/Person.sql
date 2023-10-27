CREATE TABLE Person (
    ID VARCHAR(50) NOT NULL,
    name VARCHAR(50) NOT NULL,
    dateOfBirth DATE NOT NULL,
    isCitizen BOOLEAN NOT NULL,
    PRIMARY KEY (ID),
    KEY (name)
);