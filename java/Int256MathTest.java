/* 
 * Bitcoin cryptography library, copyright (c) Project Nayuki
 * http://www.nayuki.io/page/bitcoin-cryptography-library
 * https://github.com/nayuki/Bitcoin-Cryptography-Library
 */

import static org.junit.Assert.assertEquals;
import java.math.BigInteger;
import java.util.Random;
import org.junit.Test;


public final class Int256MathTest {
	
	@Test public void testComparison() {
		String[][] cases = {
			{"0000000000000000000000000000000000000000000000000000000000000000", "0000000000000000000000000000000000000000000000000000000000000000"},
			{"0000000000000000000000000000000000000000000000000000000000000000", "0000000000000000000000000000000000000000000000000000000000000001"},
			{"0000000000000000000000000000000000000000000000000000000000000080", "0000000000000000000000000000000000000000000000000000000000000000"},
			{"ff00000000000000000000000000000000000000000000000000000000000000", "0000000000000000000000000000000000000000000000000000000000000000"},
			{"f000000000000000000000000000000000000000000000000000000000000000", "0000000000000000000000000000000000000000000000000000000000000000"},
			{"0123400000000000000000000000000000000000000000000000000000000000", "0123400000000000000000000000000000000000000000000000000000000000"},
			{"0000000000000000000000000000000000000000000000000000000000000000", "fffffffffffffffffffffffffffffffffffffffffffffffffffffffefffffc2f"},
			{"00000000000000000000000000000000000000000000000000000000000000ff", "ff00000000000000000000000000000000000000000000000000000000000000"},
		};
		for (int i = 0; i < 30000; i++) {
			String s = i < cases.length ? cases[i][0] : randomInt256Str();
			String t = i < cases.length ? cases[i][1] : randomInt256Str();
			TestInt x = new TestInt(s);
			TestInt y = new TestInt(t);
			BigInteger a = toBigInt(s);
			BigInteger b = toBigInt(t);
			int cmp = a.compareTo(b);
			assertEquals(cmp == 0 ? 1 : 0, Int256Math.equalTo (x.val, x.off, y.val, y.off));
			assertEquals(cmp <  0 ? 1 : 0, Int256Math.lessThan(x.val, x.off, y.val, y.off));
			assertEquals(cmp >  0 ? 1 : 0, Int256Math.lessThan(y.val, y.off, x.val, x.off));
		}
	}
	
	
	@Test public void testUintAdd() {
		String[][] cases = {
			{"0000000000000000000000000000000000000000000000000000000000000000", "0000000000000000000000000000000000000000000000000000000000000000"},
			{"0000000000000000000000000000000000000000000000000000000080000000", "0000000000000000000000000000000000000000000000000000000080000000"},
			{"000000000000000000000000000000000000000000000fffffffffffffffffff", "0000000000000000000000000000000000000000000000000000000000000001"},
			{"fffffffffffffffffffffffffffffffffffffffffffffffffffffffefffffc2e", "0000000000000000000000000000000000000000000000000000000000000001"},
			{"ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff", "0000000000000000000000000000000000000000000000000000000000000001"},
			{"ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff", "ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff"},
			{"ff00000000000000000000000000000000000000000000000000000000000000", "ff00000000000000000000000000000000000000000000000000000000000000"},
		};
		for (int i = 0; i < 30000; i++) {
			String s = i < cases.length ? cases[i][0] : randomInt256Str();
			String t = i < cases.length ? cases[i][1] : randomInt256Str();
			TestInt x = new TestInt(s);
			TestInt y = new TestInt(t);
			BigInteger a = toBigInt(s);
			BigInteger b = toBigInt(t);
			BigInteger c = a.add(b);
			assertEquals(0, Int256Math.uintAdd(x.val, x.off, y.val, y.off, 0));
			assertEqualsBigInt256(a, x.val, x.off);
			assertEquals(c.shiftRight(256).intValue(), Int256Math.uintAdd(x.val, x.off, y.val, y.off, 1));
			assertEqualsBigInt256(c, x.val, x.off);
		}
	}
	
	
	@Test public void testUintSubtract() {
		String[][] cases = {
			{"0000000000000000000000000000000000000000000000000000000000000000", "0000000000000000000000000000000000000000000000000000000000000000"},
			{"0000000000000000000000000000000000000000000000000000000000000003", "0000000000000000000000000000000000000000000000000000000000000002"},
			{"0000000000000000000000000000000000000000000000000000000000000000", "0000000000000000000000000000000000000000000000000000000000000001"},
			{"0000000000000000000000000000000000000000000000000000000000000000", "ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff"},
		};
		for (int i = 0; i < 30000; i++) {
			String s = i < cases.length ? cases[i][0] : randomInt256Str();
			String t = i < cases.length ? cases[i][1] : randomInt256Str();
			TestInt x = new TestInt(s);
			TestInt y = new TestInt(t);
			BigInteger a = toBigInt(s);
			BigInteger b = toBigInt(t);
			BigInteger c = a.subtract(b);
			assertEquals(0, Int256Math.uintSubtract(x.val, x.off, y.val, y.off, 0));
			assertEqualsBigInt256(a, x.val, x.off);
			assertEquals(-c.shiftRight(256).intValue(), Int256Math.uintSubtract(x.val, x.off, y.val, y.off, 1));
			assertEqualsBigInt256(c, x.val, x.off);
		}
	}
	
	
	@Test public void testUintShiftLeft1() {
		String[] cases = {
			"0000000000000000000000000000000000000000000000000000000000000000",
			"0000000000000000000000000000000000000000000000000000000000000001",
			"0000000000000000000000000000000000000000000000000000000080000000",
			"00000000000000000000000000000000000000000000000000000000ffffffff",
			"000abcdef0000000000000000000000000000000000000000000000000000000",
			"8000000000000000000000000000000000000000000000000000000000000000",
			"ffff000000000000000000000000000000000000000000000000000000000000",
			"ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff",
		};
		for (int i = 0; i < 100000; i++) {
			String s = i < cases.length ? cases[i] : randomInt256Str();
			TestInt x = new TestInt(s);
			BigInteger a = toBigInt(s);
			BigInteger b = a.shiftLeft(1);
			assertEquals(a.testBit(255) ? 1 : 0, Int256Math.uintShiftLeft1(x.val, x.off));
			assertEqualsBigInt256(b, x.val, x.off);
		}
	}
	
	
	@Test public void testUintShiftRight1() {
		String[] cases = {
			"0000000000000000000000000000000000000000000000000000000000000000",
			"0000000000000000000000000000000000000000000000000000000000000001",
			"0000000000000000000000000000000000000000000000000000000080000000",
			"00000000000000000000000000000000000000000000000000000000ffffffff",
			"000abcdef0000000000000000000000000000000000000000000000000000000",
			"8000000000000000000000000000000000000000000000000000000000000000",
			"ffff000000000000000000000000000000000000000000000000000000000000",
			"ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff",
		};
		for (int i = 0; i < 100000; i++) {
			String s = i < cases.length ? cases[i] : randomInt256Str();
			TestInt x = new TestInt(s);
			BigInteger a = toBigInt(s);
			BigInteger b = a.shiftRight(1);
			Int256Math.uintShiftRight1(x.val, x.off, 0);
			assertEqualsBigInt256(a, x.val, x.off);
			Int256Math.uintShiftRight1(x.val, x.off, 1);
			assertEqualsBigInt256(b, x.val, x.off);
		}
	}
	
	
	@Test public void testReciprocalRandomly() {
		for (int i = 0; i < 300; i++) {
			BigInteger mod;  // Choose an odd number at least 128 bits long
			do mod = new BigInteger(192 + rand.nextInt(65), rand);
			while (!mod.testBit(0) || mod.bitLength() < 128);
			
			for (int j = 0; j < 30; j++) {
				BigInteger a;  // Choose 'a' in [0, mod) that is coprime to mod
				do a = new BigInteger(mod.bitLength(), rand);
				while (!a.gcd(mod).equals(BigInteger.ONE));
				a = a.mod(mod);
				
				TestInt x = new TestInt(String.format("%064x", a));
				TestInt y = new TestInt(String.format("%064x", mod));
				Int256Math.reciprocal(x.val, x.off, y.val, y.off, new int[40], 0);
				BigInteger b = a.modInverse(mod);
				assertEqualsBigInt256(b, x.val, x.off);
			}
		}
	}
	
	
	@Test public void testFieldAdd() {
		String[][] cases = {
			{"0000000000000000000000000000000000000000000000000000000000000000", "0000000000000000000000000000000000000000000000000000000000000000"},
			{"0000000000000000000000000000000000000000000000000000000080000000", "0000000000000000000000000000000000000000000000000000000080000000"},
			{"000000000000000000000000000000000000000000000fffffffffffffffffff", "0000000000000000000000000000000000000000000000000000000000000001"},
			{"fffffffffffffffffffffffffffffffffffffffffffffffffffffffefffffc2e", "0000000000000000000000000000000000000000000000000000000000000001"},
			{"0000000000000000000000000000000000000000000000000000000000000001", "fffffffffffffffffffffffffffffffffffffffffffffffffffffffefffffc2e"},
			{"fffffffffffffffffffffffffffffffffffffffffffffffffffffffefffffc2e", "fffffffffffffffffffffffffffffffffffffffffffffffffffffffefffffc2e"},
			{"ff00000000000000000000000000000000000000000000000000000000000000", "ff00000000000000000000000000000000000000000000000000000000000000"},
			{"cf82a6e670c187af1855bfd6a02f676d58b2659a6010eda5b1ada5662135ea37", "3130ec4c765e299fe200cd62bf4439f061440b57751b104bf14552a553064508"},
			{"f93cab87674d5fea845300e8bc63c9d313585200f2da0e9b955cd07fafb46787", "42207c06d78bd8d62c57746d35d0427ad9edaa7d8aa2bfd39144167418e44dc4"},
			{"a30910e72fd6ac01a3ef74124085ecda6713b0e4d7e840c4890ecd272c83a394", "71ab910a371a0d15370c19d5b1562edb339c66c9dbc72e4194d5b3361f9208b6"},
		};
		for (int i = 0; i < 30000; i++) {
			String s = i < cases.length ? cases[i][0] : randomFieldIntStr();
			String t = i < cases.length ? cases[i][1] : randomFieldIntStr();
			TestInt x = new TestInt(s);
			TestInt y = new TestInt(t);
			BigInteger a = toBigInt(s);
			BigInteger b = toBigInt(t);
			BigInteger c = a.add(b).mod(FIELD_MODULUS);
			Int256Math.fieldAdd(x.val, x.off, y.val, y.off);
			assertEqualsBigInt256(c, x.val, x.off);
		}
	}
	
	
	@Test public void testFieldSubtract() {
		String[][] cases = {
			{"0000000000000000000000000000000000000000000000000000000000000000", "0000000000000000000000000000000000000000000000000000000000000000"},
			{"0000000000000000000000000000000000000000000000000000000000000003", "0000000000000000000000000000000000000000000000000000000000000002"},
			{"0000000000000000000000000000000000000000000000000000000000000000", "0000000000000000000000000000000000000000000000000000000000000001"},
			{"0000000000000000000000000000000000000000000000000000000000000000", "fffffffffffffffffffffffffffffffffffffffffffffffffffffffefffffc2e"},
			{"0000000000000000000000000000000000000000000000000000000000000000", "0fff000000000000000000050000000400003000000000000200100000000000"},
			{"484786b41f8cc919c7b47e08b918e13bcadf20777224e3b7c7334edf6e98cbbe", "57f3e075c0014548eb29756f8c4394d1541112662c298f8bac5211f7fc9d8930"},
			{"69a1b6279f3cf9e29d9d46c32aebab2694a25dfe291a2b6f7a8116727b811705", "290113fe9d369fe41f1cb33b29ad34666744a06a310a30a98b8d631a36133a4b"},
			{"e5e4684e5edda9eefa6d4cbf84089280750391e9e68b95c84c671b07d2af9081", "686058d3961a761d9a60e2810487bde706bb5df21005e4ec1c2b68ecc7f6e241"},
		};
		for (int i = 0; i < 30000; i++) {
			String s = i < cases.length ? cases[i][0] : randomFieldIntStr();
			String t = i < cases.length ? cases[i][1] : randomFieldIntStr();
			TestInt x = new TestInt(s);
			TestInt y = new TestInt(t);
			BigInteger a = toBigInt(s);
			BigInteger b = toBigInt(t);
			BigInteger c = a.subtract(b).mod(FIELD_MODULUS);
			Int256Math.fieldSubtract(x.val, x.off, y.val, y.off);
			assertEqualsBigInt256(c, x.val, x.off);
		}
	}
	
	
	@Test public void testFieldMultiply2() {
		String[] cases = {
			"0000000000000000000000000000000000000000000000000000000000000000",
			"0000000000000000000000000000000000000000000000000000000000000001",
			"0000000000000000000000000000000000000000000000000000000080000000",
			"00000000000000000000000000000000000000000000000000000000ffffffff",
			"000abcdef0000000000000000000000000000000000000000000000000000000",
			"8000000000000000000000000000000000000000000000000000000000000000",
			"ffff000000000000000000000000000000000000000000000000000000000000",
			"7fffffffffffffffffffffffffffffffffffffffffffffffffffffff7ffffe17",
			"7fffffffffffffffffffffffffffffffffffffffffffffffffffffff7ffffe18",
			"fffffffffffffffffffffffffffffffffffffffffffffffffffffffefffffc2e",
			"34c8b477c0e20991a17f295a49f4f671ec4126cee5a07edef85ba9ce12167127",
		};
		for (int i = 0; i < 30000; i++) {
			String s = i < cases.length ? cases[i] : randomFieldIntStr();
			TestInt x = new TestInt(s);
			BigInteger a = toBigInt(s);
			BigInteger b = a.shiftLeft(1).mod(FIELD_MODULUS);
			Int256Math.fieldMultiply2(x.val, x.off);
			assertEqualsBigInt256(b, x.val, x.off);
		}
	}
	
	
	@Test public void testFieldMultiply3() {
		String[] cases = {
			"0000000000000000000000000000000000000000000000000000000000000000",
			"0000000000000000000000000000000000000000000000000000000000000001",
			"0000000000000000000000000000000000000000000000000000000055555556",
			"ffff000000000000000000000000000000000000000000000000000000000000",
			"fffffffffffffffffffffffffffffffffffffffffffffffffffffffefffffc2e",
			"688622e8382151c73b84b18c844796ae7c2388144451b92d83ee655fbf89d5b0",
			"9699a5ac8fd7b06058fa84def940aa90e6895e729341a1c2ecf5b0f1d774d87f",
		};
		for (int i = 0; i < 30000; i++) {
			String s = i < cases.length ? cases[i] : randomFieldIntStr();
			TestInt x = new TestInt(s);
			BigInteger a = toBigInt(s);
			BigInteger b = a.multiply(BigInteger.valueOf(3)).mod(FIELD_MODULUS);
			Int256Math.fieldMultiply3(x.val, x.off, new int[8], 0);
			assertEqualsBigInt256(b, x.val, x.off);
		}
	}
	
	
	@Test public void testFieldSquare() {
		String[] cases = {
			"0000000000000000000000000000000000000000000000000000000000000000",
			"0000000000000000000000000000000000000000000000000000000000000001",
			"000000000000000000000000000000000000000000000000000000000000000d",
			"0000000000000000000000000000000000000000000000000000000000010000",
			"fffffffffffffffffffffffffffffffffffffffffffffffffffffffefffffc2c",
			"fffffffffffffffffffffffffffffffffffffffffffffffffffffffefffffc2d",
			"fffffffffffffffffffffffffffffffffffffffffffffffffffffffefffffc2e",
			"0000000000400000000100000004000000011040000000200400000000401002",
			"0000000000000000000002100000000000000001000000100000000000000800",
			"0002020040000200000000000000100000000410000000000000000000000000",
			"1000000008008400000000000000000000200000000000000020000000000000",
			"0018484a883c83014f000060a81004032e4280b134e118202cd6002001020008",
			"10090024008008c8002010800015a0113822109912105300048c0280a2b00c11",
			"0c49400420000200c031484216240a804408000240600446c122812001008040",
			"002a8023b00101000460200300e05490104240a14300008041c0210800240102",
			"d44459ec5669c56c03485be9c803519eeb977f8a6000f792d0916b43031eec1c",
			"03faec56081e36092a72fa87b01132c946a60307cd8bf2032725423d44894689",
			"ab013b2a27380172d641b9e05471e91edb00d962610e018334b19e7fa1ffe0a9",
			"32f2943da6ec0cad5e69c0c9b1437a5ea3b9a9ffa14e92c0f6b51c25fd400661",
			"b7aaf40f663fb643ba2c24eb0ba4b4e50d159041c5f8317ea452f6851bde308b",
			"e83608ece352fc369836fb19b5ebe6b565254bf4c644b71ea2cd4227c30ffa8e",
			"82914a20f06d7b6b14bb3a4e133c9502adb2d82cfe99010507425fd69870c31a",
			"f8f2c1d5a0c5005e82cf09331249d8325885d254af460deedee00df91fcfee7b",
			"ffffffffffeffffffdfffffffffffffffefabffffffffffdbffffefdffffdfff",
			"7fffeffffffffefffffeffffffbfefffffffbffffff7ffffffff7fffffffffff",
			"fffffffefff7ffbfffffefeffffffffffffffffffffffffff5fffffffff7ffff",
			"fffbffffffffffffffffffffffffffffffffffffffffffffdffffdffffffffff",
		};
		for (int i = 0; i < 30000; i++) {
			String s = i < cases.length ? cases[i] : randomFieldIntStr();
			TestInt x = new TestInt(s);
			BigInteger a = toBigInt(s);
			BigInteger b = a.pow(2).mod(FIELD_MODULUS);
			Int256Math.fieldSquare(x.val, x.off, new int[72], 0);
			assertEqualsBigInt256(b, x.val, x.off);
		}
	}
	
	
	@Test public void testFieldMultiply() {
		String[][] cases = {
			{"0000000000000000000000000000000000000000000000000000000000000000", "0000000000000000000000000000000000000000000000000000000000000000"},
			{"0000000000000000000000000000000000000000000000000000000000000001", "0000000000000000000000000000000000000000000000000000000000000001"},
			{"0000000000000000000000000000000000000000000000000000000000000002", "0000000000000000000000000000000000000000000000000000000000000004"},
			{"000000000000000000000000000000000000000000000000000000000000bb81", "0000000000000000000000000000000000000000000000000000000000002375"},
			{"fffffffffffffffffffffffffffffffffffffffffffffffffffffffefffffc2e", "fffffffffffffffffffffffffffffffffffffffffffffffffffffffefffffc2e"},
			{"fffffffffffffffffffffffffffffffffffffffffffffffffffffffefffffc2c", "fffffffffffffffffffffffffffffffffffffffffffffffffffffffefffffc25"},
			{"fffffffffffffffffffffffffffffffffffffffffffffffffffffffe00000000", "fffffffffffffffffffffffffffffffffffffffffffffffffffffffe00000000"},
			{"abc928448f874620bdb2d01f4d797eed5788cc2475334002e16e6bcc12dcf419", "d661b81bed420f5b5dd8027d1486c7d27c85e6bdb0405ec07849cfd1a7ee526c"},
			{"3720e6127667a3de448044ee8decd7c96f345cda261682a4386719a387c37ed5", "f8e32435123472d4949bf98c22a87a374923a3b06b289ef15e93f0940aab3650"},
			{"47e3d45c7f5a64de0d4913911d541bbc0df640c0920a4fb42fc6ed5ace413d51", "e8bd16cb61df4b53b66a35140377314b7ea9cb1099d7e1f05fc50fc49d0eddf2"},
			{"c3aacf7136f758a89979dd75cf45b6af07486d43d1b5767c269210ee61c904f4", "ea31def20213b863699c0f370265e53f8fdca440cc0690e6d3672928bea51dc4"},
			{"290c735eecfedd247d92d5db8ced26626f8196e2a6f0d7282d97354484134110", "e0bbdef09c2e071d94f71fa78dde9e5e785c3af99c53ddf63f29409752488c53"},
			{"926e48eb3b5e7017d3c4bdf3fcd34938bb469782991eb4d8afa8fdc192091058", "d61c4475d5e43ce7f7f922f4ba9e1d52a94602cfbf73836ec20ecf748d7a2ab2"},
			{"71d40203aec9cc1a35cf06b490a742208729b7a000ca8dd32e72a7813d98ab44", "a59f39fc19046e62cb85f0654133e0323c5a21020d5f26b1a52a41626689e330"},
			{"5cfbd891a5591d3d2b48eafa4cf890804d8b4af83d2ca1b1bf0d8e723575c657", "ced7a3ac8df01f9e1fb478ddde3db6b20fbc61709798525099c6ce4cbe21e78e"},
			{"0000000000000000000000100000000004000000008000000000000000000000", "0400020000000000000400000040000000000000000000000000000000000000"},
			{"0000000000000000800000000000000200000000000000000000020000000000", "0000000000000004000000000000000000000000000000000000000000100000"},
			{"0000000004000008000000000000000000000000100000000000000000000000", "0000000000000000000001000000000000000000000000000000000000000000"},
			{"0000000000000000000000002480000000000800000000000000008000000001", "0000000000000000000000000000000000000000000000000000000010000000"},
			{"0000000000010100000080000000000000000000000000000000000000000000", "0000000000800004002000000000000000000000000000000000000000000000"},
			{"0000000000001900004000000000000000000000000000000000000000000000", "0000000004000000002000000000000000000020010000000000000000010000"},
			{"0000000020000000000000000800020000000004004000000000000000000000", "0000000000000000000000000000000000000000000000000000000000000000"},
			{"0000000000000000000000000000000000000008000000000000500010000000", "0000020000000000000000000000020000000000000000000000000002000080"},
			{"000000080080200822000024800000809021080140000120400020000402001a", "0800002000000081000000002080080008000011408040060000008002001818"},
			{"0000000080800584081000825000448240002002000000220000000000000010", "1000004400000000000000004000020240a08200080512000080080400090400"},
			{"0000000000002010401800001030604101000010000000008408001000500000", "0020002000400040000000020210408008800025000080100004004001000000"},
			{"0009000200002008000c24200400000000040402000000004400200000000000", "00000000000820008001004400800a040002000000000004080c0000002c0010"},
			{"0800002000010240400000000004010281020000002c00200000840280000402", "0010000800000400000064000200002481000010080134001200000000001000"},
			{"0041004100000a00000480060000001000405000008000020080014001200100", "0000008000021200040006000000000220000000040200000080180082042500"},
			{"0000000442001000102010400000000000024100010000000222000008000000", "4001000000000400000000020000088000200000004000000000008100208000"},
			{"0040050240000002000010009004000110000000080880800804040001000000", "8000001400020200000000080000800080000000008008104000000280001000"},
		};
		for (int i = 0; i < 30000; i++) {
			String s = i < cases.length ? cases[i][0] : randomFieldIntStr();
			String t = i < cases.length ? cases[i][1] : randomFieldIntStr();
			TestInt x = new TestInt(s);
			TestInt y = new TestInt(t);
			BigInteger a = toBigInt(s);
			BigInteger b = toBigInt(t);
			BigInteger c = a.multiply(b).mod(FIELD_MODULUS);
			Int256Math.fieldMultiply(x.val, x.off, y.val, y.off, new int[72], 0);
			assertEqualsBigInt256(c, x.val, x.off);
		}
	}
	
	
	@Test public void testReciprocalForField() {
		TestInt y = new TestInt(String.format("%064x", FIELD_MODULUS));
		
		// Special case for zero
		{
			TestInt x = new TestInt("0000000000000000000000000000000000000000000000000000000000000000");
			Int256Math.reciprocal(x.val, x.off, y.val, y.off, new int[40], 0);
			assertEqualsBigInt256(BigInteger.ZERO, x.val, x.off);
		}
		
		// General and random cases
		String[] cases = {
			"0000000000000000000000000000000000000000000000000000000000000001",
			"000000000000000000000000000000000000000000000000000000000000000d",
			"0000000000000000000000000000000000000000000000000000000000010000",
			"fffffffffffffffffffffffffffffffffffffffffffffffffffffffefffffc2c",
			"fffffffffffffffffffffffffffffffffffffffffffffffffffffffefffffc2d",
			"fffffffffffffffffffffffffffffffffffffffffffffffffffffffefffffc2e",
			"0000000000400000000100000004000000011040000000200400000000401002",
			"0000000000000000000002100000000000000001000000100000000000000800",
			"0002020040000200000000000000100000000410000000000000000000000000",
			"1000000008008400000000000000000000200000000000000020000000000000",
			"0018484a883c83014f000060a81004032e4280b134e118202cd6002001020008",
			"10090024008008c8002010800015a0113822109912105300048c0280a2b00c11",
			"0c49400420000200c031484216240a804408000240600446c122812001008040",
			"002a8023b00101000460200300e05490104240a14300008041c0210800240102",
			"d44459ec5669c56c03485be9c803519eeb977f8a6000f792d0916b43031eec1c",
			"03faec56081e36092a72fa87b01132c946a60307cd8bf2032725423d44894689",
			"ab013b2a27380172d641b9e05471e91edb00d962610e018334b19e7fa1ffe0a9",
			"32f2943da6ec0cad5e69c0c9b1437a5ea3b9a9ffa14e92c0f6b51c25fd400661",
			"b7aaf40f663fb643ba2c24eb0ba4b4e50d159041c5f8317ea452f6851bde308b",
			"e83608ece352fc369836fb19b5ebe6b565254bf4c644b71ea2cd4227c30ffa8e",
			"82914a20f06d7b6b14bb3a4e133c9502adb2d82cfe99010507425fd69870c31a",
			"f8f2c1d5a0c5005e82cf09331249d8325885d254af460deedee00df91fcfee7b",
			"ffffffffffeffffffdfffffffffffffffefabffffffffffdbffffefdffffdfff",
			"7fffeffffffffefffffeffffffbfefffffffbffffff7ffffffff7fffffffffff",
			"fffffffefff7ffbfffffefeffffffffffffffffffffffffff5fffffffff7ffff",
			"fffbffffffffffffffffffffffffffffffffffffffffffffdffffdffffffffff",
		};
		for (int i = 0; i < 30000; i++) {
			String s = i < cases.length ? cases[i] : randomFieldIntStr();
			TestInt x = new TestInt(s);
			BigInteger a = toBigInt(s);
			if (a.signum() == 0)
				continue;
			BigInteger b = a.modInverse(FIELD_MODULUS);
			Int256Math.reciprocal(x.val, x.off, y.val, y.off, new int[40], 0);
			assertEqualsBigInt256(b, x.val, x.off);
		}
	}
	
	
	/*---- Helper definitions ----*/
	
	private static String randomInt256Str() {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < 8; i++) {
			int word;
			double type = rand.nextDouble();
			if (type < 0.2)
				word = rand.nextInt();
			else {
				word = rand.nextInt(10);
				if (type < 0.6)
					word = ~word;
			}
			sb.append(String.format("%08x", word));
		}
		return sb.toString();
	}
	
	
	private static String randomFieldIntStr() {
		while (true) {
			String s = randomInt256Str();
			if (toBigInt(s).compareTo(FIELD_MODULUS) < 0)
				return s;
		}
	}
	
	
	private static void assertEqualsBigInt256(BigInteger num, int[] val, int off) {
		for (int i = 0; i < 8; i++)
			assertEquals(num.shiftRight(i * 32).intValue(), val[off + i]);
	}
	
	
	private static BigInteger toBigInt(String s) {
		return new BigInteger(s, 16);
	}
	
	
	private static final BigInteger FIELD_MODULUS = new BigInteger("FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFEFFFFFC2F", 16);
	
	
	private static Random rand = new Random();
	
	
	
	private static class TestInt {
		
		public int[] val;
		public int off;
		
		
		// Constructs an int256 array from the given hexadecimal string,
		// using a random offset that is a multiple of 8.
		// The offset ensures that methods under test don't assume
		// an offset of 0 or mix up offsets of different arguments.
		public TestInt(String s) {
			off = rand.nextInt(4) * 8;
			val = new int[off + 8];
			for (int i = 0; i < 8; i++)
				val[off + i] = (int)Long.parseLong(s.substring((7 - i) * 8, (8 - i) * 8), 16);
		}
		
	}
	
}
