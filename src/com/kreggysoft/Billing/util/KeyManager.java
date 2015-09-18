package com.kreggysoft.Billing.util;

public class KeyManager {

	protected static String[] KEY = {
			"MIIBIjANBgkqhkiG9w",
			"0BAQEFAAOCAQ8AMIIBCgKCAQE",
			"AuGf874ZDYzR//EFPsGaoP2tpi5ZR",
			"06iT2lmSPw/lxwOwNE6x80n1ssNRRPw3AOTkveGXX",
			"NMEvR10A8h9r/2ON47asUOnEYR4BA8Iy92wkhSybFpidqrXEX",
			"754UY8KorZ9R6ESczQJqEHfqiPSaojeH8OkIkQiO28mOGnhZVQ8ovZXPDcF",
			"U5fyBTRmqzuQvIU7JWrGW6Y50L9w9dONM77I1w67SQQ0QFyDL8D4faqhODfbQH2SXKhcJ",
			"Rmub5bM5PuinTxlgjg2cPig", "fYbJtQwuoVR5wSn5AheWKbrv",
			"9BaqDgIfRe+pGdgAbq3SY7VY90Y12r9pRLLHk", "wZUzcuG3yHWwIDAQAB" };

	public static String getKEY() {
		
		String RSA_KEY=new String();
		for (int i=0;i<KEY.length;i++){
			RSA_KEY=RSA_KEY+KEY[i];
		}
		return RSA_KEY;
	}

}
