package lab4;

public class Utils {

    public static String formatContent(String content) {
        int len = content.length();
        return content.substring(1, len - 1);
    }

    public static String getOrDefault(String value, String def) {
        return value != null ? value : def;
    }

    public static String toName(String v) {
        return Character.toUpperCase(v.charAt(0)) + v.substring(1);
    }

}
