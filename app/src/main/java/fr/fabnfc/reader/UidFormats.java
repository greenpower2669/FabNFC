package fr.fabnfc.reader;

import java.math.BigInteger;
import java.util.Locale;

public final class UidFormats {
    private UidFormats() {}

    public static String hex(byte[] id) {
        StringBuilder sb = new StringBuilder(id.length * 2);
        for (byte b : id) sb.append(String.format(Locale.ROOT, "%02X", b & 0xFF));
        return sb.toString();
    }
    public static byte[] reverse(byte[] bytes) {
        byte[] result = bytes.clone();
        for (int a=0, b=result.length-1; a<b; a++, b--) {
            byte tmp=result[a]; result[a]=result[b]; result[b]=tmp;
        }
        return result;
    }
    public static String decimal(byte[] bytes) {
        if (bytes.length==0) return "";
        return new BigInteger(1, bytes).toString(10);
    }
    public static byte[] fromHex(String raw) {
        if (raw==null || raw.length()==0 || (raw.length()&1)!=0 || raw.length()>64)
            throw new IllegalArgumentException("UID invalide");
        byte[] bytes=new byte[raw.length()/2];
        for (int i=0; i<bytes.length; i++) {
            int a=Character.digit(raw.charAt(i*2),16);
            int b=Character.digit(raw.charAt(i*2+1),16);
            if (a<0 || b<0) throw new IllegalArgumentException("UID invalide");
            bytes[i]=(byte)((a<<4)|b);
        }
        return bytes;
    }
}
