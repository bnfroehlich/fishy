package bfroehlich.fishy;

public class ResetException extends Exception {
	
	private String reason;

	public ResetException(String reason) {
		this.reason = reason;
	}
	
	public String getReason() {
		return reason;
	}
}
