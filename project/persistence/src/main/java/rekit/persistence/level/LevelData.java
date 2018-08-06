package rekit.persistence.level;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.fuchss.obox.port.OBoxCMP;

@Entity
public final class LevelData {
	@Id
	@GeneratedValue
	public Integer levelId;

	public final String levelName;
	public final Integer levelType;
	public final String data;

	public int highscore;
	public boolean success;
	public boolean won;

	@SuppressWarnings("unused")
	private LevelData() {
		this(null, null, null);
	}

	public LevelData(String levelName, Integer levelType, String levelData) {
		this.levelName = levelName;
		this.levelType = levelType;
		this.data = levelData;
	}

	@Override
	public String toString() {
		return "LevelData [levelId=" + this.levelId + ", levelName=" + this.levelName + ", levelType=" + this.levelType + ", highscore=" + this.highscore
				+ ", success=" + this.success + ", won=" + this.won + "]";
	}

	public static Map<String, OBoxCMP> getCompare() {
		Map<String, OBoxCMP> res = new HashMap<>();
		res.put("levelName", OBoxCMP.EQUAL);
		res.put("levelType", OBoxCMP.EQUAL);
		res.put("data", OBoxCMP.EQUAL);
		return res;
	}
}