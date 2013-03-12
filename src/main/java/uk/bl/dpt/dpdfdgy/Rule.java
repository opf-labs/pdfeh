package uk.bl.dpt.dpdfdgy;

public class Rule {
	private boolean failOn;
	private boolean warnOn;
	private String errorCode;
	private String errorLabel;
	private String errorDesc;
	private boolean active;
	
	/**
	 * Default settings for booleans - true and true.
	 * @param code
	 * @param label
	 * @param desc
	 */
	public Rule(String code, String label, String desc) {
		this(code, label, desc, true, true);
	}

	public Rule(String code, String label, String desc, boolean failOn, boolean warnOn) {
		setErrorCode(code);
		setErrorLabel(label);
		setErrorDesc(desc);
		setFailOn(failOn);
		setWarnOn(warnOn);
		setActive(failOn||warnOn);
	}

	public boolean isFailOn() {
		return failOn;
	}
	public void setFailOn(boolean failOn) {
		this.failOn = failOn;
	}
	public boolean isWarnOn() {
		return warnOn;
	}
	public void setWarnOn(boolean warnOn) {
		this.warnOn = warnOn;
	}
	public String getErrorCode() {
		return errorCode;
	}
	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}
	public String getErrorLabel() {
		return errorLabel;
	}
	public void setErrorLabel(String errorLabel) {
		this.errorLabel = errorLabel;
	}
	public String getErrorDesc() {
		return errorDesc;
	}
	public void setErrorDesc(String errorDesc) {
		this.errorDesc = errorDesc;
	}

	public boolean isActive() {
		return active;
	}
		
	public void setActive(boolean active) {
		this.active = active;
	}
}
