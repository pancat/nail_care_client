package com.pancat.fanrong.common;

import java.security.MessageDigest;

public class ChangeMd5 {

	private static String bytes2Hex(byte[] bts) {
		StringBuffer des = new StringBuffer();
		String tmp = null;
		for (int i = 0; i < bts.length; i++) {
			tmp = (Integer.toHexString(bts[i] & 0xFF));
			if (tmp.length() == 1) {
				des.append("0");
			}
			des.append(tmp);
		}
		return des.toString();
	}

	public static String MD5(String strSrc, MessageDigest md) {
		byte[] bt = strSrc.getBytes();
		md.update(bt);
		String strDes = bytes2Hex(md.digest()); // to HexString
		return strDes;
	}

}