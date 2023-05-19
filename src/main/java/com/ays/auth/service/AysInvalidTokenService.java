package com.ays.auth.service;

import java.util.Set;

public interface AysInvalidTokenService {

    void invalidateTokens(final Set<String> tokenIds);

    void checkForInvalidityOfToken(final String tokenId);

}
