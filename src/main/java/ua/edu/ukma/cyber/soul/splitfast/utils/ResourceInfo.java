package ua.edu.ukma.cyber.soul.splitfast.utils;

import org.springframework.core.io.Resource;

public record ResourceInfo(Resource resource, String contentType, long lastModified) {}