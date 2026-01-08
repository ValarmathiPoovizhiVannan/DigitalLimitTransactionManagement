package util;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ThreadLocalRandom;

public final class AccountNumberGenerator {
	private static final SecureRandom SECURE_RANDOM = new SecureRandom();
	private static final int RANDAOM_SUFFIX=100;
	private static final  DateTimeFormatter  FORMATTER = DateTimeFormatter.ofPattern("yyMMddHHmmssSSS");


private AccountNumberGenerator() {
	throw new UnsupportedOperationException("Utility class");
}
	public static String generateAccountNumber() {
		String dateTime = LocalDateTime.now().format(FORMATTER);
		int randomSuffix = SECURE_RANDOM.nextInt(RANDAOM_SUFFIX);
		return "Acc" + dateTime + String.format("%02d", randomSuffix);

	}

}
