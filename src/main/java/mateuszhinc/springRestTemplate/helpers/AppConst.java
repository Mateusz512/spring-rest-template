package mateuszhinc.springRestTemplate.helpers;

import java.util.Arrays;
import java.util.List;

public class AppConst {
    public static final String ROLE_ADMIN = "ADMIN";
    public static final String ROLE_USER = "USER";
    public static final List<String> validAuthoritiesNames = Arrays.asList(ROLE_ADMIN,ROLE_USER);
}
