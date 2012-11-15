package ebag.pojo;

/**
 * SysInfoId entity. @author MyEclipse Persistence Tools
 */

public class SysInfoId implements java.io.Serializable {

	// Fields

	private String schoolName;
	private String secretkey;
	private String tag;

	// Constructors

	/** default constructor */
	public SysInfoId() {
	}

	/** full constructor */
	public SysInfoId(String schoolName, String secretkey, String tag) {
		this.schoolName = schoolName;
		this.secretkey = secretkey;
		this.tag = tag;
	}

	// Property accessors

	public String getSchoolName() {
		return this.schoolName;
	}

	public void setSchoolName(String schoolName) {
		this.schoolName = schoolName;
	}

	public String getSecretkey() {
		return this.secretkey;
	}

	public void setSecretkey(String secretkey) {
		this.secretkey = secretkey;
	}

	public String getTag() {
		return this.tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof SysInfoId))
			return false;
		SysInfoId castOther = (SysInfoId) other;

		return ((this.getSchoolName() == castOther.getSchoolName()) || (this
				.getSchoolName() != null
				&& castOther.getSchoolName() != null && this.getSchoolName()
				.equals(castOther.getSchoolName())))
				&& ((this.getSecretkey() == castOther.getSecretkey()) || (this
						.getSecretkey() != null
						&& castOther.getSecretkey() != null && this
						.getSecretkey().equals(castOther.getSecretkey())))
				&& ((this.getTag() == castOther.getTag()) || (this.getTag() != null
						&& castOther.getTag() != null && this.getTag().equals(
						castOther.getTag())));
	}

	public int hashCode() {
		int result = 17;

		result = 37
				* result
				+ (getSchoolName() == null ? 0 : this.getSchoolName()
						.hashCode());
		result = 37 * result
				+ (getSecretkey() == null ? 0 : this.getSecretkey().hashCode());
		result = 37 * result
				+ (getTag() == null ? 0 : this.getTag().hashCode());
		return result;
	}

}