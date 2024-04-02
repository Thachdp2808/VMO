package FileManager;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class File {
    public void readFile() {
        // Đường dẫn tệp cần đọc
        String filePath = "sample.txt";

        // Khai báo BufferedReader và FileReader, cần được khởi tạo trong khối try để đảm bảo đóng tệp đúng cách
        BufferedReader reader = null;
        try {
            // Khởi tạo FileReader và BufferedReader
            FileReader fileReader = new FileReader(filePath);
            reader = new BufferedReader(fileReader);

            // Đọc từng dòng từ tệp và hiển thị trên màn hình
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException e) {
            // Xử lý ngoại lệ nếu có lỗi khi đọc hoặc xử lý tệp
            System.err.println("Error reading file: " + e.getMessage());
        } finally {
            // Đảm bảo rằng tệp sẽ được đóng ngay cả khi có ngoại lệ xảy ra
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                // Xử lý ngoại lệ nếu có lỗi khi đóng tệp
                System.err.println("Error closing file: " + e.getMessage());
            }
        }
    }
}
