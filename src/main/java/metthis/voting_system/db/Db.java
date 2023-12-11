package metthis.voting_system.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;

import metthis.voting_system.persons.Candidate;
import metthis.voting_system.persons.CandidateRegister;
import metthis.voting_system.persons.Person;
import metthis.voting_system.persons.Voter;
import metthis.voting_system.persons.VoterRegister;

public class Db {
    private Connection conn;

    public Db(String url, String username, String password)
            throws SQLException {
        conn = DriverManager.getConnection(url, username, password);
        conn.setAutoCommit(false);
    }

    public Db(String database) throws SQLException {
        this("jdbc:mysql://localhost:3306/" + database, "root", "");
    }

    public Db() throws SQLException {
        this("");
    }

    public Connection getConn() {
        return conn;
    }

    public void close() throws SQLException {
        conn.close();
    }

    private void executeStatements(String... statements) throws SQLException {
        try {
            Statement stmt = conn.createStatement();

            for (String statement : statements) {
                stmt.addBatch(statement);
            }

            stmt.executeBatch();
            conn.commit();
        } catch (SQLException e) {
            conn.rollback();
            throw e;
        }
    }

    public void createPersonTable() throws SQLException {
        executeStatements(
                SqlStatements.DROP_PERSON_TABLE,
                SqlStatements.CREATE_PERSON_TABLE);
    }

    public void createCandidateTable() throws SQLException {
        executeStatements(
                SqlStatements.DROP_CANDIDATE_TABLE,
                SqlStatements.CREATE_CANDIDATE_TABLE);
    }

    public void createVoterTable() throws SQLException {
        executeStatements(
                SqlStatements.DROP_VOTER_TABLE,
                SqlStatements.CREATE_VOTER_TABLE);
    }

    private static <P extends Person> void insertPersonSetParameters(
            PreparedStatement pstmt,
            P person)
            throws SQLException {
        pstmt.setString(1, person.getId());
        pstmt.setString(2, person.getName());
        pstmt.setString(3, person.getDateOfBirth().toString());
        pstmt.setBoolean(4, person.getIsCitizen());
    }

    private static void insertCandidateSetParameters(
            PreparedStatement pstmt,
            Candidate candidate)
            throws SQLException {
        pstmt.setString(1, candidate.getId());
        pstmt.setString(2, candidate.getRegistrationDate().toString());
        if (candidate.getWithdrawalDate() != null) {
            pstmt.setString(3, candidate.getWithdrawalDate().toString());
        } else {
            pstmt.setNull(3, Types.NULL);
        }
        pstmt.setBoolean(4, candidate.getLostThisElection());
    }

    private static void insertVoterSetParameters(
            PreparedStatement pstmt,
            Voter voter)
            throws SQLException {
        pstmt.setString(1, voter.getId());
        pstmt.setInt(2, voter.getLastVotedRound());
    }

    public void insertCandidate(Candidate candidate) throws SQLException {
        PreparedStatement pstmt1 = conn.prepareStatement(
                SqlStatements.INSERT_PERSON);
        PreparedStatement pstmt2 = conn.prepareStatement(
                SqlStatements.INSERT_CANDIDATE);

        try {
            insertPersonSetParameters(pstmt1, candidate);
            insertCandidateSetParameters(pstmt2, candidate);

            pstmt1.executeUpdate();
            pstmt2.executeUpdate();

            conn.commit();
        } catch (SQLException e) {
            conn.rollback();
            throw e;
        }
    }

    public void insertVoter(Voter voter) throws SQLException {
        PreparedStatement pstmt1 = conn.prepareStatement(
                SqlStatements.INSERT_PERSON);
        PreparedStatement pstmt2 = conn.prepareStatement(
                SqlStatements.INSERT_VOTER);

        try {
            insertPersonSetParameters(pstmt1, voter);
            insertVoterSetParameters(pstmt2, voter);

            pstmt1.executeUpdate();
            pstmt2.executeUpdate();

            conn.commit();
        } catch (SQLException e) {
            conn.rollback();
            throw e;
        }
    }

    public void storeAllCandidates(CandidateRegister candidates)
            throws SQLException {
        if (candidates.isEmpty()) {
            return;
        }

        PreparedStatement pstmt1 = conn.prepareStatement(
                SqlStatements.INSERT_PERSON);
        PreparedStatement pstmt2 = conn.prepareStatement(
                SqlStatements.INSERT_CANDIDATE);

        for (Candidate candidate : candidates.getRegister().values()) {
            try {
                insertPersonSetParameters(pstmt1, candidate);
                pstmt1.addBatch();

                insertCandidateSetParameters(pstmt2, candidate);
                pstmt2.addBatch();
            } catch (SQLException e) {
                throw e;
            }
        }

        try {
            pstmt1.executeBatch();
            pstmt2.executeBatch();
            conn.commit();
        } catch (SQLException e) {
            conn.rollback();
            throw e;
        }
    }

    public void storeAllVoters(VoterRegister voters)
            throws SQLException {
        if (voters.isEmpty()) {
            return;
        }

        PreparedStatement pstmt1 = conn.prepareStatement(
                SqlStatements.INSERT_PERSON);
        PreparedStatement pstmt2 = conn.prepareStatement(
                SqlStatements.INSERT_VOTER);

        for (Voter voter : voters.getRegister().values()) {
            try {
                insertPersonSetParameters(pstmt1, voter);
                pstmt1.addBatch();

                insertVoterSetParameters(pstmt2, voter);
                pstmt2.addBatch();
            } catch (SQLException e) {
                throw e;
            }
        }

        try {
            pstmt1.executeBatch();
            pstmt2.executeBatch();
            conn.commit();
        } catch (SQLException e) {
            conn.rollback();
            throw e;
        }
    }

    private ResultSet selectAll(String query) throws SQLException {
        Statement stmt = conn.createStatement();
        stmt.execute(query);
        return stmt.getResultSet();
    }

    public ResultSet selectAllCandidates() throws SQLException {
        return selectAll(SqlStatements.SELECT_ALL_CANDIDATES);
    }

    public ResultSet selectAllVoters() throws SQLException {
        return selectAll(SqlStatements.SELECT_ALL_VOTERS);
    }

    public CandidateRegister loadAllCandidates() throws SQLException {
        CandidateRegister register = new CandidateRegister();
        ResultSet rs = selectAllCandidates();

        while (rs.next()) {
            Candidate candidate = new Candidate(
                    rs.getString("name"),
                    rs.getString("ID"),
                    rs.getString("dateOfBirth"),
                    rs.getBoolean("isCitizen"),
                    rs.getString("registrationDate"));
            String withdrawalDate = rs.getString("withdrawalDate");
            if (withdrawalDate != null) {
                candidate.setWithdrawalDate(withdrawalDate);
            }
            candidate.setLostThisElection(rs.getBoolean("lostThisElection"));

            register.addIfAbsent(candidate);
        }

        rs.close();
        return register;
    }

    public VoterRegister loadAllVoters() throws SQLException {
        VoterRegister register = new VoterRegister();
        ResultSet rs = selectAllVoters();

        while (rs.next()) {
            Voter voter = new Voter(
                    rs.getString("name"),
                    rs.getString("ID"),
                    rs.getString("dateOfBirth"),
                    rs.getBoolean("isCitizen"));
            voter.setLastVotedRound(rs.getInt("lastVotedRound"));

            register.addIfAbsent(voter);
        }

        rs.close();
        return register;
    }
}