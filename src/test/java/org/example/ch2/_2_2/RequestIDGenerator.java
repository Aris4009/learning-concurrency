package org.example.ch2._2_2;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Author: Aris
 * @ClassName: RequestIDGenerator
 * @Description:
 * @Date: 2022/3/16
 **/
public class RequestIDGenerator implements CircularSeqGenerator {

	private static final RequestIDGenerator INSTANCE = new RequestIDGenerator();

	private static final short UPPER_LIMIT = 999;

	private short sequence = -1;

	private RequestIDGenerator() {
	}

	@Override
	public synchronized short nextSequence() {
		if (sequence > UPPER_LIMIT) {
			sequence = 0;
			return sequence;
		}
		sequence++;
		return sequence;
	}

	public String nextID() {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		String timestamp = simpleDateFormat.format(new Date());
		DecimalFormat decimalFormat = new DecimalFormat("000");
		short sequence = nextSequence();
		return "0049" + timestamp + decimalFormat.format(sequence);
	}

	public static RequestIDGenerator getInstance() {
		return INSTANCE;
	}
}
