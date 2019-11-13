package utils;
public class Utils {
    public static String responseToString(byte[] arr){
        StringBuilder response = new StringBuilder();
        for (var b : arr) {
            if(b != 0) {
                response.append((char) b);
            }
        }
        return response.toString();
    }
}
