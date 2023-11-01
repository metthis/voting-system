SELECT
    (
        ID,
        name,
        dateOfBirth,
        isCitizen,
        registartionDate,
        withdrawalDate,
        lostThisElection
    )
FROM
    Person
    INNER JOIN Candidate ON Person.ID = Candidate.PersonID;