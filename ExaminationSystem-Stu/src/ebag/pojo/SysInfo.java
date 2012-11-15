package ebag.pojo;

/**
 * SysInfo entity. @author MyEclipse Persistence Tools
 */

public class SysInfo implements java.io.Serializable {

	// Fields

	private SysInfoId id;

	// Constructors

	/** default constructor */
	public SysInfo() {
	}

	/** full constructor */
	public SysInfo(SysInfoId id) {
		this.id = id;
	}

	// Property accessors

	public SysInfoId getId() {
		return this.id;
	}

	public void setId(SysInfoId id) {
		this.id = id;
	}

}