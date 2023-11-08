package metthis.voting_system.db;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import metthis.voting_system.persons.Candidate;
import metthis.voting_system.persons.CandidateRegister;
import metthis.voting_system.persons.Voter;
import metthis.voting_system.persons.VoterRegister;

public class DbTests {
    // TODO: Check whether MySQL is running
    private static boolean MYSQL_ALREADY_RUNNING = true;

    // TODO: Wait until MySQL starts/stop running, not just 10 seconds
    @BeforeAll
    static void startMySql() throws IOException, InterruptedException {
        if (MYSQL_ALREADY_RUNNING) {
            return;
        }
        Runtime.getRuntime().exec("brew services start mysql");
        Thread.sleep(10000);
    }

    @AfterAll
    static void stopMySql() throws IOException, InterruptedException {
        if (MYSQL_ALREADY_RUNNING) {
            return;
        }
        Runtime.getRuntime().exec("brew services stop mysql");
        Thread.sleep(10000);
    }

    @Nested
    class New {
        @Test
        void canInitConnectedToServer() throws SQLException {
            Db server = new Db();
            server.close();
        }

        @Nested
        class ConnectedToDatabase {
            @BeforeEach
            void createDatabase() throws SQLException {
                Db server = new Db();
                server.getConn().createStatement()
                        .execute("CREATE DATABASE TestDB;");
                server.close();
            }

            @AfterEach
            void dropDatabase() throws SQLException {
                Db server = new Db();
                server.getConn().createStatement()
                        .execute("DROP DATABASE TestDB;");
                server.close();
            }

            @Test
            void canInitConnectedToDatabase() throws SQLException {
                Db db = new Db("TestDB");
                db.close();
            }

            @Test
            void canInitWithFullDetails() throws SQLException {
                Db db = new Db("jdbc:mysql://localhost:3306/TestDB", "root", "");
                db.close();
            }
        }
    }

    @Nested
    class WhenNew {
        private Db db;

        @BeforeAll
        static void createDatabase() throws SQLException {
            Db server = new Db();
            server.getConn().createStatement()
                    .execute("CREATE DATABASE TestDB;");
            server.close();
        }

        @AfterAll
        static void dropDatabase() throws SQLException {
            Db server = new Db();
            server.getConn().createStatement()
                    .execute("DROP DATABASE TestDB;");
            server.close();
        }

        @BeforeEach
        void initDb() throws SQLException {
            db = new Db("TestDB");
        }

        @AfterEach
        void closeDb() throws SQLException {
            db.close();
        }

        @Test
        void getConnReturnsOpenConnection() throws SQLException {
            assertFalse(db.getConn().isClosed());
        }

        @Test
        void closeClosesConn() throws SQLException {
            db.close();
            assertTrue(db.getConn().isClosed());
        }

        static void dropTable(Db db, String tableName)
                throws SQLException {
            // Doesn't protect against SQL injection but the table name only comes
            // from the test code (not from outside input) so it isn't an issue.
            String sql = "DROP TABLE IF EXISTS ?;".replace("?", tableName);
            db.getConn().createStatement().execute(sql);
        }

        static int getCountOfTablesWithName(Db db, String dbName, String tableName)
                throws SQLException {
            PreparedStatement check = db.getConn().prepareStatement(
                    """
                            SELECT COUNT(*)
                            FROM information_schema.tables
                            WHERE table_schema = ?
                                AND table_name = ?;
                            """);
            check.setString(1, dbName);
            check.setString(2, tableName);
            ResultSet rs = check.executeQuery();
            rs.next();
            return rs.getInt(1);
        }

        static int getCountOfTablesWithName(Db db, String tableName)
                throws SQLException {
            return getCountOfTablesWithName(db, "TestDB", tableName);
        }

        static void checkTableDoesntExist(Db db, String tableName)
                throws SQLException {
            assertEquals(0, getCountOfTablesWithName(db, tableName));
        }

        static void dropTableAndCheckItDoesntExist(Db db, String tableName)
                throws SQLException {
            dropTable(db, tableName);
            checkTableDoesntExist(db, tableName);
        }

        @Nested
        class CreatePersonTable {
            @BeforeEach
            @AfterEach
            void dropTable() throws SQLException {
                WhenNew.dropTableAndCheckItDoesntExist(db, "Person");
            }

            @Test
            void createPersonTableCreatesTableCalledPerson() throws SQLException {
                db.createPersonTable();
                assertEquals(1, getCountOfTablesWithName(db, "Person"));
            }
        }

        @Nested
        class CreateCandidateTable {
            @BeforeEach
            void dropTablesAndCreatePersonTable() throws SQLException {
                WhenNew.dropTableAndCheckItDoesntExist(db, "Candidate");
                WhenNew.dropTableAndCheckItDoesntExist(db, "Person");
                db.createPersonTable();
            }

            @AfterEach
            void dropTables() throws SQLException {
                WhenNew.dropTableAndCheckItDoesntExist(db, "Candidate");
                WhenNew.dropTableAndCheckItDoesntExist(db, "Person");
            }

            @Test
            void createCandidateTableCreatesTableCalledCandidate() throws SQLException {
                db.createCandidateTable();
                assertEquals(1, getCountOfTablesWithName(db, "Candidate"));
            }
        }

        @Nested
        class CreateVoterTable {
            @BeforeEach
            void dropTablesAndCreatePersonTable() throws SQLException {
                WhenNew.dropTableAndCheckItDoesntExist(db, "Voter");
                WhenNew.dropTableAndCheckItDoesntExist(db, "Person");
                db.createPersonTable();
            }

            @AfterEach
            void dropTables() throws SQLException {
                WhenNew.dropTableAndCheckItDoesntExist(db, "Voter");
                WhenNew.dropTableAndCheckItDoesntExist(db, "Person");
            }

            @Test
            void createVoterTableCreatesTableCalledVoter() throws SQLException {
                db.createVoterTable();
                assertEquals(1, getCountOfTablesWithName(db, "Voter"));
            }
        }

        static int getCountOfEntriesInTable(Db db, String dbName, String tableName)
                throws SQLException {
            // Doesn't protect against SQL injection but the table name only comes
            // from the test code (not from outside input) so it isn't an issue.
            ResultSet rs = db.getConn().createStatement().executeQuery(
                    "SELECT COUNT(*) FROM ?;".replace("?", tableName));
            rs.next();
            return rs.getInt(1);
        }

        static int getCountOfEntriesInTable(Db db, String tableName)
                throws SQLException {
            return getCountOfEntriesInTable(db, "TestDB", tableName);
        }

        // The following classes use application code to test
        // other application code. I should look into mocks
        // to decouple the pieces of code from each other.

        @Nested
        class InsertCandidateAndStoreAllCandidates {
            @BeforeEach
            void createTables() throws SQLException {
                db.createPersonTable();
                db.createCandidateTable();
            }

            @AfterEach
            void dropTables() throws SQLException {
                dropTableAndCheckItDoesntExist(db, "Candidate");
                dropTableAndCheckItDoesntExist(db, "Person");
            }

            @Test
            void insertingOneCandidateCreatesOneEntryInTables() throws SQLException {
                String name = "name";
                String ID = "id1";
                String dateOfBirth = "1960-01-01";
                boolean isCitizen = true;
                String registrationDate = "2020-06-02";

                Candidate candidate = new Candidate(name, ID, dateOfBirth, isCitizen, registrationDate);

                db.insertCandidate(candidate);

                assertEquals(1, getCountOfEntriesInTable(db, "Person"));
                assertEquals(1, getCountOfEntriesInTable(db, "Candidate"));
            }

            @Test
            void candidateIsInsertedCorrectly() throws SQLException {
                String name = "name1";
                String ID = "id1";
                String dateOfBirth = "1960-01-01";
                boolean isCitizen = true;
                String registrationDate = "2020-06-02";
                String withdrawalDate = null;
                boolean lostThisElection = true;

                Candidate candidate = new Candidate(name, ID, dateOfBirth, isCitizen, registrationDate);
                candidate.setLostThisElection(lostThisElection);

                db.insertCandidate(candidate);

                ResultSet rs = db.selectAllCandidates();
                assertTrue(rs.next());
                assertEquals(name, rs.getString("name"));
                assertEquals(ID, rs.getString("ID"));
                assertEquals(dateOfBirth, rs.getString("dateOfBirth"));
                assertEquals(isCitizen, rs.getBoolean("isCitizen"));
                assertEquals(registrationDate, rs.getString("registrationDate"));
                assertEquals(withdrawalDate, rs.getString("withdrawalDate"));
                assertEquals(lostThisElection, rs.getBoolean("lostThisElection"));
            }

            @Test
            void twoDistinctCandidatesWithDifferentIdsAreInsertedCorrectly()
                    throws SQLException {
                String name1 = "name1";
                String ID1 = "id1";
                String dateOfBirth1 = "1960-01-01";
                boolean isCitizen1 = true;
                String registrationDate1 = "2020-06-02";
                String withdrawalDate1 = null;
                boolean lostThisElection1 = true;

                String name2 = "name2";
                String ID2 = "id2";
                String dateOfBirth2 = "1989-12-05";
                boolean isCitizen2 = false;
                String registrationDate2 = "2020-01-10";
                String withdrawalDate2 = "2020-01-30";
                boolean lostThisElection2 = false;

                Candidate candidate1 = new Candidate(name1, ID1, dateOfBirth1, isCitizen1, registrationDate1);
                candidate1.setLostThisElection(lostThisElection1);

                Candidate candidate2 = new Candidate(name2, ID2, dateOfBirth2, isCitizen2, registrationDate2);
                candidate2.withdraw(withdrawalDate2);
                candidate2.setLostThisElection(lostThisElection2);

                db.insertCandidate(candidate1);
                db.insertCandidate(candidate2);

                ResultSet rs = db.selectAllCandidates();

                assertTrue(rs.next());
                assertEquals(name1, rs.getString("name"));
                assertEquals(ID1, rs.getString("ID"));
                assertEquals(dateOfBirth1, rs.getString("dateOfBirth"));
                assertEquals(isCitizen1, rs.getBoolean("isCitizen"));
                assertEquals(registrationDate1, rs.getString("registrationDate"));
                assertEquals(withdrawalDate1, rs.getString("withdrawalDate"));
                assertEquals(lostThisElection1, rs.getBoolean("lostThisElection"));

                assertTrue(rs.next());
                assertEquals(name2, rs.getString("name"));
                assertEquals(ID2, rs.getString("ID"));
                assertEquals(dateOfBirth2, rs.getString("dateOfBirth"));
                assertEquals(isCitizen2, rs.getBoolean("isCitizen"));
                assertEquals(registrationDate2, rs.getString("registrationDate"));
                assertEquals(withdrawalDate2, rs.getString("withdrawalDate"));
                assertEquals(lostThisElection2, rs.getBoolean("lostThisElection"));
            }

            @Test
            void candidateIsReplacedCorrectly()
                    throws SQLException {
                String name1 = "name1";
                String ID1 = "id";
                String dateOfBirth1 = "1960-01-01";
                boolean isCitizen1 = true;
                String registrationDate1 = "2020-06-02";
                boolean lostThisElection1 = true;

                String name2 = "name2";
                String ID2 = "id";
                String dateOfBirth2 = "1989-12-05";
                boolean isCitizen2 = false;
                String registrationDate2 = "2020-01-10";
                String withdrawalDate2 = "2020-01-30";
                boolean lostThisElection2 = false;

                Candidate originalCandidate = new Candidate(name1, ID1, dateOfBirth1, isCitizen1, registrationDate1);
                originalCandidate.setLostThisElection(lostThisElection1);

                Candidate newCandidate = new Candidate(name2, ID2, dateOfBirth2, isCitizen2, registrationDate2);
                newCandidate.withdraw(withdrawalDate2);
                newCandidate.setLostThisElection(lostThisElection2);

                db.insertCandidate(originalCandidate);
                db.insertCandidate(newCandidate);

                assertEquals(1, getCountOfEntriesInTable(db, "Person"));
                assertEquals(1, getCountOfEntriesInTable(db, "Candidate"));

                ResultSet rs = db.selectAllCandidates();
                assertTrue(rs.next());
                assertEquals(name2, rs.getString("name"));
                assertEquals(ID2, rs.getString("ID"));
                assertEquals(dateOfBirth2, rs.getString("dateOfBirth"));
                assertEquals(isCitizen2, rs.getBoolean("isCitizen"));
                assertEquals(registrationDate2, rs.getString("registrationDate"));
                assertEquals(withdrawalDate2, rs.getString("withdrawalDate"));
                assertEquals(lostThisElection2, rs.getBoolean("lostThisElection"));
            }

            @Test
            void storeAllCandidatesWorksWithEmptyCandidateRegister()
                    throws SQLException {
                CandidateRegister register = new CandidateRegister();
                db.storeAllCandidates(register);

                assertEquals(0, getCountOfEntriesInTable(db, "Person"));
                assertEquals(0, getCountOfEntriesInTable(db, "Candidate"));
            }

            @Test
            void storeAllCandidatesStoresThemCorrectly()
                    throws SQLException {
                String name1 = "name1";
                String ID1 = "id1";
                String dateOfBirth1 = "1960-01-01";
                boolean isCitizen1 = true;
                String registrationDate1 = "2020-06-02";
                String withdrawalDate1 = null;
                boolean lostThisElection1 = true;

                String name2 = "name2";
                String ID2 = "id2";
                String dateOfBirth2 = "1989-12-05";
                boolean isCitizen2 = false;
                String registrationDate2 = "2020-01-10";
                String withdrawalDate2 = "2020-01-30";
                boolean lostThisElection2 = false;

                Candidate candidate1 = new Candidate(name1, ID1, dateOfBirth1, isCitizen1, registrationDate1);
                candidate1.setLostThisElection(lostThisElection1);

                Candidate candidate2 = new Candidate(name2, ID2, dateOfBirth2, isCitizen2, registrationDate2);
                candidate2.withdraw(withdrawalDate2);
                candidate2.setLostThisElection(lostThisElection2);

                CandidateRegister register = new CandidateRegister();
                register.addIfAbsent(candidate1);
                register.addIfAbsent(candidate2);

                db.storeAllCandidates(register);

                assertEquals(2, getCountOfEntriesInTable(db, "Person"));
                assertEquals(2, getCountOfEntriesInTable(db, "Candidate"));

                ResultSet rs = db.selectAllCandidates();

                assertTrue(rs.next());
                assertEquals(name1, rs.getString("name"));
                assertEquals(ID1, rs.getString("ID"));
                assertEquals(dateOfBirth1, rs.getString("dateOfBirth"));
                assertEquals(isCitizen1, rs.getBoolean("isCitizen"));
                assertEquals(registrationDate1, rs.getString("registrationDate"));
                assertEquals(withdrawalDate1, rs.getString("withdrawalDate"));
                assertEquals(lostThisElection1, rs.getBoolean("lostThisElection"));

                assertTrue(rs.next());
                assertEquals(name2, rs.getString("name"));
                assertEquals(ID2, rs.getString("ID"));
                assertEquals(dateOfBirth2, rs.getString("dateOfBirth"));
                assertEquals(isCitizen2, rs.getBoolean("isCitizen"));
                assertEquals(registrationDate2, rs.getString("registrationDate"));
                assertEquals(withdrawalDate2, rs.getString("withdrawalDate"));
                assertEquals(lostThisElection2, rs.getBoolean("lostThisElection"));
            }
        }

        @Nested
        class InsertVoterAndStoreAllVoters {
            @BeforeEach
            void createTables() throws SQLException {
                db.createPersonTable();
                db.createVoterTable();
            }

            @AfterEach
            void dropTables() throws SQLException {
                dropTableAndCheckItDoesntExist(db, "Candidate");
                dropTableAndCheckItDoesntExist(db, "Voter");
            }

            @Test
            void insertingOneVoterCreatesOneEntryInTables() throws SQLException {
                String name = "name";
                String ID = "id1";
                String dateOfBirth = "1960-01-01";
                boolean isCitizen = true;

                Voter voter = new Voter(name, ID, dateOfBirth, isCitizen);

                db.insertVoter(voter);

                assertEquals(1, getCountOfEntriesInTable(db, "Person"));
                assertEquals(1, getCountOfEntriesInTable(db, "Voter"));
            }

            @Test
            void voterIsInsertedCorrectly() throws SQLException {
                String name = "name1";
                String ID = "id1";
                String dateOfBirth = "1960-01-01";
                boolean isCitizen = true;
                int lastVotedRound = 0;

                Voter voter = new Voter(name, ID, dateOfBirth, isCitizen);
                voter.setLastVotedRound(lastVotedRound);

                db.insertVoter(voter);

                ResultSet rs = db.selectAllVoters();
                assertTrue(rs.next());
                assertEquals(name, rs.getString("name"));
                assertEquals(ID, rs.getString("ID"));
                assertEquals(dateOfBirth, rs.getString("dateOfBirth"));
                assertEquals(isCitizen, rs.getBoolean("isCitizen"));
                assertEquals(lastVotedRound, rs.getInt("lastVotedRound"));
            }

            @Test
            void twoDistinctVotersWithDifferentIdsAreInsertedCorrectly()
                    throws SQLException {
                String name1 = "name1";
                String ID1 = "id1";
                String dateOfBirth1 = "1960-01-01";
                boolean isCitizen1 = true;
                int lastVotedRound1 = 0;

                String name2 = "name2";
                String ID2 = "id2";
                String dateOfBirth2 = "1989-12-05";
                boolean isCitizen2 = false;
                int lastVotedRound2 = 2;

                Voter voter1 = new Voter(name1, ID1, dateOfBirth1, isCitizen1);
                voter1.setLastVotedRound(lastVotedRound1);
                Voter voter2 = new Voter(name2, ID2, dateOfBirth2, isCitizen2);
                voter2.setLastVotedRound(lastVotedRound2);

                db.insertVoter(voter1);
                db.insertVoter(voter2);

                ResultSet rs = db.selectAllVoters();

                assertTrue(rs.next());
                assertEquals(name1, rs.getString("name"));
                assertEquals(ID1, rs.getString("ID"));
                assertEquals(dateOfBirth1, rs.getString("dateOfBirth"));
                assertEquals(isCitizen1, rs.getBoolean("isCitizen"));
                assertEquals(lastVotedRound1, rs.getInt("lastVotedRound"));

                assertTrue(rs.next());
                assertEquals(name2, rs.getString("name"));
                assertEquals(ID2, rs.getString("ID"));
                assertEquals(dateOfBirth2, rs.getString("dateOfBirth"));
                assertEquals(isCitizen2, rs.getBoolean("isCitizen"));
                assertEquals(lastVotedRound2, rs.getInt("lastVotedRound"));
            }

            @Test
            void voterIsReplacedCorrectly()
                    throws SQLException {
                String name1 = "name1";
                String ID1 = "id";
                String dateOfBirth1 = "1960-01-01";
                boolean isCitizen1 = true;
                int lastVotedRound1 = 0;

                String name2 = "name2";
                String ID2 = "id";
                String dateOfBirth2 = "1989-12-05";
                boolean isCitizen2 = false;
                int lastVotedRound2 = 2;

                Voter originalVoter = new Voter(name1, ID1, dateOfBirth1, isCitizen1);
                originalVoter.setLastVotedRound(lastVotedRound1);

                Voter newVoter = new Voter(name2, ID2, dateOfBirth2, isCitizen2);
                newVoter.setLastVotedRound(lastVotedRound2);

                db.insertVoter(originalVoter);
                db.insertVoter(newVoter);

                assertEquals(1, getCountOfEntriesInTable(db, "Person"));
                assertEquals(1, getCountOfEntriesInTable(db, "Voter"));

                ResultSet rs = db.selectAllVoters();
                assertTrue(rs.next());
                assertEquals(name2, rs.getString("name"));
                assertEquals(ID2, rs.getString("ID"));
                assertEquals(dateOfBirth2, rs.getString("dateOfBirth"));
                assertEquals(isCitizen2, rs.getBoolean("isCitizen"));
                assertEquals(lastVotedRound2, rs.getInt("lastVotedRound"));
            }

            @Test
            void storeAllVotersWorksWithEmptyVoterRegister()
                    throws SQLException {
                VoterRegister register = new VoterRegister();
                db.storeAllVoters(register);

                assertEquals(0, getCountOfEntriesInTable(db, "Person"));
                assertEquals(0, getCountOfEntriesInTable(db, "Voter"));
            }

            @Test
            void storeAllVotersStoresThemCorrectly()
                    throws SQLException {
                String name1 = "name1";
                String ID1 = "id1";
                String dateOfBirth1 = "1960-01-01";
                boolean isCitizen1 = true;
                int lastVotedRound1 = 0;

                String name2 = "name2";
                String ID2 = "id2";
                String dateOfBirth2 = "1989-12-05";
                boolean isCitizen2 = false;
                int lastVotedRound2 = 2;

                Voter voter1 = new Voter(name1, ID1, dateOfBirth1, isCitizen1);
                voter1.setLastVotedRound(lastVotedRound1);
                Voter voter2 = new Voter(name2, ID2, dateOfBirth2, isCitizen2);
                voter2.setLastVotedRound(lastVotedRound2);

                VoterRegister register = new VoterRegister();
                register.addIfAbsent(voter1);
                register.addIfAbsent(voter2);

                db.storeAllVoters(register);

                assertEquals(2, getCountOfEntriesInTable(db, "Person"));
                assertEquals(2, getCountOfEntriesInTable(db, "Voter"));

                ResultSet rs = db.selectAllVoters();

                assertTrue(rs.next());
                assertEquals(name1, rs.getString("name"));
                assertEquals(ID1, rs.getString("ID"));
                assertEquals(dateOfBirth1, rs.getString("dateOfBirth"));
                assertEquals(isCitizen1, rs.getBoolean("isCitizen"));
                assertEquals(lastVotedRound1, rs.getInt("lastVotedRound"));

                assertTrue(rs.next());
                assertEquals(name2, rs.getString("name"));
                assertEquals(ID2, rs.getString("ID"));
                assertEquals(dateOfBirth2, rs.getString("dateOfBirth"));
                assertEquals(isCitizen2, rs.getBoolean("isCitizen"));
                assertEquals(lastVotedRound2, rs.getInt("lastVotedRound"));
            }
        }

        @Nested
        class SelectAllCandidatesAndLoadAllCandidates {
            private String name1 = "name1";
            private String ID1 = "id1";
            private String dateOfBirth1 = "1960-01-01";
            private boolean isCitizen1 = true;
            private String registrationDate1 = "2020-06-02";
            private String withdrawalDate1 = null;
            private boolean lostThisElection1 = true;

            private String name2 = "name2";
            private String ID2 = "id2";
            private String dateOfBirth2 = "1989-12-05";
            private boolean isCitizen2 = false;
            private String registrationDate2 = "2020-01-10";
            private String withdrawalDate2 = "2020-01-30";
            private boolean lostThisElection2 = false;

            private Candidate candidate1;
            private Candidate candidate2;

            @BeforeEach
            void createTablesAndInsertCandidates() throws SQLException {
                db.createPersonTable();
                db.createCandidateTable();

                candidate1 = new Candidate(name1, ID1, dateOfBirth1, isCitizen1, registrationDate1);
                candidate1.setLostThisElection(lostThisElection1);

                candidate2 = new Candidate(name2, ID2, dateOfBirth2, isCitizen2, registrationDate2);
                candidate2.withdraw(withdrawalDate2);
                candidate2.setLostThisElection(lostThisElection2);

                db.insertCandidate(candidate1);
                db.insertCandidate(candidate2);
            }

            @AfterEach
            void dropTables() throws SQLException {
                dropTableAndCheckItDoesntExist(db, "Candidate");
                dropTableAndCheckItDoesntExist(db, "Voter");
            }

            @Test
            void selectAllCandidatesReturnsCorrectly()
                    throws SQLException {
                ResultSet rs = db.selectAllCandidates();

                assertTrue(rs.next());
                assertEquals(name1, rs.getString("name"));
                assertEquals(ID1, rs.getString("ID"));
                assertEquals(dateOfBirth1, rs.getString("dateOfBirth"));
                assertEquals(isCitizen1, rs.getBoolean("isCitizen"));
                assertEquals(registrationDate1, rs.getString("registrationDate"));
                assertEquals(withdrawalDate1, rs.getString("withdrawalDate"));
                assertEquals(lostThisElection1, rs.getBoolean("lostThisElection"));

                assertTrue(rs.next());
                assertEquals(name2, rs.getString("name"));
                assertEquals(ID2, rs.getString("ID"));
                assertEquals(dateOfBirth2, rs.getString("dateOfBirth"));
                assertEquals(isCitizen2, rs.getBoolean("isCitizen"));
                assertEquals(registrationDate2, rs.getString("registrationDate"));
                assertEquals(withdrawalDate2, rs.getString("withdrawalDate"));
                assertEquals(lostThisElection2, rs.getBoolean("lostThisElection"));

                assertFalse(rs.next());
            }

            @Test
            void loadAllCandidatesReturnsCorrectly()
                    throws SQLException {
                CandidateRegister expected = new CandidateRegister();
                expected.addIfAbsent(candidate1);
                expected.addIfAbsent(candidate2);

                CandidateRegister actual = db.loadAllCandidates();

                assertEquals(expected.getRegister().keySet(), actual.getRegister().keySet());

                Candidate[] expectedCandidates = expected.getRegister()
                        .values().toArray(Candidate[]::new);
                Candidate[] actualCandidates = actual.getRegister()
                        .values().toArray(Candidate[]::new);

                for (int i = 0; i < actualCandidates.length; i++) {
                    Candidate expectedCandidate = expectedCandidates[i];
                    Candidate actualCandidate = actualCandidates[i];

                    assertEquals(expectedCandidate.getName(),
                            actualCandidate.getName());
                    assertEquals(expectedCandidate.getID(),
                            actualCandidate.getID());
                    assertEquals(expectedCandidate.getDateOfBirth(),
                            actualCandidate.getDateOfBirth());
                    assertEquals(expectedCandidate.getIsCitizen(),
                            actualCandidate.getIsCitizen());
                    assertEquals(expectedCandidate.getRegistrationDate(),
                            actualCandidate.getRegistrationDate());
                    assertEquals(expectedCandidate.getWithdrawalDate(),
                            actualCandidate.getWithdrawalDate());
                    assertEquals(expectedCandidate.getLostThisElection(),
                            actualCandidate.getLostThisElection());
                }
            }
        }

        @Nested
        class SelectAllVotersAndLoadAllVoters {
            private String name1 = "name1";
            private String ID1 = "id1";
            private String dateOfBirth1 = "1960-01-01";
            private boolean isCitizen1 = true;
            private int lastVotedRound1 = 0;

            private String name2 = "name2";
            private String ID2 = "id2";
            private String dateOfBirth2 = "1989-12-05";
            private boolean isCitizen2 = false;
            private int lastVotedRound2 = 2;

            private Voter voter1;
            private Voter voter2;

            @BeforeEach
            void createTablesAndInsertVoters() throws SQLException {
                db.createPersonTable();
                db.createVoterTable();

                voter1 = new Voter(name1, ID1, dateOfBirth1, isCitizen1);
                voter1.setLastVotedRound(lastVotedRound1);

                voter2 = new Voter(name2, ID2, dateOfBirth2, isCitizen2);
                voter2.setLastVotedRound(lastVotedRound2);

                db.insertVoter(voter1);
                db.insertVoter(voter2);
            }

            @AfterEach
            void dropTables() throws SQLException {
                dropTableAndCheckItDoesntExist(db, "Candidate");
                dropTableAndCheckItDoesntExist(db, "Voter");
            }

            @Test
            void selectAllVotersReturnsCorrectly()
                    throws SQLException {
                ResultSet rs = db.selectAllVoters();

                assertTrue(rs.next());
                assertEquals(name1, rs.getString("name"));
                assertEquals(ID1, rs.getString("ID"));
                assertEquals(dateOfBirth1, rs.getString("dateOfBirth"));
                assertEquals(isCitizen1, rs.getBoolean("isCitizen"));
                assertEquals(lastVotedRound1, rs.getInt("lastVotedRound"));

                assertTrue(rs.next());
                assertEquals(name2, rs.getString("name"));
                assertEquals(ID2, rs.getString("ID"));
                assertEquals(dateOfBirth2, rs.getString("dateOfBirth"));
                assertEquals(isCitizen2, rs.getBoolean("isCitizen"));
                assertEquals(lastVotedRound2, rs.getInt("lastVotedRound"));

                assertFalse(rs.next());
            }

            @Test
            void loadAllVotersReturnsCorrectly()
                    throws SQLException {
                VoterRegister expected = new VoterRegister();
                expected.addIfAbsent(voter1);
                expected.addIfAbsent(voter2);

                VoterRegister actual = db.loadAllVoters();

                assertEquals(expected.getRegister().keySet(), actual.getRegister().keySet());

                Voter[] expectedVoters = expected.getRegister()
                        .values().toArray(Voter[]::new);
                Voter[] actualVoters = actual.getRegister()
                        .values().toArray(Voter[]::new);

                for (int i = 0; i < actualVoters.length; i++) {
                    Voter expectedVoter = expectedVoters[i];
                    Voter actualVoter = actualVoters[i];

                    assertEquals(expectedVoter.getName(),
                            actualVoter.getName());
                    assertEquals(expectedVoter.getID(),
                            actualVoter.getID());
                    assertEquals(expectedVoter.getDateOfBirth(),
                            actualVoter.getDateOfBirth());
                    assertEquals(expectedVoter.getIsCitizen(),
                            actualVoter.getIsCitizen());
                    assertEquals(expectedVoter.getLastVotedRound(),
                            actualVoter.getLastVotedRound());
                }
            }
        }
    }
}