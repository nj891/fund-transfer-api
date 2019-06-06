package com.finance.fund.transfer.constants;

import java.util.Objects;

/**
 * @author Nilesh
 */
public enum AccountMetadata {

	BALANCE("�?�", "�?‘�?°�?»�?°�?½Ñ��?¾�?²Ñ‹�?µ Ñ�Ñ‡�?µÑ‚�?°"),
    TRUST_MANAGEMENT("�?‘", "�?¡Ñ‡�?µÑ‚�?° �?´�?¾�?²�?µÑ€�?¸Ñ‚�?µ�?»ÑŒ�?½�?¾�?³�?¾ Ñƒ�?¿Ñ€�?°�?²�?»�?µ�?½�?¸Ñ�"),
    OFF_BALANCE("�?’", "�?’�?½�?µ�?±�?°�?»�?°�?½Ñ��?¾�?²Ñ‹�?µ Ñ�Ñ‡�?µÑ‚�?°");

    private final String code;
    private final String description;

    AccountMetadata(String code, String description) {
        Objects.requireNonNull(code);
        Objects.requireNonNull(description);

        this.code = code;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }
}
