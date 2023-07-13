package hello.jdbc.exception;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.net.ConnectException;
import java.sql.SQLException;
@Slf4j
public class UnCheckedAppTest {

    @Test
    @DisplayName("런타임 예외 예외 테스트")
    void unChecked_test() {
        Controller controller = new Controller();
        Assertions.assertThatThrownBy(() -> controller.request())
                .isInstanceOf(RuntimeSqlException.class);
    }
    @Test
    @DisplayName("예외 포함과 스택 트레이스")
    void printEx() {
        Controller controller = new Controller();
        try{
            controller.request();
        } catch (Exception e) {
            // e.printStackTrace();
            log.info("ex", e);
        }
    }

    static class Controller{
        Service service = new Service();

        public void request() {
            service.logic();
        }
    }
    static class Service{
        Repository repository = new Repository();
        NetworkClient networkClient = new NetworkClient();

        public void logic() {
            repository.call();
            networkClient.call();
        }
    }
    static class NetworkClient{
        public void call() {
            throw new RuntimeConnectException("연결 실패");
        }
    }
    static class Repository{
        public void call()  {
            try {
                runSQL();
            } catch (SQLException e) {
                throw new RuntimeSqlException(e);
            }
        }

        public void runSQL() throws SQLException {
            throw new SQLException("ex");
        }
    }

    static class RuntimeConnectException extends RuntimeException {
        public RuntimeConnectException(String message) {
            super(message);
        }
    }

    static class RuntimeSqlException extends RuntimeException {
        public RuntimeSqlException(Throwable cause){
            super(cause);
        }

        public RuntimeSqlException() {
        }
    }
}
