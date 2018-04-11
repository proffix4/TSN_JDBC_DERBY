package tsn.jdbc.derby;

import java.io.File;
import org.apache.derby.drda.NetworkServerControl;
import java.net.InetAddress;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.util.Properties;

public class App1 {

    public static void main(String[] args) {
        try {
            // ��� ����������� � ����� ������
            String name_base_dir = "MyBase";

            // ����������� �������� ������� ��������� (�������� ��������)
            String prog_dir = new File(".").getAbsoluteFile().getParentFile().getAbsolutePath()
                    + System.getProperty("file.separator");

            // ��������� �������� ��
            System.setProperty("derby.system.home", prog_dir + name_base_dir);

            // ������ �������
            NetworkServerControl server = new NetworkServerControl(InetAddress.getByName("localhost"), 1527);
            server.start(null);

            // �������� �������� JavaDB
            Class.forName("org.apache.derby.jdbc.EmbeddedDriver").newInstance();

            // �������� ������� ���������� � ����� ������
            Properties authorization = new Properties();
            authorization.setProperty("user", "tsn"); // ������� ��� ������������ ��
            authorization.setProperty("password", "tsn"); // ������� ������ ������� � ��

            // �������� ���������� � ����� ������
            Connection connection = DriverManager.getConnection("jdbc:derby://localhost:1527/disks", authorization);

            // �������� ��������� ������� � ���� ������
            java.sql.Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
                    ResultSet.CONCUR_UPDATABLE);

            // ���������� ������� � ���� ������
            statement.execute("select ID, NAME_DISK, PRICE_PUR, PRICE_SEL from disk_1"); // ������� �������
            // ��������� ������ ������
            ResultSet table = statement.getResultSet();

            table.first(); // ������� ����� �����
            for (int j = 1; j <= table.getMetaData().getColumnCount(); j++) {
                System.out.print(table.getMetaData().getColumnName(j) + "\t\t");
            }
            System.out.println();

            table.beforeFirst(); // ������� ������ �������
            while (table.next()) {
                for (int j = 1; j <= table.getMetaData().getColumnCount(); j++) {
                    System.out.print(table.getString(j) + "\t\t");
                }
                System.out.println();
            }

            if (table != null) { table.close(); } // �������� ������ ������
            if (statement != null) { statement.close(); } // �������� ���� ������
            if (connection != null) { connection.close(); } // ���������� �� ���� ������
            server.shutdown(); // ���������� �������
        } catch (Exception e) {
            System.err.println("Error accessing database!");
        }
    }
}
