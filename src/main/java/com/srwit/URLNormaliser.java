package com.srwit;

import java.net.URI;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class URLNormaliser implements IURLNormaliser {
    private Pattern trailingSlashPatter = Pattern.compile("/$");

    @Override
    public String normalise(URI uri)
    {
        String scheme = convertToHTTP(uri.getScheme());
        String authorityPath = uri.getAuthority() + uri.getPath();

        Matcher matcher = trailingSlashPatter.matcher(authorityPath);
        String trimmedAuthorityPath = matcher.replaceFirst("");

        return scheme + "//" + trimmedAuthorityPath;
    }

    private String convertToHTTP(String scheme)
    {
        String lowerCaseScheme = scheme.toLowerCase();

        if (lowerCaseScheme.equals("http:") || lowerCaseScheme.equals("https:"))
        {
            throw new RuntimeException("Unrecognised scheme " + lowerCaseScheme);
        }

        return "http:";
    }
}
