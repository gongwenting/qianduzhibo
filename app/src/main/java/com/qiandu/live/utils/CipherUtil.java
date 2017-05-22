package com.qiandu.live.utils;

import java.security.Key;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * 加/解密工具类
 * 1、genIV 加密  enString
 * 2、转换为ivString
 * 3、value = ivSting + enString
 *
 * @author: Andruby
 * @time: 2016/10/12 11:23
 */
public class CipherUtil {
	private static final String TAG = "CipherUtil";
	private static final char[] DIGITS_LOWER = new char[]{'0', '1', '2', '3',
			'4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
	private static final char[] DIGITS_UPPER = new char[]{'0', '1', '2', '3',
			'4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

	private static final String AES_PASSWORD = "TR%an%^&1^G4LUCK";

	public static void main(String[] args) throws Exception {
		String data = "123";
//		String key = "keykkeykkeykkeyk";
//		System.err.println(key.length());
//		byte[] iv = CipherUtil.genIV();
//		byte[] enData = CipherUtil.encryptAES(data.getBytes("utf-8"),
//				key.getBytes("utf-8"), iv);
//		String value = CipherUtil.byte2hex(iv) + CipherUtil.byte2hex(enData);

		System.out.println(CipherUtil.getAESInfo(data));

	}

	public static String getAESInfo(String info) {
		byte[] iv = CipherUtil.genIV();
		byte[] enData = new byte[0];
		try {
			byte[] pwdByte = AES_PASSWORD.getBytes("utf-8");
			for (int i = 0; i < pwdByte.length; i++) {
				pwdByte[i] = (byte) (0xf1 & pwdByte[i]);
			}
			enData = CipherUtil.encryptAES(info.getBytes("utf-8"),
					pwdByte, iv);
		} catch (Exception e) {
			e.printStackTrace();
		}
		String value = CipherUtil.byte2hex(iv) + CipherUtil.byte2hex(enData);
		return value;
	}

	/**
	 * 转化十六进制格式的字符串转化为字节数组
	 *
	 * @param s 格式如"8ADF5438EE2358912D96FD6822DF1090"
	 * @return s对应的字节数组
	 */
	public static byte[] hex2byte(String s) {
		if (s == null || s.length() == 0)
			return new byte[0];
		if (s.length() == 1)
			s = "0" + s;
		byte[] baKeyword = new byte[s.length() / 2];
		for (int i = 0; i < baKeyword.length; i++) {
			try {
				baKeyword[i] = (byte) (0xff & Integer.parseInt(
						s.substring(i * 2, i * 2 + 2), 16));
			} catch (Exception e) {
				System.err.println(e);
			}
		}

		return baKeyword;
	}

	/**
	 * 字节数组转化为对应的十六进制的字符串
	 *
	 * @param data
	 * @return
	 */
	public static String byte2hex(byte[] data) {
		if (data == null)
			return "";
		else
			return encodeHexString(data).toUpperCase();
	}

	public static String encodeHexString(byte[] data) {
		return new String(encodeHex(data, true));
	}

	public static char[] encodeHex(byte[] data, boolean toLowerCase) {
		return encodeHex(data, toLowerCase ? DIGITS_LOWER : DIGITS_UPPER);
	}

	private static char[] encodeHex(byte[] data, char[] toDigits) {
		int l = data.length;
		char[] out = new char[l << 1];
		int i = 0;

		for (int j = 0; i < l; ++i) {
			out[j++] = toDigits[(240 & data[i]) >>> 4];
			out[j++] = toDigits[15 & data[i]];
		}

		return out;
	}

	/**
	 * 转换密钥
	 *
	 * @param key 二进制密钥
	 * @return Key 密钥
	 */
	private static Key toKey(byte[] key, String key_algorithm) throws Exception {
		// 实例化DES密钥
		// 生成密钥
		SecretKey secretKey = new SecretKeySpec(key, key_algorithm);
		return secretKey;
	}

	/**
	 * 加密数据
	 *
	 * @param data             待加密数据
	 * @param key              加密密钥
	 * @param key_algorithm    密钥算法
	 * @param cipher_algorithm 数据加密算法
	 * @return 加密后数据
	 * @throws Exception
	 */
	private static byte[] encrypt(byte[] data, byte[] key,
								  String key_algorithm, String cipher_algorithm,
								  AlgorithmParameterSpec params) throws Exception {
		Key k = toKey(key, key_algorithm);
		Cipher cipher = Cipher.getInstance(cipher_algorithm);
		// 初始化，设置为加密模式
		if (params == null)
			cipher.init(Cipher.ENCRYPT_MODE, k);
		else
			cipher.init(Cipher.ENCRYPT_MODE, k, params);
		// 执行操作
		return cipher.doFinal(data);
	}


	/**
	 * 加密数据 采用aes ofb模式
	 *
	 * @param data 带加密数据
	 * @param key  加密密钥
	 * @return 加密后数据
	 * @throws Exception
	 */
	public static byte[] encryptAES(byte[] data, byte[] key, byte[] iv) throws Exception {
		javax.crypto.spec.IvParameterSpec params = new javax.crypto.spec.IvParameterSpec(
				iv);
		return encrypt(data, key, "AES", "AES/OFB128/NoPadding", params);
	}

	private static byte[] genIV() {
		byte[] iv = new byte[16];
		SecureRandom random = new SecureRandom();
		random.nextBytes(iv);
		return iv;
	}

}
