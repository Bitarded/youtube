package com.bitarrded.youtube.confing;
//
//public class AudienceValidator implements OAuth2TokenValidator<Jwt> {
//
//    private final String audience;
//
//    public AudienceValidator(String audience) {
//        this.audience = audience;
//    }
//
//    @Override
//    public OAuth2TokenValidatorResult validate(Jwt jwt) {
//        if (jwt.getAudience().contains(audience)) {
//            return OAuth2TokenValidatorResult.success();
//        }
//        return OAuth2TokenValidatorResult.failure(new OAuth2Error("invalid audience for the given token"));
//    }
//}