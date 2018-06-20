package ng.apmis.audreymumplus.utils;

import android.util.Log;

import ru.bullyboo.encoder.Encoder;
import ru.bullyboo.encoder.methods.AES;

/**
 * Created by Thadeus on 6/20/2018.
 */

public class EncryptionUtils {

    public static String encrypt (String value) {
        return Encoder.BuilderAES()
                .message(value)
                .method(AES.Method.AES_CBC_PKCS5PADDING)
                .key("test key")
                .keySize(AES.Key.SIZE_128)
                .iVector("test vector")
                .encrypt();
    }

    public static String decrypt (String value) {
        return Encoder.BuilderAES()
                .message(value)
                .method(AES.Method.AES_CBC_PKCS5PADDING)
                .key("test key")
                .keySize(AES.Key.SIZE_128)
                .iVector("test vector")
                .decrypt();
    }

}
