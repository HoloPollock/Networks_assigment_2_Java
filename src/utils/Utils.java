package utils;

public class Utils {
    /**
     * @param arr padded byte array
     * @return String conatined in large byte array
     * remove 0 padding from byte array and return a string
     */
    public static String responseToString(byte[] arr) {
        StringBuilder response = new StringBuilder();
        for (var b : arr) {
            if (b != 0) {
                response.append((char) b);
            }
        }
        return response.toString();
    }
}
