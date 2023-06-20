package moontrack.analytics;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class SchemeVerificationResult {
	public enum ResultType {
		MISSING,
		DIFFERENT_TYPE
	}
	
	private final List<ResultAtom> result = new LinkedList<>();

	public void verify(Map<String, Class<?>> fields, String fieldName, Class<?> requiredFieldType) {
		Class<?> fieldType = fields.get(fieldName);
		if (fieldType == requiredFieldType) {
			return;
		}
		
		result.add(new ResultAtom(fieldName, getAtomType(fieldType, requiredFieldType)));
		
	}

	private ResultType getAtomType(Class<?> fieldType, Class<?> requiredFieldType) {
		return fieldType == null ? ResultType.MISSING : ResultType.DIFFERENT_TYPE;
	}

	public class ResultAtom {
		public final String fieldName;
		public final ResultType atomType;

		public ResultAtom(String fieldName, ResultType atomType) {
			this.fieldName = fieldName;
			this.atomType = atomType;
		}
		
	}

	public SchemeVerificationResult merge(SchemeVerificationResult other) {
		result.addAll(other.result);
		return this;
	}

	public boolean isOK() {
		return result.isEmpty();
	}
}
