package jedis;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class Connection {


    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("localhost", 6379);

        InputStream is = socket.getInputStream();
        OutputStream os = socket.getOutputStream();

        System.out.println("SET:\n============");
        String msg = "*3\r\n$3\r\nSET\r\n$5\r\nhello\r\n$4\r\n2018\r\n";
        System.out.println(msg);
        os.write(msg.getBytes());
        byte[] bytes = new byte[1024];
        is.read(bytes);
        System.out.println("reply:\n" + new String(bytes));

        System.out.println("GET:\n============");
        msg = "*2\r\n$3\r\nGET\r\n$5\r\nhello\r\n";
        os.write(msg.getBytes());
        is.read(bytes);
        System.out.println("reply:\n" + new String(bytes));

        is.close();
        os.close();
    }
}
