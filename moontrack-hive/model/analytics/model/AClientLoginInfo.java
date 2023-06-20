package analytics.model;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import com.moonmana.OS;
import com.moonmana.PlatformID;

@Embeddable
public class AClientLoginInfo {
	public static final AClientLoginInfo ZERO = new AClientLoginInfo(OS.NONE, PlatformID.NONE, null, null);
	
	@Column(name = "platform_id")
	public final Byte platformID;
	
	@Column(name = "os_id")
	public final Byte osId;
	
	@Column(name = "os_version", length = 120)
	public final String osVersion;
	
	@Column(name = "device", length = 120)
	public final String deviceDescription;
	
	public AClientLoginInfo() {
		super();
		platformID = ZERO.platformID;
		osId = ZERO.osId;
		osVersion = ZERO.osVersion;
		deviceDescription = ZERO.deviceDescription;
	}

	public AClientLoginInfo(byte platformID, byte osId, String osVersion,
			String deviceDescription) {
		super();
		this.platformID = platformID;
		this.osId = osId;
		this.osVersion = osVersion;
		this.deviceDescription = deviceDescription;
	}

	public boolean uninited() {
		return (platformID == null || platformID == PlatformID.NONE) 
				&& (osId == null || osId == OS.NONE);
	}
}
