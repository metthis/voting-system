SELECT
    (ID, name, dateOfBirth, isCitizen, lastVotedRound)
FROM
    Person
    INNER JOIN Voter ON Person.ID = Voter.PersonID;