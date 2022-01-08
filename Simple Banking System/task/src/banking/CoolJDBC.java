package banking;

import org.sqlite.SQLiteDataSource;

import java.sql.*;

/**
 * класс для работы с базой данных SQLite
 */
public class CoolJDBC {
    private String strDb;
    private Connection connection;

    public CoolJDBC(String strDb) {
        this.strDb = strDb;
    }

    /**
     * метод создает connection
     *
     * @return connection
     * @throws SQLException .
     */
    public Connection init() throws SQLException {
        String url = "jdbc:sqlite:" + strDb;
        SQLiteDataSource dataSource = new SQLiteDataSource();
        dataSource.setUrl(url);
        connection = dataSource.getConnection();
        return connection;
    }

    /**
     * закрывает connection
     *
     * @throws Exception .
     */
    public void close() throws Exception {
        if (connection != null) {
            connection.close();
        }
    }

    /**
     * метод сохраняет карту в базе данных
     *
     * @param card данные карты
     * @throws Exception .
     */
    public void saveCard(Card card) throws Exception {
        try {
            connection = init();
            try (Statement statement = connection.createStatement()) {
                statement.executeUpdate("INSERT INTO  card "
                        + "(number,pin) VALUES ('"
                        + card.getNumberCard()
                        + "','" + card.getPin() + "');");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close();
        }
    }

    /**
     * метод проверяет введенные пользователем данные карты
     *
     * @param card данные карты
     * @return правильно ли введены данные
     * @throws Exception .
     */
    public boolean selectCheckCardAndPin(Card card) throws Exception {
        try {
            connection = init();
            try (Statement statement = connection.createStatement()) {
                try (ResultSet resultSet = statement.executeQuery(
                        "SELECT * FROM card " +
                                "WHERE number = " + card.getNumberCard() +
                                " AND pin = " + card.getPin() + ";")) {
                    if (resultSet.next()) {
                        return true;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close();
        }
        return false;
    }

    /**
     * метод проверяет наличие номера карты в базе данных
     *
     * @param numberCard параметры карты
     * @return есть ли такая карта
     * @throws Exception .
     */
    public boolean selectCheckCard(String numberCard) throws Exception {
        try {
            connection = init();
            try (Statement statement = connection.createStatement()) {
                try (ResultSet resultSet = statement.executeQuery(
                        "SELECT * FROM card " +
                                "WHERE number = " + numberCard +
                                ";")) {
                    if (resultSet.next()) {
                        return true;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close();
        }
        return false;
    }

    /**
     * метод возвращает баланс карты
     *
     * @param card параметры карты
     * @return баланс карты
     * @throws Exception .
     */
    public int selectBalance(Card card) throws Exception {
        int res = 0;
        try {
            connection = init();
            try (Statement statement = connection.createStatement()) {
                try (ResultSet resultSet = statement.executeQuery(
                        "SELECT * FROM card " +
                                "WHERE number = " + card.getNumberCard() + ";")) {
                    res = resultSet.getInt("balance");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close();
        }
        return res;
    }

    public void enrollmentBalance(int sum, Card card) throws Exception {
        String updateStr = "UPDATE card SET balance = balance + ? WHERE number = ?";
        try {
            connection = init();
            try (PreparedStatement preparedStatement = connection.prepareStatement(updateStr)) {
                preparedStatement.setInt(1, sum);
                preparedStatement.setString(2, card.getNumberCard());
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close();
        }
    }

    /**
     * метод создает таблицу card
     *
     * @throws Exception .
     */
    public void creatSchema() throws Exception {
        try {
            connection = init();
            try (Statement statement = connection.createStatement()) {
                statement.executeUpdate("CREATE TABLE IF NOT EXISTS card (" +
                        "id INTEGER NOT NULL," +
                        "number TEXT NOT NULL UNIQUE," +
                        "pin TEXT NOT NULL," +
                        "balance tINTEGER DEFAULT 0," +
                        "PRIMARY KEY(id AUTOINCREMENT)" +
                        ");");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close();
        }
    }

    /**
     * метод удаляет аккаунт
     *
     * @param card параметры карты
     * @throws Exception .
     */
    public void deleteAccount(Card card) throws Exception {
        String delete = "DELETE FROM card WHERE number = ?";
        try {
            connection = init();
            try (PreparedStatement preparedStatement = connection.prepareStatement(delete)) {
                preparedStatement.setString(1, card.getNumberCard());
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close();
        }
    }

    /**
     * трансфер средств на другой счет
     *
     * @param numberTransfer номер счета получателя
     * @param card           параметры счета списания
     * @param sum            сумма списания-зачисления
     * @throws SQLException .
     */
    public void transferMoney(String numberTransfer, Card card, int sum) throws SQLException {
        String updateBalanceMyAccount = "UPDATE card SET balance = balance - ? WHERE number = ?";
        String updateBalanceTransferAccount =
                "UPDATE card SET balance = balance + ? WHERE number = ?";
        Savepoint savepoint = null;
        try {
            connection = init();
            connection.setAutoCommit(false);
            savepoint = connection.setSavepoint();
            try (PreparedStatement updateMy = connection.prepareStatement(updateBalanceMyAccount);
                 PreparedStatement updateTransfer =
                         connection.prepareStatement(updateBalanceTransferAccount)) {
                updateMy.setInt(1, sum);
                updateMy.setString(2, card.getNumberCard());
                updateTransfer.setInt(1, sum);
                updateTransfer.setString(2, numberTransfer);
                updateMy.executeUpdate();
                updateTransfer.executeUpdate();
                connection.commit();
            }
        } catch (SQLException e) {
            connection.rollback(savepoint);
        }
    }
}
